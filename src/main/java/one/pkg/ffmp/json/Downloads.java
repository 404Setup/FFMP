package one.pkg.ffmp.json;

import com.google.gson.annotations.SerializedName;

public class Downloads {
    @SerializedName("artifact")
    private Artifact artifact;

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }
}
