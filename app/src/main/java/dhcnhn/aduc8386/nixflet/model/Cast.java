package dhcnhn.aduc8386.nixflet.model;

import com.google.gson.annotations.SerializedName;

public class Cast {

    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profilePath;

    public Cast(String name, String profilePath) {
        this.name = name;
        this.profilePath = profilePath;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }
}
