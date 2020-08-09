/**
 * Refers to a delay event, during which no Slicers spawn for a
 * given amount of time
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class DelayEvent extends WaveEvent {
    private final static int DELAY_INDEX = 2;
    private boolean inProgress;
    private Timekeeper timekeeper;
    private double delay;

    /**
     * Creates a new Delay Event.
     * @param event: An array of strings containing the event
     *             information
     */
    public DelayEvent(String[] event){
        setFinished(false);
        this.delay = Double.parseDouble(event[DELAY_INDEX]);
    }

    /**
     * Updates the delay event according to the current time modifier.
     * @param timeModifier: The current in-game time modifier
     */
    public void update(int timeModifier){
        if (!inProgress){
            this.timekeeper = new Timekeeper(delay);
            inProgress = true;
        }
        timekeeper.update(timeModifier);
        if (timekeeper.shouldRelease()){
            setFinished(true);
        }
    }
}
