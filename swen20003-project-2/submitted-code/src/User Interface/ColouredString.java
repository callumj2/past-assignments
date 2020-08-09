import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Vector2;
/**
 * Wrapper class for a string containing printing details including a desired
 * colour, message, font and size.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class ColouredString {
    private static String DEFAULT_FONT = "res/fonts/dejaVuSans-Bold.ttf";
    private static int DEFAULT_SIZE = 25;
    private DrawOptions drawOptions;
    private Font font;
    private String message;

    /**
     * Create a new Coloured String
     * @param message: The string of characters to be displayed
     * @param colour: The desired colour of the string
     */
    public ColouredString(String message, Colour colour) {
        this.message = message;
        this.drawOptions = new DrawOptions();
        drawOptions.setBlendColour(colour);
        this.font = new Font(DEFAULT_FONT, DEFAULT_SIZE);
    }

    /**
     * Create a new Coloured String
     * @param message: The string of characters to be displayed
     * @param colour: The desired colour of the string
     * @param fontSize: The desired font size of the string.
     */
    public ColouredString(String message, Colour colour, int fontSize) {
        this.message = message;
        this.drawOptions = new DrawOptions();
        drawOptions.setBlendColour(colour);
        this.font = new Font(DEFAULT_FONT, fontSize);
    }

    /**
     * Draws the Coloured String onto the screen
     * @param position: The x,y co-ordinate the string is to be drawn at
     */
    public void render(Vector2 position){
        font.drawString(message, position.x, position.y, drawOptions);
    }

    /**
     * Sets a new colour for the string
     * @param colour: The new colour.
     */
    public void setColour(Colour colour) {
        drawOptions.setBlendColour(colour);
    }

    /**
     * Sets a new message for the string
     * @param message: The new message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
