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
 * Sounds by Blizzard
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
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
    private final JComboBox algorithmMenu;
    private final JLabel algorithmMenuLabel;

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

        String algorithmTypes[] = {"Dijkstra" , "A*", "Breadth First Search"};
        algorithmMenu = new JComboBox(algorithmTypes);
        algorithmMenuLabel = new JLabel("Pick algorithm: ");
        algorithmMenuLabel.setLabelFor(algorithmMenu);
        algorithmMenu.setSize(new Dimension(55, 10));
        algorithmMenuLabel.setHorizontalAlignment(JLabel.LEFT);

        buttonPanel = new JPanel(new GridLayout(1, 3, 15, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(clearExploredButton);
        buttonPanel.add(clearAllButton);
        controlPanel.add(buttonPanel, BorderLayout.WEST);

        optionPanel = new JPanel(new GridLayout(2,1,0,5));
        optionPanel.add(algorithmMenuLabel);
        optionPanel.add(algorithmMenu);
        controlPanel.add(optionPanel, BorderLayout.WEST);

        controlPanel.setSize(new Dimension(400,65));

        container.add(grid, BorderLayout.SOUTH);
        container.add(controlPanel, BorderLayout.NORTH);

        this.add(container);
        this.setResizable(false);
        this.pack();
    }

    /**
     * Listens to the buttons firing off
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent){
        if("run".equals(actionEvent.getActionCommand())){

            SwingWorker swingWorker = new SwingWorker<Void,Void>(){
                protected Void doInBackground() {
                    grid.createEdges();
                    algorithmThread = new AlgorithmThread(grid, algorithmMenu.getSelectedIndex());
                    grid.setAlgorithmThread(algorithmThread);
                    grid.start();

                    return null;
                }
            };
            swingWorker.run();
            clearAllButton.setEnabled(true);
            clearExploredButton.setEnabled(true);
            startButton.setEnabled(false);

            playSound(actionEvent);
        }

        if("clearAll".equals(actionEvent.getActionCommand())){

            grid.stopThreadAndCreateGrid();
            startButton.setEnabled(true);
            clearAllButton.setEnabled(false);
            clearExploredButton.setEnabled(false);

            playSound(actionEvent);
        }

        if("clearExplored".equals(actionEvent.getActionCommand())){
            if(this.algorithmThread != null){
                System.out.println("thread isnt null");
                grid.stopThread();
                this.algorithmThread = null;
            }
            grid.clearExploredAfterRun();

            startButton.setEnabled(true);
            clearAllButton.setEnabled(true);

            playSound(actionEvent);
        }

    }

    private void playSound(ActionEvent actionEvent) {
        String source = actionEvent.getActionCommand();
        System.out.println("source = " + source);
        try{
            switch(source){
                case "run":
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                            new File("sounds/run.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                case "clearExplored":
                    audioInputStream = AudioSystem.getAudioInputStream(
                            new File("sounds/clearExplored.wav").getAbsoluteFile());
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                case "clearAll":
                    audioInputStream = AudioSystem.getAudioInputStream(
                            new File("sounds/clearAll.wav").getAbsoluteFile());
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                default:
                    System.out.println("Something went wrong with the sound");
                    break;
            }
        } catch (LineUnavailableException e) {
            System.out.println("Something went wrong with the sound = LineUnavailableException");
        } catch (UnsupportedAudioFileException e2) {
            System.out.println("Something went wrong with the sound = UnsupportedAudioFileException");
        } catch (IOException e3){
            System.out.println("Something went wrong with the sound = IOException");
        }
    }

    /**
     * Initialises a MainFrame object
     */
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