import bagel.Image;
import bagel.map.TiledMap;

/**
 * Specific type extension of the 'Slicer' class.
 *
 * Made for SWEN20003, Project,
 * @author Callum Johnson, 910519.
 */
public class RegularSlicer extends Slicer {
    private static final String REGULAR_SLICER_CHILDREN_INSTRUCTIONS = "";
    private static final int REG_SLICER_HEALTH = 1;
    private static final int REG_SLICER_REWARD = 2;
    private static final int REG_SLICER_PENALTY = 1;
    private static final int REG_SLICER_MOVEMENT_SPEED = 2;
    public RegularSlicer(TiledMap map){
        super(map);
        setImage(new Image("res/images/slicer.png"));
        setMovementSpeed(REG_SLICER_MOVEMENT_SPEED);
        setHealth(REG_SLICER_HEALTH);
        setReward(REG_SLICER_REWARD);
        setPenalty(REG_SLICER_PENALTY);
        setChildrenSpawnInstructions(REGULAR_SLICER_CHILDREN_INSTRUCTIONS);
    }
}
