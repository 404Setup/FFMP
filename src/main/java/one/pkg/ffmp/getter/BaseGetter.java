package one.pkg.ffmp.getter;

import one.pkg.ffmp.finder.BaseFinder;

import java.io.File;

public class BaseGetter {
    private static File librariesDir;

    public static File getLibrariesDir() {
        if (librariesDir != null) return librariesDir;
        File path = BaseFinder.CURRENT_DIR.resolve("libraries").toFile();
        if (!path.exists()) {
            path = BaseFinder.CURRENT_DIR.getParent().getParent().resolve("libraries").toFile();
            if (!path.exists())
                throw new RuntimeException("Cannot find libraries folder");
        }
        librariesDir = path;
        return librariesDir;
    }
}
