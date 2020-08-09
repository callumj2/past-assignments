import bagel.Image;
import bagel.util.Vector2;
/**
 * Super Tanks are a specific Type extension of the 'Tank' class
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class SuperTank extends Tank {
    private static final int SUPER_TANK_EFFECT_RADIUS = 150;
    private static final int SUPER_TANK_COOL_DOWN_PERIOD = 500;
    private final Level parentLevel;
    /**
     * Creates a new Super Tank.
     * @param level: The current level of the game
     * @param position: The position where the tank is to be placed
     */
    public SuperTank(Level level, Vector2 position){
        super(level, position);
        this.parentLevel = level;
        setEffectRadius(SUPER_TANK_EFFECT_RADIUS);
        setCoolDownTimer(new Timekeeper(SUPER_TANK_COOL_DOWN_PERIOD));
        setImage(new Image("res/images/superTank.png"));
    }

    /**
     * Specific implementation of instructions for firing the tank
     */
    @Override
    public void fireProjectile(Slicer target) {
        parentLevel.addKineticObject(new SuperTankProjectile(target, this.getPosition()));
    }
}
