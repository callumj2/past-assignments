import bagel.Image;
import bagel.Input;
import bagel.MouseButtons;
import bagel.Window;
import bagel.util.Point;
/**
 * Represents any pending purchase the player has decided to make
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class PurchasedItem {
    private final String type;
    private final Image image;
    private final Level parentLevel;
    private final int cost;
    private boolean placed;

    /**
     * Creates a new Purchased Item
     * @param image: The image of the purchased item
     * @param type: The type (eg. plane,
     * @param parentLevel
     * @param cost
     */
    public PurchasedItem(Image image, String type, Level parentLevel, int cost){
        this.image = image;
        this.type = type;
        this.parentLevel = parentLevel;
        this.cost = cost;
        this.placed = false;
    }

    /**
     * Updates the purchased item, rendering it to the current mouse position and
     * processing purchase or refund depending on player input.
     * @param input: Input from the player's keyboard and mouse
     */
    public void update(Input input){
        Point mousePos = input.getMousePosition();
        // check if the current position is valid
        if (checkValidPosition(mousePos)) {
            // render the item to the current mouse position
            image.draw(input.getMouseX(), input.getMouseY());
            // if the left mouse button is clicked over a valid position, spawn the item
            if (input.wasPressed(MouseButtons.LEFT)) {
                parentLevel.getPlayer().takeMoney(cost);
                spawnTowerFromType(mousePos);
                placed = true;
            }
        }
    }

    private void spawnTowerFromType(Point position){
        if (type.equals("RegularTank")) {
            parentLevel.addPassiveTower(new RegularTank(parentLevel, position.asVector()));
        }
        else if (type.equals("SuperTank")){
            parentLevel.addPassiveTower(new SuperTank(parentLevel, position.asVector()));
        }
        else {
            // Toggle the 'next plane horizontal' setting and spawn a new plane
            boolean horizontalSetting = parentLevel.getBuyPanel().isNextPlaneHorizontal();
            parentLevel.getBuyPanel().setNextPlaneHorizontal(!horizontalSetting);
            parentLevel.addKineticObject(new Plane(position, horizontalSetting , parentLevel));
        }
    }

    // Checks whether the current mouse position corresponds to a valid spot for a tower to be placed
    private boolean checkValidPosition(Point mousePos){
        // first check that the mouse is inside the window
        if (mousePos.y > Window.getHeight() || mousePos.x > Window.getWidth()){
            return false;
        }
        // if the mouse is hovering over the buy panel, return false
        if (mousePos.y < parentLevel.getBuyPanel().getImage().getHeight()){
            return false;
        }
        // perform the same check with the status panel
        if (mousePos.y > (Window.getHeight() - parentLevel.getStatusPanel().getPanelImage().getHeight())){
            return false;
        }
        // check if the mouse is hovering over any blocked tiles
        if (parentLevel.getMap().getPropertyBoolean((int)mousePos.x, (int)mousePos.y, "blocked",
                false)){
            return false;
        }

        // finally check if the mouse is intersecting any other towers
        for (Tower tower : parentLevel.getPassiveTowers()){
            if (tower.intersectsAt(mousePos)){
                return false;
            }
        }
        return true;
    }

    /**
     * @return: True if the tower has been placed, false otherwise
     */
    public boolean isPlaced() {
        return placed;
    }
}
