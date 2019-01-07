package uni.eagle.quickblit.General;

import uni.eagle.quickblit.Game.Game;
import uni.eagle.quickblit.General.Annotiations.IsSave;
import uni.eagle.quickblit.General.Enums.DataOperation;
import uni.eagle.quickblit.General.Interfaces.iPoint;

/**
 * @brief stellt einige Hilfsfunktionen zur Verfügung
 */
public final class Helper {


    public static boolean inArray(Object[] t, Object s){
        for(Object _t : t)
            if(t.equals(s)) return true;
        return false;
    }

    public static boolean strArrayContains(String[] arr, String str){
        for(String s : arr)
            if(str.contains(s)) return true;
        return  false;
    }


    public static class Point implements iPoint {
        int x, y; public Point father = null;
        public Point(int x, int y){
            this.x = x; this.y = y;
        }
        public Point(int cx, int cy, Point pt){
            this.father = pt;
            this.x = pt.getX()+cx; this.y = pt.getY()+cy;
        }
        public Point(iPoint pt){
            this.x = pt.getX(); this.y = pt.getY();
        }

        public int getTile(){
            return Math.floorDiv(x,GameCore.getConfig().getTileSize()) + Math.floorDiv(y,GameCore.getConfig().getTileSize())* GameCore.getConfig().getWidth()/GameCore.getConfig().getTileSize();
        }

        public boolean equals(Point obj){
            if(obj != null)
                return(this.x == ((iPoint) obj).getX()) && (this.y == ((iPoint) obj).getY());
            return false;
        }

        @Override
        public int getX() { return x; }

        @Override
        public int getY() { return y; }
    }

    /**
     * @brief verwandelt einen String in einen Integer
     *
     * @param str der String
     * @return der String als Zahl. Liefert -1 wenn es sich nicht um eine zahl gehandelt hat
     */
    @IsSave
    public static int toInteger(String str){
        return toInteger(str,-1);
    }


    @IsSave
    public static int dRound(double i){return (int)Math.round(i);}


    /**
     * @brief verwandelt einen String in einen Integer
     *
     * @param str der String
     * @param i der Wert wenn der String keine in eine Zahl konvertierbare Sequenz ist
     * @return der String als Zahl, oder eben der default wert
     */
    @IsSave
    public static int toInteger(String str, int i){
        if(str.length() < 1 || !Character.isDigit(str.charAt(0))) return i;
        else return Integer.parseInt(str);
    }

    /**
     * @brief foricert eine Zahl zwischen Zwei andere
     *
     * @param x Zahl
     * @param min untere Grenze
     * @param max obere Grenze
     * @return Zwischen Grenzen gedrückte Zahl
     */
    @IsSave
    public static int forceInBetween(int x, int min, int max){
        return Math.min(Math.max(x,min) ,max);
    }

    /**
     * @brief führt eine Operation auf einem Datenfeld durch
     *
     * @param arr Das Array
     * @param arrop die Arrayoperation
     * @return das Array, nach der Operation
     */
    @IsSave
    public static int[][] dataOperation(int[][] arr, DataOperation arrop){
        switch (arrop){
            case MIRROR_POINT:
                return mirrorArrayPoint(arr);
            case MIRROR_HORI:
                return mirrorArrayHori(arr);
            case MIRROR_VERT:
                return mirrorArrayVert(arr);
            case ROTATE_90:
                return mirrorArrayHori(mirrorArrayPoint(arr));
            case ROTATE_180:
                return mirrorArrayVert(mirrorArrayHori(arr));
            case ROTATE_270:
                return mirrorArrayVert(mirrorArrayPoint(arr));
            default: return arr;
        }
    }

    /**
     * @brief führt eine Operation auf einem Datenfeld durch
     *
     * @param arr Das Array
     * @param width die Breite des Datenfeldes
     * @param height die Höhe des Datenfeldes
     * @param arrop die Arrayoperation
     * @return das Array, nach der Operation
     */
    public static int[] dataOperation(int[] arr, int width, int height, DataOperation arrop){
           switch (arrop){
               case MIRROR_POINT:
                   return mirrorArrayPoint(arr,width,height);
               case MIRROR_HORI:
                   return mirrorArrayHori(arr,width,height);
               case MIRROR_VERT:
                   return mirrorArrayVert(arr,width,height);
               case ROTATE_90:
                   return mirrorArrayHori(mirrorArrayPoint(arr,width,height),height,width);
               case ROTATE_180:
                   return mirrorArrayVert(mirrorArrayHori(arr,width,height),width,height);
               case ROTATE_270:
                   return mirrorArrayVert(mirrorArrayPoint(arr,width,height),height,width);
               default: return arr;
           }
    }

    /**
     * @brief führt eine Punktspiegelung durch
     *
     * @param arr Das Array
     * @param width die Breite des Datenfeldes
     * @param height die Höhe des Datenfeldes
     * @return das Array, nach der Operation
     */
    private static int[] mirrorArrayPoint(int[] arr, int width, int height){
        int[] ret = new int[arr.length];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                ret[y + x*height] = arr[x + y*width];
            }
        }
        return ret;
    }

    /**
     * @brief führt eine Horizontale-Spiegelung durch
     *
     * @param arr Das Array
     * @param width die Breite des Datenfeldes
     * @param height die Höhe des Datenfeldes
     * @return das Array, nach der Operation
     */
    private static int[] mirrorArrayHori(int[] arr, int width, int height){
        int[] ret = new int[arr.length];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                ret[(width-x-1) + y*width] = arr[x + y*width];
            }
        }
        return ret;
    }

    /**
     * @brief führt eine Vertikale-Spiegelung durch
     *
     * @param arr Das Array
     * @param width die Breite des Datenfeldes
     * @param height die Höhe des Datenfeldes
     * @return das Array, nach der Operation
     */
    private static int[] mirrorArrayVert(int[] arr, int width, int height){
        int[] ret = new int[arr.length];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){ //Gespiegelte Kopie
                ret[x + (height-y-1)*width] = arr[x + y*width];
            }
        }
        return ret;
    }

    /**
     * @brief führt eine Punktspiegelung durch
     *
     * @param arr Das Array
     * @return das Array, nach der Operation
     */
    private static int[][] mirrorArrayPoint(int[][] arr){
        int[][] ret = new int[arr[0].length][arr.length];
        for(int y = 0; y < arr.length; y++){
            for(int x = 0; x < arr[0].length; x++){
                ret[x][y] = arr[y][x];
            }
        }
        return ret;
    }

    /**
     * @brief führt eine Horizontale-Spiegelung durch
     *
     * @param arr Das Array
     * @return das Array, nach der Operation
     */
    private static int[][] mirrorArrayHori(int[][] arr){
        int[][] ret = new int[arr.length][arr[0].length];
        for(int y = 0; y < arr.length; y++){
            for(int x = 0; x < arr[0].length; x++){
                ret[y][arr[0].length-x-1] = arr[y][x];
            }
        }
        return ret;
    }

    /**
     * @brief führt eine Vertikale-Spiegelung durch
     *
     * @param arr Das Array
     * @return das Array, nach der Operation
     */
    private static int[][] mirrorArrayVert(int[][] arr){
        int[][] ret = new int[arr.length][arr[0].length];
        for(int y = 0; y < arr.length; y++){
            for(int x = 0; x < arr[0].length; x++){ //gespiegelte Kopie
                ret[arr.length-y-1][x] = arr[y][x];
            }
        }
        return ret;
    }

}
