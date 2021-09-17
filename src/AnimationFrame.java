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

public class AnimationFrame extends ModeFrame {


    private final ImageIcon startIcon;


    private final JButton startButton;


    public AnimationFrame(){

        startIcon = new ImageIcon("images/icon_start_30.png");


        startButton = new JButton();
        startButton.setIcon(startIcon);
        startButton.setMnemonic(KeyEvent.VK_S);
        startButton.setActionCommand("run");
        startButton.addActionListener(this);

        buttonPanel.add(startButton);

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

    protected void playSound(ActionEvent actionEvent) {
        String source = actionEvent.getActionCommand();
        System.out.println("source = " + source);
        try{
            switch(source){
                case "run":
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                            new File("sounds/button_click.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                case "clearExplored":
                    audioInputStream = AudioSystem.getAudioInputStream(
                            new File("sounds/button_click.wav").getAbsoluteFile());
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                case "clearAll":
                    audioInputStream = AudioSystem.getAudioInputStream(
                            new File("sounds/button_click.wav").getAbsoluteFile());
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
        AnimationFrame af = new AnimationFrame();
        af.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        af.setTitle("Interactive PathVisualiser");
        af.setVisible(true);
    }

    public static void main(String[] args){
        AnimationFrame.initialise();
    }
}
