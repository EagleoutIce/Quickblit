package uni.eagle.quickblit.Management;

import uni.eagle.quickblit.General.Helper;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @brief liefert Einstellungen
 */
public class Configuration {
    private int WIDTH=600;
    private int HEIGHT=450;
    private int FPS = 60;
    private int SCALE=2;
    private int TILESIZE=32;
    private boolean dFilter = false;
    private  String NAME = "";

    /**
     * @return Liefert die konfigurierten FPS
     */
    public int getFPS() { return FPS; }

    /**
     * @return Liefet die konfigurierte TileSize
     */
    public int getTileSize() { return TILESIZE; }

    /**
     * @return Soll das Bild durch Filter gejagt werden?
     */
    public boolean doFilter() {return dFilter; }

    /**
     * @return Grundlegender Skalierungs-Faktor für ALLES
     */
    public int getScale() { return SCALE; }

    /**
     * @return die konfigurierte Breite
     */
    public int getWidth() { return WIDTH; }

    /**
     * @return die konfigurierte Höhe
     */
    public int getHeight() { return HEIGHT; }

    /**
     * @return der konfigurierte Name
     */
    public String getName() { return NAME; }


    /**
     * @brief Konstrutkor lädt die Einstellungen aus einem gewissen Pfad
     *
     * @param loadPath der Pfad der Einstellungsdatei
     */
    public Configuration(String loadPath){
        Scanner configFile = new Scanner(this.getClass().getResourceAsStream(loadPath));
        String line;
        while(configFile.hasNextLine()){
            line = configFile.nextLine();
            if(line.length()>2 && !line.substring(0, 2).equals("//")){
                Pattern ptr = Pattern.compile("([^= ]*) *= *([^=]*)$"); //Matches: <BEZEICHNER> = <WERT>
                Matcher mtr = ptr.matcher(line);
                if(mtr.find() && mtr.groupCount()==2){
                    switch (mtr.group(1).toUpperCase()){
                        case "WIDTH":  WIDTH = Helper.toInteger(mtr.group(2)); break;
                        case "HEIGHT": HEIGHT = Helper.toInteger(mtr.group(2)); break;
                        case "FRAMES_PER_SECOND":
                        case "FPS": FPS = Helper.toInteger(mtr.group(2)); break;
                        case "SCALE": SCALE = Helper.toInteger(mtr.group(2)); break;
                        case "TILESIZE": TILESIZE = Helper.toInteger(mtr.group(2)); break;
                        case "NAME": NAME = mtr.group(2); break;
                        case "FILTER": dFilter = (mtr.group(2).equals("YES")); break;
                    }
                } else
                    System.out.println("\"" + line + "\" ist keine gültige Zuweisung");
            }
        }
        configFile.close();
        if(WIDTH<=0) WIDTH = 600;
        if(HEIGHT<=0) HEIGHT = WIDTH/12*9;
        if(SCALE<=0) SCALE = 2;
        if(FPS<=0) FPS =60;
        if(TILESIZE<=0) TILESIZE = 32;
    }

    /**
     * @brief lädt die Einstellungen aus der Standart-Datei
     */
    public Configuration(){
        this("/configs/config.configuration");
    }



}
