package uni.eagle.quickblit.Game.Entity.Enemy;

import uni.eagle.quickblit.Game.Game;
import uni.eagle.quickblit.Game.Player.Player;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.Graphics.Map.Map;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;
import uni.eagle.quickblit.Management.Pathfinder;

import java.util.ArrayList;

import static uni.eagle.quickblit.General.GameCore.rnd;

/**
 * @class Ghost
 * @brief eine Beispielimplementation dafür, wie man Gegner erstellen kann
 */
public class Ghost extends Enemy {


    private ArrayList<Enemy> enemies;

    /**
     * @brief Konstruktor
     * @param x x Position (start)
     * @param y y Position (start)
     * @param player der Player
     * @param map die Karte
     */
    public Ghost(int x, int y, Player player, ArrayList<Enemy> enemies, Map map) {

        super(x, y, 32, 32);
        super.s = new Sprite[3];
        super.s[0] = new Sprite(32,32,new SpriteSheet("/graphics/enemys/260.010_lilhaunt.png"),0,0,"enemy");
        super.s[1] = new Sprite(32,32,new SpriteSheet("/graphics/enemys/260.010_lilhaunt.png"),32,0,"enemy");
        super.s[2] = new Sprite(32,32,new SpriteSheet("/graphics/enemys/260.010_lilhaunt.png"),0,32,"enemy");
        super.health = 10;
        this.enemies = enemies;
        pathfinder = new Pathfinder(this,player,map,new String[]{"#"}, Pathfinder.Mode.NORMAL);
    }

    /**
     * @brief Da das Schadensystem für den Spieler nicht implementiert wurde, können Gegner nicht sterben
     * @param enemies Die Liste aller Gegner
     * @return immer false => immortal
     */
    @Override
    public boolean hasToDie(ArrayList<Enemy> enemies) {
        return false;
    }

    private boolean doesCollide() {
        for(Enemy e : enemies){
            if(!e.equals(this))
            if(e.collision(getX(), getY(), this.height,this.width))
                return true;

        }
        return false;
    }

    /**
     * @brief bewegt den Gegner
     * @param p die Koordinaten des Players
     */
    @Override
    public void move(Player p) {
        int bx = getX(), by = getY();
        targetPoint = this.pathfinder.calculateMove();
        if(getX() != targetPoint.getX())
        this.x += (targetPoint.getX()* GameCore.getConfig().getTileSize() -this.x)/120;
        if(getY() != targetPoint.getY())
        this.y += (targetPoint.getY()* GameCore.getConfig().getTileSize() -this.y)/120;
        if(bx==getX() && by == getY()){
            this.x += (p.getX()-this.x)/100;
            this.y += (p.getY()-this.y)/100;
        }
    }

    /**
     * @return liefert die aktuelle X position
     */
    @Override
    public int getX() {
        return (int) Math.round(this.x);
    }

    /**
     * @return liefert die aktuelle y position
     */
    @Override
    public int getY() {
        return (int) Math.round(this.y);
    }
}
