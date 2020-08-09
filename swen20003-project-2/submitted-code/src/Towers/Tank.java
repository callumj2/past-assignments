import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;
/**
 * Represents the Tanks in the game,
 * Each tank will turn to face (locking on to) any slicer that enters it's
 * effect radius and spawn a new projectile every time it's cool-down period
 * has elapsed.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public abstract class Tank extends Tower{
    private final DrawOptions drawOptions;
    private Timekeeper coolDownTimer;
    private final Level parentLevel;
    private final Vector2 position;
    private Image image;
    private Slicer target;
    private boolean lockedOn;
    private boolean readyToFire;
    private int effectRadius;

    /**
     * Creates a new Tank
     * @param level: The current level of the game
     * @param position: The position where the tank is to be spawned
     */
    public Tank(Level level, Vector2 position){
        this.parentLevel = level;
        this.position = position;
        this.lockedOn = false;
        this.drawOptions = new DrawOptions();
        this.readyToFire = true;
    }

    /**
     * Updates the tank, scanning for and locking on to targets if it doesn't already
     * have one, as well as spawning new projectiles if the tank isn't cooling down.
     *
     * @param timeModifier: Integer number representing how fast or slow the game is moving.
     */
    public void update(int timeModifier){
        // Scan for possible targets, or if already locked on check if the target is still valid
        scanForTarget();
        updateDirection();
        // if locked on and ready to fire, spawn a projectile and begin cooldown period
        if (lockedOn) {
            if (readyToFire) {
                fireProjectile(target);
                readyToFire = false;
                coolDownTimer.resetTimer();
            }
        }
        // update the timer, and update the tank if it is ready to fire again
        coolDownTimer.update(timeModifier);
        if (coolDownTimer.shouldRelease()){
            readyToFire = true;
        }

        // if the target is dead or finished the course, set the tank to not locked on
        if (lockedOn && target.isFinished()){
            lockedOn = false;
            target = null;
        }
        // draw the tank
        image.draw(position.x,position.y, drawOptions);


    }

    /**
     * Fires the tank, the specific instructions on what type of projectile to fire
     * are contained within the subclasses of Tank
     */
    public abstract void fireProjectile(Slicer target);

    private void updateDirection(){
        if (lockedOn) {
            Vector2 vectorToTarget = target.getCurrentPos().sub(position);
            double rotation = Math.atan(vectorToTarget.y / vectorToTarget.x);
            if (vectorToTarget.x < 0) {
                rotation = rotation - Math.PI;
            }
            // since the tank image file has 0 = tank pointing up, rotate it a further 90 degrees
            rotation = rotation + Math.PI/2;
            drawOptions.setRotation(rotation);
        }
    }

    // Checks the game's current slicer list for valid targets to lock on to
    private void scanForTarget() {
        double distanceToTarget;
        // If locked on, check if the current target is still in range
        if (lockedOn){
            distanceToTarget = target.getCurrentPos().sub(position).length();
            if (distanceToTarget > effectRadius){
                lockedOn = false;
            }
        }
        // Otherwise check all the current slicers in the game and lock on to the first one that is in range
        else {
            ArrayList<Slicer> possibleTargets = parentLevel.getSlicerManager().getActiveSlicers();
            for (Slicer slicer : possibleTargets) {
                distanceToTarget = position.sub(slicer.getCurrentPos()).length();
                if (distanceToTarget < effectRadius) {
                    this.target = slicer;
                    this.lockedOn = true;
                }
            }
        }
    }

    /**
     * Takes a point and returns whether or not that point sits within the current
     * bounding box of this tower.
     * @param point: The x,y co-ordinate being queried.
     * @return: true if the given position intersects this Tower, false otherwise.
     */
    @Override
    public boolean intersectsAt(Point point){
        if (image.getBoundingBoxAt(position.asPoint()).intersects(point)){
            return true;
        }
        else
            return false;
    }

    /**
     * Sets the effect radius for this tower
     * @param effectRadius: The integer number of pixels of range this tank has
     */
    public void setEffectRadius(int effectRadius){
        this.effectRadius = effectRadius;
    }

    /**
     * Sets the Image for this tank
     * @param image: The image to be set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return: The current position of this tank in vector form
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Assigns a new cool down timer to this tank
     * @param coolDownTimer: The Timekeeper to be used
     */
    public void setCoolDownTimer(Timekeeper coolDownTimer) {
        this.coolDownTimer = coolDownTimer;
    }
}
