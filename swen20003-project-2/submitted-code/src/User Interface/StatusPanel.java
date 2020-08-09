import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Vector2;

/**
 * Represents the 'status panel' of the game, sitting at the bottom of the
 * screen and displaying the current wave status, time modifier, and player lives
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class StatusPanel{
    private static final int INITIAL_XPOS = 5;
    private static final int TYPING_Y_OFFSET = 5;
    private static final int STATUS_FONT_SIZE = 20;
    private final Image panelImage;
    private final Level parentLevel;
    ColouredString waveNumMessage;
    ColouredString timeScaleMessage;
    ColouredString waveStatusMessage;
    ColouredString livesMessage;

    /**
     * Creates a new instance of the status panel
     * @param level: The current level of the game
     */
    public StatusPanel(Level level){
        this.parentLevel = level;
        this.panelImage = new Image("res/images/statusPanel.png");
        // Since the messages are updated every time the game updates, we'll just construct them as empty
        // white coloured strings for now
        this.waveNumMessage = new ColouredString("", Colour.WHITE, STATUS_FONT_SIZE);
        this.timeScaleMessage = new ColouredString("", Colour.WHITE, STATUS_FONT_SIZE);
        this.waveStatusMessage = new ColouredString("",Colour.WHITE, STATUS_FONT_SIZE);
        this.livesMessage = new ColouredString("",Colour.WHITE, STATUS_FONT_SIZE);

    }

    /**
     * Updates the status panel with information from the parent level
     */
    public void update(){
        double typeHeight = getPixelsFromTopLeft(TYPING_Y_OFFSET);
        panelImage.drawFromTopLeft(0, getPixelsFromTopLeft(panelImage.getHeight()));
        // update the wave number
        setWaveNumMessage(parentLevel.getWaveSet().getCurrentWaveNum() + 1);
        waveNumMessage.render(new Vector2(INITIAL_XPOS, typeHeight));
        // update the time scale
        setTimeScaleMessage(parentLevel.getTimeModifier());
        timeScaleMessage.render(new Vector2(panelImage.getWidth()/4, typeHeight));
        // update the wave status
        setWaveStatusMessage(parentLevel.getWaveSet().getWaveStatus());
        // if placing, override the wave status
        if (parentLevel.getBuyPanel().isPendingPurchase()){
            setWaveStatusMessage("Placing");
        }
        // if the game has been beaten, override the wave status
        if (parentLevel.getGame().isFinished()){
            setWaveStatusMessage("Winner!");
        }
        waveStatusMessage.render(new Vector2(panelImage.getWidth()/2, typeHeight));
        // update player lives
        setLivesMessage(parentLevel.getPlayer().getLives());
        livesMessage.render(new Vector2(4*panelImage.getWidth()/5, typeHeight));
    }

    //function that takes an integer number of pixels from the bottom of the game Window and
    // returns the same point referenced from the top left
    private double getPixelsFromTopLeft(double pos){
        return Window.getHeight() - pos;
    }

    private void setWaveNumMessage(int waveNum) {
        waveNumMessage.setMessage(String.format("Wave: %d", waveNum));
    }

    private void setTimeScaleMessage(int timeScale) {
        timeScaleMessage.setMessage(String.format("TimeScale: %d.0", timeScale));
        if (timeScale > 1){
            timeScaleMessage.setColour(Colour.GREEN);
        }
        else {
            timeScaleMessage.setColour(Colour.WHITE);
        }
    }

    private void setWaveStatusMessage(String waveStatus) {
        waveStatusMessage.setMessage(waveStatus);
    }

    private void setLivesMessage(int lives) {
        livesMessage.setMessage(String.format("Lives: %d", lives));
    }

    /**
     * @return: Returns the status panel image
     */
    public Image getPanelImage() {
        return panelImage;
    }

}
