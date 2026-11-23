import java.awt.Color;

public class Goldfish extends Fish {
    public Goldfish(int row, int col) {
        super(row, col, "G", new Color(255, 170, 45));
    }

    @Override
    public void move() {
        moveBy(0, 1);
    }
}
