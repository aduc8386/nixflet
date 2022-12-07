package dhcnhn.aduc8386.nixflet.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResult {

    @SerializedName("results")
    private List<Video> videos;

    public VideoResult(List<Video> videos) {
        this.videos = videos;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
