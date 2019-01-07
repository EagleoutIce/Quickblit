/**
 * @brief liefert alle Klassen die für die Daten Ein- und Ausgabe benötigt werden
 *
 */
package uni.eagle.quickblit.IO;

import uni.eagle.quickblit.General.Annotiations.Internal;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;

import java.awt.*;
import java.util.HashMap;
import java.util.Scanner;


/**
 * @brief Stellt einen Controller zur Verfügung der eine Schriftart aus einer Bilddatei laden kann und eine small-Schriftart mitliefert
 */
public class FontController {
    ///@brief die vordefinierte Schriftart
    private static HashMap<String,int[][]> small_font;
    ///@brief die geladene Schriftart
    private HashMap<String,int[][]> default_font = new HashMap<>();

    //char width & char height
    private int cW, cH;

    /**
     * @return die Breite eines Zeichens der geladenen Schriftart
     */
    public int getCharWidth() { return cW; }

    /**
     * @return die Höhe eines Zeichens der geladenen Schriftart
     */
    public int getCharHeight() { return cH; }


    /**
     * @brief Konstruktor lädt die Standartschriftart
     */
    public FontController(){
        loadFont("/data/font_data/default_font.png","/data/font_data/default_font.data");
    }

    /**
     * Konstruktur lädt eine gezielte Schriftart
     * @param fontPath der Pfad zur Bilddatei
     * @param fontData der Pfad zur Datendatei
     */
    public FontController(String fontPath, String fontData){
        loadFont(fontPath,fontData);
    }


    /**
     * @brief Bringt eine Zeichenkette auf den Bildschirm. Akzeptiert '\n' nutzt die vordefinierte Schriftart
     *
     * @param pixels Array auf das der String gezeichnet werden soll
     * @param string der String der gezeichnet werden soll. Unbekannte Zeichen werden zu ?
     * @param x start-position X
     * @param y start-position Y
     */
    public static void drawSmallString(int[] pixels, String string, int x, int y){ drawSmallString(pixels, string, x,y, 1,1); }

    /**
     * @brief Bringt eine Zeichenkette auf den Bildschirm. Akzeptiert '\n' nutzt die vordefinierte Schriftart
     *
     * @param pixels Array auf das der String gezeichnet werden soll
     * @param string der String der gezeichnet werden soll. Unbekannte Zeichen werden zu ?
     * @param x start-position X
     * @param y start-position Y
     * @param scale die Skalierung der Schriftart
     */
    public static void drawSmallString(int[] pixels, String string, int x, int y,int scale){ drawSmallString(pixels, string, x,y, scale,1); }

    /**
     * @brief Bringt eine Zeichenkette auf den Bildschirm. Akzeptiert '\n' nutzt die vordefinierte Schriftart
     *
     * @param pixels Array auf das der String gezeichnet werden soll
     * @param string der String der gezeichnet werden soll. Unbekannte Zeichen werden zu ?
     * @param x start-position X
     * @param y start-position Y
     * @param scale die Skalierung der Schriftart
     * @param color die Farbe in der die Schriftart eingefärbt werden sollen. Soll im RGB format übermittelt werden
     */
    public static void drawSmallString(int[] pixels, String string, int x, int y, int scale,int color){
        int posx = x, posy = y;
        for(int i = 0; i < string.length(); i++){
            int[][] c;
            if(string.charAt(i)=='\n') { posy+=5+getSpacing()*2; posx = x; continue; }
            c = getSmall(""+string.charAt(i));
            posx = renderNext(pixels, posy, scale, color, posx, c);
        }
    }

    /**
     * @brief Bringt eine Zeichenkette auf den Bildschirm. Akzeptiert '\n' nutzt die geladene Schriftart
     *
     * @param pixels Array auf das der String gezeichnet werden soll
     * @param string der String der gezeichnet werden soll. Unbekannte Zeichen werden zu ?
     * @param x start-position X
     * @param y start-position Y
     */
    public void drawString(int[] pixels, String string, int x, int y){ drawString(pixels,string,x,y,1,1); }

    /**
     * @brief Bringt eine Zeichenkette auf den Bildschirm. Akzeptiert '\n' nutzt die geladene Schriftart
     *
     * @param pixels Array auf das der String gezeichnet werden soll
     * @param string der String der gezeichnet werden soll. Unbekannte Zeichen werden zu ?
     * @param x start-position X
     * @param y start-position Y
     * @param scale die Skalierung der Schriftart
     */
    public void drawString(int[] pixels, String string, int x, int y,int scale){ drawString(pixels,string,x,y,scale,1); }

    /**
     * @brief Bringt eine Zeichenkette auf den Bildschirm. Akzeptiert '\n' nutzt die geladene Schriftart
     *
     * @param pixels Array auf das der String gezeichnet werden soll
     * @param string der String der gezeichnet werden soll. Unbekannte Zeichen werden zu ?
     * @param x start-position X
     * @param y start-position Y
     * @param scale die Skalierung der Schriftart
     * @param color die Farbe in der die Schriftart eingefärbt werden sollen. Soll im RGB format übermittelt werden
     */
    public void drawString(int[] pixels, String string, int x, int y, int scale,int color){
        int posx = x, posy = y; int tCol = color;
        for(int i = 0; i < string.length(); i++){
            int[][] c;
            if(string.charAt(i)=='\n') { posy+=cH*scale; posx = x; continue; }
            if(string.charAt(i)=='\033'){
                if(string.length()>i+1){
                    switch (string.charAt(i+1)){
                        case 'r':
                        default: tCol = 1; break;
                        case 'R': tCol = new Color(255, 0, 29).getRGB(); break;
                        case 'M': tCol = new Color(255, 0, 159).getRGB(); break;
                        case 'B': tCol = new Color(107, 152, 255).getRGB(); break;
                        case 'G': tCol = new Color(143, 199, 42).getRGB(); break;
                        case 'O': tCol = new Color(255, 209, 87).getRGB(); break;
                        case 'I': tCol = new Color(255, 250, 0).getRGB(); break;
                    }
                    i++;
                }
                continue;
            }
            c = get(""+string.charAt(i));
            posx = renderNext(pixels, posy, scale, tCol, posx, c);
        }
    }


    /**
     * @brief Zeichnet den nächsten Buchstaben an der jeweiligen Position
     *
     * @param pixels Array auf das gezeichnet werden soll
     * @param y Zeilenposition
     * @param scale Skalierungsfaktor
     * @param color Farbe
     * @param posx Aktuelle Spaltenposition
     * @param c Schriftzeichen
     * @return die neue Spaltenposition.
     */
    @Internal
    private static int renderNext(int[] pixels, int y, int scale, int color, int posx, int[][] c) {
        if(color>0) Painter.drawData(pixels,c,posx,y,scale);
        else Painter.drawDataColored(pixels,c,posx,y,scale,color);
        posx+=scale*(c[0].length+getSpacing());
        return posx;
    }

    //Intern verwendete Farben - Zur Klarheit
    private static int o = Color.OPAQUE;
    private static int W = Color.WHITE.getRGB();
    private static int R = Color.RED.getRGB();
    private static int G = Color.GREEN.getRGB();
    private static int B = Color.BLUE.getRGB();

    /**
     * @brief gibt ein Zeichen der vordefinierten Schriftart zurück.
     * @param key das Zeichen, was gefordert wird.
     * @return das Zeichen als daten-Array
     */
    public static int[][] getSmall(String key){
        if(key.equals(" ")) return new int[][] {{o,o,o}};
        if(small_font==null) loadSmallFont();
        if(small_font.containsKey(key))
            return small_font.get(key);
        else return new int[][]{ //Zeichen unbekannt
                {o,W,W,o},
                {o,o,o,W},
                {o,W,W,o},
                {o,W,o,o},
                {o,o,o,o},
                {o,W,o,o}

        };
    }

    /**
     * @brief gibt ein Zeichen der geladenen Schriftart zurück.
     * @param key das Zeichen, was gefordert wird.
     * @return das Zeichen als daten-Array
     */
    public int[][] get(String key){
        if(key.equals(" ")) return new int[][] {{o,o,o,o,o}};
        if(default_font.containsKey(key))
            return default_font.get(key);
        else return new int[][]{ //Zeichen unbekannt
                {W,W,o,o,W,W,W,o,W,o,W,W,o,G,G},
                {W,o,o,o,o,o,o,o,o,o,B,o,o,o,G},
                {R,R,o,o,W,W,W,o,o,W,W,o,o,W,W},
                {R,o,o,o,o,W,o,o,o,o,B,o,o,o,W},
                {o,o,o,o,o,o,o,o,o,o,B,o,o,o,o},
                {o,o,o,o,o,o,o,o,o,o,B,o,o,o,o},
                {W,o,o,o,o,W,o,o,o,o,B,o,o,o,W},
                {W,W,o,o,W,W,W,o,o,W,W,o,o,W,W},
                {W,o,o,o,o,W,o,o,o,o,B,o,o,o,W},
                {o,o,o,o,o,o,o,o,o,o,B,o,o,o,o},
                {o,o,o,o,o,o,o,o,o,o,B,o,o,o,o},
                {W,o,o,o,o,W,o,o,o,o,B,o,o,o,W},
                {W,W,o,o,W,W,W,o,o,W,W,o,o,W,W},
                {W,o,o,o,o,o,o,o,o,o,B,o,o,o,R},
                {W,W,o,o,W,W,W,o,W,o,W,W,o,R,R},

        };
    }

    /**
     * @brief liefert den Abstand zwischen zwei Zeichen zurück - wird eigentlich nur zur Klarheit verwendet (es wurde davon abgesehen das ganze direkt über Scaling zu berechnen)
     * @return den Abstand zwischen zwei Zeichen
     */
    public static int getSpacing(){ return 1; }

    /**
     * @brief Setzt die kleine Schriftart welche Von Hand eingebaut wurde. sie Stellt zudem Symbole zur Verfügung und kann dementsprechend modifiziert werden
     */
    @Internal
    private static void loadSmallFont(){
        small_font = new HashMap<>();
        //Default font-map von Florian Sihler
        small_font.put("A",new int[][]{
                {o,W,o},
                {W,o,W},
                {W,W,W},
                {W,o,W},
                {W,o,W}});
        small_font.put("B",new int[][]{{W,W,o}, {W,o,W}, {W,W,o}, {W,o,W}, {W,W,o}});
        small_font.put("C",new int[][]{{o,W,W}, {W,o,o}, {W,o,o}, {W,o,o}, {o,W,W}});
        small_font.put("D",new int[][]{{W,W,o}, {W,o,W}, {W,o,W}, {W,o,W}, {W,W,o}});
        small_font.put("E",new int[][]{{W,W,W}, {W,o,o}, {W,W,W}, {W,o,o}, {W,W,W}});
        small_font.put("F",new int[][]{{W,W,W}, {W,o,o}, {W,W,W}, {W,o,o}, {W,o,o}});
        small_font.put("G",new int[][]{{W,W,W,W}, {W,o,o,o}, {W,o,W,W}, {W,o,o,W}, {o,W,W,o}});
        small_font.put("H",new int[][]{{W,o,W}, {W,o,W}, {W,W,W}, {W,o,W}, {W,o,W}});
        small_font.put("I",new int[][]{{W,W,W}, {o,W,o}, {o,W,o}, {o,W,o}, {W,W,W}});
        small_font.put("J",new int[][]{{W,W,W}, {o,o,W}, {o,o,W}, {W,o,W}, {o,W,o}});
        small_font.put("K",new int[][]{{W,o,W}, {W,o,W}, {W,W,o}, {W,o,W}, {W,o,W}});
        small_font.put("L",new int[][]{{W,o,o}, {W,o,o}, {W,o,o}, {W,o,o}, {W,W,W}});
        small_font.put("M",new int[][]{{o,W,o,W,o}, {W,o,W,o,W}, {W,o,W,o,W}, {W,o,o,o,W}, {W,o,o,o,W}});
        small_font.put("N",new int[][]{{W,o,o,W}, {W,W,o,W}, {W,W,W,W}, {W,o,W,W}, {W,o,o,W}});
        small_font.put("O",new int[][]{{W,W,W}, {W,o,W}, {W,o,W}, {W,o,W}, {W,W,W}});
        small_font.put("P",new int[][]{{W,W,W}, {W,o,W}, {W,W,W}, {W,o,o}, {W,o,o}});
        small_font.put("Q",new int[][]{{W,W,W,W}, {W,o,o,W}, {W,o,W,W}, {W,W,W,W}, {o,o,o,W}});
        small_font.put("R",new int[][]{{W,W,W}, {W,o,W}, {W,W,o}, {W,o,W}, {W,o,W}});
        small_font.put("S",new int[][]{{W,W,W}, {W,o,o}, {W,W,W}, {o,o,W}, {W,W,W}});
        small_font.put("T",new int[][]{{W,W,W}, {o,W,o}, {o,W,o}, {o,W,o}, {o,W,o}});
        small_font.put("U",new int[][]{{W,o,o,W}, {W,o,o,W}, {W,o,o,W}, {W,o,o,W}, {W,W,W,W}});
        small_font.put("V",new int[][]{{W,o,o,W}, {W,o,o,W}, {W,o,o,W}, {W,o,o,W}, {o,W,W,o}});
        small_font.put("W",new int[][]{{W,o,o,o,W}, {W,o,W,o,W}, {W,o,W,o,W}, {W,o,W,o,W}, {o,W,o,W,o}});
        small_font.put("X",new int[][]{{W,o,o,W}, {o,W,W,o}, {o,W,W,o}, {o,W,W,o}, {W,o,o,W}});
        small_font.put("Y",new int[][]{{W,o,W}, {W,o,W}, {o,W,o}, {o,W,o}, {o,W,o}});
        small_font.put("Z",new int[][]{{W,W,W}, {o,o,W}, {o,W,o}, {W,o,o}, {W,W,W}});
        small_font.put("0",new int[][]{{W,W,W}, {W,o,W}, {W,o,W}, {W,o,W}, {W,W,W}});
        small_font.put("1",new int[][]{{o,W,W}, {W,o,W}, {o,o,W}, {o,o,W}, {o,W,W}});
        small_font.put("2",new int[][]{{W,W,W}, {o,o,W}, {W,W,W}, {W,o,o}, {W,W,W}});
        small_font.put("3",new int[][]{{W,W,W}, {o,o,W}, {W,W,W}, {o,o,W}, {W,W,W}});
        small_font.put("4",new int[][]{{W,o,W}, {W,o,W}, {W,W,W}, {o,o,W}, {o,o,W}});
        small_font.put("5",new int[][]{{W,W,W}, {W,o,o}, {W,W,o}, {o,o,W}, {W,W,o}});
        small_font.put("6",new int[][]{{W,W,W}, {W,o,o}, {W,W,W}, {W,o,W}, {W,W,W}});
        small_font.put("7",new int[][]{{W,W,W}, {o,o,W}, {o,W,W}, {o,o,W}, {o,o,W}});
        small_font.put("8",new int[][]{{W,W,W}, {W,o,W}, {W,W,W}, {W,o,W}, {W,W,W}});
        small_font.put("9",new int[][]{{W,W,W}, {W,o,W}, {W,W,W}, {o,o,W}, {W,W,W}});
        small_font.put("+",new int[][]{{o,o,o}, {o,W,o}, {W,W,W}, {o,W,o}, {o,o,o}});
        small_font.put("-",new int[][]{{o,o,o}, {o,o,o}, {W,W,W}, {o,o,o}, {o,o,o}});
        small_font.put("*",new int[][]{{o,o,o}, {W,o,W}, {o,W,o}, {W,o,W}, {o,o,o}});
        small_font.put("/",new int[][]{{o,o,o,W}, {o,o,W,W}, {o,W,W,o}, {W,W,o,o}, {W,o,o,o}});
        small_font.put("#",new int[][]{{o,W,W,o,o}, {W,o,o,o,o}, {o,W,G,o,o}, {o,G,R,G,o}, {o,o,G,o,o}});
        small_font.put("]",new int[][]{{o,W,W}, {o,o,W}, {o,o,W}, {o,o,W}, {o,W,W}});
        small_font.put("[",new int[][]{{W,W,o}, {W,o,o}, {W,o,o}, {W,o,o}, {W,W,o}});
        small_font.put("!",new int[][]{{o,W,o}, {o,W,o}, {o,W,o}, {o,o,o}, {o,W,o}});
        small_font.put(",",new int[][]{{o,o,o}, {o,o,o}, {o,W,o}, {o,W,o}, {W,o,o}});
        small_font.put(".",new int[][]{{o,o,o}, {o,o,o}, {o,o,o}, {W,W,o}, {W,W,o}});
        small_font.put(":",new int[][]{{o,o,o}, {o,W,o}, {o,o,o}, {o,W,o}, {o,W,o}});

        small_font.put("❤",new int[][]{
                {o,R,o,R,o},
                {R,R,R,R,R},
                {R,R,R,R,R},
                {o,R,R,R,o},
                {o,o,R,o,o}});
    }

    /**
     * @brief Lädt die Schriftart
     * @param fontPath Pfad zur Bilddatei
     * @param fontData Pfad zur Datendatei
     */
    @Internal
    private void loadFont(String fontPath, String fontData){
        SpriteSheet font = new SpriteSheet(fontPath);
        Scanner data = new Scanner(this.getClass().getResourceAsStream(fontData));
        String fData = "1508ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜabcdefghijklmnopqrstuvwxyzäöü0123456789,.?!*/|[]{}❤";
        if(data.hasNextLine())
            fData = data.nextLine();
        data.close();
        cH = Math.max(Helper.toInteger(fData.substring(0,2),0),1);
        cW = Math.max(Helper.toInteger(fData.substring(2,4),0),1);

        fData = fData.substring(4);
        //System.out.println(cW + "  " + cH + " " + fData);
        for(int i = 0; i < Math.min(fData.length(),font.getW()/cW); i++){
            //System.out.println(""+fData.charAt(i));
            default_font.put(""+fData.charAt(i),new Sprite(cW,cH,font,cW*i,0,""+fData.charAt(i)).getPixels2D());
        }
    }
}
