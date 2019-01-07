/**
 * @brief die Spiel-Demonstration der Engine
 */
package uni.eagle.quickblit.Game;

import uni.eagle.quickblit.Game.Entity.Enemy.Enemy;
import uni.eagle.quickblit.Game.Entity.Enemy.Ghost;
import uni.eagle.quickblit.Game.Entity.Items.Item;
import uni.eagle.quickblit.Game.Player.Direction;
import uni.eagle.quickblit.Game.Player.Player;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.Graphics.Map.Map;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.IO.FontController;
import uni.eagle.quickblit.IO.MessageBox;
import uni.eagle.quickblit.Management.Configuration;
import uni.eagle.quickblit.Management.Pathfinder;

import java.awt.*;
import java.util.ArrayList;

/**
 * @file Game.java
 *
 * @brief liefert die Spieledemo
 */


/**
 * @brief die Game Klasse, sie erbt von GameCore und stellt so das Spiel zur Verfügung. Das erstellen eines Objektes der Klasse startet das Spiel
 */
public class Game extends GameCore {
    private Game(){
        super.start();
    }
    private Map map;
    private Player player = new Player();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private long ticks,elapsed, start=0;
    //Für den Introtext
    private MessageBox intro = new MessageBox(10,10,GameCore.getConfig().getWidth()-30,GameCore.getConfig().getHeight()-30);
    Pathfinder pathfinder,pathfinder2;
    Helper.Point pt, pt2;
    /**
     * @brief lädt den Introtext und initialisiert die Karte
     */
    @Override
    public void onInit() {
        map = new Map("/data/map_data/game_default.map", "/configs/game.dict");
        intro.setText("Das folgende Spiel ist eine Demo für die \033OQuickblit-Engine\033r." +
                "\nSie wurde, wie gefordert, nur in der Vorlesungsfreienzeit erstellt." +
                "\nSie kommt zudem mit der \033IFirework-Demo\033r, welche für Silvester gebaut\nwurde und ein Feuerwerk in der Engine simuliert." +
                "\nDie im Spiel verwendeten Grafiken entstammen dem Spiel \033R\"The Binding of Isaac\"\033r und wurden mithilfe des mitgelieferten Extractor" +
                "\nextrahiert. Andere Daten wie die Schrift wurden für die Engine mithilfe von \033GGIMP\033r erstellt." +
                "\nAlle Render-Operationen werden auf einem Integer-Array durchgeführt und von der Engine zur Verfügung gestellt." +
                "\nZur Anzeige des Arrays wird von der \033RJava-AWT\033r Bibliothek gebrauch gemacht.\nDa die Zeit begrenzt war, " +
                "\nliefert die Engine eingige Funktionen wie einen \033IPathfinder\033r oder " +
                "\n\033BSound-Controller\033r, welche ihren Weg nichtmehr in die Demo geschafft haben." +
                "\nDennoch ist das ganze Paket als Sandbox zu verstehen.\nDie Dokumentation wurde im \033CDoxygen\033r-Style geschrieben. " +
                "\nHinzuzufügen sei nur noch, dass die Engine an sich mit viel ❤ aufgebaut wurde und sich grundlegend dazu eignet an Applets für die" +
                "\nWebanwendung angepasst zu werden. Dieses Intro kann mit \"E\" übersprungen werden!" +
                "                                                                                                                        ");
        intro.teletype(3);
        /*
        items.add(Item.generateItem(400,200,32,32));
        items.add(Item.generateItem(450,200,32,32));
        items.add(Item.generateItem(350,200,32,32));
        items.add(Item.generateItem(400,250,32,32));
        items.add(Item.generateItem(400,300,32,32));
        items.add(Item.generateItem(400,100,32,32));
        enemies.add(new Ghost(3*GameCore.getConfig().getTileSize()+64,4*GameCore.getConfig().getTileSize()+64,Player,map));
        */

        /*
         * Pathfinder example
         */

        //pathfinder = new Pathfinder(enemies.get(0),Player,map,new String[]{"#"}, Pathfinder.Mode.NORMAL);
        //pathfinder2 = new Pathfinder(enemies.get(0),Player,map,new String[]{"#"}, Pathfinder.Mode.RIGHT_ANGLE_ONLY);

    }

    /**
     * @param frames Anzahl an FPS im vergangenen Zyklus
     * @param updates Anzahl an Updates im vergangenen Zyklus
     * @param elapsedTime Zeit, die seit dem starten des Spiels vergangen ist
     * @return true wenn das Rendern forciert werden soll. false wenn nicht
     */
    @Override
    public boolean onTickStart(long frames, long updates, long elapsedTime) {
        ticks= updates; elapsed = elapsedTime-start; return false;
    }


    /**
     * @return true wenn das Rendern forciert werden soll. false wenn nicht
     */
    @Override
    public boolean onUpdate() {
        if(intro.isFinished()){ //das intro ist fertig? => Verstecken
            intro.hide();
        } else{ start = elapsed; return true;  }//Solange es nicht fertig ist, läuft die Zeit nicht
        updateMovement();
        if(switchRoom()){
            loadNew();
        }
        items.removeIf(e -> e.pickedUp((int)Math.round(player.get_rawX()),(int)Math.round(player.get_rawY()),16,12,this.player));
        for(Enemy e : enemies) e.move(this.player);
        for(Enemy e : enemies) if(e.collision((int)player.getX(),(int) player.getY(),32,32)) player.setHealth(player.getHealth()-10);
        //pt = pathfinder.calculateMove();

        /*System.out.println("Pathfinder predicts: +(" + pt.getX() + "," + pt.getY() + ") from: ("
                + pathfinder.getObjPoint().getX() + "," + pathfinder.getObjPoint().getY() + ") tar: (" +
                pathfinder.getTarPoint().getX() + "," + pathfinder.getTarPoint().getY() + ")");*/
        //pt2 = pathfinder2.calculateMove();

        return true;
    }

    /**
     * @param pixels das Pixel-Array auf das gezeichnet wird
     */
    @Override
    public void onRender(int[] pixels) {
        map.render(pixels,0,0);
        if(player.getHealth()<0) {
            fontController.drawString(pixels,"Game Over",getConfig().getWidth()/2-190,getConfig().getHeight()/2-100,5,new Color(239, 0, 127).getRGB());
            return;
        }

            intro.render(pixels);
        if(!intro.isFinished()) {
            if(inputHandler.event.isPressed()) intro.skipTeletype();
            FontController.drawSmallString(pixels,"QUICKBLIT - VERISON: " + CVERSION,GameCore.getConfig().getWidth()-200,GameCore.getConfig().getHeight()-40);
            return;
        }
        for(Item e : items) e.render(pixels,10,4);
        player.render(pixels);
        for(Enemy e : enemies) e.render(pixels);
        FontController.drawSmallString(pixels,"TICKS: " + ticks , GameCore.getConfig().getWidth()-("Ticks: xxxx".length()*5),0);
        FontController.drawSmallString(pixels,"TOTAL TIME: " + elapsed/(1000000000.0) + "S" , 0,0);

        //if(enemies.size()>0){
        //    Painter.fillRect(pixels,enemies.get(0).pathfinder.calculateMove().getX()*GameCore.getConfig().getTileSize()-16,enemies.get(0).pathfinder.calculateMove().getY()*GameCore.getConfig().getTileSize()-16,32,32,Color.GREEN.getRGB());
        //}
       //Painter.fillRect(pixels,pt.getX()*GameCore.getConfig().getTileSize()-16,pt.getY()*GameCore.getConfig().getTileSize()-16,32,32,Color.GREEN.getRGB());
       //Painter.fillRect(pixels,pt2.getX()*GameCore.getConfig().getTileSize()-8,pt2.getY()*GameCore.getConfig().getTileSize()-8,16,16,Color.MAGENTA.getRGB());

    }

    /**
     * @brief Überprüft, ob ein neuer Raum betreten werden soll
     * @return true, wenn man einen neuen Raum betritt
     */
    private boolean switchRoom(){
        if(player.get_rawY()+28 < config.getTileSize() && player.direction == Direction.UP) {player.set_rawY(config.getHeight()-32); return true;}
        if(player.get_rawY() > config.getHeight()-36 && player.direction == Direction.DOWN) {player.set_rawY(3); return true;}
        if(player.get_rawX()+28 < config.getTileSize() && player.direction == Direction.LEFT) {player.set_rawX(config.getWidth()-32); return true;}
        if(player.get_rawX() > config.getWidth()-36 && player.direction == Direction.RIGHT) {player.set_rawX(3); return true;}
        return false;
    }

    /**
     * @brief überprüft ob der Spieler auf das gegebene Feld laufen kann
     * @param x neuer x wert
     * @param y neuer y wert
     * @return true wenn das Feld 'frei' ist - weils lustig ist nur als inline-ifs
     */
    private boolean movable(double x, double y){
        return !(player.get_rawX()+x < 0 || player.get_rawY()+y < 0 || player.get_rawX()+x >= config.getWidth()-config.getTileSize() || player.get_rawY()+y >= config.getHeight()-config.getTileSize()) &&
                (!map.getTile((int)Math.round((player.get_rawX()+9+x))/config.getTileSize(),(int)Math.round((player.get_rawY()+12+y))/config.getTileSize()).getName().contains("#")) &&
                (!map.getTile((int)Math.round((player.get_rawX()+9+x))/config.getTileSize(),(int)Math.round((player.get_rawY()+22+y))/config.getTileSize()).getName().contains("#")) &&
                (!map.getTile((int)Math.round((player.get_rawX()+23+x))/config.getTileSize(),(int)Math.round((player.get_rawY()+12+y))/config.getTileSize()).getName().contains("#")) &&
                (!map.getTile((int)Math.round((player.get_rawX()+23+x))/config.getTileSize(),(int)Math.round((player.get_rawY()+22+y))/config.getTileSize()).getName().contains("#"));
    }

    /**
     * @brief aktualisiert die Position auf Basis der Tastatureingaben - berücksichtigt ob das Ziel betreten werden kann
     */
    private void updateMovement(){
        double speed = ((inputHandler.up.isPressed() || inputHandler.down.isPressed())&&(inputHandler.left.isPressed() || inputHandler.right.isPressed()))?(player.getSpeed()/Math.sqrt(2)/config.getFPS()):(player.getSpeed()/config.getFPS());
        double x = 0, y = 0;
        if(inputHandler.up.isPressed())
            if(movable(0,-speed)) y-=speed;
        if(inputHandler.down.isPressed())
            if(movable(0,+speed)) y+=speed;
        if(inputHandler.left.isPressed())
            if(movable(-speed,0)) x-=speed;
        if(inputHandler.right.isPressed())
            if(movable(speed,0)) x+=speed;
        player.move(x,y);
    }

    /**
     * @brief Lädt einen neuen Raum - in diesem Fall mit der gleichen Karte, wäre aber nicht möglich
     */
    public void loadNew(){
        enemies.clear();
        items.clear();
        //for(int i = 0; i < rnd.nextInt(2)+1;i++)
            enemies.add(new Ghost(rnd.nextInt(config.getWidth()-100)+50,rnd.nextInt(config.getHeight()-100)+50,player,enemies, map));
        for(int i = 0; i < rnd.nextInt(10);i++)
            items.add(Item.generateItem(rnd.nextInt(config.getWidth()-100)+50,rnd.nextInt(config.getHeight()-100)+50,32,32));

    }

    /**
     * @brief Die Main methode - startet das Spiel
     * @param args Command line argumente - nicht nötig
     */
    public static void main(String[] args){
        //Setze die Konfiguration auf derer Basis das Spiel geladen werden soll
        GameCore.config = new Configuration("/configs/config.configuration");
        new Game();
    }
}
