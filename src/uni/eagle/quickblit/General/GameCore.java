/**
 * @brief dieses Package liefert generelle, für die Engine relevante Komponenten
 *
 * @author Florian Sihler
 */
package uni.eagle.quickblit.General;

import uni.eagle.quickblit.General.Annotiations.Internal;
import uni.eagle.quickblit.General.Enums.GameState;
import uni.eagle.quickblit.General.Interfaces.iRenderable;
import uni.eagle.quickblit.Graphics.Filter.*;
import uni.eagle.quickblit.Graphics.Painter;
import uni.eagle.quickblit.IO.FontController;
import uni.eagle.quickblit.IO.InputHandler;
import uni.eagle.quickblit.IO.MessageBox;
import uni.eagle.quickblit.IO.Sound.SoundController;
import uni.eagle.quickblit.Management.Configuration;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Random;


/**
 * @mainpage Quickblit Engine
 * @author Florian Sihler
 * @author Rapahel Straub
 * 
 * @note diese Dokumentation wurde ohne weitere Anpassungen mithilfe von Doxygen anhand der Kommentare generiert. Das dafür verwendete Template stammt von Florian Sihler
 *
 * Das folgende Spiel ist eine Demo für die <b>Quickblit-Engine</b>
   Sie wurde, wie gefordert, nur in der Vorlesungsfreienzeit erstellt
                Sie kommt zudem mit der Firework-Demo, welche für Silvester gebaut\nwurde und ein Feuerwerk in der Engine simuliert
                Die im Spiel verwendeten Grafiken entstammen dem Spiel The Binding of Isaac und wurden mithilfe des mitgelieferten Extractor
                extrahiert. Andere Daten wie die Schrift wurden für die Engine mithilfe von GIMP erstellt
                Alle Render-Operationen werden auf einem Integer-Array durchgeführt und von der Engine zur Verfügung gestellt
                Zur Anzeige des Arrays wird von der Java-AWT Bibliothek gebrauch gemacht.\nDa die Zeit begrenzt war,
                liefert die Engine eingige Funktionen wie einen Pathfinder oder
                Sound-Controller, welche ihren Weg nichtmehr in die Demo geschafft haben
                Dennoch ist das ganze Paket als Sandbox zu verstehen. Die Dokumentation wurde im Doxygen-Style geschrieben.
                Hinzuzufügen sei nur noch, dass die Engine an sich mit viel ❤ aufgebaut wurde und sich grundlegend dazu eignet an Applets für die
                Webanwendung angepasst zu werden.
                
    
 *
 */


/**
 * @file GameCore.java
 *
 * @brief Diese Datei liefert den GameCore - den Kern der Quickblit-Engine
 *
 *
 */


public abstract class GameCore extends Canvas implements Runnable{
    public static final long serialVersionUID = 42L;
    public static final String Version = "1.0.8V {Alpha Cookie}";
    public static final String CVERSION = "1.0.8V ALPHA COOKIE";

    /**
     * @brief die Konfiguration auf derer Basis das Spiel funktioniert
     */
    protected static Configuration config;

    /**
     * @brief der Font-Controller
     */
    public static final FontController fontController = new FontController();

    /**
     * @brief der Sound-Controller
     */
    protected static SoundController soundController = new SoundController(6);

    /**
     * @brief der Update-Controller
     */
    protected final UpdateController update_ctrl;

    /**
     * @brief der Input-Handler handhabt Tastatureingaben
     */
    protected final InputHandler inputHandler = new InputHandler(this);;

    /**
     * @brief stellt den Zufallsgenerator zur Verfüung
     */
    public static final Random rnd = new Random(System.nanoTime());

    /**
     * @brief Enthält den Aktuellen Zustand des Games
     */
    protected GameState state = null;

    //Startzeit
    protected final long startTime = System.nanoTime();


    /**
     * @return die Konfiguration für alle Klassen (eigentlich nichtmehr nötig - ermöglicht Debug
     */
    public static Configuration getConfig(){
        if(config==null) config = new Configuration();
        return config;
    }

    @Internal
    private BufferedImage img;
    @Internal
    private int[] pixels;


    /**
     * @brief hat es ermöglicht Objekte in die Render-Queue abzulegen
     */
    @Deprecated
    ArrayList<iRenderable> objects = new ArrayList<>();

    @Internal
    private BufferStrategy bufferStrategy;
    @Internal
    private Graphics g;

    @Deprecated
    public Graphics getGraphics(Painter.signature sign){
        return this.g;
    }

    /**
     * @brief wird vor dem Starten des Game-Loops ausgeführt. Ermöglicht das Initialisieren von wichtigen Objekten
     */
    public abstract void onInit();

    /**
     * @brief Wird am Start jedes Zyklus aufgerufen - gewährleistet vor UpdateSkips() und render()
     *
     * @warning diese Funktion sollte nur dazu Dienen Debug-Informationen zu gewinnen, Latenzen anzupassen und im Notfall das Rendern forcieren
     *
     * @param frames Anzahl an FPS im vergangenen Zyklus
     * @param updates Anzahl an Updates im vergangenen Zyklus
     * @param elapsedTime Zeit, die seit dem starten des Spiels vergangen ist
     * @return true wenn das Rendern forciert werden soll. false wenn nicht
     */
    public abstract boolean onTickStart(long frames, long updates, long elapsedTime);

    /**
     * @brief Wird im Update-Zyklus durchgeführt.
     *
     * @return true wenn das Update ein Rendern benötigt (default sollte true sein - ein true pro Zyklus reicht um Rendern zu forcieren)
     */
    public abstract boolean onUpdate();

    /**
     * @brief rendert den Bildschirm. Muss nicht Zwangsläufig den Konfigurierten FPS entsprechen. Versucht diese allerdings zu halten
     * @param pixels das Pixel-Array auf das gezeichnet wird
     */
    public abstract void onRender(int[] pixels);

    /**
     * @brief Konstruktor - Initialisiert alles notwendige
     */
    public GameCore(){
        this.state = GameState.PREPARING;

        img = new BufferedImage(getConfig().getWidth(),getConfig().getHeight(), BufferedImage.TYPE_INT_RGB); //Lade Bild - RGB-Farbformat
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData(); //pixels[] zeigt nun auf das Raster des Bildes

        setPreferredSize(new Dimension(getConfig().getWidth() * getConfig().getScale(), getConfig().getHeight() * getConfig().getScale())); // Setze Fenstergröße

        JFrame frame = new JFrame(getConfig().getName()); //initialisiere Fenster
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false); frame.setLocationRelativeTo(null); frame.setVisible(true);

        update_ctrl = new UpdateController(getConfig().getFPS()); //initialisiere update control

        createBufferStrategy(3); //Buffer Strategie auf triple-Buffering
        bufferStrategy = getBufferStrategy();
        g = bufferStrategy.getDrawGraphics(); //Setze Grafik - hier momentan überflüssig

        this.state = GameState.READY; //Setze Spiel auf bereit - Wird nicht überprüft, kann aber zur Sicherheit verwendet werden
    }

    /**
     * @brief startet das Spiel
     */
    protected synchronized void start(){
        this.state = GameState.RUNNING; //Starte das Spiel
        new Thread(this).start(); /* store to thread holder */
    }

    /**
     * @brief stoppt das Spiel
     */
    public synchronized void stop() {
        this.state = GameState.STOP;
    }

    /**
     * @brief die "Echte" - Render Routine - überträgt pixels auf das Bild
     */
    @Internal
    private void render(){
        g = bufferStrategy.getDrawGraphics();

        onRender(pixels); // ruft abstract onRender auf

        if(getConfig().doFilter()) g.drawImage(GaussianFilter.processImage(img), 0,0,getWidth(),getHeight(), null);
        else g.drawImage(img, 0, 0, getWidth(), getHeight(), null);

        bufferStrategy.show();
        g.dispose();

    }


    /**
     * @param b soll das Fenster in den Fokus rücken dürfen?
     */
    @Override
    public void setFocusable(boolean b) {
        super.setFocusable(b);
    }

    /**
     * @brief initialisiert das Fenster, vor dem GameLoop - lädt eine Informationsgrafik
     */
    private void init(){
        setFocusable(true);
        MessageBox msg = new MessageBox(getConfig().getWidth()/2-190,getConfig().getHeight()/2-100,12*fontController.getCharWidth()*4,200);
        msg.setText(fontController,"Quickblit",true,4);
        msg.show(); msg.render(pixels);
        msg = new MessageBox(getConfig().getWidth()/2-205,getConfig().getHeight()/2-100+70,1000,300);
        msg.setColors(1,Color.MAGENTA.getRGB());
        msg.setText("Version: " + Version+"\nAuthoren: Florian Sihler & Raphael Straub\nFür: EidI 2018/2019");
        msg.show(); msg.render(pixels);
        msg = new MessageBox(3,getConfig().getHeight()-63,1000,60);
        msg.setColors(1,Color.LIGHT_GRAY.getRGB());
        msg.setText("Settings - \"" +config.getName() + "\" :\nFPS= " + config.getFPS()+"\nFilter: " + config.doFilter()+"\nDimensionen: " + config.getWidth() + "x" + config.getHeight() + ": (Scaling: " + config.getScale() + ")");
        msg.show(); msg.render(pixels);
        FontController.drawSmallString(pixels,"[TESTBILD] ❤",config.getWidth()-105,config.getHeight()-20,2);

        onInit(); //ruft abstract onInit() auf
    }

    /**
     * @brief das Echte update - sorgt dafür, dass der update Handler richtige ergebnisse liefert - Pausiert das Spiel *nicht* - muss eigens implementiert werden (über hasFocus())
     */
    private synchronized boolean update(){
        if(!this.hasFocus()) {
            inputHandler.disableAll();
        }
        return onUpdate(); //ruft abstract onUpdate() auf
    }


    /**
     * @brief Der Main Game-Loop
     */
    public void run(){
        long lastTick = update_ctrl.getTime(), lastBlit = update_ctrl.getTime();
        long frames= 0x0, updates = 0x0, lFrames = -0x1, lUpdates = -0x1;

        double idleTime = 0;
        init();//initialisiere
        while(this.state.equals(GameState.RUNNING)){ //nur solange es laufen soll
            long current = update_ctrl.getTime();
            idleTime  += (current - lastTick)/update_ctrl.getMaxFpsClock(); //berechnet nichts-tu Zeit
            lastTick = current;
            boolean needsRender = onTickStart(lFrames,lUpdates,current-startTime); // soll gerendert werden? ruft abstract onTickstart() auf

            /* keep updates */
            while (idleTime >= 1.0){ //updates - versucht immer korrekt zu Halten
                updates++;
                idleTime--;
                if(update()) needsRender = true;
            }

            if(needsRender){ // soll gerendert werden?
                frames ++;
                render();
            } else { update_ctrl.idle(); }
            if(update_ctrl.getTime() - lastBlit >= update_ctrl.second2ns()){ //reset frameCounter
                lastBlit+=update_ctrl.second2ns();
                lFrames = frames; lUpdates = updates;
                frames = 0; updates = 0;
            }
        }
    }
}
