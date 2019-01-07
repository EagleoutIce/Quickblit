package uni.eagle.quickblit.Demo.Firework;

import uni.eagle.quickblit.General.Interfaces.iRenderable;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.IO.FontController;

import java.awt.*;
import java.util.ArrayList;

public class Firework extends Particle implements iRenderable {

    private int initlifetime;
    private Color secondcolor;
    boolean isHeart;
    Firework(int xpos, int ypos, int size, int life, float triggertolerance, Color color, Color color2) {
        x = xpos; y = ypos;
        vy = -5+ DemoFirework.rnd.nextInt(3)-1;
        vx = DemoFirework.rnd.nextInt(3)-1;
        lifetime = initlifetime = life;
        tt = triggertolerance;
        starCount = size;
        secondcolor = color2;
        this.color= color;
        isHeart = rndFloat(4)>3.8;
    }

    private int starCount;
    private ArrayList<Particle> stars = new ArrayList<>();

    private float rndFloat(float max){
        return DemoFirework.rnd.nextFloat()*max;
    }

    void update(){
        if(lifetime<=tt){ //Explode
            if(stars.size()==0){
                for(int i = 0; i < starCount; i++){
                    Particle particle = new Particle();
                    particle.x = x; particle.y = y;
                    float angle = rndFloat((rndFloat(3.6f)+0.4f)*3.14159f) + rndFloat(1f);
                    float power = rndFloat((Math.floorMod((int)(1.3*i),6))+2)+0.1f;
                    particle.lifetime = initlifetime/(rndFloat(1.1f)+1f);
                    particle.vx = (int)(Math.cos(angle)*power-(angle)/7);
                    particle.vy = (int)(Math.sin(angle)*power-(angle)/7);
                    if(i%2==0) particle.color = this.color;
                    else particle.color = secondcolor;

                    stars.add(particle);
                }
            } else {
                for(Particle prt : stars){
                    if(prt.lifetime<=tt){// we should delete u. we don't care
                    } else {
                        prt.x += (prt.vx) * (prt.lifetime / 100) + rndFloat(2f)-1+rndFloat(2f)-1;
                        prt.y += (prt.vy) * (prt.lifetime / 100) + rndFloat(2f)-1 +
                                1.8*((14 - 14* Math.exp(-0.07*lifetime)) / 7.0) + y/100.0;
                        prt.lifetime /= 1.035;
                    }


                }
            }
        } else {
            x += (vx) * (lifetime / 100);
            y += (vy) * (lifetime / 100);
            lifetime /= 1.045;
        }
    }

    boolean isdead = false;

    private int lastx=0, lasty=0;
    @Override
    public void render(int[] pixels, int startx, int starty) {
        if(stars.size()==0) {
            if(isHeart){
                FontController.drawSmallString(pixels,"❤",x,y,1,this.color.getRGB());
            }else
            Painter.drawPixel(pixels, x, y, this.color.getRGB());
            if(x==lastx && y == lasty){
                isdead = true;
            }
            lastx = x; lasty = y;
        }else {
            for(Particle prt: stars){
                isdead = true;
                if(prt.lifetime>tt) {

                    Painter.drawPixel(pixels, prt.x, prt.y, prt.color.getRGB());

                    Painter.drawPixel(pixels, prt.x+1, prt.y+1, prt.color.darker().getRGB());
                    Painter.drawPixel(pixels, prt.x-1, prt.y-1, prt.color.darker().getRGB());
                    Painter.drawPixel(pixels, prt.x+1, prt.y-1, prt.color.darker().getRGB());
                    Painter.drawPixel(pixels, prt.x-1, prt.y+1, prt.color.darker().getRGB());

                    Painter.drawPixel(pixels, prt.x, prt.y+1, prt.color.darker().darker().getRGB());
                    Painter.drawPixel(pixels, prt.x, prt.y-1, prt.color.darker().darker().getRGB());
                    Painter.drawPixel(pixels, prt.x+1, prt.y, prt.color.darker().darker().getRGB());
                    Painter.drawPixel(pixels, prt.x-1, prt.y, prt.color.darker().darker().getRGB());


                    Painter.drawPixel(pixels, prt.x+2, prt.y+2, prt.color.darker().darker().getRGB());
                    Painter.drawPixel(pixels, prt.x-2, prt.y-2, prt.color.darker().darker().getRGB());
                    Painter.drawPixel(pixels, prt.x+2, prt.y-2, prt.color.darker().darker().getRGB());
                    Painter.drawPixel(pixels, prt.x-2, prt.y+2, prt.color.darker().darker().getRGB());



                    isdead = false;
                }
            }
        }
    }
}
