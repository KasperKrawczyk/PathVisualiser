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

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;

public class AnimationPanel extends ModePanel {


    private final ImageIcon startIcon;
    private final JButton startButton;


    public AnimationPanel(Grid grid, String panelName) {
        super(grid, panelName);

        startIcon = new ImageIcon(this.getClass()
                .getClassLoader()
                .getResource("icon_start_30.png"), "Start");

        startButton = new JButton();
        startButton.setIcon(startIcon);
        startButton.setMnemonic(KeyEvent.VK_S);
        startButton.setActionCommand("run");
        startButton.addActionListener(this);

        buttonPanel.add(startButton);

    }

    /**
     * Listens to the actionEvents from the buttons
     *
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        if ("run".equals(actionEvent.getActionCommand())) {

            SwingWorker swingWorker = new SwingWorker<Void, Void>() {
                protected Void doInBackground() {
                    grid.createEdges();
                    algorithmThread = new AlgorithmThread(grid, algorithmMenu.getSelectedIndex(), 25);
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

        if ("clearAll".equals(actionEvent.getActionCommand())) {

            grid.stopThreadAndCreateGrid();
            startButton.setEnabled(true);
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

            startButton.setEnabled(true);
            clearAllButton.setEnabled(true);

            playSound(actionEvent);
        }

    }

    /**
     * Plays sound on an action event
     *
     * @param actionEvent
     */
    protected void playSound(ActionEvent actionEvent) {
        String source = actionEvent.getActionCommand();
        System.out.println("source = " + source);
        try {
            switch (source) {
                case "run":
                case "clearExplored":
                case "clearAll":
                    URL buttonClickURL = this.getClass()
                            .getClassLoader()
                            .getResource("button_click.wav");
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

    /**
     * Creates an AnimationFrame object
     */
    public static ModePanel initialise() {
        return new AnimationPanel(new Grid(500, 500, 45, 45), "Animation Panel");
    }

    public static void main(String[] args) {
        AnimationPanel.initialise();
    }
}
