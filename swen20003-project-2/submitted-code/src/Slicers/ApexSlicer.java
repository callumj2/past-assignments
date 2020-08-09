import bagel.Image;
import bagel.map.TiledMap;

/**
 * Specific type extension of the 'Slicer' class.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class ApexSlicer extends Slicer {
    private static final String APEX_SLICER_CHILDREN_INSTRUCTIONS = "mega:4";
    private static final int APEX_SLICER_HEALTH = 25;
    private static final int APEX_SLICER_REWARD = 150;
    private static final int APEX_SLICER_PENALTY = 4;
    private static final double APEX_SLICER_MOVEMENT_SPEED = 0.75;
    public ApexSlicer(TiledMap map){
        super(map);
        setImage(new Image("res/images/apexslicer.png"));
        setMovementSpeed(APEX_SLICER_MOVEMENT_SPEED);
        setHealth(APEX_SLICER_HEALTH);
        setReward(APEX_SLICER_REWARD);
        setPenalty(APEX_SLICER_PENALTY);
        setChildrenSpawnInstructions(APEX_SLICER_CHILDREN_INSTRUCTIONS);
    }

}
