import bagel.Image;
import bagel.util.Vector2;

/**
 * Represents the projectiles fired by a regular tank
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class RegularTankProjectile extends Projectile{
    private static final int REGULAR_TANK_PROJECTILE_DAMAGE = 1;

    /**
     * Creates a new Regular Tank Projectile
     * @param target: The slicer target of this projectile
     * @param position: The starting spawn position of this projectile
     */
    public RegularTankProjectile(Slicer target, Vector2 position){
        super(target, position);
        setDamage(REGULAR_TANK_PROJECTILE_DAMAGE);
        setImage(new Image("res/images/tank_projectile.png"));
    }
}
