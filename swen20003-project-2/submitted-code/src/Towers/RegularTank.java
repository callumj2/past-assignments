import bagel.Image;
import bagel.util.Vector2;
/**
 * Regular Tanks are a specific type extension of the 'Tank' class.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class RegularTank extends Tank{
    private static final int REGULAR_TANK_EFFECT_RADIUS = 100;
    private static final int REGULAR_TANK_COOL_DOWN_PERIOD = 1000;
    private final Level parentLevel;
    /**
     * Creates a new Regular Tank.
     * @param level: The current level of the game
     * @param position: The position where the tank is to be placed
     */
    public RegularTank(Level level, Vector2 position){
        super(level, position);
        this.parentLevel = level;
        setEffectRadius(REGULAR_TANK_EFFECT_RADIUS);
        setCoolDownTimer(new Timekeeper(REGULAR_TANK_COOL_DOWN_PERIOD));
        setImage(new Image("res/images/tank.png"));
    }

    /**
     * Specific implementation of instructions for firing the tank
     */
    @Override
    public void fireProjectile(Slicer target) {
       parentLevel.addKineticObject(new RegularTankProjectile(target, this.getPosition()));
    }
}
