/**
 * Class referencing the values and attributes of the player, specifically
 * their money and remaining lives.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class Player {
    private static final int STARTING_MONEY = 500;
    private static final int STARTING_LIVES = 25;
    private int money;
    private int lives;

    public Player(){
        this.money = STARTING_MONEY;
        this.lives = STARTING_LIVES;
    }

    /**
     * @return: Returns the integer amount of money the player has
     */
    public int getMoney() {
        return money;
    }

    /**
     * Takes an integer amount away from the player's money count
     * @param cost: The amount of money to be deducted
     */
    public void takeMoney(int cost) {
        this.money -= cost;
    }

    /**
     * Adds an integer amount to the player's money count.
     * @param reward: The amount of money to be added.
     */
    public void addMoney(int reward) {
        this.money += reward;
    }

    /**
     * @return: Returns the amount of lives the player has remaining.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Takes a given number of lives from the players lives count.
     * @param penalty: The amount of lives to be deducted.
     */
    public void takeLives(int penalty){
        this.lives -= penalty;
    }
}

