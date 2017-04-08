package socialMedia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naitsabes on 07.04.2017.
 */

enum FeedAgregate {
    INSTANCE;
    Set<Feed> feeds;

    FeedAgregate(){
        feeds = new HashSet<Feed>();
    }

    void LoadFeedsFromMedia(){
        feeds.addAll(new FacebookSynchronizer().synchronizeData());
        feeds.addAll(new InstagramSynchronizer().synchronizeData());
    }

    List<Feed> getFeeds(){
        return new ArrayList<>(feeds);
    }
}
