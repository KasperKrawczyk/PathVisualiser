import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public GUI() {
        setTitle("Interactive Path Visualiser");
        setSize(1450, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(tabbedPane);

        addModePanel(InstantPanel.initialise());
        addModePanel(AnimationPanel.initialise());
        this.pack();


    }

    private void addModePanel(ModePanel modePanel){
        JPanel tabPanel = new TabPanel(modePanel);
        tabPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabbedPane.addTab(modePanel.getPanelName(), tabPanel);
    }

    public static class TabPanel extends JPanel {

        private final JPanel tabPanel;


        private static final long serialVersionUID = 1L;

        public TabPanel(ModePanel modePanel) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
            this.tabPanel = modePanel;
            contentPanel.add(modePanel);
            contentPanel.add(Box.createRigidArea(new Dimension(25, 25)));
            add(Box.createRigidArea(new Dimension(10, 10)));
            this.add(contentPanel);
        }
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
        });

    }

}
