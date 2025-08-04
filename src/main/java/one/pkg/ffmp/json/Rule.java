package one.pkg.ffmp.json;

import com.google.gson.annotations.SerializedName;

public class Rule {
    @SerializedName("action")
    private String action;

    @SerializedName("os")
    private OperatingSystem os;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public OperatingSystem getOs() {
        return os;
    }

    public void setOs(OperatingSystem os) {
        this.os = os;
    }
}
