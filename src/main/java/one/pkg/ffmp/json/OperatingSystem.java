package one.pkg.ffmp.json;

import com.google.gson.annotations.SerializedName;

public class OperatingSystem {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
