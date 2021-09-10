/**
 * Copyright © 2021 Kasper Krawczyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Icons by Icons8 (https://icons8.com)
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {

    private final Grid grid;
    private AlgorithmThread algorithmThread;
    private final ImageIcon startIcon;
    private final ImageIcon clearAllIcon;
    private final ImageIcon clearExploredIcon;
    private final JPanel container;
    private final JPanel controlPanel;
    private final JPanel buttonPanel;
    private final JPanel optionPanel;
    private final JButton startButton;
    private final JButton clearExploredButton;
    private final JButton clearAllButton;
    private final JComboBox algorithmDropdown;
    private final JLabel algorithmDropdownLabel;

    public MainFrame(){

        grid = new Grid(500,500,45,45);

        startIcon = new ImageIcon("images/icon_start_30.png");
        clearAllIcon = new ImageIcon("images/icon_clearAll_30.png");
        clearExploredIcon = new ImageIcon("images/icon_clearExplored_30.png");

        container = new JPanel(new BorderLayout());
        controlPanel = new JPanel();

        startButton = new JButton();
        startButton.setIcon(startIcon);
        startButton.setMnemonic(KeyEvent.VK_S);
        startButton.setActionCommand("run");
        startButton.addActionListener(this);

        clearAllButton = new JButton();
        clearAllButton.setIcon(clearAllIcon);
        clearAllButton.setMnemonic(KeyEvent.VK_R);
        clearAllButton.setActionCommand("clearAll");
        clearAllButton.addActionListener(this);

        clearExploredButton = new JButton();
        clearExploredButton.setIcon(clearExploredIcon);
        clearExploredButton.setActionCommand("clearExplored");
        clearExploredButton.addActionListener(this);

        String algorithms[] = {"Dijkstra" , "A*", "Breadth First Search"};
        algorithmDropdown = new JComboBox(algorithms);
        algorithmDropdownLabel = new JLabel("Pick algorithm: ");
        algorithmDropdownLabel.setLabelFor(algorithmDropdown);
        algorithmDropdown.setSize(new Dimension(55, 10));
        algorithmDropdownLabel.setHorizontalAlignment(JLabel.LEFT);

        buttonPanel = new JPanel(new GridLayout(1, 3, 15, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(clearExploredButton);
        buttonPanel.add(clearAllButton);
        controlPanel.add(buttonPanel, BorderLayout.WEST);

        optionPanel = new JPanel(new GridLayout(2,1,0,5));
        optionPanel.add(algorithmDropdownLabel);
        optionPanel.add(algorithmDropdown);
        controlPanel.add(optionPanel, BorderLayout.WEST);

        controlPanel.setSize(new Dimension(400,65));

        container.add(grid, BorderLayout.SOUTH);
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
            clearAllButton.setEnabled(true);
            clearExploredButton.setEnabled(true);
            startButton.setEnabled(false);
        }

        if("clearAll".equals(mouseEvent.getActionCommand())){

            grid.stopThreadAndCreateGrid();
            startButton.setEnabled(true);
            clearAllButton.setEnabled(false);
            clearExploredButton.setEnabled(false);
        }

        if("clearExplored".equals(mouseEvent.getActionCommand())){
            if(this.algorithmThread != null){
                System.out.println("thread isnt null");
                grid.stopThread();
                this.algorithmThread = null;
            }
            grid.clearExploredAfterRun();

            startButton.setEnabled(true);
            clearAllButton.setEnabled(true);
        }

    }

    public static void initialise(){
        MainFrame mf = new MainFrame();
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("Interactive PathVisualiser");
        mf.setVisible(true);
    }

    public static void main(String[] args){
        MainFrame.initialise();
    }
}
