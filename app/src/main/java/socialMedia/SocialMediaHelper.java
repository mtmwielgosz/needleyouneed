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

    static AsyncTask<Void, Void, Void> synchronizeWithSocialMedia(final SwipeRefreshLayout swipeRefreshLayout, final FeedAdapter adapter) {
        return new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void[] params) {
                new FacebookSynchronizer().synchronizeData(adapter);
                new InstagramSynchronizer().synchronizeData(adapter);
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    static void executeInstagramOnResult(int requestCode, int resultCode) { // TODO no needed when no logging in Instagram

        if (requestCode == InstagramHelperConstants.INSTA_LOGIN) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.insta_msg_succeed), Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.insta_msg_unsuceed), Toast.LENGTH_LONG).show();
            }
        }
    }

}
