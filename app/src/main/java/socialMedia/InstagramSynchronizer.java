package socialMedia;

import com.example.mtmwi.needleyouneed.R;
import com.facebook.FacebookSdk;
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

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by mtmwi on 06.04.2017.
 */

public class InstagramSynchronizer implements IMediaSynchronizer {

    private String instagramAccessToken = SharedPrefUtils.getToken(getApplicationContext());

    @Override
    public String getAccessToken() {
        return instagramAccessToken;
    }

    @Override
    public void setAccessToken(String token) {
        instagramAccessToken = token;
    }

    @Override
    public void synchronizeData(FeedAdapter adapter) {
        try {
            final List<Feed> feedList = new ArrayList<Feed>();
            URL url = new URL(FacebookSdk.getApplicationContext().getString(R.string.insta_nun_recent_media_uri) + "?access_token="
                    + getAccessToken());
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
