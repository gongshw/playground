package com.gongshw.playground.image.decoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author gongshiwei
 */
public class GifDecoder {
    public void extract(File gif, File targetDirection) throws IOException {

        String[] imageatt = new String[] {
                "imageLeftPosition",
                "imageTopPosition",
                "imageWidth",
                "imageHeight"
        };

        String dir = targetDirection.getAbsolutePath();

        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        ImageInputStream ciis = ImageIO.createImageInputStream(gif);
        reader.setInput(ciis, false);
        int noi = reader.getNumImages(true);
        BufferedImage master = null;

        for (int i = 0; i < noi; i++) {
            BufferedImage image = reader.read(i);
            IIOMetadata metadata = reader.getImageMetadata(i);

            Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
            NodeList children = tree.getChildNodes();

            for (int j = 0; j < children.getLength(); j++) {
                Node nodeItem = children.item(j);

                if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                    Map<String, Integer> imageAttr = new HashMap<>();

                    for (int k = 0; k < imageatt.length; k++) {
                        NamedNodeMap attr = nodeItem.getAttributes();
                        Node attnode = attr.getNamedItem(imageatt[k]);
                        imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
                    }
                    if (i == 0) {
                        master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"),
                                BufferedImage.TYPE_INT_ARGB);
                    }
                    master.getGraphics()
                            .drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"),
                                    null);
                }
            }
            ImageIO.write(master, "GIF", new File(dir + "/" + gif.getName() + "." + i + "" + ".gif"));
        }
    }
}
