/**
 * @brief liefert die Möglichkeit Ton abzuspielen
 *
 */
package uni.eagle.quickblit.IO.Sound;

import uni.eagle.quickblit.General.Annotiations.Internal;

import javax.sound.sampled.*;


/**
 * @class SoundController
 *
 * @brief liefert die Möglichkeit Sound zu spielen.
 */
public class SoundController {

    private final int MAX_CLIPS;
    private  Clip[] clips;

    /**
     * @brief Konstruktor des SoundControllers
     *
     * @param max_clips repräsentiert die maximale Anzahl an Clips die maximal gleicheitig abgespielt werden sollen
     */
    public SoundController(int max_clips){
        MAX_CLIPS = max_clips;
        clips = new Clip[MAX_CLIPS];
    }

    /**
     * @brief versucht eine .wav-Datei abzuspielen
     *
     * @param ressourcePath der Pfad zur Datei (abhängig vom Ressourcen Verzeichnis)
     * @return die Position im Loop Array. Liegt zwischen 0 und MAX_CLIPS. Wenn kleiner 0 wurde kein clip abgespielt
     */
    public int play(String ressourcePath){return play(ressourcePath, false);}

    /**
     * @brief versucht eine .wav-Datei abzuspielen
     *
     * @param ressourcePath der Pfad zur Datei (abhängig vom Ressourcen Verzeichnis)
     * @param loop soll der Clip wiederholt werden bis er gestoppt wird?
     * @return die Position im Loop Array. Liegt zwischen 0 und MAX_CLIPS. Wenn kleiner 0 wurde kein clip abgespielt
     */
    public int play(String ressourcePath, boolean loop){
        clean();
        for(int i = 0; i < MAX_CLIPS; i++){
            if(clips[i] == null){
                clips[i] = createClip(ressourcePath);
                if(clips[i]==null) return -1;
                if(loop) clips[i].loop(Clip.LOOP_CONTINUOUSLY);
                else clips[i].start();
                return i;
            }
        }
        return -1;
    };

    /**
     * @brief versucht den Clip an position i zu stoppen
     * @param i die Position, welche von Play geliefert wurde
     * @return gibt true zurück, wenn der clip gestoptt wurde. false wenn es den clip nicht (mehr) gab oder i < 0 || i > MAX_CLIPS
     */
    public boolean stop(int i){
        if(i >= 0  && i < MAX_CLIPS && clips[i] != null){
            clips[i].stop();
            return true;
        }
        return  false;

    }

    /**
     * @brief internal clean - testet ob der clip noch läuft, löscht ihn wenn nicht
     */
    @Internal
    private void clean() {
        for (int i = 0; i < MAX_CLIPS; i++) {
            if(clips[i] != null && (!clips[i].isRunning())) clips[i] = null;
        }
    }


    /**
     * @brief versucht einen neuen Clip zu erstelln der abgespielt werden kann - verwendet das Standart javasound prozedere
     * @param path der Pfad zur Datei (abhängig vom Ressourcen Verzeichnis)
     * @return den Clip. null wenn das anlegen gescheitert ist
     */
    @Internal
    private Clip createClip(String path){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(path));
            AudioFormat af = audioInputStream.getFormat();
            int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
            byte[] audio = new byte[size];
            DataLine.Info info = new DataLine.Info(Clip.class, af, size);
            audioInputStream.read(audio, 0, size);
            Clip clip = (Clip) AudioSystem.getLine(info); clip.open(af, audio, 0, size);
            return clip;
        }catch(Exception e){ e.printStackTrace(); return null; }
    }
}
