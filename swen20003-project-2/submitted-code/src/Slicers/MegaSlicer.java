import bagel.Image;
import bagel.map.TiledMap;

/**
 * Specific type extension of the 'Slicer' class.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class MegaSlicer extends Slicer {
    private static final String MEGA_SLICER_CHILDREN_INSTRUCTIONS = "super:2";
    private static final int MEGA_SLICER_HEALTH = 2;
    private static final int MEGA_SLICER_REWARD = 10;
    private static final int MEGA_SLICER_PENALTY = 4;
    private static final double MEGA_SLICER_MOVEMENT_SPEED = 1.5;
    public MegaSlicer(TiledMap map){
        super(map);
        setImage(new Image("res/images/megaslicer.png"));
        setMovementSpeed(MEGA_SLICER_MOVEMENT_SPEED);
        setHealth(MEGA_SLICER_HEALTH);
        setReward(MEGA_SLICER_REWARD);
        setPenalty(MEGA_SLICER_PENALTY);
        setChildrenSpawnInstructions(MEGA_SLICER_CHILDREN_INSTRUCTIONS);

    }
}
