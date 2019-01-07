package uni.eagle.quickblit.Demo.Firework;

import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;
import uni.eagle.quickblit.IO.FontController;
import uni.eagle.quickblit.Management.Configuration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * @file DemoFirework.java
 *
 * @brief liefert die Feuerwerk demo die nicht kommentiert wurde, da das vorgehen so relativ straighforward ist.
 */


public class DemoFirework extends GameCore {
    private DemoFirework(){ super.start(); }

    @Override
    public void onRender(int[] pixels) {
        background.render(pixels);
        for(Firework f : fireworks){
            if(!f.isdead) {
                f.update();
                f.render( pixels, 0,0);
            }
        }
        FontController.drawSmallString(pixels,"PRESS W",3,3);
    }


    @Override
    public void onInit() {
        background = new Sprite(getConfig().getWidth(), getConfig().getHeight(),new SpriteSheet("/graphics/testimages/black.png"),"blackback");
    }

    private ArrayList<Firework> fireworks = new ArrayList<>();

    private Sprite background;
    static Random rnd = new Random(System.nanoTime());


    private final Color[] colors= {
            Color.ORANGE, Color.CYAN, Color.GREEN,Color.MAGENTA,
            Color.PINK, Color.BLUE, Color.YELLOW, Color.RED
    };

    @Override
    public boolean onUpdate() {
        if(inputHandler.up.isPressed()){
            Color col = colors[rnd.nextInt(colors.length)];
            Color col1;
            int col2 = rnd.nextInt(colors.length*3-2);
            if(col2 > colors.length+3){
                 col1 = col;
            } else if(col2 < colors.length) col1 = colors[col2];
            else col1 = new Color(rnd.nextInt(235)+20,rnd.nextInt(235)+20,rnd.nextInt(235)+20);
            fireworks.add(
                    new Firework((int)(rnd.nextFloat()*getConfig().getWidth()),getConfig().getHeight(),
                            rnd.nextInt(520),200 + ((int)(rnd.nextFloat()*100)-45),
                            9.4f, col,col1 ));
        }
        return true; //wir wollen immer Rendern
    }

    @Override
    public boolean onTickStart(long frames, long updates, long currentTime) {
        return false; /* enforce render */
    }

    public static void main(String[] args){
        GameCore.config = new Configuration("/configs/demo/Firework/firework.configuration");
        new DemoFirework();
    }

}
