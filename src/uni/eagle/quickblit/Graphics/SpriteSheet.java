/**
 * @brief liefert alle Komponenten die dazu benötigt werden um Grafiken anzuzeigen
 *
 */
package uni.eagle.quickblit.Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {

    /**
     * @brief eigentlich nur für DEBUG-Zwecke
     */
    @Deprecated
    private String filePath;

    private int w, h;
    int[] sprite_pixels;

    /**
     * @return Die Breite
     */
    public int getW() { return w; }

    /**
     * @return Die Höhe
     */
    public int getH() { return h; }


    /**
     * @brief Konstruktor - erstellt den Spritesheet auf Basis eines Bildes
     *
     * @param path Der Pfad zum Bild
     */
    public SpriteSheet(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (img == null) return;

        this.filePath = path;
        w = img.getWidth();
        h = img.getHeight();
        sprite_pixels = img.getRGB(0, 0, this.w, this.h, null, 0, this.w);
        //cleaning alpha channel:

        //System.out.println(sprite_pixels[22]);
    }

    /**
     * @brief Konstruktor - erstellt den Spritesheet auf Basis eines Datenfeldes
     *
     * @param data das Datenfeld
     * @param w die Breite des Sheets
     * @param h die Höhe des Sheets
     */
    public SpriteSheet(int[] data, int w, int h){
        sprite_pixels = data; this.w = w; this.h = h;
    }

}
