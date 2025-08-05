package one.pkg.ffmp.changer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import one.pkg.ffmp.meta.MixinJar;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientJsonChange {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    protected ClientJsonChange() {
    }

    public static boolean modifyJsonFileSafely(String jsonFilePath, String targetLibraryName, MixinJar mixinJar) throws IOException {
        return modifyJsonFileSafely(jsonFilePath, targetLibraryName, mixinJar.name(),
                mixinJar.fullPath(), mixinJar.url(), mixinJar.hash(), mixinJar.size());
    }

    public static boolean modifyJsonFileSafely(String jsonFilePath, String targetLibraryName,
                                               String newName, String newPath, String newUrl,
                                               String newSha1, long newSize) throws IOException {

        JsonObject rootObject;
        try (FileReader reader = new FileReader(jsonFilePath, StandardCharsets.UTF_8)) {
            rootObject = JsonParser.parseReader(reader).getAsJsonObject();
        }

        JsonArray librariesArray = rootObject.getAsJsonArray("libraries");
        if (librariesArray == null) {
            throw new IllegalStateException("Could not find 'libraries' field in JSON file");
        }

        boolean found = false;
        for (JsonElement element : librariesArray) {
            JsonObject libraryObj = element.getAsJsonObject();

            if (targetLibraryName.equals(libraryObj.get("name").getAsString())) {
                libraryObj.addProperty("name", newName);

                JsonObject downloads = libraryObj.getAsJsonObject("downloads");
                if (downloads != null) {
                    JsonObject artifact = downloads.getAsJsonObject("artifact");
                    if (artifact != null) {
                        artifact.addProperty("path", newPath);
                        artifact.addProperty("url", newUrl);
                        artifact.addProperty("sha1", newSha1);
                        artifact.addProperty("size", newSize);
                    }
                }
                found = true;
                break;
            }
        }

        if (found) {
            try (FileWriter writer = new FileWriter(jsonFilePath, StandardCharsets.UTF_8)) {
                gson.toJson(rootObject, writer);
            }
        } else {
            System.err.println("Library '" + targetLibraryName + "' not found in JSON file");
        }

        return found;
    }

    public static void addLibraryToJsonFile(String jsonFilePath, MixinJar mixinJar) throws IOException {
        addLibraryToJsonFile(jsonFilePath, mixinJar.name(), mixinJar.fullPath(),
                mixinJar.url(), mixinJar.hash(), mixinJar.size());
    }

    public static void addLibraryToJsonFile(String jsonFilePath, String name, String path,
                                            String url, String sha1, long size) throws IOException {

        JsonObject rootObject;
        try (FileReader reader = new FileReader(jsonFilePath, StandardCharsets.UTF_8)) {
            rootObject = JsonParser.parseReader(reader).getAsJsonObject();
        }

        JsonArray librariesArray = rootObject.getAsJsonArray("libraries");
        if (librariesArray == null) {
            librariesArray = new JsonArray();
            rootObject.add("libraries", librariesArray);
        }

        JsonObject newLibrary = new JsonObject();
        newLibrary.addProperty("name", name);

        JsonObject downloads = new JsonObject();
        JsonObject artifact = new JsonObject();
        artifact.addProperty("path", path);
        artifact.addProperty("url", url);
        artifact.addProperty("sha1", sha1);
        artifact.addProperty("size", size);

        downloads.add("artifact", artifact);
        newLibrary.add("downloads", downloads);

        librariesArray.add(newLibrary);

        try (FileWriter writer = new FileWriter(jsonFilePath, StandardCharsets.UTF_8)) {
            gson.toJson(rootObject, writer);
        }
    }

    public static void removeLibraryFromJsonFile(String jsonFilePath, String libraryName) throws IOException {

        JsonObject rootObject;
        try (FileReader reader = new FileReader(jsonFilePath, StandardCharsets.UTF_8)) {
            rootObject = JsonParser.parseReader(reader).getAsJsonObject();
        }

        JsonArray librariesArray = rootObject.getAsJsonArray("libraries");
        if (librariesArray == null) {
            return;
        }

        for (int i = 0; i < librariesArray.size(); i++) {
            JsonObject libraryObj = librariesArray.get(i).getAsJsonObject();
            if (libraryName.equals(libraryObj.get("name").getAsString())) {
                librariesArray.remove(i);
                break;
            }
        }

        try (FileWriter writer = new FileWriter(jsonFilePath, StandardCharsets.UTF_8)) {
            gson.toJson(rootObject, writer);
        }
    }

}
