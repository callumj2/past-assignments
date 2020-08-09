import bagel.util.Point;

/**
 * Represents the Towers in the game, which are placed by the player.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public abstract class Tower {
    /**
     * Updates the Tower, including drawing, rotating or moving depending on the implementation
     * @param timeModifier: Integer number representing how fast or slow the game is moving.
     */
    public abstract void update(int timeModifier);

    /**
     * Takes a point and returns whether or not that point sits within the current
     * bounding box of this tower.
     * @param point: The x,y co-ordinate being queried.
     * @return: true if the given position intersects this Tower, false otherwise.
     */
    public abstract boolean intersectsAt(Point point);

}
