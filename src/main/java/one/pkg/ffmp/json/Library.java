package one.pkg.ffmp.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Library {
    @SerializedName("downloads")
    private Downloads downloads;

    @SerializedName("name")
    private String name;

    @SerializedName("rules")
    private List<Rule> rules;

    public Downloads getDownloads() {
        return downloads;
    }

    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
