/**
 * Copyright © 2021 Kasper Krawczyk
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Icons by Icons8 (https://icons8.com)
 * Sounds by zapsplat (https://zapsplat.com)
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;

public class ModePanel extends JPanel implements ActionListener {

    protected Grid grid;
    protected AlgorithmThread algorithmThread;
    protected String panelName;

    protected final JButton instructionButton;
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

    /**
     * Creates a ModePanel object
     *
     * @param grid      Grid object to draw on
     * @param panelName the name of this panel to be displayed
     */
    public ModePanel(Grid grid, String panelName) {

        this.grid = grid;
        this.panelName = panelName;

        clearAllIcon = new ImageIcon(this.getClass()
                .getClassLoader()
                .getResource("icon_clearAll_30.png"), "Clear All");
        clearExploredIcon = new ImageIcon(this.getClass()
                .getClassLoader()
                .getResource("icon_clearExplored_30.png"), "Clear Explored");

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


        menuPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        menuPanel.add(algorithmMenuLabel);
        menuPanel.add(algorithmMenu);
        instructionButton = new JButton("Help");
        instructionButton.addActionListener((ActionEvent ae) -> {
            JDialog dialog = new JDialog();
            dialog.add(new JLabel(help()));
            dialog.setBounds(400, 400, 250, 250);
            dialog.setVisible(true);
        });
        menuPanel.add(instructionButton);
        controlPanel.add(menuPanel, BorderLayout.WEST);


        controlPanel.setSize(new Dimension(400, 65));

        container.add(grid, BorderLayout.SOUTH);
        container.add(controlPanel, BorderLayout.NORTH);

        this.add(container);

    }

    /**
     * Returns the text of the help dialog window
     *
     * @return String object with the help
     */
    private String help() {
        return "<html>" +
                "<style>" +
                "h1 {text-align: center;}"
                + "</style>"
                + "<h1>Help</h1>"
                + "<h2>Controls</h2>"
                + "<p>LMB for setting Start</p>"
                + "<p>RMB for setting Goal</p>"
                + "<p>LMB + ctrl for painting / erasing walls</p>"
                + "<p>RMB + alt for painting / erasing swamp</p>"
                + "</html>";
    }

    /**
     * Listens to the actionEvents from the buttons
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

    /**
     * Plays sound on an action event
     *
     * @param actionEvent the ActionEvent object that triggers the sound
     */
    protected void playSound(ActionEvent actionEvent) {
        String source = actionEvent.getActionCommand();
        try {
            switch (source) {
                case "clearExplored":
                case "clearAll":
                    URL buttonClickURL = this.getClass().getClassLoader().getResource("button_click.wav");
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(buttonClickURL);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                default:
                    System.out.println("Something went wrong with the sound");
                    break;
            }
        } catch (LineUnavailableException e) {
            System.out.println("playSound() threw LineUnavailableException");
        } catch (UnsupportedAudioFileException e2) {
            System.out.println("playSound() threw UnsupportedAudioFileException");
        } catch (IOException e3) {
            System.out.println("playSound() threw IOException");
        }
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }
}
