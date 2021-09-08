import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {

    private final Grid grid;
    private AlgorithmThread algorithmThread;
    private final ImageIcon startIcon;
    private final ImageIcon clearIcon;
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

        startIcon = new ImageIcon("images/icon_start_30.png");
        clearIcon = new ImageIcon("images/icon_clear_30.png");

        container = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new BorderLayout());

        startButton = new JButton();
        startButton.setIcon(startIcon);
        startButton.setMnemonic(KeyEvent.VK_S);
        startButton.setActionCommand("run");
        startButton.addActionListener(this);

        clearButton = new JButton();
        clearButton.setIcon(clearIcon);
        clearButton.setMnemonic(KeyEvent.VK_R);
        clearButton.setActionCommand("clear");
        clearButton.addActionListener(this);

        String algorithms[] = {"Dijkstra" , "A*", "Breadth First Search"};
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
                    algorithmThread = new AlgorithmThread(grid, algorithmDropdown.getSelectedIndex());
                    grid.setAlgorithmThread(algorithmThread);
                    grid.start();

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
