package one.pkg.ffmp;

import one.pkg.ffmp.changer.ClientJsonChange;
import one.pkg.ffmp.changer.ShimChange;
import one.pkg.ffmp.finder.BaseFinder;
import one.pkg.ffmp.finder.ForgeFinder;
import one.pkg.ffmp.getter.BaseGetter;
import one.pkg.ffmp.getter.MixinGetter;
import one.pkg.ffmp.meta.MixinJar;

import java.io.File;
import java.util.List;

public class FFMPMain {


    public static void main(String[] args) {
        try {
            System.out.println("Starting Forge Fabric Mixin Patcher");
            System.out.println("Github: https://github.com/404Setup/ForgeFabricMixinPatcher");
            System.out.println("Author: 404Setup");
            System.out.println("License: BSD-3-Clause");

            File librariesDir = BaseGetter.getLibrariesDir();

            String latestMixin = MixinGetter.getLatestVersion();
            System.out.println("Latest Mixin version: " + latestMixin);
            MixinJar mixinJar;
            if (MixinJar.hasFile(BaseGetter.getLibrariesDir().toPath(), latestMixin)) {
                System.out.println("MixinJar already exists, skipping download");
                mixinJar = MixinJar.of(latestMixin);
            } else {
                System.out.println("Start downloading Fabric Mixin " + latestMixin);
                mixinJar = MixinGetter.getJar(latestMixin);
            }

            List<File> forgeShims = ForgeFinder.find();
            for (File forgeJar : forgeShims) ShimChange.change(forgeJar, mixinJar);

            File launcherJson = BaseFinder.findLauncherJson();
            if (launcherJson != null) {
                if (ClientJsonChange.modifyJsonFileSafely(launcherJson.getAbsolutePath(),
                        "org.spongepowered:mixin:0.8.7",
                        mixinJar)) {
                    mixinJar.moveTo(librariesDir.toPath());
                    return;
                }
                if (ClientJsonChange.modifyJsonFileSafely(launcherJson.getAbsolutePath(),
                        "org.spongepowered:mixin:0.8.6",
                        mixinJar)) {
                    mixinJar.moveTo(librariesDir.toPath());
                    return;
                }
                ClientJsonChange.modifyJsonFileSafely(launcherJson.getAbsolutePath(),
                        "org.spongepowered:mixin:0.8.5",
                        mixinJar);
            }
            mixinJar.moveTo(librariesDir.toPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
