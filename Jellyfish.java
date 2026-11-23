import java.awt.Color;

public class Jellyfish extends AquaticCreature {
    public Jellyfish(int row, int col) {
        super(row, col, "J", new Color(190, 130, 235));
    }

    protected Jellyfish(int row, int col, String symbol, Color color) {
        super(row, col, symbol, color);
    }

    @Override
    public void move() {
        moveBy(1, 0);
    }
}
