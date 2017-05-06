package socialMedia;

import com.example.mtmwi.needleyouneed.R;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by mtmwi on 06.04.2017.
 */

class InstagramSynchronizer implements IMediaSynchronizer {

    @Override
    public List<Feed> synchronizeData() {
        final List<Feed> feedList = new ArrayList<Feed>();
        try {

            JSONObject json = new JSONObject(JsonConverter.getJSONResult(getApplicationContext().getString(R.string.insta_nun_recent_media_uri)));
            JSONArray data = json.getJSONArray("items");
            for (int i = 0; i < data.length(); i++) {
                JSONObject oneFeed = data.getJSONObject(i);

                JSONObject caption = oneFeed.getJSONObject("caption");

                Long created_time = Long.parseLong(caption.optString("created_time"));
                DateTime dateTime = new DateTime((created_time * 1000L));
                String date = DateTime.parse(dateTime.toString()).toString("dd-MM-yyyy, HH:mm:ss");

                JSONObject images = oneFeed.getJSONObject("images");
                JSONObject image = images.getJSONObject("standard_resolution");

                feedList.add(new Feed(JsonConverter.addEmptyLines(caption.optString("text")), oneFeed.optString("type"), oneFeed.optString("link"),
                        image.optString("url"), date, oneFeed.optString("id")));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return feedList;
    }
}
