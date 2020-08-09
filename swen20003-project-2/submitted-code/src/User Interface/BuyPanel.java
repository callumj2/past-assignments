import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * Represents the 'buy panel' of the game, sitting at the top of the
 * screen and displaying player cash, key binds and possible purchases to be made
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class BuyPanel {
    private static final int WIDTH_OFFSET = 64;
    private static final int HEIGHT_OFFSET = 10;
    private static final int MONEY_RIGHT_WIDTH_OFFSET = 200;
    private static final int MONEY_HEIGHT_OFFSET = 65;
    private static final int SPACE_BETWEEN_ITEMS = 120;
    private static final int NUMBER_OF_PURCHASES = 3;
    private boolean nextPlaneHorizontal;
    private Level parentLevel;
    private Image image;
    private BuyItem[] buyItems;
    private ColouredString keyBinds;
    private ColouredString playerMoney;
    private PurchasedItem purchasedItem;
    private boolean pendingPurchase;

    /**
     * Creates a new instance of the buy panel
     * @param level: The current level of the game
     */
    public BuyPanel(Level level){
        this.nextPlaneHorizontal= true;
        this.parentLevel = level;
        this.image = new Image("res/images/buyPanel.png");
        // Initialize buy Images
        this.buyItems = new BuyItem[NUMBER_OF_PURCHASES];
        // Initialize key binds and money display
        this.keyBinds = new ColouredString(
                "Key Binds:\n\nS- Start Wave\nL - Increase Timescale\nK- Decrease Timescale",
                Colour.WHITE, 10);
        this.playerMoney = new ColouredString(String.format("$%d", level.getPlayer().getMoney()),
                Colour.WHITE, 60);

        // Start initializing the buy items 10 px's above halfway down the panel, and 64 px's to the right of
        // the left border
        double startX = WIDTH_OFFSET;
        double startY = image.getHeight()/2 - HEIGHT_OFFSET;

        // initialise each item in sequence
        for (int i = 0; i < NUMBER_OF_PURCHASES; i ++) {
            buyItems[i] = new BuyItem(i, new Vector2(startX, startY));
            startX += SPACE_BETWEEN_ITEMS;
        }

        // initialize the state of purchases
        this.pendingPurchase = false;
    }

    /**
     * Updates the panel, checking for purchases made.
     *
     * @param input: Input from the keyboard and mouse, used to check
     *             whether the player has attempted to make a purchase and if that purchase
     *             is valid or not.
     */
    public void update(Input input){
        // if there is a pending purchase, update it
        if (pendingPurchase){
            purchasedItem.update(input);
            if (purchasedItem.isPlaced()){
                this.pendingPurchase = false;
            }
            // if the mouse is right clicked, refund the pending purchase
            if (input.wasPressed(MouseButtons.RIGHT)){
                this.pendingPurchase = false;
                purchasedItem = null;
            }
        }
        // otherwise check if an buy item was clicked
        else {
            if (input.isDown(MouseButtons.LEFT) && !pendingPurchase) {
                checkPurchase(input);
            }
        }
        // render in the panel from the top left corner of the window
        image.drawFromTopLeft(0,0);
        // for each item in the panel, update it
        for (int i = 0; i < NUMBER_OF_PURCHASES; i ++){
            buyItems[i].update(parentLevel.getPlayer().getMoney());
        }
        playerMoney.setMessage(String.format("$%d", parentLevel.getPlayer().getMoney()));
        // render in the key binds and player money
        keyBinds.render(new Vector2(image.getWidth()/2, image.getHeight()/3));
        playerMoney.render(new Vector2(image.getWidth() - MONEY_RIGHT_WIDTH_OFFSET, MONEY_HEIGHT_OFFSET));
    }

    private void checkPurchase(Input input){
        Point mousePos = input.getMousePosition();
        // check if the mouse is hovering over a buy item
        for (BuyItem buyItem : buyItems) {
            // if the mouse has clicked this item and the player has enough money, register the purchase
            if (buyItem.getImage().getBoundingBoxAt(buyItem.getImagePosition().asPoint()).intersects(mousePos)
                    && parentLevel.getPlayer().getMoney() >= buyItem.getCost()) {
                this.pendingPurchase = true;
                this.purchasedItem = new PurchasedItem(buyItem.getImage(), buyItem.getType(), parentLevel,
                        buyItem.getCost());
            }
        }
    }

    /**
     * @return: The image of the panel
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return: True if the player is placing a tower, false otherwise.
     */
    public boolean isPendingPurchase() {
        return pendingPurchase;
    }

    /**
     * @return: True if the next plane to be spawned should be horizontal, false otherwise
     */
    public boolean isNextPlaneHorizontal() {
        return nextPlaneHorizontal;
    }

    /**
     * Sets the spawn conditions for the next plane to be purchased.
     * @param nextPlaneHorizontal: True if the next plane should be horizontal, false otherwise
     */
    public void setNextPlaneHorizontal(boolean nextPlaneHorizontal) {
        this.nextPlaneHorizontal = nextPlaneHorizontal;
    }
}
