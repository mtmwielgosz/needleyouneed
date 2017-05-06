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

//TODO zmienmy ta nazwe... ewidetnie klasa ma zwiazek z watkami do synchronizacji. Moze Fabryka watkow? Moze Budowniczy watkow? Ale nie helpler.
public final class SocialMediaHelper {

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

    public static AsyncTask<Void, Void, Void> synchronizeWithSocialMedia() {
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
}
