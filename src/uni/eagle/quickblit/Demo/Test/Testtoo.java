package uni.eagle.quickblit.Demo.Test;

import uni.eagle.quickblit.General.GameCore;

/**
 * @brief diese Klasse ist zur deomstration der Grundlegenden Strutktur für eine Klasse, die auf der Engine basiert
 */
public class Testtoo extends GameCore {
    public Testtoo() {super.start();}

    @Override
    public void onInit() {

    }

    /**
     *
     * @param frames Anzahl an FPS im vergangenen Zyklus
     * @param updates Anzahl an Updates im vergangenen Zyklus
     * @param elapsedTime Zeit, die seit dem starten des Spiels vergangen ist
     * @return
     */
    @Override
    public boolean onTickStart(long frames, long updates, long elapsedTime) {
        return false;
    }

    @Override
    public boolean onUpdate() {
        return true;
    }

    @Override
    public void onRender(int[] pixels) {

    }

    public static  void main(String[] args){
        new Testtoo();
    }
}
