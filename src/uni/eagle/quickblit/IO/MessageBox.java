package uni.eagle.quickblit.IO;

import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.General.Interfaces.iPoint;
import uni.eagle.quickblit.General.Interfaces.iRenderable;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.Graphics.Sprite;


/**
 * @brief ermöglicht es eine Nachrichten Meldung auf den Bildschirm zu klatschen
 */
public class MessageBox implements iRenderable, iPoint {

    private int posX,posY,w,h,scaling = 1;
    private int back=1,front=1;
    private Sprite back_img;
    private boolean show = false;
    private String text = "",unformatted_text = "";
    private FontController _font = null;

    /**
     * @return Liefert den Unformatierten Text - der Gerade gesetzt ist.
     */
    public String getText() {return unformatted_text;}

    /**
     * @return Liefert die aktuell verwendete Schrift
     */
    public FontController getFont() {return _font;}

    /**
     * @brief Intialisiert die MessageBox
     *
     * @param x X-Position
     * @param y Y-Position
     * @param width Breite
     * @param height Höhe (nur für den Hintergrund relevant)
     */
    public MessageBox(int x, int y, int width, int height){ this(x,y,width,height,1,null,1);}

    /**
     * @brief Intialisiert die MessageBox
     *
     * @param x X-Position
     * @param y Y-Position
     * @param width Breite
     * @param height Höhe (nur für den Hintergrund relevant)
     * @param backgroundColor Hintergrundfarbe im RGB-Format. Wert > 0 für Transparent
     * @param foregroundColor Vordergrundfarbe im RGB-Format. Wert > 0 für Transparent
     */
    public MessageBox(int x,int y, int width, int height, int backgroundColor, int foregroundColor){ this(x,y,width,height,backgroundColor,null,foregroundColor); }

    /**
     * @brief Intialisiert die MessageBox
     *
     * @param x X-Position
     * @param y Y-Position
     * @param width Breite
     * @param height Höhe (nur für den Hintergrund relevant)
     * @param backImage Hintergrund-Bild
     * @param foregroundColor Vordergrundfarbe im RGB-Format. Wert > 0 für Transparent
     */
    public MessageBox(int x,int y, int width, int height, Sprite backImage, int foregroundColor){ this(x,y,width,height,1,backImage,foregroundColor);}

    /**
     * @brief Intialisiert die MessageBox
     *
     * @param x X-Position
     * @param y Y-Position
     * @param width Breite
     * @param height Höhe (nur für den Hintergrund relevant)
     * @param backgroundColor Hintergrundfarbe im RGB-Format. Wert > 0 für Transparent
     * @param backImage Hintergrund-Bild
     * @param foregroundColor Vordergrundfarbe im RGB-Format. Wert > 0 für Transparent
     */
    public MessageBox(int x,int y, int width, int height, int backgroundColor, Sprite backImage, int foregroundColor){
        posX = x;  posY = y; w = width; h = height; back = backgroundColor; front = foregroundColor; setBackImage(backImage);
    }

    /**
     * @brief Setzt ein neues Hintergrundbild
     *
     * @param backImage das Hintergrundbild
     */
    public void setBackImage(Sprite backImage){
        if(backImage != null) back_img = new Sprite(backImage.getPixels(),w,h, backImage.getName() + " (MessageBoxBackground)");
    }

    /**
     * @brief Setzt neue Farben
     *
     * @param backgroundColor neue Hintergrundfarbe im RGB-Format. Wert > 0 für Transparent
     * @param foregroundColor neue Vordergrundfarbe im RGB-Format. Wert > 0 für Transparent
     */
    public void setColors(int backgroundColor, int foregroundColor){
        back = backgroundColor; front = foregroundColor;
    }

    /**
     * @brief Setzt den Text der angezeigt werden soll (!wird nicht direkt durch rendern angezeigt)
     *
     * @param text der Text
     * @return den formatierten String
     */
    public String setText(String text){
        return setText(GameCore.fontController,text,true,1);
    }

    /**
     * @brief Setzt den Text der angezeigt werden soll (!wird nicht direkt durch rendern angezeigt)
     *
     * @param font die zu verwendende Schriftart
     * @param text der Text
     * @return den formatierten String
     */
    public String setText(FontController font, String text){
        return  setText(font, text,true,1);
    }

    /**
     * @brief Setzt den Text der angezeigt werden soll (!wird nicht direkt durch rendern angezeigt)
     *
     * @param font die zu verwendende Schriftart
     * @param txt der Text
     * @param fit soll die Zeichenkette an die Breite der MessageBox angepasst werden?
     * @param scale die Schriftgröße die verwendet werden soll
     * @return den formatierten String
     */
    public String setText(FontController font, String txt, boolean fit,int scale){
        unformatted_text = txt; scaling = scale; show = false;
        text=""; int curPos = 0; _font = font;
        for(int i = 0; i < txt.length(); i++){
            if(txt.charAt(i)=='\033') curPos-= 2;
            if(txt.charAt(i)=='\n') curPos = 0;
            if(fit && ++curPos>=(Math.floorDiv(w,_font.getCharWidth()*scaling))-2){
                curPos=0; text+="\n";
            }
            text+=txt.charAt(i);
        }
        return "";
    }

    //Teletype - Stats
    private int type_to = 0 , interval = 0, current_counter = 0;

    /**
     * @brief Rendert den Text mit Tipp-Animation
     *
     * @param framesBetweenType Geschwindikeit der Tipp-Animation
     */
    public void teletype(int framesBetweenType){
        show = true; type_to = 0; interval = Helper.forceInBetween(framesBetweenType,0,1024); current_counter = 0;
    }

    /**
     * @brief Zeigt den Text der Message-Box an
     */
    public void show(){
        show = true; type_to = text.length();
    }

    /**
     * @brief Versteckt die Message-Box wieder
     */
    public void hide(){
        show = false;
    }

    /**
     * @return ob der ganze Text angezeigt wird.s
     */
    public boolean isFinished(){ return type_to==text.length();}

    /**
     * @brief Skippt eine eventuelle Tipp-Animation
     */
    public void skipTeletype(){
        type_to = text.length();
    }

    /**
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     * @param startx startPosition-X (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     * @param starty startPosition-Y (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     */
    @Override
    public void render(int[] pixels, int startx, int starty) {
        if(!show) return;
        if(back < 0) { Painter.fillRect(pixels,posX+startx,posY+starty,w,h,back);}
        if(back_img != null) {back_img.render(pixels,posX+startx,posY+starty);}
        if(front < 0) {_font.drawString(pixels,text.substring(0,type_to),posX+startx,posY+starty,scaling,front);}
        else _font.drawString(pixels,text.substring(0,type_to),posX+startx,posY+starty,scaling,front);

        if(type_to<text.length() && (++current_counter > interval)) {
                type_to++; current_counter = 0;
        }

    }

    /**
     * @return XPosition
     */
    @Override
    public int getX() {
        return posX;
    }

    /**
     * @return YPosition
     */
    @Override
    public int getY() {
        return posY;
    }
}
