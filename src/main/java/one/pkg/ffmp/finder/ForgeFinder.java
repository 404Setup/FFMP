package one.pkg.ffmp.finder;

import one.pkg.ffmp.getter.BaseGetter;
import one.pkg.ffmp.json.ClientJson;
import one.pkg.ffmp.json.Library;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

public class ForgeFinder {
    private ForgeFinder() {
    }

    public static List<File> find() {
        File launcherJson = BaseFinder.findLauncherJson();
        if (launcherJson == null) {
            List<File> librariesForgeJar = findLibrariesJar();
            File[] localFiles = findLocalJar();
            if (localFiles != null && localFiles.length > 0) {
                Collections.addAll(librariesForgeJar, localFiles);
                return librariesForgeJar;
            }
            throw new RuntimeException("Forge main program not found");
        } else {
            List<File> list = new ArrayList<>();
            list.add(findClientLibrariesJar(launcherJson));
            return list;
        }
    }

    private static File findClientLibrariesJar(File launcherJson) {
        String version = null;
        try (Reader reader = new FileReader(launcherJson)) {
            ClientJson cJson = BaseFinder.GSON.fromJson(reader, ClientJson.class);
            for (Library library : cJson.getLibraries()) {
                if (library.getName().startsWith("net.minecraftforge:forge:") &&
                        library.getName().endsWith(":universal")) {
                    version = library.getName().substring(25, library.getName().length() - 10);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (version == null) throw new RuntimeException("Cannot parser forge version");

        File path = BaseGetter.getLibrariesDir();

        File shim = path.toPath().resolve("net").resolve("minecraftforge").resolve("forge").resolve(version)
                .resolve("forge-" + version + "-shim.jar").toFile();
        if (shim.exists() && isShimJar(shim))
            return shim;
        throw new RuntimeException("Cannot find forge shim jar");
    }

    private static List<File> findLibrariesJar() {
        Path path = BaseFinder.CURRENT_DIR.resolve("libraries").resolve("net")
                .resolve("minecraftforge").resolve("forge");
        File[] files = path.toFile().listFiles();
        if (files != null) {
            List<File> jFiles = new ArrayList<>();
            for (File file : files) {
                if (!file.isDirectory()) continue;
                var files2 = file.listFiles((a, b) -> b.toLowerCase().endsWith(".jar"));
                if (files2 != null) {
                    for (File f : files2) {
                        if (isShimJar(f)) jFiles.add(f);
                    }
                }
            }
            if (!jFiles.isEmpty())
                return jFiles;
        }
        throw new RuntimeException("Forge main program not found");
    }

    private static boolean isShimJar(String name) {
        return isShimJar(new File(name));
    }

    private static boolean isShimJar(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            return jarFile.getEntry("bootstrap-shim.list") != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static File[] findLocalJar() {
        return BaseFinder.CURRENT_DIR_FILE.listFiles((dir, name) -> {
            boolean jar = name.toLowerCase().endsWith(".jar");
            if (jar) return isShimJar(name);
            else return false;
        });
    }

}
