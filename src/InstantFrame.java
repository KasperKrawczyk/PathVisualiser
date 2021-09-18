import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InstantFrame extends ModeFrame{


    public InstantFrame(InstantGrid grid){
        super(grid);
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

    public static void initialise(){
        InstantFrame instantFrame = new InstantFrame(new InstantGrid(500, 500, 45, 45));
        instantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instantFrame.setTitle("PathVisualiser InstantMode");
        instantFrame.setVisible(true);
    }

    public static void main(String[] args){
        InstantFrame.initialise();
    }

}
