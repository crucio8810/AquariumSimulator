import java.awt.Color;

public class Shark extends Fish {
    public Shark(int row, int col) {
        super(row, col, "S", new Color(145, 165, 190));
    }

    @Override
    public void move() {
        moveBy(0, 1);
    }
}
