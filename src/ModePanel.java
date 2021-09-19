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

public class ModePanel extends JPanel implements ActionListener {

    protected Grid grid;
    protected AlgorithmThread algorithmThread;
    protected String panelName;

    protected final ImageIcon clearAllIcon;
    protected final ImageIcon clearExploredIcon;
    protected final JPanel container;
    protected final JPanel controlPanel;
    protected final JPanel buttonPanel;
    protected final JPanel menuPanel;

    protected final JButton clearExploredButton;
    protected final JButton clearAllButton;
    protected final JComboBox algorithmMenu;
    protected final JLabel algorithmMenuLabel;

    public ModePanel(Grid grid, String panelName) {

        this.grid = grid;
        this.panelName = panelName;

        clearAllIcon = new ImageIcon("images/icon_clearAll_30.png");
        clearExploredIcon = new ImageIcon("images/icon_clearExplored_30.png");

        container = new JPanel(new BorderLayout());
        controlPanel = new JPanel();

        clearAllButton = new JButton();
        clearAllButton.setIcon(clearAllIcon);
        clearAllButton.setMnemonic(KeyEvent.VK_R);
        clearAllButton.setActionCommand("clearAll");
        clearAllButton.addActionListener(this);

        clearExploredButton = new JButton();
        clearExploredButton.setIcon(clearExploredIcon);
        clearExploredButton.setActionCommand("clearExplored");
        clearExploredButton.addActionListener(this);

        String algorithmTypes[] = {"Dijkstra", "A*", "Breadth First Search"};
        algorithmMenu = new JComboBox(algorithmTypes);
        algorithmMenuLabel = new JLabel("Pick algorithm: ");
        algorithmMenuLabel.setLabelFor(algorithmMenu);
        algorithmMenu.setSize(new Dimension(55, 10));
        algorithmMenuLabel.setHorizontalAlignment(JLabel.LEFT);

        buttonPanel = new JPanel(new GridLayout(1, 3, 15, 10));

        buttonPanel.add(clearExploredButton);
        buttonPanel.add(clearAllButton);
        controlPanel.add(buttonPanel, BorderLayout.WEST);

        menuPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        menuPanel.add(algorithmMenuLabel);
        menuPanel.add(algorithmMenu);
        controlPanel.add(menuPanel, BorderLayout.WEST);

        controlPanel.setSize(new Dimension(400, 65));

        container.add(grid, BorderLayout.SOUTH);
        container.add(controlPanel, BorderLayout.NORTH);

        this.add(container);
//        this.setResizable(false);
//        this.pack();
    }

    /**
     * Listens to the buttons firing off
     *
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {


        if ("clearAll".equals(actionEvent.getActionCommand())) {

            grid.stopThreadAndCreateGrid();

            clearAllButton.setEnabled(true);
            clearExploredButton.setEnabled(false);

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

            playSound(actionEvent);
        }

    }

    protected void playSound(ActionEvent actionEvent) {
        String source = actionEvent.getActionCommand();
        System.out.println("source = " + source);
        try {
            switch (source) {
                case "clearExplored":
                case "clearAll":
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                            new File("sounds/button_click.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
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
        } catch (IOException e3) {
            System.out.println("Something went wrong with the sound = IOException");
        }
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }
}
