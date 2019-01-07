package uni.eagle.quickblit.Management;

import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @brief liefert einen Übersetzer der es ermöglicht Kartendaten auf Sprites zuzordnen
 */
public abstract class Translator<T1> {


    ///hält die Daten
    private HashMap<String, T1> dictonary = new HashMap<>();

    /**
     * @brief Soll ein neues Objekt der Ressource erstellen
     *
     * @param arg1 Erstes Argument (Bezeichner)
     * @param arg2 Zweites Argument (Wert)
     * @param arg3 Drittes Argument (Arg3)
     * @param arg4 Viertes Argument (Arg4)
     * @return das neue Objekt
     */
    abstract T1 createResource(String arg1, String arg2, String arg3, String arg4);

    /**
     * @brief Soll ein DummyObjekt erstellen
     *
     * @return das DummyObjekt
     */
    abstract T1 createDummy();


    /**
     * @brief Lädt aus gewissem Verzeichnis die Übersetzer Daten
     *
     * @param dictonaryPath Pfad des Verzeichnisses
     */
    public Translator(String dictonaryPath){
        Scanner dict = new Scanner(this.getClass().getResourceAsStream(dictonaryPath));
        System.out.println(dict);
        while(dict.hasNextLine()){
            String line = dict.nextLine();
            if(line.length()>2 && !line.substring(0, 2).equals("//")){
                Pattern ptr = Pattern.compile("^([^= ]*) *= *([^=]*):([^,]*),([^,]*)$"); //Matches <Bezeichner> = <Wert>:<Arg3>,<Arg4>
                Matcher mtr = ptr.matcher(line);
                if(mtr.find() && mtr.groupCount()==4){
                    // Gibt es die ressource
                    if(this.getClass().getResourceAsStream(mtr.group(2)) != null)
                        dictonary.put(mtr.group(1), createResource(mtr.group(1), mtr.group(2), mtr.group(3), mtr.group(4)));
                    else System.out.println("Bei: \"" + mtr.group(2) + "\" handelt es sich nicht um einge gültige Ressource") ;
                } else
                    System.out.println("\"" + line + "\" ist keine gültige Zuweisung");
            }
        }
        dict.close();
    }

    /**
     * @brief Liefert den Wert eines Schlüssels zurück
     *
     * @param mapKey der Schlüssel
     * @return der Wert
     */
    public T1 get(String mapKey){
        if(dictonary.containsKey(mapKey)) return dictonary.get(mapKey);
        else return createDummy(); //Wert gibt es nicht
    }

}
