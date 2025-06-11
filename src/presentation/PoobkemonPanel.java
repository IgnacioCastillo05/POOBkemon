package presentation;

import javax.swing.JPanel;

public abstract class PoobkemonPanel extends JPanel {
    protected PoobkemonGUI mainGUI;
    
    public PoobkemonPanel(PoobkemonGUI mainGUI) {
        this.mainGUI = mainGUI;
        setOpaque(false);
    }
    
    public void initialize() {
        prepareElements();
        prepareActions();
    }
    
    protected abstract void prepareElements();
    protected abstract void prepareActions();
}