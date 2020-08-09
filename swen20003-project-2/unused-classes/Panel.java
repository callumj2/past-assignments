import bagel.Font;
import bagel.Image;
import bagel.Input;

public abstract class Panel {
    private Image panelImage;
    private Level level;

    public abstract void update(Input input);

    public Image getPanelImage() {
        return panelImage;
    }

    public void setPanelImage(Image panelImage) {
        this.panelImage = panelImage;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
