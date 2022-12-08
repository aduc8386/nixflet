package dhcnhn.aduc8386.nixflet.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class UserComment {
    private long id;
    private User user;
    private String comment;

    public UserComment() {
    }

    public UserComment(User user, String comment) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.id = timestamp.getTime();
        this.user = user;
        this.comment = comment;
    }


    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
