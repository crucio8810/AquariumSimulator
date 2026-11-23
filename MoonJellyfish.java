import java.awt.Color;

public class MoonJellyfish extends Jellyfish {
    public MoonJellyfish(int row, int col) {
        super(row, col, "M", new Color(190, 145, 250));
    }

    @Override
    public void move() {
        moveBy(1, 0);
    }
}
