import java.awt.Color;

public class Fish extends AquaticCreature {
    public Fish(int row, int col) {
        super(row, col, "F", new Color(90, 190, 220));
    }

    protected Fish(int row, int col, String symbol, Color color) {
        super(row, col, symbol, color);
    }

    @Override
    public void move() {
        moveBy(0, 1);
    }
}
