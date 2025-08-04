package one.pkg.ffmp.changer;

import one.pkg.ffmp.meta.MixinJar;

import java.io.IOException;
import java.nio.file.Path;

public class MixinChange {
    public static void change(MixinJar mixinJar, Path basePath) throws IOException {
        mixinJar.moveTo(basePath);
    }
}
