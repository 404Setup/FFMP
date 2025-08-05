package one.pkg.ffmp.finder;

import com.google.gson.Gson;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseFinder {
    public static final Gson GSON = new Gson();
    public static final Path CURRENT_DIR;
    public static final File CURRENT_DIR_FILE;

    static {
        try {
            CURRENT_DIR = Paths.get(BaseFinder.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().getParent();
            CURRENT_DIR_FILE = CURRENT_DIR.toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private BaseFinder() {
    }

    public static File findLauncherJson() {
        File[] jarFiles = CURRENT_DIR_FILE.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        File[] jsonFiles = CURRENT_DIR_FILE.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jarFiles == null || jsonFiles == null) {
            return null;
        }

        for (File jar : jarFiles) {
            String baseName = jar.getName().substring(0, jar.getName().length() - 4);
            for (File json : jsonFiles) {
                String jsonBaseName = json.getName().substring(0, json.getName().length() - 5);
                if (baseName.equals(jsonBaseName)) {
                    return json;
                }
            }
        }
        return null;
    }
}
