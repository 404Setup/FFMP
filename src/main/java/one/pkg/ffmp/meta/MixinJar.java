package one.pkg.ffmp.meta;

import io.papermc.paperclip.paperclip.Util;
import one.pkg.ffmp.getter.BaseGetter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record MixinJar(String version, String hash, File file) {
    private static final String[] PATH_COMPONENTS = {"net", "fabricmc", "sponge-mixin"};
    private static final String JAR_PREFIX = "sponge-mixin-";
    private static final String JAR_EXTENSION = ".jar";

    public static MixinJar of(String version, String hash, File file) {
        return new MixinJar(version, hash, file);
    }

    public static MixinJar of(String version, byte[] hash, File file) {
        return new MixinJar(version, formatHexHash(hash), file);
    }

    public static MixinJar of(String version, String hash) {
        return new MixinJar(version, hash, new File("sponge-mixin-" + version + ".jar"));
    }

    public static MixinJar of(String version) {
        File file = getPath(BaseGetter.getLibrariesDir().toPath(), version).toFile();
        System.out.println("MixinJar File: " + file);
        byte[] bytes = Util.sha256Digest.digest(readFileAsBytes(file));
        return of(version, bytes, file);
    }

    public static boolean hasFile(Path basePath, String version) {
        return getPath(basePath, version).toFile().exists();
    }

    public static String formatHexHash(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static void ensureDirectoryExists(Path path) {
        File directory = path.toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static Path getPath(Path basePath, String version) {
        Path currentPath = basePath;

        for (String component : PATH_COMPONENTS) {
            currentPath = currentPath.resolve(component);
            ensureDirectoryExists(currentPath);
        }

        Path versionPath = currentPath.resolve(version);
        ensureDirectoryExists(versionPath);

        return versionPath.resolve(JAR_PREFIX + version + JAR_EXTENSION);
    }

    private static byte[] readFileAsBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path path(Path basePath) {
        return MixinJar.getPath(basePath, version);
    }

    public String url() {
        return "https://maven.fabricmc.net/net/fabricmc/sponge-mixin/" + version + "/sponge-mixin-" + version + ".jar";
    }

    public int size() {
        try {
            return (int) Files.size(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveTo(Path basePath) throws IOException {
        if (path(basePath).toFile().exists()) return;
        Files.move(file.toPath(), path(basePath));
        file.delete();
    }

    public String name() {
        return "net.fabricmc:sponge-mixin:" + version;
    }

    public String fullPath() {
        return "net/fabricmc/sponge-mixin/" + version + "/sponge-mixin-" + version + ".jar";
    }
}