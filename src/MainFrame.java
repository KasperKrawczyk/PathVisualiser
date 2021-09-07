import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {

    //ui components
    private Grid grid;
    private AlgorithmThread algorithmThread;
    private JPanel container;
    private JPanel controlPanel;
    private JPanel buttonPanel;
    private JPanel optionPanel;
    private JButton startButton;
    private JButton clearButton;
    private JSpinner stepSpinner;
    private JComboBox algorithmDropdown;
    private JLabel stepSpinnerLabel;
    private JLabel algorithmDropdownLabel;

    public MainFrame(){

        grid = new Grid(500,500,30,30);
//        algorithmThread = new AlgorithmThread(this.grid);
//        grid.setAlgorithmThread(this.algorithmThread);

        container = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new BorderLayout());

        startButton = new JButton("Run");
        startButton.setMnemonic(KeyEvent.VK_S);
        startButton.setActionCommand("run");
        startButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.setMnemonic(KeyEvent.VK_R);
        clearButton.setActionCommand("clear");
        clearButton.addActionListener(this);

//        SpinnerNumberModel stepSizeModel = new SpinnerNumberModel(250, 50, 1000, 50);
//        stepSpinner = new JSpinner(stepSizeModel);
//        stepSpinnerLabel = new JLabel("Time per Step (ms): ");
//        stepSpinnerLabel.setLabelFor(stepSpinner);
//        stepSpinnerLabel.setHorizontalAlignment(JLabel.RIGHT);

        String algorithms[] = {"Dijkstra" , "A*"};
        algorithmDropdown = new JComboBox(algorithms);
        algorithmDropdownLabel = new JLabel("Search Algorithm: ");
        algorithmDropdownLabel.setLabelFor(algorithmDropdown);
        algorithmDropdownLabel.setHorizontalAlignment(JLabel.LEFT);

        buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(clearButton);
        controlPanel.add(buttonPanel, BorderLayout.WEST);

        optionPanel = new JPanel(new GridLayout(1, 2, 15, 5));
        optionPanel.add(algorithmDropdownLabel);
        optionPanel.add(algorithmDropdown);
        controlPanel.add(optionPanel, BorderLayout.CENTER);

        controlPanel.setPreferredSize(new Dimension(400,75));

        container.add(grid, BorderLayout.CENTER);
        container.add(controlPanel, BorderLayout.NORTH);

        this.add(container);
        this.setResizable(false);
        this.pack();
    }

    //listen to the ui components and respond to user input
    public void actionPerformed(ActionEvent mouseEvent){
        if("run".equals(mouseEvent.getActionCommand())){

            SwingWorker swingWorker = new SwingWorker<Void,Void>(){
                protected Void doInBackground(){
                    grid.createEdges();
                    algorithmThread = new AlgorithmThread(grid);
                    grid.setAlgorithmThread(algorithmThread);
                    grid.start(algorithmDropdown.getSelectedIndex());

                    return null;
                }
            };
            swingWorker.run();
            startButton.setEnabled(false);
        }

        if("clear".equals(mouseEvent.getActionCommand())){

            grid.stopThread();
            startButton.setEnabled(true);
        }

    }
    public static void main(String[] args){
        MainFrame mf = new MainFrame();
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("Interactive PathVisualiser");
        mf.setVisible(true);
    }
}
