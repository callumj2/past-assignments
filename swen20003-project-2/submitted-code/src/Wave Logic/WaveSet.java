import bagel.Input;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Class for handling and passing wave rewards back to the level class
 * as well as scheduling individual waves
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class WaveSet {
    private final static int BASE_WAVE_REWARD = 150;
    private final static int INCREMENTAL_WAVE_REWARD = 100;
    private final Level parentLevel;
    private String waveStatus;
    private Wave[] waves;
    private int currentWave;
    private boolean isFinished;
    /**
     * Creates a new Wave Set
     * @param waveInstructionsFilePath: The filepath from the source folder to the .txt file containing
     *                                the wave instructions
     * @param parentLevel: The current level of the game
     * @throws IOException: Throws an IO Exception in the case of failed input of the wave instructions
     */
    public WaveSet(String waveInstructionsFilePath, Level parentLevel) throws IOException{
        // Initialise the starting variables for the wave set
        this.parentLevel = parentLevel;
        this.currentWave = 0;
        this.waves = createWaves(getWaveLines(waveInstructionsFilePath));
        this.waveStatus = "Awaiting Start";
        this.isFinished = false;
    }

    /**
     * Updates the wave set, running the waves and wave.
     * @param input: Keyboard and mouse input from the player.
     * @param timeModifier: The current integer time modifier for the game.
     */
    public void update(Input input, int timeModifier){
        waves[currentWave].update(input, timeModifier);
        if (waves[currentWave].isWaveInProgress()){
            waveStatus = "Wave In Progress";
        }
        else {
            waveStatus = "Awaiting Start";
        }
        // if the current wave is finished, award the cash to the player and
        // move on to the next
        if (waves[currentWave].isFinished()){
            parentLevel.getPlayer().addMoney(BASE_WAVE_REWARD +
                    (currentWave + 1) * INCREMENTAL_WAVE_REWARD);

            if (currentWave < waves.length - 1){
                currentWave ++;
            }
            else {
                isFinished = true;
            }
        }
    }

    // Takes the waveInstructions file location and returns each line
    // of text in the file as an array of strings
    private String[] getWaveLines(String waveInstructionsFilePath) throws IOException {
        return Files.readAllLines(Paths.get(waveInstructionsFilePath)).toArray(new String[0]);
    }

    // Creates an array of waves from an array of Strings containing wave instructions
    private Wave[] createWaves(String[] waveLines){
        // The wave number of the last wave event in waveLines indicates how many waves to create
        waves = new Wave[Integer.parseInt(waveLines[waveLines.length - 1].split(",")[0])];
        // keep track of the current wave and previous wave when reading in the events
        int prevWave = 0;
        int currWave = 1;
        int eventIndex = 0;
        // For each line of input, check the wave number
        for (int i = 0; i < waveLines.length; i++){
            int waveNumber = Integer.parseInt(waveLines[i].split(",")[0]);
            // if the wave number we find is different to our current wave, or if this is the last line,
            // we know we can group all the lines of text and create one wave from them
            if (waveNumber != currWave || i == waveLines.length - 1){
                waves[prevWave] = new Wave(Arrays.copyOfRange(waveLines, eventIndex, i), parentLevel);
                currWave ++;
                prevWave ++;
                eventIndex = i;
            }
        }
        return waves;
    }

    /**
     * Gets the current wave status
     * @return: Returns the string status of the wave (either in progress or not)
     */
    public String getWaveStatus() {
        return waveStatus;
    }

    /**
     * Gets the number of the current wave
     * @return the integer wave number with a 0 offset ie. wave 1 returns 0
     */
    public int getCurrentWaveNum(){
        return currentWave;
    }

    /**
     * @return: returns true iff all waves have been completed
     */
    public boolean isFinished() {
        return isFinished;
    }
}
