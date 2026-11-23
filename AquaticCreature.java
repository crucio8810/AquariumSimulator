import java.awt.Color;

public abstract class AquaticCreature {
    private int row;
    private int col;
    private final String symbol;
    private final Color color;

    protected AquaticCreature(int row, int col, String symbol, Color color) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.color = color;
    }

    public abstract void move();

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getX() {
        return col;
    }

    public int getY() {
        return row;
    }

    public String getSymbol() {
        return symbol;
    }

    public Color getColor() {
        return color;
    }

    protected void moveBy(int rowChange, int colChange) {
        row += rowChange;
        col += colChange;
    }

    void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
