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

import java.awt.event.ActionEvent;

public class InstantPanel extends ModePanel {


    public InstantPanel(InstantGrid grid, String panelName) {
        super(grid, panelName);
        algorithmMenu.addActionListener(this);
        grid.startAlgorithmThread();

    }


    /**
     * Listens to the actionEvents from the buttons
     *
     * @param actionEvent
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource().equals(this.algorithmMenu)) {
            ((InstantGrid) grid).setChosenAlgorithm(algorithmMenu.getSelectedIndex());
            grid.clearExploredAfterRun();
            ((InstantGrid) grid).startAlgorithmThread();
        }

        if ("clearAll".equals(actionEvent.getActionCommand())) {

            grid.stopThreadAndCreateGrid();

            clearAllButton.setEnabled(true);
            clearExploredButton.setEnabled(true);
            ((InstantGrid) grid).startAlgorithmThread();

            playSound(actionEvent);
        }

        if ("clearExplored".equals(actionEvent.getActionCommand())) {
            if (this.algorithmThread != null) {
                grid.stopThread();
                this.algorithmThread = null;
            }
            grid.clearExploredAfterRun();


            clearAllButton.setEnabled(true);
            clearExploredButton.setEnabled(true);
            ((InstantGrid) grid).startAlgorithmThread();

            playSound(actionEvent);
        }

    }

    /**
     * Creates an InstantPanel object
     */
    public static ModePanel initialise() {
        return new InstantPanel(new InstantGrid(500, 500, 45, 45), "Instant Panel");
    }

    public static void main(String[] args) {
        InstantPanel.initialise();
    }

}
