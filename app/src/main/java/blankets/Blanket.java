package blankets;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;

class Blanket implements Comparable<Blanket> {

    private String message;
    private String type;
    private String link;
    private String picture;
    private String updatedTime;
    private String id;
    private Money price;

    Blanket(String message, String type, String link, String picture, String updatedTime, String id, Money price) {
        this.message = message;
        this.type = type;
        this.link = link;
        this.picture = picture;
        this.updatedTime = updatedTime;
        this.id = id;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blanket feed = (Blanket) o;

        return this.id != null ? this.id.equals(feed.id) : feed.id == null;
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    @Override
    public int compareTo(Blanket feed) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy, HH:mm:ss");

        DateTime time1 = formatter.parseDateTime(this.updatedTime);
        DateTime time2 = formatter.parseDateTime(feed.updatedTime);

        return time2.compareTo(time1);
    }
}
