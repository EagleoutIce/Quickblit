/**
 * @brief liefert Filter die zur Bildmanipulation und für einen besseren Look verwendet werden können
 */
package uni.eagle.quickblit.Graphics.Filter;

import uni.eagle.quickblit.General.Interfaces.iCustomFilter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BlurFilter implements iCustomFilter {
    /**
     * @brief führt die Filteroperation auf das jeweilige Bild durch
     *
     * @param image Das Bild für die Operation
     * @return das Bild nach der Operation
     */
    public static BufferedImage processImage(BufferedImage image) {
        float[] blurMatrix = { 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
                1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f };
        BufferedImageOp blurFilter = new ConvolveOp(new Kernel(3, 3, blurMatrix),
                ConvolveOp.EDGE_NO_OP, null);
        return blurFilter.filter(image, null);
    }
}
