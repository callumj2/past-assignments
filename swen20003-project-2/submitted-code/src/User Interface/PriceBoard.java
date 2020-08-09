import bagel.util.Colour;
import bagel.util.Vector2;
/**
 * Simple class for creating and displaying Coloured Strings
 * in the context of in-game prices.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class PriceBoard {
    private int cost;
    private ColouredString display;

    /**
     * Creates a new Price Board
     * @param cost: The cost associated with this Price Board
     */
    public PriceBoard(int cost){
        this.cost = cost;
        this.display = new ColouredString(String.format("$%d", cost), Colour.GREEN);
    }

    /**
     * Updates and renders the Coloured String associated with
     * this Price Board
     *
     * @param position: The desired position for this board to be rendered at
     * @param playerMoney: The current amount of money the player has
     */
    public void update(Vector2 position, int playerMoney){
        // if this item costs more than the player has, set the price display
        // colour to red
        if (playerMoney < cost){
            display.setColour(Colour.RED);
        }
        // otherwise set it to green
        display.render(position);
        display.setColour(Colour.GREEN);
    }
}
