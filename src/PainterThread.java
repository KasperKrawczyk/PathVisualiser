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

import java.awt.event.MouseEvent;

public class PainterThread extends Thread {

    private final Grid grid;
    private boolean isThreadStopped = true;
    private boolean isPainting;
    private CellType cellType;

    /**
     * Creates a PainterThread object
     *
     * @param grid       the Grid object to paint on
     * @param isPainting boolean flag indicating whether to paint or to clear Wall / Swamp cell types
     * @param mouseEvent
     */
    public PainterThread(Grid grid, boolean isPainting, MouseEvent mouseEvent) {
        this.setCellType(mouseEvent);
        this.grid = grid;
        this.isPainting = isPainting;
    }

    /**
     * Runs this thread until the isThreadStopped flag is set to true
     */
    public void run() {

        if (!isThreadStopped) {
            System.out.println("isPainting = " + isPainting);
            do {
                int x = (int) grid.getMousePosition().getX();
                int y = (int) grid.getMousePosition().getY();
                Cell curCell = grid.getGrid()[x / grid.getCellWidth()][y / grid.getCellHeight()];
                if (curCell.getCellType() == CellType.START || curCell.getCellType() == CellType.GOAL) {
                    continue;
                } else if (this.cellType == curCell.getCellType() && !isPainting) {
                    curCell.setCellType(CellType.REGULAR);
                } else if (this.cellType != curCell.getCellType() && isPainting) {
                    curCell.setCellType(this.cellType);
                }


                this.grid.update();
            } while (!isThreadStopped);
        }

    }

    /**
     * Setting the isThreadStopped flag to true stops the thread
     *
     * @param isThreadStopped boolean flag
     */
    public void setThreadStopped(boolean isThreadStopped) {
        this.isThreadStopped = isThreadStopped;
    }

    /**
     * Sets CellType to use when painting with this thread
     *
     * @param mouseEvent with the mouse button indicating which CellType to use
     */
    private void setCellType(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            this.cellType = CellType.WALL;
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            this.cellType = CellType.SWAMP;
        }
    }

}
