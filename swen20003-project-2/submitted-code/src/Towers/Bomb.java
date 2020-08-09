import bagel.Image;
import bagel.util.Point;

/**
 * Represents the 'bombs' dropped by the 'Plane' class.
 * Each bomb has a set detonation time, damage and area of effect, and will
 * detonate, dealing damage to any nearby slicers, after the detonation time.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class Bomb implements Moveable{
    private static final int DETONATION_TIME = 2000;
    private static final int BOMB_DAMAGE = 500;
    private static final int AREA_OF_EFFECT = 200;
    private final Level parentLevel;
    private final Image bombImage;
    private final Point position;
    private final Timekeeper detonationClock;
    private boolean isFinished;

    /**
     * Creates a new Bomb, initiating the image for the bomb and the
     * timekeeper that will act as a detonation clock
     * @param position: The x,y co-ordinate that the bomb is to be placed at
     * @param parentLevel: The level class that is currently running in the game
     */
    public Bomb(Point position, Level parentLevel){
        this.parentLevel = parentLevel;
        this.bombImage = new Image("res/images/explosive.png");
        this.position = position;
        this.isFinished = false;
        this.detonationClock = new Timekeeper(DETONATION_TIME);
    }

    /**
     * Updates the Bomb, checking if the detonation timer is complete
     * @param timeModifier: Integer number representing how fast or slow the game is moving.
     */
    public void update(int timeModifier){
        bombImage.draw(position.x, position.y);
        detonationClock.update(timeModifier);
        // if the timer is complete, detonate the bomb
        if (detonationClock.shouldRelease()){
            detonate();
            isFinished = true;
        }
    }

    private void detonate(){
        // Check all the current slicers, and if the distance to them from this bomb
        // is less than the bomb radius, deal damage to them
        for (Slicer slicer : parentLevel.getSlicerManager().getActiveSlicers()){
            double distanceFromDetonation = slicer.getCurrentPos().asPoint().distanceTo(position);
            if (distanceFromDetonation <= AREA_OF_EFFECT){
                slicer.adjustHealth(BOMB_DAMAGE);
            }
        }
    }

    /**
     * @return: returns true if the Bomb no longer needs to be updated,
     * and false otherwise.
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
