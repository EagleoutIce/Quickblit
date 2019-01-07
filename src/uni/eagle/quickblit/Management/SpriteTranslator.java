/**
 * @brief liefert generelle Informationen, die die Handhabung mit den Daten (enorm) vereinfachen
 *
 */
package uni.eagle.quickblit.Management;

import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;

/**
 * @brief Liefert einen Übersetzer für Sprite informationen
 */
public class SpriteTranslator extends Translator<Sprite> {

    /**
     * @brief Konstruktor
     *
     * @param path der Pfad zur Wörterbuchdatei
     */
    public SpriteTranslator(String path){super(path);}

    /**
     * Erstellt die Ressource
     * @param arg1 Erstes Argument (Bezeichner)
     * @param arg2 Zweites Argument (Wert)
     * @param arg3 Drittes Argument (Arg3)
     * @param arg4 Viertes Argument (Arg4)
     * @return die jeweilige Sprite Ressource
     */
    @Override
    Sprite createResource(String arg1, String arg2, String arg3, String arg4) {
        return new Sprite(GameCore.getConfig().getTileSize(),GameCore.getConfig().getTileSize(), new SpriteSheet(arg2), Helper.toInteger(arg3,0), Helper.toInteger(arg4,0),arg1);
    }

    /**
     * @return die Dummy-Textur
     */
    @Override
    Sprite createDummy() {
        return new Sprite(32,32, new SpriteSheet("/graphics/testimages/hedgehog.png"),100,100,"DUMMY");
    }
}
