package uni.eagle.quickblit.Graphics;

import uni.eagle.quickblit.General.Annotiations.IsSave;
import uni.eagle.quickblit.General.Enums.DataOperation;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.General.Interfaces.iPoint;
import uni.eagle.quickblit.General.Interfaces.iRenderable;

public class Sprite implements iRenderable, iPoint {

    private int xOffset = 0; private int yOffset = 0;
    public int width;     public int height;
    private String name;
    private SpriteSheet sheet;

    /**
     * @return den Namen des Sprites
     */
    public String getName(){
        return name;
    }


    /**
     * @brief Konstruktor, erstellt ein neues Sprite auf Basis eines SpriteSheets
     *
     * @param width Die Breite des Sprites
     * @param height Die Höhe des Sprites
     * @param spriteSheet Das Spritesheet auf dessen Basis der Sprite erstellt wird (beginnt bei 0,0)
     * @param Name Der Name des Sprites
     */
    public Sprite(int width, int height, SpriteSheet spriteSheet,String Name){
        this(width, height, spriteSheet,0,0,Name);
    }

    /**
     * @brief Konstruktor, erstellt ein neues Sprite auf Basis eines SpriteSheets
     *
     * @param width Die Breite des Sprites
     * @param height Die Höhe des Sprites
     * @param spriteSheet Das Spritesheet auf dessen Basis der Sprite erstellt wird
     * @param xof XPosition an der das Sprite beginnt
     * @param yof YPosition an der das Sprite beginnt
     * @param Name der Name des Sprites
     */
    public Sprite(int width, int height, SpriteSheet spriteSheet,int xof, int yof,String Name){
        this.width = width; this.height = height; this.sheet = spriteSheet; xOffset = xof; yOffset = yof;
        name = Name;
    }


    /**
     * @brief Konstruktor, erstllt ein neues Sprite auf Basis eines Datenfelds
     *
     * @param data Das Datenfeld
     * @param width die Breite des Sprites
     * @param height die höhe des Sprites
     * @param Name der Name des Sprites
     */
    public Sprite(int[] data, int width, int height, String Name){
        this.sheet = new SpriteSheet(data, width, height);
        this.width = width; this.height =height;
        name = Name;
    }

    /**
     * @brief Testmethode - Dient dazu ein Datenfeld mit Random Bilddaten zu erstellen
     *
     * @param len Die Länge des Datenfelds (width * height)
     * @return liefert das Datenfeld zurück
     */
    @IsSave
    public static int[] createNoise(int len){
        int[] ret = new int[len];
        for(int i = 0; i < len; i++)
            ret[i] = GameCore.rnd.nextInt(0xFFFFFF) + 0xFF000000;
        return ret;
    }

    /**
     * @return Das Datenfeld der Pixel, die das Sprite anzeigt
     */
    @IsSave
    public int[] getPixels(){
        int[] ret = new int[width*height];
        for(int y = 0; y < Math.min(height,sheet.getH()); y++){
            for(int x= 0; x < Math.min(width,sheet.getW()); x++){
                if( sheet.sprite_pixels[Math.floorMod(x+xOffset,sheet.getW())+ sheet.getW()*(Math.floorMod(y + yOffset,sheet.getH()))] < 0) {
                    ret[x+y*width] = sheet.sprite_pixels[Math.floorMod(x + xOffset, sheet.getW()) + sheet.getW() * (Math.floorMod(y + yOffset, sheet.getH()))];
                }
            }
        }
        return ret;
    }

    /**
     * @return Das Datenfeld der Pixel, die das Sprite anzeigt, als 2-Dimensionales Array
     */
    @IsSave
    public int[][] getPixels2D(){
        int[][] ret = new int[height][width];
        for(int y = 0; y < Math.min(height,sheet.getH()); y++){
            for(int x= 0; x < Math.min(width,sheet.getW()); x++){
                if( sheet.sprite_pixels[Math.floorMod(x+xOffset,sheet.getW())+ sheet.getW()*(Math.floorMod(y + yOffset,sheet.getH()))] < 0) {
                    ret[y][x] = sheet.sprite_pixels[Math.floorMod(x + xOffset, sheet.getW()) + sheet.getW() * (Math.floorMod(y + yOffset, sheet.getH()))];
                }
            }
        }
        return ret;
    }

    /**
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     * @param startx startPosition-X (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     * @param starty startPosition-Y (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     */
    public void render(int[] pixels, int startx, int starty){
        for(int y = 0; y < Math.min(height,sheet.getH()); y++){
            for(int x= 0; x < Math.min(width,sheet.getW()); x++){
                if( sheet.sprite_pixels[Math.floorMod(x+xOffset,sheet.getW())+ sheet.getW()*(Math.floorMod(y + yOffset,sheet.getH()))] < 0) {
                    pixels[Math.floorMod(x + startx, GameCore.getConfig().getWidth()) + GameCore.getConfig().getWidth() * (Math.floorMod(y + starty, GameCore.getConfig().getHeight()))] = sheet.sprite_pixels[Math.floorMod(x + xOffset, sheet.getW()) + sheet.getW() * (Math.floorMod(y + yOffset, sheet.getH()))];
                }
            }
        }
    }

    public void render(int[] pixels, int startx, int starty, DataOperation operation){
        render(pixels,startx,starty,operation,1);
    }

    public void render(int[] pixels, int startx, int starty, DataOperation operation, int scale){
        Painter.drawData(pixels, Helper.dataOperation(getPixels2D(),operation),startx,starty);
    }

    /**
     * @return die X-position im SpriteSheet
     */
    @Override
    public int getX() {
        return xOffset;
    }

    /**
     * @return die Y-Position im Spritesheet
     */
    @Override
    public int getY() {
        return yOffset;
    }

}
