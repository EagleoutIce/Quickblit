package uni.eagle.quickblit.Game.Player;

import uni.eagle.quickblit.General.Enums.DataOperation;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.General.Interfaces.iPoint;
import uni.eagle.quickblit.General.Interfaces.iRenderable;
import uni.eagle.quickblit.Graphics.Sprite;
import uni.eagle.quickblit.Graphics.SpriteSheet;
import uni.eagle.quickblit.IO.MessageBox;

public class Player implements iRenderable, iPoint {

    private int health = 10000;
    private double dmg = 5;
    private double x = 200;
    private double y = 100;
    private double speed = 100;
    private Sprite[][] sprites = new Sprite[2][0];
    private double headAnimation = 0, bodyAnimation;
    public Direction direction = Direction.DOWN;
    private Weapon weapon = new Weapon();
    private MessageBox mb = new MessageBox(8,8,550,150);

    public Player(){
        SpriteSheet s = new SpriteSheet("/graphics/char/character_001_isaac.png");
        sprites[0] = new Sprite[6];
        sprites[1] = new Sprite[20];

        for(int i = 0; i < 6; i++)
            sprites[0][i] = new Sprite(32,32,s,i*32,0,"StandardHead" + i);

        for(int i = 0; i < 20; i++)
            sprites[1][i] = new Sprite(32,32,s,((i+6)%8)*32,32*(Math.floorDiv(i+6,8)),"StandardBody" + i);
    }

    public void move(double x, double y){
        headAnimation += 0.007;
        if(x == y && y == 0) return;
        bodyAnimation += 0.05;
        this.x += x;
        this.y += y;
        if(x>0){
            this.direction = Direction.RIGHT;
            if(y>0) this.weapon.d = Direction.RIGHT_UP;
            else if (y==0.0) this.weapon.d = Direction.RIGHT;
            else this.weapon.d = Direction.RIGHT_DOWN;
        } else  if(x==0.0){
            if(y>=0.0) this.direction = Direction.DOWN;
            else this.direction = Direction.UP;
            this.weapon.d = this.direction;
        } else {
            this.direction = Direction.LEFT;
            if(y>0) this.weapon.d = Direction.LEFT_UP;
            else if (y==0) this.weapon.d = Direction.LEFT;
            else this.weapon.d = Direction.LEFT_DOWN;
        }
    }

    @Override
    public void render(int[] pixels, int startx, int starty) {
        mb.setText("LIVE:  \033R" + this.health/1000 + "\033r\nDMG:  \033G" + this.dmg + "\033r\nSPEED: \033B" + this.speed);
        mb.show();

        mb.render(pixels,startx,starty);

        if(headAnimation > 1) headAnimation = 0;
        if(bodyAnimation > 10) bodyAnimation = 0;
        if(this.direction == Direction.UP || this.direction == Direction.DOWN)
            sprites[1][Helper.dRound(bodyAnimation)].render(pixels,Helper.dRound(x),Helper.dRound(y));
        else if(this.direction == Direction.RIGHT)
            sprites[1][9+Helper.dRound(bodyAnimation)].render(pixels,Helper.dRound(x),Helper.dRound(y));
        else if(this.direction == Direction.LEFT)
            sprites[1][9+Helper.dRound(bodyAnimation)].render(pixels,Helper.dRound(x),Helper.dRound(y),DataOperation.MIRROR_HORI);

        weapon.render(pixels,Helper.dRound(x),Helper.dRound(y));

        if(this.direction == Direction.DOWN)
            sprites[0][Helper.dRound(headAnimation)].render(pixels,Helper.dRound(x),Helper.dRound(y-10));
        else if(this.direction == Direction.UP)
            sprites[0][4+Helper.dRound(headAnimation)].render(pixels,Helper.dRound(x),Helper.dRound(y-10));
        else if(this.direction == Direction.RIGHT)
            sprites[0][2+Helper.dRound(headAnimation)].render(pixels,Helper.dRound(x),Helper.dRound(y-10));
        else if(this.direction == Direction.LEFT)
            sprites[0][2+Helper.dRound(headAnimation)].render(pixels,Helper.dRound(x),Helper.dRound(y-10),DataOperation.MIRROR_HORI);
    }




    public double get_rawX() {
        return x;
    }

    public void set_rawX(double x) {
        this.x = x;
    }

    public double get_rawY() {
        return y;
    }

    public void set_rawY(double y) {
        this.y = y;
    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = Math.round(speed*1000)/1000.0;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getDmg() {
        return dmg;
    }

    public void setDmg(double dmg) {
        this.dmg =  Math.round(dmg*1000)/1000.0;
    }

    @Override
    public int getX() {
        return Helper.dRound(x);
    }

    @Override
    public int getY() {
        return Helper.dRound(y);
    }
}
