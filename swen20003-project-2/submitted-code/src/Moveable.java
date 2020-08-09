/**
 * Moveable classes are those which have a set path they follow across the game screen, and
 * have distinct conditions for when they are finished. They can be updated with no other information
 * other than the time modifier. This includes Projectiles, Planes and SLicers.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public interface Moveable {
    void update(int timeModifier);
    boolean isFinished();
}
