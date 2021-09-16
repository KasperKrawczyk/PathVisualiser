import javax.swing.*;
import java.awt.event.ActionEvent;

public class InstantFrame extends ModeFrame{

    protected InstantPainterThread instantPainterThread;


    /**
     * Listens to the buttons firing off
     *
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {


        if ("clearAll".equals(actionEvent.getActionCommand())) {

            grid.stopThreadAndCreateGrid();

            clearAllButton.setEnabled(false);
            clearExploredButton.setEnabled(false);
            this.runInstantPainterThread();

            playSound(actionEvent);
        }

        if ("clearExplored".equals(actionEvent.getActionCommand())) {
            if (this.algorithmThread != null) {
                System.out.println("thread isnt null");
                grid.stopThread();
                this.algorithmThread = null;
            }
            grid.clearExploredAfterRun();


            clearAllButton.setEnabled(true);

            this.runInstantPainterThread();

            playSound(actionEvent);
        }

    }

    private void runInstantPainterThread(){

        SwingWorker swingWorker = new SwingWorker<Void,Void>(){
            protected Void doInBackground() {

                grid.createEdges();
                instantPainterThread = new InstantPainterThread(grid, algorithmMenu.getSelectedIndex());
                grid.setAlgorithmThread(instantPainterThread);
                grid.start();

                return null;
            }
        };
        swingWorker.run();
        clearAllButton.setEnabled(true);
        clearExploredButton.setEnabled(true);
    }



    public static void initialise(){
        InstantFrame iF = new InstantFrame();
        iF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        iF.setTitle("Interactive PathVisualiser");
        iF.setVisible(true);
        iF.runInstantPainterThread();
    }

    public static void main(String[] args){
        InstantFrame.initialise();
    }

}
