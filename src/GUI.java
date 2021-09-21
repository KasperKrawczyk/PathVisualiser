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

import javax.swing.*;

public class GUI extends JFrame {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    /**
     * Creates a GUI object
     */
    public GUI() {
        setTitle("Interactive Path Visualiser");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(tabbedPane);
        addModePanel(InstantPanel.initialise());
        addModePanel(AnimationPanel.initialise());
        pack();
        //centres the JFrame object relative to the screen
        setLocationRelativeTo(null);
    }

    /**
     * Adds a ModePanel object to this GUI
     *
     * @param modePanel a ModePanel object
     */
    private void addModePanel(ModePanel modePanel) {
        tabbedPane.addTab(modePanel.getPanelName(), modePanel);
    }


    public static void main(String[] args) {
        GUI gui = new GUI();
        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
        });

    }

}
