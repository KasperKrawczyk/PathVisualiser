import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InstantPanel extends ModePanel{



    public InstantPanel(InstantGrid grid, String panelName){
        super(grid, panelName);
        algorithmMenu.addActionListener(this);
        grid.startAlgorithmThread();

    }


    /**
     * Listens to the buttons firing off
     *
     * @param actionEvent
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if(actionEvent.getSource().equals(this.algorithmMenu)){
            ((InstantGrid)grid).setChosenAlgorithm(algorithmMenu.getSelectedIndex());
            grid.clearExploredAfterRun();
            ((InstantGrid)grid).startAlgorithmThread();
        }

        if ("clearAll".equals(actionEvent.getActionCommand())) {

            grid.stopThreadAndCreateGrid();

            clearAllButton.setEnabled(true);
            clearExploredButton.setEnabled(true);
            ((InstantGrid)grid).startAlgorithmThread();

            playSound(actionEvent);
        }

        if ("clearExplored".equals(actionEvent.getActionCommand())) {
            if (this.algorithmThread != null) {
                grid.stopThread();
                this.algorithmThread = null;
            }
            grid.clearExploredAfterRun();


            clearAllButton.setEnabled(true);
            clearExploredButton.setEnabled(true);
            ((InstantGrid)grid).startAlgorithmThread();

            playSound(actionEvent);
        }

    }

    /**
     * Creates an InstantPanel object
     */
    public static InstantPanel initialise(){
         return new InstantPanel(new InstantGrid(500, 500, 45, 45), "Instant Panel");
//        instantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        instantFrame.setTitle("PathVisualiser InstantMode");
//        instantFrame.setVisible(true);
    }

    public static void main(String[] args){
        InstantPanel.initialise();
    }

}
