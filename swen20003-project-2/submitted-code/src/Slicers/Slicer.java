import bagel.DrawOptions;
import bagel.map.TiledMap;
import bagel.Image;
import bagel.util.Vector2;

/**
 * Represents the main enemy class in ShadowDefend,
 * All slicers spawn at the start of the first polyline of the map,and have the goal
 * of navigating through the map. Each Slicer has an image and value representing it's health
 * as well as a number of variables relating to it's movement logic.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public abstract class Slicer implements Moveable {
    private Image image;
    private final Vector2[] polyLines;
    private Vector2 currentPos;
    private Vector2 velocity;
    private String childrenSpawnInstructions;
    private int health;
    private int reward;
    private int penalty;
    private int currentLine;
    private double direction;
    private double movementSpeed;
    private boolean isFinished;
    private boolean wasKilled;

    /**
     * Creates a new Slicer.
     * @param map: The current level map being used in the game, which will contain
     *           the polylines the slicer is to follow.
     */
    public Slicer(TiledMap map){
        // Get the polylines and initialise them into an array of vectors
        this.polyLines = new Vector2[map.getAllPolylines().get(0).size()];
        for (int i = 0; i < polyLines.length; i ++){

            polyLines[i] = map.getAllPolylines().get(0).get(i).asVector();
        }
        // The Slicer must start heading in the direction of the first polyline
        this.currentLine = 1;
        this.isFinished = false;
        this.wasKilled = false;

        // initialise the current position and velocity of the slicer
        this.currentPos = polyLines[0];
        this.velocity = polyLines[1].sub(currentPos);
        this.velocity = velocity.normalised();
        updateDirection();
    }


    /**
     * Updates the Slicer's position within the game.
     * @param timeModifier: Integer number representing how fast or slow the game is moving.
     */
    public void update(int timeModifier){
        // if the Slicer has not yet completed the course, Calculate the speed needed for this update
        if (!isFinished) {
            double currSpeed = movementSpeed * timeModifier;
            updatePosition(currSpeed);
            render();
        }

    }

    // Draws the slicer facing towards it's current velocity
    private void render(){
        DrawOptions drawOptions = new DrawOptions();
        updateDirection();
        drawOptions.setRotation(direction);
        image.draw(currentPos.x,currentPos.y, drawOptions);
    }

    // Updates the Slicer's current direction
    private void updateDirection() {

        double xVal = velocity.x;
        double yVal = velocity.y;
        double rotation = Math.atan(yVal/xVal);
        if (xVal < 0){
            rotation = rotation-Math.PI;
        }

        this.direction = rotation;
    }

    // Recursive function to assist in moving and cornering so that Slicers don't jump off polylines
    private void updatePosition(double currSpeed){
        // Calculate the distance to the end of the current polyline and decide which action to take
        double distanceToTarget = polyLines[currentLine].sub(currentPos).length();

        //Base case where only a normal move is required.
        if (distanceToTarget > currSpeed){
            this.currentPos = currentPos.add(velocity.mul(currSpeed));
        }
        //Special case where this is the last polyline
        else if (currentLine == polyLines.length - 1){
            this.currentPos = polyLines[currentLine];
            this.isFinished = true;
        }
        //Recursive case, where our one step will cross a corner.
        else {
            this.currentPos = polyLines[currentLine];
            this.velocity = polyLines[++currentLine].sub(currentPos).normalised();
            updatePosition(currSpeed - distanceToTarget);
        }
    }

    /**
     * Public method called by towers to deal damage to the slicer, sets
     * the slicer to 'killed' if the damage is greater than or equal to the
     * slicer's current health.
     *
     * @param damage: The integer amount of damage to be dealt.
     */
    public void adjustHealth(int damage){
        health -= damage;
        // if the slicer has been killed, set it to finished
        if (health <= 0){
            this.wasKilled = true;
            this.isFinished = true;
        }
    }

    // Getters and Setters
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setCurrentPos(Vector2 currentPos) {
        this.currentPos = currentPos;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Image getImage() {
        return image;
    }
    public Vector2 getCurrentPos() {
        return currentPos;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean wasKilled() {
        return wasKilled;
    }

    public int getReward() {
        return reward;
    }

    public int getPenalty() {
        return penalty;
    }

    public String getChildrenSpawnInstructions() {
        return childrenSpawnInstructions;
    }

    public void setChildrenSpawnInstructions(String spawnInstructions) {
        this.childrenSpawnInstructions = spawnInstructions;
    }
}
