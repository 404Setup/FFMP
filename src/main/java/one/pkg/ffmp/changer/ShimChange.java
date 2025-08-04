package one.pkg.ffmp.changer;

import one.pkg.ffmp.meta.MixinJar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class ShimChange {
    protected ShimChange() {
    }

    public static void change(File forgeJar, MixinJar mixinJar) throws IOException {
        File tempFile = new File(forgeJar.getParent(), forgeJar.getName() + ".tmp");

        try (JarFile originalJar = new JarFile(forgeJar);
             JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempFile))) {

            String librariesList = null;
            ZipEntry librariesEntry = originalJar.getEntry("bootstrap-shim.list");
            if (librariesEntry != null)
                librariesList = new String(originalJar.getInputStream(librariesEntry).readAllBytes(), StandardCharsets.UTF_8);

            originalJar.stream().forEach(entry -> {
                try {
                    if (!entry.getName().equals("bootstrap-shim.list")) {
                        jos.putNextEntry(new JarEntry(entry.getName()));
                        originalJar.getInputStream(entry).transferTo(jos);
                        jos.closeEntry();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            if (librariesList != null) {

                String[] lines = librariesList.split("\n");
                StringBuilder newLibrariesList = new StringBuilder();

                for (String line : lines) {
                    if (line.contains("org.spongepowered:mixin")) {
                        newLibrariesList.append(mixinJar.hash())
                                .append("\t")
                                .append(mixinJar.name())
                                .append("\t")
                                .append(mixinJar.fullPath())
                                .append("\n");
                    } else {
                        newLibrariesList.append(line).append("\n");
                    }
                }

                jos.putNextEntry(new JarEntry("bootstrap-shim.list"));
                jos.write(newLibrariesList.toString().getBytes(StandardCharsets.UTF_8));
                jos.closeEntry();
            }
        }

        Files.move(tempFile.toPath(), forgeJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
