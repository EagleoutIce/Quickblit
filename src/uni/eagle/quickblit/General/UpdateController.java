package uni.eagle.quickblit.General;


/**
 * @class UpdateController
 *
 * @brief Diese Klasse wird über den LoadParser mit den richtigen Einstellungen geladen
 */
final class UpdateController {
    private int max_frames = 0x0;

    /**
     * @brief Konstruktor - Wird mit der Anzahl an Maximal möglichen FPS geladen
     *
     * @param maximum_frames Maximale Anzahl. Wird zwischen 1 und 1024 forciert.
     */
    UpdateController(int maximum_frames){
        this.max_frames = Helper.forceInBetween(maximum_frames,1,1024); //minmax-borders
    }

    /**
     * @return die Konfigurierten FPS (default = config.getFPS())
     */
    public int getMaxFps() { return this.max_frames; }

    /**
     * @return Die Clock-Rate für die konfigurierten FPS
     */
    public double getMaxFpsClock() { return (1000000000.0/this.max_frames); }

    /**
     * @return Liefert die Zeit. Wird zur Normierung auf Nanosekunden genutzt
     */
    public long getTime() { return System.nanoTime(); } //ensure ns

    /**
     * @return Konvertierungsfaktor - zur Klarheit
     */
    public long second2ns(int factor) {return factor*1000000000; }

    /**
     * @return Konvertierungsfaktor - zur Klarheit
     */
    public long second2ns() {return second2ns(1); }

    /**
     * @brief idle iteration - sorgt dafür dass der Java Garbage Collector Arbeiten Kann
     */
    public void idle() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
