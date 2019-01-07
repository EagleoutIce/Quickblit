package uni.eagle.quickblit.Game.Entity.Items;

import uni.eagle.quickblit.Game.Entity.Entity;
import uni.eagle.quickblit.Game.Player.Player;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;

import java.util.Random;

/**
 * @class Item
 * @brief Liefert die Möglichkeit Items auf der Karte zu platzieren
 */
public class Item extends Entity {

    private static Random r = new Random();

    /**
     * @brief Konstruiert ein neues Item - wird über generateItem() verwendet um direkt die richtigen Grahiken zu laden
     * @param x die Startposition X
     * @param y die Startposition Y
     * @param height die Breite des Items
     * @param width die Höhe des Items
     * @param path der Pfad zur Grafik, welche das Item präsentiert
     */
    private Item(int x, int y, int height, int width, String path) {
        super(x, y, height, width);
        super.s = new Sprite[1];
        super.s[0] = new Sprite(32,32,new SpriteSheet(path),0,0,"item");
    }

    /**
     * @brief convenience wrapper zur Item-erstellung
     *
     * @param x die Startposition X
     * @param y die Startposition Y
     * @param height die Breite des Items
     * @param width die Höhe des Items
     * @return das Item-Objekt
     */
    public static Item generateItem(int x, int y, int height, int width){
        switch(r.nextInt(5)) {
            case 0: return new Item(x, y, height, width, "/graphics/items/collectibles_013_thevirus.png");
            case 1: return new Item(x, y, height, width, "/graphics/items/collectibles_014_roidrage.png");
            case 2: return new Item(x, y, height, width, "/graphics/items/collectibles_070_growthhormones.png");
            case 3: return new Item(x, y, height, width, "/graphics/items/collectibles_143_speedball.png");
            case 4: return new Item(x, y, height, width, "/graphics/items/collectibles_240_experimentaltreatment.png");
            default: return new Item(x, y, height, width, "/graphics/items/collectibles_345_synthoil.png");
        }
    }

    /**
     * @brief Wird das Item aufgenommen?
     *
     * @note die "Dopplung" der Spielerdaten ist in der Entwicklung entstanden
     *
     * @param x Position x des Spielers
     * @param y Position des Spielers
     * @param height Höhe des Spielers
     * @param width Breite des Spielers
     * @param p der Spieler
     * @return true wenn das Item gerade aufgenommen wird
     */
    public boolean pickedUp(int x, int y, int height, int width, Player p){
        boolean b = super.collision(x,y,height,width);
        if(b){
            switch (r.nextInt(3)){
                case 0: p.setSpeed(p.getSpeed()+(r.nextInt(1000)+100)/100.0); break; //Geschwindigkeitsspritze
                case 1: p.setHealth(p.getHealth()+r.nextInt(4000)+1000); break; //Lebensspritze
                case 2: p.setDmg(p.getDmg()+(r.nextInt(300)+30)/100.0); break; //Schadensspritze
                default: break;
            }
        }
        return b;
    }

    /**
     * @return die aktuelle x-Position
     */
    @Override
    public int getX() {
        return (int)this.x;
    }

    /**
     * @return die aktuelle y-Positon
     */
    @Override
    public int getY() {
        return (int)this.y;
    }
}
