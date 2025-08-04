package one.pkg.ffmp.json;

import com.google.gson.annotations.SerializedName;

public class Artifact {
    @SerializedName("path")
    private String path;

    @SerializedName("sha1")
    private String sha1;

    @SerializedName("size")
    private long size;

    @SerializedName("url")
    private String url;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
