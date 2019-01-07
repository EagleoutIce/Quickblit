package uni.eagle.quickblit.IO;

import uni.eagle.quickblit.General.Annotiations.Internal;
import uni.eagle.quickblit.General.GameCore;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @class InputHandler
 *
 * @brief liefert die Möglichkeit Tastatureingaben zu verarbeiten.
 */
public class InputHandler implements KeyListener {
    /**
     * @brief initialisiert den InputHandler mit dem zugehörigen GameCore
     *
     * @param gameCore der verknüpfte GameCore
     */
    public InputHandler(GameCore gameCore){
        gameCore.addKeyListener(this);
    }

    /**
     * @brief liefert die nötigen Keys, die abgefragt werden können
     */
    public Key up = new Key(), down = new Key(), left = new Key(), right = new Key(), event = new Key(), escape = new Key();


    /**
     * @brief setzt alle Keys auf nicht gedrückt. Kann bei Fokus-Verlust verwendet werden.
     */
    public void disableAll(){
        up.setKey(false); left.setKey(false); down.setKey(false); right.setKey(false); event.setKey(false); escape.setKey(false);
    }

    ///Internals

    @Internal
    /**
     * @class Key
     *
     * @brief repräsentiert die Key-Datenstruktur
     */
    public class Key{
        ///@brief ist der Key gedrückt
        private boolean pressed = false;

        /**
         * @brief gibt an, ob die Taste gedrückt ist.
         * @return eben diesen Zustand
         */
        public boolean isPressed() {return this.pressed;}

        /**
         * @brief ändert den Wert des Keys - nur zur besseren Lesbarkeit
         * @param newstate der neue Zustand
         */
        private void setKey(boolean newstate) {pressed = newstate;}
    }

    @Override
    @Internal
    public void keyTyped(KeyEvent keyEvent) { /* Benötigen wir nicht */ }

    /**
     * @brief beim Drücken einer Taste
     * @param e Event
     */
    @Internal
    public void keyPressed(KeyEvent e){
        setKey(e.getKeyCode(), true);
    }

    /**
     * @brief beim Loslassen einer Taste
     * @param e Event
     */
    @Internal
    public void keyReleased(KeyEvent e){
        setKey(e.getKeyCode(), false);
    }

    /**
     * @brief Setzt die Taste entsprechend des KeyCodes
     * @param keyCode der KeyCode
     * @param state der Zustand
     */
    @Internal
    private void setKey(int keyCode, boolean state){
        switch (keyCode){
            default: break;
            case KeyEvent.VK_W: up.setKey(state); break;
            case KeyEvent.VK_A: left.setKey(state); break;
            case KeyEvent.VK_S: down.setKey(state); break;
            case KeyEvent.VK_D: right.setKey(state); break;
            case KeyEvent.VK_E: event.setKey(state); break;
            case KeyEvent.VK_ESCAPE: escape.setKey(state); break;
        }
    }


}
