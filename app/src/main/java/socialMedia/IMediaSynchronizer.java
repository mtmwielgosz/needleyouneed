package socialMedia;

/**
 * Created by mtmwi on 06.04.2017.
 */

public interface IMediaSynchronizer {

    String getAccessToken();
    void setAccessToken(String token);
    void synchronizeData(FeedAdapter adapter);
}
