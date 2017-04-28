package socialMedia;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.example.mtmwi.needleyouneed.R;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by mtmwi on 24.03.2017.
 */

final class SocialMediaHelper {

    static AsyncTask<Void, Void, Void> synchronizeWithSocialMedia(final SwipeRefreshLayout swipeRefreshLayout) {
        return new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void[] params) {
                FeedAgregate.INSTANCE.LoadFeedsFromMedia();
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }
    static AsyncTask<Void, Void, Void> synchronizeWithSocialMedia() {
        return new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void[] params) {
                FeedAgregate.INSTANCE.LoadFeedsFromMedia();
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
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
