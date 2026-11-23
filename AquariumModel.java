import java.util.Random;

public class AquariumModel {
    private static final int DEFAULT_ROWS = 10;
    private static final int DEFAULT_COLS = 18;

    private final int rows;
    private final int cols;
    private final int goldfishCount;
    private final int clownfishCount;
    private final int sharkCount;
    private final int moonJellyfishCount;
    private AquaticCreature[][] grid;

    public AquariumModel() {
        this(DEFAULT_ROWS, DEFAULT_COLS, 1, 1, 1, 1);
    }

    public AquariumModel(int rows, int cols) {
        this(rows, cols, 1, 1, 1, 1);
    }

    public AquariumModel(int rows, int cols, int goldfishCount, int clownfishCount,
            int sharkCount, int moonJellyfishCount) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Aquarium dimensions must be positive.");
        }
        if (goldfishCount < 0 || clownfishCount < 0 || sharkCount < 0
                || moonJellyfishCount < 0) {
            throw new IllegalArgumentException("Creature counts cannot be negative.");
        }
        long requestedCreatures = (long) goldfishCount + clownfishCount
                + sharkCount + moonJellyfishCount;
        long cellCapacity = (long) rows * cols;
        if (cellCapacity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Aquarium has too many cells.");
        }
        if (requestedCreatures > cellCapacity) {
            throw new IllegalArgumentException("There cannot be more creatures than aquarium cells.");
        }

        this.rows = rows;
        this.cols = cols;
        this.goldfishCount = goldfishCount;
        this.clownfishCount = clownfishCount;
        this.sharkCount = sharkCount;
        this.moonJellyfishCount = moonJellyfishCount;
        initialize();
    }

    public final void initialize() {
        grid = new AquaticCreature[rows][cols];
        Random random = new Random();

        for (int i = 0; i < goldfishCount; i++) {
            int[] cell = findEmptyCell(random);
            grid[cell[0]][cell[1]] = new Goldfish(cell[0], cell[1]);
        }
        for (int i = 0; i < clownfishCount; i++) {
            int[] cell = findEmptyCell(random);
            grid[cell[0]][cell[1]] = new Clownfish(cell[0], cell[1]);
        }
        for (int i = 0; i < sharkCount; i++) {
            int[] cell = findEmptyCell(random);
            grid[cell[0]][cell[1]] = new Shark(cell[0], cell[1]);
        }
        for (int i = 0; i < moonJellyfishCount; i++) {
            int[] cell = findEmptyCell(random);
            grid[cell[0]][cell[1]] = new MoonJellyfish(cell[0], cell[1]);
        }
    }

    public void update() {
        AquaticCreature[][] nextGrid = new AquaticCreature[rows][cols];
        boolean[][] reserved = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                reserved[row][col] = grid[row][col] != null;
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                AquaticCreature creature = grid[row][col];
                if (creature == null) {
                    continue;
                }

                int oldRow = creature.getRow();
                int oldCol = creature.getCol();
                reserved[oldRow][oldCol] = false;
                creature.move();

                int newRow = Math.floorMod(creature.getRow(), rows);
                int newCol = Math.floorMod(creature.getCol(), cols);
                creature.setPosition(newRow, newCol);

                if (nextGrid[newRow][newCol] != null || reserved[newRow][newCol]) {
                    creature.setPosition(oldRow, oldCol);
                    nextGrid[oldRow][oldCol] = creature;
                } else {
                    nextGrid[newRow][newCol] = creature;
                }
            }
        }

        grid = nextGrid;
    }

    public AquaticCreature getCreatureAt(int row, int col) {
        checkCoordinates(row, col);
        return grid[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getCreatureCount() {
        int count = 0;
        for (AquaticCreature[] row : grid) {
            for (AquaticCreature creature : row) {
                if (creature != null) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Aquarium (").append(rows).append(" x ").append(cols).append(")\n");
        output.append("   ");
        for (int col = 0; col < cols; col++) {
            output.append(String.format("%2d ", col));
        }
        output.append('\n');

        for (int row = 0; row < rows; row++) {
            output.append(String.format("%2d ", row));
            for (int col = 0; col < cols; col++) {
                AquaticCreature creature = grid[row][col];
                output.append(' ').append(creature == null ? "." : creature.getSymbol()).append(' ');
            }
            output.append('\n');
        }
        output.append("Creatures: ").append(getCreatureCount())
                .append(" (G=goldfish, C=clownfish, S=shark, M=moon jellyfish)");
        return output.toString();
    }

    private int[] findEmptyCell(Random random) {
        int row = random.nextInt(rows);
        int col = random.nextInt(cols);
        while (grid[row][col] != null) {
            row = random.nextInt(rows);
            col = random.nextInt(cols);
        }
        return new int[] {row, col};
    }

    private void checkCoordinates(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException(
                    "Grid position (" + row + ", " + col + ") is outside the aquarium.");
        }
    }
}
