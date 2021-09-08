import java.awt.*;

public enum CellType {

    REGULAR(new Color(218, 200, 140)),
    START(new Color(40, 45, 148)),
    GOAL(new Color(232, 57, 41)),
    EXPLORED(new Color(60, 134, 83)),
    TO_EXPLORE(new Color(86, 229, 109)),
    PATH(new Color(250, 100, 36)),
    WALL(new Color(15, 15, 15)),
    SWAMP(new Color(51, 82, 33)),
    EXPLORED_SWAMP(new Color(81, 134, 55));

    public final Color color;

    CellType(Color color){
        this.color = color;
    }

    @Override
    public String toString() {
        return "CellType{" +
                "color=" + color +
                '}';
    }

}
