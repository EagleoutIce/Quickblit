package uni.eagle.quickblit.Management;

import uni.eagle.quickblit.Game.Entity.Enemy.Enemy;
import uni.eagle.quickblit.Game.Game;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;
import uni.eagle.quickblit.General.Interfaces.iPoint;
import uni.eagle.quickblit.Graphics.Map.Map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Pathfinder {

    public static enum Mode{
        NORMAL,
        RIGHT_ANGLE_ONLY
    }

    public Helper.Point getTarPoint(){
        return new Helper.Point(Math.floorDiv(target.getX(), GameCore.getConfig().getTileSize()),
                Math.floorDiv(target.getY(), GameCore.getConfig().getTileSize()));
    }
    public Helper.Point getObjPoint(){
        return new Helper.Point(Math.floorDiv(obj.getX(), GameCore.getConfig().getTileSize()),
                Math.floorDiv(obj.getY(), GameCore.getConfig().getTileSize()));
    }

    private Mode mode;
    private iPoint obj, target;
    //ArrayList<Enemy> lockedFields;
    private Helper.Point lastObj = new Helper.Point(0,0), lastTarget= new Helper.Point(0,0), lastMove= new Helper.Point(0,0);
    Map field;
    boolean[][] visited;
    String[] locked;
    long last_check=0;
    public Pathfinder(iPoint obj, iPoint target, Map operation_field, String[] locked_field_names, Mode mode){
        this.obj = obj; this.target = target;
        this.field = operation_field;
        this.locked = locked_field_names;
        this.mode = mode;
        visited = new boolean[Math.floorDiv(GameCore.getConfig().getHeight(),GameCore.getConfig().getTileSize())][Math.floorDiv(GameCore.getConfig().getWidth(),GameCore.getConfig().getTileSize())];
        //lockedFields = enemies;
    }

    public boolean isPassable(int x, int y){
        if(x < 0 || x >= Math.floorDiv(GameCore.getConfig().getWidth(),GameCore.getConfig().getTileSize())) return false;
        if(y < 0 || y >= Math.floorDiv(GameCore.getConfig().getHeight(),GameCore.getConfig().getTileSize())) return false;
        if(visited[y][x]) return false;
        /*for(Enemy e : lockedFields){
            if(e.collision(x*GameCore.getConfig().getTileSize(),x*GameCore.getConfig().getTileSize(),40,40))
                return false;
        }*/
        return !Helper.strArrayContains(locked,field.getTile(x,y).getName());
    }

    public Helper.Point calculateMove(){
        if(System.nanoTime()-last_check<500000) return lastMove;
        last_check = System.nanoTime();
        if(getObjPoint().equals(lastMove)){
            return getTarPoint();
        }
        if( (getObjPoint().equals(lastObj)) && (getTarPoint().equals(lastTarget))) {
            return lastMove;
        }
        return findPathBFS();
    }



    private byte countNeighbours(iPoint pt){
        byte n = 0;
        switch (mode){
            default:
            case NORMAL:
                if(isPassable(pt.getX()+1,pt.getY()+1)) n++;
                if(isPassable(pt.getX()+1,pt.getY()-1)) n++;
                if(isPassable(pt.getX()-1,pt.getY()+1)) n++;
                if(isPassable(pt.getX()-1,pt.getY()-1)) n++;
            case RIGHT_ANGLE_ONLY:
                if(isPassable(pt.getX()+1,pt.getY())) n++;
                if(isPassable(pt.getX(),pt.getY()-1)) n++;
                if(isPassable(pt.getX()-1,pt.getY())) n++;
                if(isPassable(pt.getX(),pt.getY()+1)) n++;
        }
        return n;
    }

    private Helper.Point findPathBFS(){
        visited = new boolean[Math.floorDiv(GameCore.getConfig().getHeight(),GameCore.getConfig().getTileSize())][Math.floorDiv(GameCore.getConfig().getWidth(),GameCore.getConfig().getTileSize())];
        ArrayList<Helper.Point> points = new ArrayList<>();
        points.add(new Helper.Point(getObjPoint()));
        visited[getObjPoint().getY()][getObjPoint().getX()] = true;
        for(;points.size()>0;){
            int s = points.size();
            for(int i = 0; i < s; i++ ){
                Helper.Point pt = points.get(i);
                if(pt.equals(getTarPoint())){
                    Helper.Point z = pt;
                    while(z.father != null && z.father.father != null && !z.father.father.equals(getObjPoint())){ z = z.father; } //dirty get next
                    lastMove = z;
                    visited = new boolean[Math.floorDiv(GameCore.getConfig().getHeight(),GameCore.getConfig().getTileSize())][Math.floorDiv(GameCore.getConfig().getWidth(),GameCore.getConfig().getTileSize())];
                    return z;
                }

                switch (mode) {
                    default:
                    case NORMAL:
                        if (isPassable(pt.getX() + 1, pt.getY()+1)) {
                            points.add(new Helper.Point(1, 1, pt));
                            visited[pt.getY()+1][pt.getX() + 1] = true;
                        }
                        if (isPassable(pt.getX() - 1, pt.getY()+1)) {
                            points.add(new Helper.Point(1, -1, pt));
                            visited[pt.getY() + 1][pt.getX() - 1] = true;
                        }
                        if (isPassable(pt.getX()+1, pt.getY() - 1)) {
                            points.add(new Helper.Point(-1, 1, pt));
                            visited[pt.getY() - 1][pt.getX()+1] = true;
                        }
                        if (isPassable(pt.getX()-1, pt.getY() - 1)) {
                            points.add(new Helper.Point(-1, -1, pt));
                            visited[pt.getY() - 1][pt.getX()-1] = true;
                        }
                    case RIGHT_ANGLE_ONLY:
                        if (isPassable(pt.getX() + 1, pt.getY())) {
                            points.add(new Helper.Point(1, 0, pt));
                            visited[pt.getY()][pt.getX() + 1] = true;
                        }
                        if (isPassable(pt.getX(), pt.getY() + 1)) {
                            points.add(new Helper.Point(0, 1, pt));
                            visited[pt.getY() + 1][pt.getX()] = true;
                        }
                        if (isPassable(pt.getX() - 1, pt.getY())) {
                            points.add(new Helper.Point(-1, 0, pt));
                            visited[pt.getY()][pt.getX() - 1] = true;
                        }
                        if (isPassable(pt.getX(), pt.getY() - 1)) {
                            points.add(new Helper.Point(0, -1, pt));
                            visited[pt.getY() - 1][pt.getX()] = true;
                        }
                }
            }
            points.removeIf(p -> (countNeighbours(p) == 0));
        }
        return (lastMove==null)?getTarPoint():lastMove;
    }


}
