package one.pkg.ffmp.getter;

import io.papermc.paperclip.paperclip.Util;
import one.pkg.ffmp.meta.MixinJar;
import one.tranic.t.proxy.RequestWithProxyParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

public class MixinGetter {
    public final static String METADATA_URL = "https://maven.fabricmc.net/net/fabricmc/sponge-mixin/maven-metadata.xml";
    public final static String MIXIN_JAR_URL = "https://maven.fabricmc.net/net/fabricmc/sponge-mixin/%s/sponge-mixin-%s.jar";

    public static MixinJar getJar(String version) throws Exception {
        URL fileURL = URI.create(MIXIN_JAR_URL.formatted(version, version)).toURL();
        System.out.println("FabricMixin File URL: " + fileURL);
        try (InputStream inputStream = RequestWithProxyParser.openStream(fileURL)) {
            File file = new File("sponge-mixin-" + version + ".jar");
            if (file.exists()) file.delete();
            file.createNewFile();

            byte[] nativeBytes = inputStream.readAllBytes();
            Files.write(file.toPath(), nativeBytes);
            return MixinJar.of(
                    version,
                    Util.sha256Digest.digest(nativeBytes),
                    file
            );
        }
    }

    private static String parseLatestVersion(InputStream xmlInputStream) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlInputStream);
            document.getDocumentElement().normalize();

            NodeList versioningNodes = document.getElementsByTagName("versioning");
            if (versioningNodes.getLength() > 0) {
                Element versioningElement = (Element) versioningNodes.item(0);
                NodeList latestNodes = versioningElement.getElementsByTagName("latest");

                if (latestNodes.getLength() > 0) {
                    Element latestElement = (Element) latestNodes.item(0);
                    return latestElement.getTextContent().trim();
                }
            }

            return null;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new Exception("Error parsing XML: " + e.getMessage(), e);
        }
    }

    public static String getLatestVersion() throws Exception {
        try {
            try (InputStream inputStream = RequestWithProxyParser.openStream(METADATA_URL)) {
                return parseLatestVersion(inputStream);
            }
        } catch (IOException e) {
            throw new Exception("Cannot access metadata URL: " + e.getMessage(), e);
        }
    }


}
