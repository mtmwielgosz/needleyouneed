package socialMedia;

import java.util.List;

/**
 * Created by mtmwi on 06.04.2017.
 */

public interface IMediaSynchronizer {

    String getAccessToken();
    void setAccessToken(String token);
    List<Feed> synchronizeData();
}
