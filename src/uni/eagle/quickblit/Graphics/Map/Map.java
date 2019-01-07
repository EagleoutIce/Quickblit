/**
 * @brief liefert alles nötige um eine Karte zu handhaben
 */
package uni.eagle.quickblit.Graphics.Map;

import uni.eagle.quickblit.General.Annotiations.Internal;
import uni.eagle.quickblit.General.Enums.DataOperation;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.General.Interfaces.iPoint;
import uni.eagle.quickblit.General.Interfaces.iRenderable;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Management.SpriteTranslator;

import java.util.Scanner;

public class Map implements iRenderable {

    /**
     * @brief eigentlich nur für DEBUG-Zwecke
     */
    @Deprecated
    private String mapPath = "";

    private Sprite[][] tiles;
    private SpriteTranslator herrmann;
    private int w,  h;

    /**
     * @brief Lädt die Karte aus einer Datei
     *
     * @param path der Pfad zur Kartendatei
     */
    public Map(String path) {this("/configs/default.dict",path); }

    /**
     * @brief Lädt die Karte aus einer Datei
     *
     * @param path Der Pfad zur Datei
     * @param dictonaryPath der Pfad zur Datei für den Übersetzer
     */
    public Map(String path, String dictonaryPath) { init(dictonaryPath); loadTiles(path); }


    /**
     * @brief Lädt die Karte aus einem String
     *
     * @param mapData String mit den Kartendaten
     * @param w Breite der Karte
     * @param h Höhe der Karte
     */
    public Map(String mapData, int w, int h) {this(mapData, w,h,"/configs/default.dict"); }

    /**
     * @brief Lädt die Karte aus einem String
     *
     * @param mapData String mit den Kartendaten
     * @param w Breite der Karte
     * @param h Höhe der Karte
     * @param dictonaryPath der Pfad zur Datei für den Übersetzer
     */
    public Map(String mapData, int w, int h,String dictonaryPath) { init(dictonaryPath); loadTiles(mapData, w,h); }


    /**
     * @brief liefert das Sprite an der jeweiligen Position
     *
     * @param x index
     * @param y index
     * @return das dortige Sprite
     */
    public Sprite getTile(int x, int y){
        return tiles[y][x];
    }

    /**
     * @brief Lädt die Tiles aus einem String - unterstützt nur unformatierte Übersetzungen
     *
     * @param data die Daten
     * @param w die Breite der Karte
     * @param h die Höhe der Karte
     */
    @Internal
    private void loadTiles(String data, int w, int h){
        this.w = w; this.h = h;
        for(int y = 0; y<h; y++){
            for(int x = 0; x<w; x++){
                tiles[y][x] = herrmann.get(data.charAt(x+w*y)+"");
            }
        }
    }

    /**
     * @brief Lädt die Tiles aus einer Datei
     *
     * @param path Pfad zur Datei
     */
    private void loadTiles(String path){
        Scanner rudolf = new Scanner(this.getClass().getResourceAsStream(path));
        int _h = 0x0, _w = GameCore.getConfig().getWidth()/GameCore.getConfig().getTileSize();
        for(; rudolf.hasNextLine(); _h++){
            String[] line = rudolf.nextLine().split(" ");
            if(line.length < 1 || line[0].startsWith("//")) {
                _h--; continue;
            }
            for(int i = 0; i < _w && i < line.length; i++) {
                if (line[i].contains(":")){ //<Bezeichner>:<Drehung>
                    Sprite s =  herrmann.get(line[i].substring(0,line[i].indexOf(':')));
                    switch (line[i].substring(line[i].indexOf(':')+1)){
                        case "1": tiles[_h][i] = new Sprite(Helper.dataOperation(s.getPixels(),s.width,s.height, DataOperation.ROTATE_90), s.height, s.width,line[i].substring(0,line[i].indexOf(':')));
                            break;
                        case "2": tiles[_h][i] = new Sprite(Helper.dataOperation(s.getPixels(),s.width,s.height, DataOperation.ROTATE_180), s.width, s.height,line[i].substring(0,line[i].indexOf(':')));
                            break;
                        case "3": tiles[_h][i] = new Sprite(Helper.dataOperation(s.getPixels(),s.width,s.height, DataOperation.ROTATE_270), s.height, s.width,line[i].substring(0,line[i].indexOf(':')));
                            break;
                        default:  tiles[_h][i] = herrmann.get(line[i]); break;

                    }
                } else if (line[i].contains("#")){ //<Bezeichner_Unten>#<Bezeichner_Oben>
                    Sprite s =  herrmann.get(line[i].substring(0,line[i].indexOf('#')));
                    Sprite s2=  herrmann.get(line[i].substring(line[i].indexOf('#')+1));

                    int[] data = s.getPixels(), data_s2 = s2.getPixels();
                    for(int y = 0; y < GameCore.getConfig().getTileSize(); y++){
                        for(int x = 0; x< GameCore.getConfig().getTileSize(); x++){
                            if(data_s2[x + GameCore.getConfig().getTileSize()*y] <0)
                                data[x + GameCore.getConfig().getTileSize()*y] = data_s2[x + GameCore.getConfig().getTileSize()*y];
                        }
                    }
                    tiles[_h][i] = new Sprite(data, GameCore.getConfig().getTileSize(), GameCore.getConfig().getTileSize(),line[i]);
                }
                else {
                    tiles[_h][i] = herrmann.get(line[i]);
                }
            }
        }
        rudolf.close();
        this.w = _w; this.h = _h;
        mapPath = path;
    }

    /**
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     * @param startx startPosition-X (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     * @param starty startPosition-Y (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     */
    public void render(int[] pixels, int startx, int starty) {
        for(int y = 0; y<Math.min(Math.min(tiles.length,h),GameCore.getConfig().getHeight()); y++){
            for(int x = 0; x<Math.min(Math.min(tiles[0].length,w),GameCore.getConfig().getWidth()); x++){
                if(tiles[y][x] != null)
                    tiles[y][x].render(pixels,x*GameCore.getConfig().getTileSize()+startx,y*GameCore.getConfig().getTileSize()+ starty);
            }
        }
    }

    /**
     * @brief Liefert den Bezeichner des Tiles in dem sich der Punkt befindet
     * @param e das Punktobjekt
     * @return der Name des Tiles
     */
    public String getTileName(iPoint e){
        return getTile(Math.floorDiv(e.getX(),GameCore.getConfig().getTileSize()), Math.floorDiv(e.getY(),GameCore.getConfig().getTileSize())).getName();
    }


    /**
     * @brief initialisierungs Shortcut
     *
     * @param dictonaryPath der Pfad zum dict für den Sprite-Übersetzer
     */
    @Internal
    private void init(String dictonaryPath){
        tiles = new Sprite[GameCore.getConfig().getHeight()/GameCore.getConfig().getTileSize()][GameCore.getConfig().getWidth()/GameCore.getConfig().getTileSize()];
        herrmann = new SpriteTranslator(dictonaryPath);
    }




}
