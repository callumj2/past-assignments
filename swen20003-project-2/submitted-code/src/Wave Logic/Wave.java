import bagel.Input;
import bagel.Keys;

/**
 *  A wave will take in a text file and convert it into an array of wave events (spawn/delay inclusive)
 * The wave class will also be responsible for starting each wave event once the previous wave event is finished
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class Wave {
    private final static int WAVE_TYPE_INDEX = 1;
    private int currentEvent;
    private boolean isFinished;
    private boolean waveInProgress;
    private final Level parentLevel;
    private final WaveEvent[] waveEvents;

    /**
     * Creates a new Wave
     * @param waveInfo: The String array containing information about wave events for
     *                this wave
     * @param parentLevel: The current level of the game
     */
    public Wave(String[] waveInfo, Level parentLevel) {
        this.parentLevel = parentLevel;
        this.currentEvent = 1;
        // initialise default wave state
        this.waveInProgress = false;
        this.waveEvents = new WaveEvent[waveInfo.length];
        //For each line of the wave text file, instantiate a new wave event
        for (int i = 0; i < waveInfo.length; i++) {
            waveEvents[i] = createWaveEvent(waveInfo[i]);
        }
    }

    /**
     * Updates the wave
     * @param input: Input from the player's keyboard and mouse
     * @param timeModifier: The current in-game time modifier
     */
    public void update(Input input, int timeModifier) {
        // if 'S' is pressed and the wave hasn't already started start the wave
        if (input.wasPressed(Keys.S) && !waveInProgress) {
            waveInProgress = true;
        }

        // if the wave is in progress, update all the current wave events
        if (waveInProgress) {
            waveEvents[currentEvent - 1].update(timeModifier);
        }

        // if the current wave event is finished, start the next
        if (waveEvents[currentEvent - 1].isFinished() && currentEvent < waveEvents.length) {
            currentEvent++;
        }

        // if we have completed all the current wave events, check if all slicers have either completed the course or
        // been removed
        if (parentLevel.getSlicerManager().getActiveSlicers().size() == 0 && allWaveEventsCompleted()) {
            waveInProgress = false;
            if (currentEvent == waveEvents.length) {
                isFinished = true;
            }
        }
    }

    private WaveEvent createWaveEvent(String event) {
        String[] eventSplit = event.split(",");
        if (eventSplit[WAVE_TYPE_INDEX].equals("spawn")) {
            return new SpawnEvent(eventSplit, parentLevel);
        } else if (eventSplit[WAVE_TYPE_INDEX].equals("delay")) {
            return new DelayEvent(eventSplit);
        } else {
            return null;
        }
    }

    /**
     * @return: Returns true if all wave events are completed, or false otherwise
     */
    public boolean allWaveEventsCompleted(){
        for (int i = 0; i < waveEvents.length; i ++){
            if (!waveEvents[i].isFinished()){
                return false;
            }
        }
        return true;
    }

    /**
     * @return: Returns true if this wave is in progress, or false otherwise
     */
    public boolean isWaveInProgress() {
        return waveInProgress;
    }
    /**
     * @return: Returns true if this wave is finished, or false otherwise
     */
    public boolean isFinished() {
        return isFinished;
    }
}

