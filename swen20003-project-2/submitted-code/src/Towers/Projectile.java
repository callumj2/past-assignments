import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Vector2;
/**
 * Represents the projectiles fired by passive towers in the game,
 * each projectile has a slicer target and moves toward it at a set speed each time the
 * projectile is updated.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class Projectile implements Moveable{
    private static final int PROJECTILE_SPEED = 10;
    private Vector2 position;
    private Slicer target;
    private Image image;
    private int damage;
    private boolean isFinished;

    /**
     * Creates a new projectile
     * @param target: The slicer it is to target
     * @param position: The starting position of the projectile, the position
     *                of the tank that spawned it.
     */
    public Projectile(Slicer target, Vector2 position){
        this.position = position;
        this.target = target;
    }

    /**
     * Updates the projectile, moving it closer to the target and checking if it intersects
     * with it's target bounding box - in which case it deals damage to the target.
     *
     * @param timeModifier: Integer number representing how fast or slow the game is moving.
     */
    @Override
    public void update(int timeModifier){
        // if the target has already been killed, we no longer update this projectile
        if (target.wasKilled() || target.isFinished()){
            this.isFinished = true;
        }
        Vector2 vectorToTarget = target.getCurrentPos().sub(position).normalised();
        // if we don't hit a target this step, continue drawing the projectile
        if (!registerHit(vectorToTarget)){
            position = position.add(vectorToTarget.mul(PROJECTILE_SPEED).mul(timeModifier));
            image.draw(position.x,position.y);
        }

    }

    private boolean registerHit(Vector2 vectorToTarget){
        Rectangle enemyBoundingBox = target.getImage().getBoundingBoxAt(target.getCurrentPos().asPoint());
        Vector2 trialPos = position;
        // for each possible point in the current move, check if a hit is registered
        for (int i = 1; i < PROJECTILE_SPEED + 1; i ++){
            trialPos = position.mul(i);
            if (enemyBoundingBox.intersects(trialPos.asPoint())){
                target.adjustHealth(damage);
                isFinished = true;
                return true;
            }
        }
        // if nothing is hit, return false
        return false;
    }

    /**
     * Sets the image for this projectile
     * @param image: The image to be set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Sets the damage this projectile will do
     * @param damage: The integer damage to be set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * @return: true if the projectile no longer needs to be updated, false otherwise.
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
