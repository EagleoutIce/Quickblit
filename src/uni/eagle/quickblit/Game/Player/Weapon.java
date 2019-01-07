package uni.eagle.quickblit.Game.Player;

import uni.eagle.quickblit.General.Enums.DataOperation;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.General.Interfaces.iRenderable;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;

/**
 * @class Weapon
 * @brief Liefert die Möglichkeit eine Waffe aufs Display zu bringen - kann aus Zeitgründen nicht angreifen
 */
public class Weapon implements iRenderable {

    Sprite s,sr;
    Direction d = Direction.DOWN;

    /**
     * @brief Konstruktor, lädt zwei vordefinierte Grafiken sr ist das rotierte Messer
     */
    public Weapon(){
        s = new Sprite(32,32,new SpriteSheet("/graphics/items/collectibles_114_momsknife.png"),0,0,"knife");
        sr = new Sprite(32,32,new SpriteSheet("/graphics/items/collectibles_114_momsknife_rotate45.png"),0,0,"knife45");
    }

    /**
     * @brief rendert das Messer auf Basis der Richtung
     *
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     * @param startx startPosition-X (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     * @param starty startPosition-Y (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     */
    @Override
    public void render(int[] pixels, int startx, int starty) {
        switch (d){
            case UP: s.render(pixels,startx,starty-20);
                break;
            case DOWN: Painter.drawData(pixels, Helper.dataOperation(s.getPixels(),32,32, DataOperation.MIRROR_VERT),startx,starty+20,32,32);
                break;
            case RIGHT: Painter.drawData(pixels, Helper.dataOperation(s.getPixels(),32,32, DataOperation.ROTATE_90),startx+20,starty,32,32);
                break;
            case LEFT: Painter.drawData(pixels, Helper.dataOperation(s.getPixels(),32,32, DataOperation.MIRROR_POINT),startx-20,starty,32,32);
                break;
            case RIGHT_DOWN:
                sr.render(pixels,startx+20,starty-20); break;
            case RIGHT_UP: Painter.drawData(pixels, Helper.dataOperation(sr.getPixels(),32,32, DataOperation.MIRROR_VERT),startx+15,starty+15,32,32);
                break;
            case LEFT_DOWN:Painter.drawData(pixels, Helper.dataOperation(sr.getPixels(),32,32, DataOperation.MIRROR_HORI),startx-15,starty-15,32,32);
                break;
            case LEFT_UP:Painter.drawData(pixels, Helper.dataOperation(sr.getPixels(),32,32, DataOperation.MIRROR_POINT),startx-15,starty+15,32,32);
                break;


        }
    }
}
