package uni.eagle.quickblit.Graphics;

import uni.eagle.quickblit.Game.Game;
import uni.eagle.quickblit.General.Annotiations.IsSave;
import uni.eagle.quickblit.General.GameCore;
import uni.eagle.quickblit.General.Helper;

import java.util.HashMap;

public class Painter {

    /**
     * @brief wurde für exklusiven Zugriff auf die GameCore-Daten geschrieben, wurde dann allerdings entfernt
     */
    @Deprecated
    public static final class signature{private signature(){}}
    private static final signature sign = new signature();

    /**
     * @brief Zeichnet einen Pixel
     *
     * @param pixels das Array auf das gezeichnet werden soll
     * @param x die X-Position
     * @param y die Y-Position
     * @param color die Farbe des Pixels im RGB format (>0 = Transparent)
     */
    @IsSave
    public static void drawPixel(int[] pixels, int x, int y, int color){
        if(x>=GameCore.getConfig().getWidth() || x < 0 || y>=GameCore.getConfig().getHeight() || y <0)
            return;
        pixels[x + GameCore.getConfig().getWidth()*y] = color;
    }

    /**
     * @brief Füllt ein Rechteck
     *
     * @param pixels das Array auf das gezeichnet werden soll
     * @param startx StartX-Position
     * @param starty StartY-Position
     * @param width Breite
     * @param height Höhe
     * @param color Farbe im RGB-Format (>0 = Transparent)
     */
    @IsSave
    public static void fillRect(int[] pixels, int startx, int starty, int width, int height, int color){
        width = Helper.forceInBetween(width, 0, GameCore.getConfig().getWidth());
        height= Helper.forceInBetween(height, 0, GameCore.getConfig().getHeight());
        startx = Helper.forceInBetween(startx, 0,GameCore.getConfig().getWidth()-width);
        starty = Helper.forceInBetween(starty, 0, GameCore.getConfig().getHeight()-height);
        fillRectRaw(pixels, startx, starty, width, height, color);
    }

    /**
     * @brief Füllt ein Rechteck
     *
     * @param pixels das Array auf das gezeichnet werden soll
     * @param startx StartX-Position
     * @param starty StartY-Position
     * @param width Breite
     * @param height Höhe
     * @param color Farbe im RGB-Format (>0 = Transparent)
     */
    public static void fillRectRaw(int[] pixels, int startx, int starty, int width, int height, int color){
        for(int y = starty; y < starty+height; y++){
            for(int x = startx; x < startx+width; x++){
                drawPixel(pixels, x,y,color);
            }
        }
    }

    /**
     * @brief Zeichnet eine Linie mit dem Besenham-Algorithmus
     *
     * @param pixels das Array auf das gezeichnet werden soll
     * @param startx StartX-Position
     * @param starty StartY-Position
     * @param endx EndX-Position
     * @param endy EndY-Position
     * @param color Farbeim RGB-Format (>0 = Transparent)
     */
    @IsSave
    public static void drawLine(int[] pixels, int startx, int starty, int endx, int endy, int color){
        startx = Helper.forceInBetween(startx, 0, endx); endx = Helper.forceInBetween(endx, startx, GameCore.getConfig().getWidth());
        starty = Helper.forceInBetween(starty, 0, endy); endy = Helper.forceInBetween(endy, starty, GameCore.getConfig().getHeight());
        drawLineRaw(pixels, startx, starty, endx, endy, color);
    }

    /**
     * @brief Zeichnet eine Linie mit dem Besenham-Algorithmus
     *
     * @param pixels das Array auf das gezeichnet werden soll
     * @param startx StartX-Position
     * @param starty StartY-Position
     * @param endx EndX-Position
     * @param endy EndY-Position
     * @param color Farbeim RGB-Format (>0 = Transparent)
     */
    public static void drawLineRaw(int[] pixels, int startx, int starty, int endx, int endy, int color) {
        int deltaX = endx - startx, deltaY = endy - starty;
        double error = 0;

        if (deltaX == 0) {
            for (int y = starty; y < endy; y++) pixels[startx + y * GameCore.getConfig().getWidth()] = color;
        } else if (deltaY == 0) {
            for (int x = startx; x < endy; x++) pixels[x + starty * GameCore.getConfig().getWidth()] = color;
        } else {
            double deltaError = Math.abs(deltaY / ((float) deltaX));
            int v = startx, max = endx, x = startx, y =starty; //naming

            if(deltaX<=deltaY){
                deltaError = Math.abs(deltaX/((float) deltaY));
                v = starty; max = endy;
            }

            for (; v < max; v++) {
                if(deltaX>deltaY) pixels[v + y * GameCore.getConfig().getWidth()] = color;
                else pixels[x + v * GameCore.getConfig().getWidth()] = color;

                error += deltaError;
                while (error >= 0.5) {
                    if(deltaX>deltaY) y++;
                    else x++;
                    error -= 1;
                }
            }
        }
    }

    /**
     * @brief Zeichnet ein Datenfeld in das Array (gezielter Array-Copy)
     *
     * @param pixels das Array auf das gezeichnet werden soll
     * @param data die Daten die geschrieben werden sollen
     * @param startx startX position des Rechtecks der Daten
     * @param starty starty position des Rechtecks der daten
     * @param width die Briete des Rechtecks der Daten
     * @param height die Höhe des Rechtecks der Daten
     * @param scale der Skalierungsfaktor des Daten arrays
     */
    @IsSave
    public static void drawData(int[] pixels, int[] data, int startx, int starty, int width, int height, int scale){
        for(int y = Math.max(starty,0); y < Math.min(starty + height, Game.getConfig().getHeight()); y++){
            for(int x = Math.max(startx,0); x < Math.min(startx + width,Game.getConfig().getWidth()); x++)
                if(data[x + (y)*width] < 0){
                    fillRect(pixels,x*scale + startx, y*scale + starty, scale,scale,data[x + (y)*width]);
                }
        }
    }

    /**
     * @brief Zeichnet ein Datenfeld in das Array (gezielter Array-Copy)
     *
     * @param pixels das Array auf das gezeichnet werden soll
     * @param data die Daten die geschrieben werden sollen
     * @param startx startX position des Rechtecks der Daten
     * @param starty starty position des Rechtecks der daten
     * @param width die Briete des Rechtecks der Daten
     * @param height die Höhe des Rechtecks der Daten
     * @param scale der Skalierungsfaktor des Daten arrays
     * @param color Die Farbe in die das Feld eingefärbt werden soll im RGB-Format (>0 = Transparent)
     */
    @IsSave
    public static void drawDataColored(int[] pixels, int[] data, int startx, int starty, int width, int height, int scale, int color){
        for(int y = Math.max(starty,0); y < Math.min(starty + height, Game.getConfig().getHeight()); y++){
            for(int x = Math.max(startx,0); x < Math.min(startx + width,Game.getConfig().getWidth()); x++)
                if(data[x-startx + (y-starty)*width] < 0){
                    fillRect(pixels,(x-startx)*scale+startx, (y-starty)*scale+starty, scale,scale,color);
                }
        }
    }

///------------------------------------------------------- Diverse Abwandlungen er obigen Data Funktionen - dienen der Einfacheit


    @IsSave
    public static void drawData(int[] pixels, int[][] data, int startx, int starty, int scale){
        for(int y = 0; y < Math.min(data.length, Game.getConfig().getHeight()); y++){
            for(int x = 0; x < Math.min(data[0].length,Game.getConfig().getWidth()); x++)
                if(data[y][x] < 0)
                    fillRect(pixels,x*scale + startx, y*scale + starty, scale,scale,data[y][x]);
        }
    }

    @IsSave
    public static void drawDataColored(int[] pixels, int[][] data, int startx, int starty, int scale, int color){
        for(int y = 0; y < Math.min(data.length, Game.getConfig().getHeight()); y++){
            for(int x = 0; x < Math.min(data[0].length,Game.getConfig().getWidth()); x++)
                if(data[y][x] < 0)
                    fillRect(pixels,x*scale + startx, y*scale + starty, scale,scale,color);
        }
    }

    @IsSave
    public static void drawData(int[] pixels, int[][] data, int startx, int starty){
        startx = Helper.forceInBetween(startx,0,Game.getConfig().getWidth());
        starty = Helper.forceInBetween(starty,0, Game.getConfig().getHeight());
        for(int y = 0; y < Math.min(data.length, Game.getConfig().getHeight()); y++){
            for(int x = 0; x < Math.min(data[0].length,Game.getConfig().getWidth()); x++)
                if(data[y][x] < 0)
                    pixels[x+startx + (y+starty) * GameCore.getConfig().getWidth()] = data[y][x] ;
        }
    }

    @IsSave
    public static void drawDataColored(int[] pixels, int[][] data, int startx, int starty, int color){
        for(int y = 0; y < Math.min(data.length, Game.getConfig().getHeight()); y++){
            for(int x = 0; x < Math.min(data[0].length,Game.getConfig().getWidth()); x++)
                if(data[y][x] < 0)
                    pixels[x+startx + (y+starty) * GameCore.getConfig().getWidth()] = color;
        }
    }

    @IsSave
    public static void drawData(int[] pixels, int[] data, int startx, int starty, int width, int height){
        for(int y = Math.max(starty,0); y < Math.min(starty + height, Game.getConfig().getHeight()); y++){
            for(int x = Math.max(startx,0); x < Math.min(startx + width,Game.getConfig().getWidth()); x++) {
                if(data[x - startx + (y - starty) * width] < 0)
                pixels[x + y * GameCore.getConfig().getWidth()] = data[x - startx + (y - starty) * width];
            }
        }
    }

    @IsSave
    public static void drawDataColored(int[] pixels, int[] data, int startx, int starty, int width, int height, int color){
        for(int y = Math.max(starty,0); y < Math.min(starty + height, Game.getConfig().getHeight()); y++){
            for(int x = Math.max(startx,0); x < Math.min(startx + width,Game.getConfig().getWidth()); x++)
                if(data[x-startx + (y-starty)*width] < 0)
                    pixels[x + y * GameCore.getConfig().getWidth()] = color;
        }
    }
}
