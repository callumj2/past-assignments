import java.util.ArrayList;
/**
 * Management class that keeps track of all active slicers, as well as
 * handling the spawning of slicer children, as well as adjusting player lives and money
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class SlicerManager {
    private final static int UPDATES_BETWEEN_CHILD_SPAWNS = 8;
    private final Level parentLevel;
    private final ArrayList<Slicer> activeSlicers;
    ArrayList<Slicer> slicersToRemove;
    ArrayList<Slicer> slicersToAdd;

    /**
     * Creates a new Slicer Manager
     * @param parentLevel: The current level of the game
     */
    public SlicerManager(Level parentLevel){
        this.parentLevel = parentLevel;
        this.activeSlicers = new ArrayList<>();
        slicersToRemove = new ArrayList<>();
        slicersToAdd = new ArrayList<>();
    }

    /**
     * Updates the slicer manager, checking for any changes to currently active
     * slicers
     */
    public void update(){
        // for each finished slicer,if it is finished factor the
        // reward/penalty and remove the slicer from the game
        for (Slicer slicer : activeSlicers) {
            if (slicer.isFinished()) {
                slicersToRemove.add(slicer);
                if (slicer.wasKilled()) {
                    spawnChildren(slicer);
                    parentLevel.getPlayer().addMoney(slicer.getReward());
                } else {
                    parentLevel.getPlayer().takeLives(slicer.getPenalty());
                }
            }
        }
        // for each finished slicer, remove it from the game
        for (Slicer slicer : slicersToRemove){
            activeSlicers.remove(slicer);
        }

        // for each child slicer created, add  it to the game
        for (Slicer slicer : slicersToAdd){
            parentLevel.addSlicer(slicer);
        }
        // clear the arraylists
        slicersToAdd.clear();
        slicersToRemove.clear();
    }

    // Takes a Slicer and runs it's children spawn instructions
    private void spawnChildren(Slicer slicer) {
        // Check that the spawn instructions are non-empty
        if (!slicer.getChildrenSpawnInstructions().isEmpty()) {
            String[] instructions = slicer.getChildrenSpawnInstructions().split(":");
            // for aesthetics, we will leave room in between each slicer, with the first slicer spawning at the same
            // spot it's parent died
            int numToSpawn = Integer.parseInt(instructions[1]);
            for (int i = 0; i < numToSpawn; i++) {
                Slicer newSlicer = createNewSlicer(instructions[0], slicer);
                // to spread the slicers out - each spawn will be moved forward relative to the original
                for (int j = 0; j < i * UPDATES_BETWEEN_CHILD_SPAWNS; j++) {
                    newSlicer.update(1);
                }
                // prepare the slicer to be added to the game
                slicersToAdd.add(newSlicer);
            }
        }
    }
    // create a new slicer at a given position
    private Slicer createNewSlicer(String SlicerType, Slicer parent){
        Slicer slicer;
        switch (SlicerType) {
            case "regular":
                slicer = new RegularSlicer(parentLevel.getMap());
                break;
            case "super":
                slicer = new SuperSlicer(parentLevel.getMap());
                break;
            case "mega":
                slicer = new MegaSlicer(parentLevel.getMap());
                break;
            case "apex":
                slicer = new ApexSlicer(parentLevel.getMap());
                break;
            default:
                return null;
        }
        // set the slicer to the same state as it's parent
        slicer.setCurrentPos(parent.getCurrentPos());
        slicer.setVelocity(parent.getVelocity());
        slicer.setCurrentLine(parent.getCurrentLine());
        return slicer;
    }

    /**
     * @return: Returns the arrayList containing all active slicers.
     */
    public ArrayList<Slicer> getActiveSlicers() {
        return activeSlicers;
    }

    /**
     * Adds a given slicer into the arraylist of active slicers.
     * @param slicer: The Slicer to be added
     */
    public void addSlicer(Slicer slicer){
        activeSlicers.add(slicer);
    }
}
