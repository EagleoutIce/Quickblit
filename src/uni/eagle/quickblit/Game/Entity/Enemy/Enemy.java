package uni.eagle.quickblit.Game.Entity.Enemy;

import uni.eagle.quickblit.Game.Entity.Entity;
import uni.eagle.quickblit.Game.Player.Player;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.Management.Pathfinder;

import java.awt.*;
import java.util.ArrayList;

/**
 * @brief Erweitert die Entity-Klasse um Eigenschaften eines Gegners
 */
public abstract class Enemy extends Entity {
    /**
     * Konstruktur - konstruiert den Gegner grundlegend
     * @param x Startposition X
     * @param y Startposition Y
     * @param height Höhe
     * @param width Breite
     */
    public Enemy(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    /**
     * @brief der Pathfinder
     */
    public Pathfinder pathfinder;
    public Helper.Point targetPoint;

    double health, animation = 0, animationSpeed = 0.02;

    /**
     * @brief rendert den Gegner an der jeweiligen Position
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     * @param startx startPosition-X (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     * @param starty startPosition-Y (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     */
    @Override
    public void render(int[] pixels, int startx, int starty) {
        animation += animationSpeed;
        if(animation > super.s.length-1) animation = 0;
        s[(int)Math.round(animation)].render(pixels,(int)Math.round(super.x),(int)Math.round(super.y));
        if (targetPoint!=null){
                Painter.fillRect(pixels,targetPoint.getX()*GameCore.getConfig().getTileSize()-12,targetPoint.getY()* GameCore.getConfig().getTileSize()-12,24,24, Color.GREEN.getRGB());
        }
    }

    /**
     * @brief Testet, ob der Gegner sterben muss
     * @param enemies Die Liste aller Gegner
     * @return true wenn der Gegner sterben soll
     */
    public abstract boolean hasToDie(ArrayList<Enemy> enemies);

    /**
     * @brief bewegt den Gegner
     * @param p die Koordinaten des Players
     */
    public abstract void move(Player p);

}
