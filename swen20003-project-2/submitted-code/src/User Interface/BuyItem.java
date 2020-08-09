import bagel.Image;
import bagel.util.Vector2;
/**
 * Represents any item that can be bought by the player, containing the cost and image
 * of the item as well as the position the display is to be drawn in
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class BuyItem {
    private static final int PRICE_OF_TANK = 250;
    private static final int PRICE_OF_SUPER_TANK= 600;
    private static final int PRICE_OF_PLANE = 500;
    private String type;
    private Image image;
    private PriceBoard priceBoard;
    private Vector2 imagePosition;
    private Vector2 pricePosition;
    private int cost;

    /**
     * Create a new Buy Item display
     * @param renderNum: Integer referring to which item to display
     * @param position: The desired position for the display to be rendered at
     */
    public BuyItem(int renderNum, Vector2 position) {
        // the integer renderNum refers to which image from the library to load
        if (renderNum == 0){
            this.image = new Image("res/images/tank.png");
            this.cost = PRICE_OF_TANK;
            this.priceBoard = new PriceBoard(PRICE_OF_TANK);
            this.type = "RegularTank";
        }
        else if (renderNum == 1){
            this.image = new Image("res/images/superTank.png");
            this.cost = PRICE_OF_SUPER_TANK;
            this.priceBoard = new PriceBoard(PRICE_OF_SUPER_TANK);
            this.type = "SuperTank";
        }
        else if (renderNum == 2){
            this.image = new Image("res/images/airSupport.png");
            this.cost = PRICE_OF_PLANE;
            this.priceBoard = new PriceBoard(PRICE_OF_PLANE);
            this.type = "Plane";
        }
        else {
            System.out.println("ERROR LOADING PRICES");
        }
        this.imagePosition = position;
        // initialise the price position directly below the purchase image
        this.pricePosition = new Vector2(position.x - image.getWidth()/2,
                position.y + image.getHeight()/1.2);
    }

    /**
     * Updates and draws the buy item display
     * @param playerMoney: The current amount of money the player has
     */
    public void update(int playerMoney){
        image.draw(imagePosition.x, imagePosition.y);
        priceBoard.update(pricePosition, playerMoney);
    }

    /**
     * @return: returns the position of this Buy Item
     */
    public Vector2 getImagePosition() {
        return imagePosition;
    }

    /**
     * @return: Returns the image of this Buy Item
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return Returns the cost of the item being represented
     */
    public int getCost() {
        return cost;
    }

    /**
     * @return Returns the type of this buy items
     */
    public String getType() {
        return type;
    }
}

