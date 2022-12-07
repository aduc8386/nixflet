package dhcnhn.aduc8386.nixflet.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
