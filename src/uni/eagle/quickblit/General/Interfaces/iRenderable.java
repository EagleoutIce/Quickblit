package uni.eagle.quickblit.General.Interfaces;

public interface iRenderable {
    /**
     * @brief Rendert das iRenderable Objekt
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     */
    default void render(int[] pixels) {render(pixels, 0,0);}

    /**
     * @brief Rendert das iRenderable objekt
     * @param pixels Array auf dass das Objekt gezeichnet werden soll
     * @param startx startPosition-X (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     * @param starty startPosition-Y (hat das Objekt eigene Koordinaten handelt es sich um den offest)
     */
    void render(int[] pixels, int startx, int starty);
}
