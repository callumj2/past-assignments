import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Represents the Plane class, which is an 'active tower' in the game.
 * The Plane flies across the screen either horizontally or vertically and drops
 * a bomb once every 0-3 seconds (inclusive). The Plane stops updating once it has
 * left the screen and all it's dropped bombs have detonated.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class Plane extends Tower implements Moveable{
    private static final int PLANE_SPEED = 3;
    private static final int DROP_TIME_LOWER_BOUND = 1000;
    private static final int DROP_TIME_UPPER_BOUND = 2000;
    private final Image planeImage;
    private final DrawOptions drawOps;
    private final Timekeeper bombDropper;
    private final Level parentLevel;
    private final ArrayList<Bomb> bombs;
    private Point currPos;
    private boolean justDropped;
    private boolean flyingHorizontally;
    private boolean isFinished;

    /**
     * Creates a new Plane.
     * @param spawnPoint The position that the player clicked when placing the plane
     * @param flyingHorizontally True or false value as to whether the plane will fly from left to
     *                           right across the screen.
     * @param parentLevel The currently active level in the game.
     */
    public Plane(Point spawnPoint, boolean flyingHorizontally, Level parentLevel){
        // Initialize the variables for the plane
        this.flyingHorizontally = flyingHorizontally;
        this.drawOps = new DrawOptions();
        this.planeImage = new Image("res/images/airsupport.png");
        this.parentLevel = parentLevel;
        this.isFinished = false;
        this.bombs = new ArrayList<>();

        // Initialize the timekeeper that will control when a bomb should be dropped
        // '1' is added to the RNG here as the upper bound is not inclusive
        int initialDropTime = ThreadLocalRandom.current().nextInt(DROP_TIME_LOWER_BOUND,DROP_TIME_UPPER_BOUND + 1);
        this.bombDropper = new Timekeeper(initialDropTime);
        this.justDropped = false;
        // The Plane can either fly horizontally or vertically, which is specified in its constructor
        if (flyingHorizontally){
            // Set the plane facing to the right, at the leftmost point of the window and at the correct
            // height given by the spawn position
            this.currPos = new Point(0, spawnPoint.y);
            this.drawOps.setRotation(Math.PI/2);
            this.flyingHorizontally = true;
        }
        else {
            // Set the plane facing downward, at the top of the window and at the correct
            // x co-ordinate given by the spawn position
            this.currPos = new Point(spawnPoint.x, 0);
            this.drawOps.setRotation(Math.PI);
            this.flyingHorizontally = false;
        }
    }

    /**
     * Updates the Plane, spawning a new bomb when it is time to do so and moving it across
     * the screen.
     *
     * @param timeModifier: Integer number representing how fast or slow the game is moving.
     */
    @Override
    public void update(int timeModifier) {
        // Check if a bomb can be dropped and if the plane is still within the game window
        bombDropper.update(timeModifier);
        if (bombDropper.shouldRelease() && isInGameWindow()){
            justDropped = true;
            bombs.add(new Bomb(currPos, parentLevel));
        }

        // if we just dropped a bomb, reset the timer to a new random value
        if (justDropped){
            int dropTime = ThreadLocalRandom.current().nextInt(DROP_TIME_LOWER_BOUND, DROP_TIME_UPPER_BOUND + 1);
            bombDropper.setNewTime(dropTime);
            justDropped = false;
        }

        // Update all current bombs
        ArrayList<Bomb> bombsToRemove = new ArrayList<>();
        for (Bomb bomb : bombs){
            bomb.update(timeModifier);
            if (bomb.isFinished()){
                bombsToRemove.add(bomb);
            }
        }
        // Remove all finished bombs
        for (Bomb bomb : bombsToRemove){
            bombs.remove(bomb);
        }

        // update the current position
        if (flyingHorizontally) {
            double newX = currPos.x + PLANE_SPEED * timeModifier;
            currPos = new Point(newX, currPos.y);
        }
        else {
            double newY = currPos.y + PLANE_SPEED * timeModifier;
            currPos = new Point(currPos.x, newY);
        }

        // if the plane has moved outside the window, and has no remaining
        // bombs, it no longer needs to be updated
        if (!isInGameWindow() && bombs.size() == 0){
            isFinished = true;
        }
        render();

    }
    // Checks if the plane is within the game window
    private boolean isInGameWindow(){
        if (currPos.x > Window.getWidth() || currPos.y > Window.getHeight()){
            return false;
        }
        return true;
    }
    // Draws the plane at its current position
    private void render(){
        planeImage.draw(currPos.x,currPos.y, drawOps);
    }

    /**
     * Takes a point and returns whether or not that point sits within the current
     * bounding box of this tower.
     * @param point: The x,y co-ordinate being queried.
     * @return: true if the given position intersects this Tower, false otherwise.
     */
    @Override
    public boolean intersectsAt(Point point){
        if (planeImage.getBoundingBoxAt(currPos).intersects(point)){
            return true;
        }
        else
            return false;
    }

    /**
     * @return: true if the plane no longer needs to be updated, false otherwise.
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
