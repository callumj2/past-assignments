import bagel.Image;
import bagel.map.TiledMap;

/**
 * Specific type extension of the 'Slicer' class.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class SuperSlicer extends Slicer {
    private static final String SUPER_SLICER_CHILDREN_INSTRUCTIONS = "regular:2";
    private static final int SUPER_SLICER_HEALTH = 1;
    private static final int SUPER_SLICER_REWARD = 15;
    private static final int SUPER_SLICER_PENALTY = 4;
    private static final double SUPER_SLICER_MOVEMENT_SPEED = 1.5;
    public SuperSlicer(TiledMap map){
        super(map);
        setImage(new Image("res/images/superslicer.png"));
        setMovementSpeed(SUPER_SLICER_MOVEMENT_SPEED);
        setHealth(SUPER_SLICER_HEALTH);
        setReward(SUPER_SLICER_REWARD);
        setPenalty(SUPER_SLICER_PENALTY);
        setChildrenSpawnInstructions(SUPER_SLICER_CHILDREN_INSTRUCTIONS);
    }
}
