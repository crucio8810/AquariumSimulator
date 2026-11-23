import java.awt.Color;

public class Clownfish extends Fish {
    public Clownfish(int row, int col) {
        super(row, col, "C", new Color(255, 120, 55));
    }

    @Override
    public void move() {
        moveBy(0, -1);
    }
}
