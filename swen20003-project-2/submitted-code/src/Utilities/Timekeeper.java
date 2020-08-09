/**
 * Class used for measuring time elapsed in Shadow Defend. This class is used
 * for delays in wave events, bomb detonation timers and more. It is
 * robust to in-game time modification.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class Timekeeper {
    private double releaseTime;
    private long timeAtLastFrame;
    private long timeSinceLastRelease;
    private boolean shouldRelease;

    /**
     * Creates a new Timekeeper with a given release time
     * @param releaseTime: The amount of time (in milliseconds)
     *                   we would like the timekeeper to keep track of.
     */
    public Timekeeper(double releaseTime) {
        this.releaseTime = releaseTime;
        this.timeAtLastFrame = System.currentTimeMillis();
        this.shouldRelease = false;
        this.timeSinceLastRelease = 0;

    }

    /**
     * Updates the timekeeper according to a given time modifier
     * @param timeModifier: The factor by which actual elapsed time should be multiplied by to
     *                    give in-game time.
     */
    public void update(int timeModifier){
            // if the value of should release is true then it means a slicer was released last update
            if (shouldRelease) {
                shouldRelease = false;
            }
            // update the time since last release by adding the elapsed time between this frame and the
            // previous one
            long timeElapsed = System.currentTimeMillis() - timeAtLastFrame;
            timeSinceLastRelease += timeElapsed * timeModifier;

            // if it has been the designated amount of time, we should release a slicer
            if (timeSinceLastRelease >= releaseTime) {
                shouldRelease = true;
                timeSinceLastRelease = 0;
            }

            timeAtLastFrame = System.currentTimeMillis();
    }

    /**
     *  Resets the timekeeper so that it starts back at 0.
     */
    public void resetTimer() {
        this.timeSinceLastRelease = 0;
    }

    /**
     * @return: Returns true if the release time has elapsed.
     */
    public boolean shouldRelease() {
        return shouldRelease;
    }

    /**
     * Changes the release time associated with this timekeeper.
     * @param newReleaseTime: The desired new release time (in milliseconds).
     */
    public void setNewTime(double newReleaseTime){
        this.timeSinceLastRelease = 0;
        this.releaseTime = newReleaseTime;
    }
}