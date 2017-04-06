package socialMedia;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.example.mtmwi.needleyouneed.R;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.utils.SharedPrefUtils;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import other.SimpleHelper;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by mtmwi on 24.03.2017.
 */

final class SocialMediaHelper {

    static boolean isLoggedInFacebook() {

        return AccessToken.getCurrentAccessToken() != null;
    }

    static FacebookCallback<LoginResult> getFacebookCallback(final View view) {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Snackbar.make(view, getApplicationContext().getString(R.string.fb_msg_succeed), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

                Snackbar.make(view, getApplicationContext().getString(R.string.fb_msg_unsuceed), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {

                onCancel();
            }
        };
    }

    static GraphRequest getFacebookRequest(final FeedAdapter adapter) {
        return new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "needleyouneed/feed?fields=full_picture,link,message,type,updated_time",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            final List<Feed> feedList = new ArrayList<Feed>();



                            JSONObject json = new JSONObject(response.getRawResponse());
                            JSONArray data = json.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject oneFeed = data.getJSONObject(i);

                                String message = oneFeed.optString("message");
                                if (!SimpleHelper.isEmpty(message)) {
                                    message = "\n" + message;
                                    message += "\n";
                                }

                                String date = oneFeed.optString("updated_time");
                                if (!SimpleHelper.isEmpty(date)) {
                                    date = DateTime.parse(date).toString("dd-MM-yyyy, HH:mm:ss");
                                }

                                feedList.add(new Feed(message, oneFeed.optString("type"), "fb://facewebmodal/f?href=" + oneFeed.optString("link"),
                                        oneFeed.optString("full_picture"), date, oneFeed.optString("id")));
                            }
                            adapter.addAll(feedList);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    static boolean isLoggedInInstagram() {
        return SimpleHelper.isEmpty(SharedPrefUtils.getToken(getApplicationContext()));
    }

    static void logInInstagram(Activity activity) {

        String scope = "basic+public_content";

        InstagramHelper instagramHelper = new InstagramHelper.Builder()
                .withClientId(getApplicationContext().getString(R.string.instagram_client_id))
                .withRedirectUrl(getApplicationContext().getString(R.string.instagram_redirect_uri))
                .withScope(scope)
                .build();

        instagramHelper.loginFromActivity(activity);
    }

    static void executeInstagramOnResult(int requestCode, int resultCode) {

        if (requestCode == InstagramHelperConstants.INSTA_LOGIN) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.insta_msg_succeed), Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.insta_msg_unsuceed), Toast.LENGTH_LONG).show();
            }
        }
    }

    static AsyncTask<Void, Void, Void> getInstagramRequest(final List<Feed> feedList) {
        return new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void[] params) {
                try {
                    URL url = new URL(getApplicationContext().getString(R.string.nun_recent_media_uri) + "?access_token="
                            + SharedPrefUtils.getToken(getApplicationContext()));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    int response = connection.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        try {
                            JSONObject json = new JSONObject(sb.toString());
                            JSONArray data = json.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject oneFeed = data.getJSONObject(i);

                                JSONObject caption = oneFeed.getJSONObject("caption");
                                String message = caption.optString("text");
                                if (!SimpleHelper.isEmpty(message)) {
                                    message = "\n" + message;
                                    message += "\n";
                                }

                                Long created_time = Long.parseLong(caption.optString("created_time"));
                                DateTime dateTime = new DateTime((created_time * 1000L));
                                String date = DateTime.parse(dateTime.toString()).toString("dd-MM-yyyy, HH:mm:ss");

                                JSONObject images = oneFeed.getJSONObject("images");
                                JSONObject image = images.getJSONObject("standard_resolution");

                                feedList.add(new Feed(message, oneFeed.optString("type"), oneFeed.optString("link"),
                                        image.optString("url"), date, oneFeed.optString("id")));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    static AsyncTask<Void, Void, Void> synchronizeWithSocialMedia(final SwipeRefreshLayout swipeRefreshLayout, final FeedAdapter adapter) {
        return new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void[] params) {
                SocialMediaHelper.getFacebookRequest(adapter).executeAsync();
                updateInsta(adapter);
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    private static void updateInsta(FeedAdapter adapter) {
        try {
            final List<Feed> feedList = new ArrayList<Feed>();
            URL url = new URL(getApplicationContext().getString(R.string.nun_recent_media_uri) + "?access_token="
                    + SharedPrefUtils.getToken(getApplicationContext()));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                try {
                    JSONObject json = new JSONObject(sb.toString());
                    JSONArray data = json.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject oneFeed = data.getJSONObject(i);

                        JSONObject caption = oneFeed.getJSONObject("caption");
                        String message = caption.optString("text");
                        if (!SimpleHelper.isEmpty(message)) {
                            message = "\n" + message;
                            message += "\n";
                        }

                        Long created_time = Long.parseLong(caption.optString("created_time"));
                        DateTime dateTime = new DateTime((created_time * 1000L));
                        String date = DateTime.parse(dateTime.toString()).toString("dd-MM-yyyy, HH:mm:ss");

                        JSONObject images = oneFeed.getJSONObject("images");
                        JSONObject image = images.getJSONObject("standard_resolution");

                        feedList.add(new Feed(message, oneFeed.optString("type"), oneFeed.optString("link"),
                                image.optString("url"), date, oneFeed.optString("id")));
                    }

                    adapter.addAll(feedList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
