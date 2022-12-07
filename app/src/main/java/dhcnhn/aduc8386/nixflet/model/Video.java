package dhcnhn.aduc8386.nixflet.model;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("iso_639_1")
    private String iso6391;
    @SerializedName("iso_3166_1")
    private String iso31661;
    @SerializedName("name")
    private String name;
    @SerializedName("key")
    private String key;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private int size;
    @SerializedName("type")
    private String type;
    @SerializedName("official")
    private boolean official;
    @SerializedName("published_at")
    private String publishAt;
    @SerializedName("id")
    private String id;

    public Video(String iso6391, String iso31661, String name, String key, String site, int size, String type, boolean official, String publishAt, String id) {
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.name = name;
        this.key = key;
        this.site = site;
        this.size = size;
        this.type = type;
        this.official = official;
        this.publishAt = publishAt;
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public boolean isOfficial() {
        return official;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public String getId() {
        return id;
    }
}
