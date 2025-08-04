package one.pkg.ffmp.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientJson {
    @SerializedName("libraries")
    private List<Library> libraries;

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }
}