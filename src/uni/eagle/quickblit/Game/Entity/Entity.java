package uni.eagle.quickblit.Game.Entity;

import uni.eagle.quickblit.General.Interfaces.iPoint;
import uni.eagle.quickblit.General.Interfaces.iRenderable;
import uni.eagle.quickblit.Graphics.Sprite;

/**
 * @brief liefert die Entity Struktur, jede Kreatur/jedes Item erbt von dieser Kreatur
 */
public abstract class Entity implements iRenderable , iPoint {
    protected double x;
    protected double y;
    protected int height;
    protected int width;

    ///@brief die Grafiken aus denen es (gerade) basteht
    public Sprite[] s;

    /**
     * @brief Konstruktor
     * @param x Position
     * @param y Position
     * @param height des entity
     * @param width des entity
     */
    public Entity(int x,int y,int height,int width){
       this.x = x;
       this.y = y;
       this.height = height;
       this.width = width;
    }

    /**
     * @brief Überprüft ob es zu einer kollision kommt
     * @param x die x Koordinate
     * @param y die y Koordinate
     * @param height die Höhe des Objekts
     * @param width die Breite des Objekts
     * @return Ob sich die Objekte überschneiden
     */
    public boolean collision(int x, int y, int height, int width){
        return (this.x < x+width && this.x > x - this.width && this.y < y+height && this.y > y - this.height);
    }

    /**
     * Rendert die Grafiken, aus denen das Entity besteht
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     * @param startx startPosition-X (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     * @param starty startPosition-Y (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     */
    @Override
    public void render(int[] pixels, int startx, int starty) {
        for(Sprite s1 : s)
            s1.render(pixels,(int)Math.round(this.x+startx),(int)Math.round(this.y+starty));
    }
}
