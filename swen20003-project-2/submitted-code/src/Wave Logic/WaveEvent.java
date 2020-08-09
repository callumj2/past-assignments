/**
 * Class referring broadly to Wave Events which can be
 * either Delay or Spawn Events.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public abstract class WaveEvent{
    private boolean isFinished;

    /**
     * Updates the wave event.
     * @param timeModifier: The current in-game time modifier
     */
    public abstract void update(int timeModifier);

    //getters and setters

    /**
     * @return: Returns true if the wave event has finished, or false otherwise
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * @param finished: New boolean value to set this events 'isFinished'
     *                value to
     */
    public void setFinished(boolean finished) {
        isFinished = finished;
    }

}
