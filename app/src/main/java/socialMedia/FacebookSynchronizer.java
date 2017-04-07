package socialMedia;

import com.example.mtmwi.needleyouneed.R;

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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by mtmwi on 06.04.2017.
 */

public class FacebookSynchronizer implements IMediaSynchronizer {

    private static String facebookAccessToken =  "1747122368951836|OjzCmZompznsqin_oNs7wlq05go";

    @Override
    public String getAccessToken() {
        return facebookAccessToken;
    }

    @Override
    public void setAccessToken(String accessToken) {
        facebookAccessToken = accessToken;
    }

    @Override
    public void synchronizeData(FeedAdapter adapter) {
        try {
            final List<Feed> feedList = new ArrayList<Feed>();
            URL url = new URL(getApplicationContext().getString(R.string.fb_nun_recent_media_uri) + "&access_token="
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

                JSONObject json = new JSONObject(sb.toString());
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
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
