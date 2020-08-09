import bagel.Image;
import bagel.util.Vector2;
/**
 * Represents the projectiles fired by a Super Tank
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class SuperTankProjectile extends Projectile {
    private static final int SUPER_TANK_PROJECTILE_DAMAGE = 3;

    /**
     * Creates a new Super Tank Projectile
     * @param target: The slicer target of this projectile
     * @param position: The starting spawn position of this projectile
     */
    public SuperTankProjectile(Slicer target, Vector2 position){
        super(target, position);
        setDamage(SUPER_TANK_PROJECTILE_DAMAGE);
        setImage(new Image("res/images/superTank_projectile.png"));
    }
}

