/**
 * @brief enthält diverse Tests
 */
package uni.eagle.quickblit.Demo.Test;

import uni.eagle.quickblit.General.Enums.DataOperation;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.Graphics.Map.Map;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;
import uni.eagle.quickblit.IO.FontController;
import uni.eagle.quickblit.IO.MessageBox;

import java.awt.*;

public class Testing extends GameCore {
    private Testing(){ super.start(); }

    int[] data = {
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.PINK.getRGB(), Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.GREEN.getRGB(),
            Color.RED.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.ORANGE.getRGB()
    };
    int[][] data2 = {
            { Color.CYAN.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.MAGENTA.getRGB()},
            { Color.CYAN.getRGB(),Color.PINK.getRGB(), Color.OPAQUE, Color.GREEN.getRGB(), Color.MAGENTA.getRGB()},
            { Color.CYAN.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.MAGENTA.getRGB()},
            { Color.CYAN.getRGB(),Color.PINK.getRGB(), Color.OPAQUE, Color.GREEN.getRGB(), Color.MAGENTA.getRGB()},            { Color.CYAN.getRGB(),Color.GREEN.getRGB(), Color.OPAQUE, Color.OPAQUE, Color.MAGENTA.getRGB()},
            { Color.CYAN.getRGB(),Color.PINK.getRGB(), Color.OPAQUE, Color.GREEN.getRGB(), Color.MAGENTA.getRGB()}

    };
    //int i = 0;

    @Override
    public void onRender(int[] pixels) {
        m.render(pixels);
        //sprite_pixels[28] = Color.MAGENTA.getRGB();
        fontController.drawString(pixels,"Hallo WELT,\nna wie geht es dir? ❤#",10,400,2);

        //fontController.drawString(pixels,"Hallo Welt", 10,20,0.5);


        FontController.drawSmallString(pixels,"[AEE❤H],!.",10,80,3,Color.GREEN.getRGB());
        FontController.drawSmallString(pixels,"❤",280,170,14);
        Painter.drawDataColored(pixels, data, 100,100,5, 14,3,Color.ORANGE.getRGB());
        Painter.drawDataColored(pixels, Helper.dataOperation(data,5,14, DataOperation.ROTATE_90), 200,100,14, 5,3,Color.ORANGE.getRGB());
        Painter.drawDataColored(pixels, Helper.dataOperation(data,5,14, DataOperation.ROTATE_180), 300,100,5, 14,3,Color.ORANGE.getRGB());
        Painter.drawDataColored(pixels, Helper.dataOperation(data,5,14, DataOperation.ROTATE_270), 400,100,14, 5,3,Color.ORANGE.getRGB());

        Painter.drawData(pixels,data2,100,200,3);
        Painter.drawData(pixels,Helper.dataOperation(data2, DataOperation.ROTATE_90),200,200,3);
        Painter.drawData(pixels,Helper.dataOperation(data2, DataOperation.ROTATE_180),300,200,3);
        Painter.drawData(pixels,Helper.dataOperation(data2, DataOperation.ROTATE_270),400,200,3);

        Painter.drawData(pixels, Sprite.createNoise(40000),400,300,200,200);

        msg.render(pixels);
    }
    Map m;
    MessageBox msg;
    @Override
    public void onInit() {
        m = new Map("/data/map_data/dummy_map.map");
        //m.getTile(3,2).getName().equals("T");


        Sprite hedgehog = new Sprite(200,60,new SpriteSheet("/graphics/testimages/hedgehog.png"),300,40,"testBild");
        msg = new MessageBox(0,config.getHeight()-60,210,60,Color.GREEN.darker().getRGB(),1);
        msg.setText("Hallo Welt, na wie geht es dir? ich mag Züge wirklich sehr! Jaaay das ist ein so toller füller Text tihihiihih");
        msg.teletype(20);
    }

    @Override
    public boolean onUpdate() {return true;}

    @Override
    public boolean onTickStart(long frames, long updates, long currentTime) {
        return false;
    }

    public static void main(String args[]){ new Testing();}

}
