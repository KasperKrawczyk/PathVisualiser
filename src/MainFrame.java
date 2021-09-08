import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {

    private final Grid grid;
    private AlgorithmThread algorithmThread;
    private final JPanel container;
    private final JPanel controlPanel;
    private final JPanel buttonPanel;
    private final JPanel optionPanel;
    private final JButton startButton;
    private final JButton clearButton;
    private final JComboBox algorithmDropdown;
    private final JLabel algorithmDropdownLabel;

    public MainFrame(){

        grid = new Grid(500,500,45,45);

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

        String algorithms[] = {"Dijkstra" , "A*"};
        algorithmDropdown = new JComboBox(algorithms);
        algorithmDropdownLabel = new JLabel("Pick algorithm: ");
        algorithmDropdownLabel.setLabelFor(algorithmDropdown);
        algorithmDropdown.setSize(new Dimension(55, 10));
        algorithmDropdownLabel.setHorizontalAlignment(JLabel.RIGHT);

        buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(clearButton);
        controlPanel.add(buttonPanel, BorderLayout.WEST);

        optionPanel = new JPanel(new GridLayout(1, 2, 25, 5));
        optionPanel.add(algorithmDropdownLabel);
        optionPanel.add(algorithmDropdown);
        controlPanel.add(optionPanel, BorderLayout.CENTER);

        controlPanel.setSize(new Dimension(400,65));

        container.add(grid, BorderLayout.CENTER);
        container.add(controlPanel, BorderLayout.NORTH);

        this.add(container);
        this.setResizable(true);
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
