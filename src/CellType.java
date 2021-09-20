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

public enum CellType {

    START(new Color(40, 45, 148)),
    GOAL(new Color(232, 57, 41)),

    REGULAR(new Color(218, 200, 140)),

    EXPLORED(new Color(144, 189, 144)),
    TO_EXPLORE(new Color(86, 229, 109)),

    PATH(new Color(250, 100, 36)),
    WALL(new Color(63, 62, 62)),

    SWAMP(new Color(39, 61, 26)),
    SWAMP_TO_EXPLORE(new Color(66, 98, 54)),
    EXPLORED_SWAMP(new Color(89, 71, 71));

    public final Color color;

    CellType(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "CellType{" +
                "color=" + color +
                '}';
    }

}
