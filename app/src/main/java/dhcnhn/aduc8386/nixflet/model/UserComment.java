package dhcnhn.aduc8386.nixflet.model;

public class UserComment {
    private String name;
    private String comment;
    private String avatarPath;

    public UserComment(String name, String comment, String avatarPath) {
        this.name = name;
        this.comment = comment;
        this.avatarPath = avatarPath;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getAvatarPath() {
        return avatarPath;
    }
}
