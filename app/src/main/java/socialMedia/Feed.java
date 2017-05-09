package socialMedia;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by mtmwi on 21.03.2017.
 */

class Feed implements Comparable<Feed> {

    private String message;
    private String type;
    private String link;
    private String picture;
    private String updatedTime;
    private String id;

    Feed(String message, String type, String link, String picture, String updatedTime, String id) {
        this.message = message;
        this.type = type;
        this.link = link;
        this.picture = picture;
        this.updatedTime = updatedTime;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        return getId() != null ? getId().equals(feed.getId()) : feed.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public int compareTo(Feed feed) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy, HH:mm:ss");

        DateTime time1 = formatter.parseDateTime(this.getUpdatedTime());
        DateTime time2 = formatter.parseDateTime(feed.getUpdatedTime());

        return time2.compareTo(time1);
    }

    public boolean isFaceBook() {
        return link.contains("facebook");
    }

    public String getBrowserAddress() {
        String[] parts = link.split("href=");
        return parts[1];
    }
}
