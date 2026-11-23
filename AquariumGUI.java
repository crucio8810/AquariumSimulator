import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class AquariumGUI extends JPanel {
    private static final int CELL_WIDTH = 48;
    private static final int CELL_HEIGHT = 44;
    private static final Color WATER_TOP = new Color(11, 76, 125);
    private static final Color WATER_BOTTOM = new Color(9, 35, 83);

    private final AquariumModel model;
    private final Timer timer;
    private final BufferedImage[] sprites;
    private final JLabel statusLabel;

    public AquariumGUI(AquariumModel model, JLabel statusLabel) {
        this.model = model;
        this.statusLabel = statusLabel;
        this.sprites = loadSprites();
        setPreferredSize(new Dimension(model.getCols() * CELL_WIDTH + 1,
                model.getRows() * CELL_HEIGHT + 1));
        timer = new Timer(500, event -> {
            model.update();
            updateStatus();
            repaint();
        });
    }

    public void start() {
        timer.start();
    }

    public void pause() {
        timer.stop();
    }

    public void reset() {
        pause();
        model.initialize();
        repaint();
        updateStatus();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int y = 0; y < getHeight(); y++) {
            float ratio = getHeight() == 0 ? 0 : (float) y / getHeight();
            g.setColor(blend(WATER_TOP, WATER_BOTTOM, ratio));
            g.drawLine(0, y, getWidth(), y);
        }

        g.setColor(new Color(180, 225, 245, 45));
        for (int row = 0; row <= model.getRows(); row++) {
            int y = row * CELL_HEIGHT;
            g.drawLine(0, y, getWidth(), y);
        }
        for (int col = 0; col <= model.getCols(); col++) {
            int x = col * CELL_WIDTH;
            g.drawLine(x, 0, x, getHeight());
        }

        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                AquaticCreature creature = model.getCreatureAt(row, col);
                if (creature != null) {
                    drawCreature(g, creature, col * CELL_WIDTH, row * CELL_HEIGHT);
                }
            }
        }

        g.dispose();
    }

    private void drawCreature(Graphics2D g, AquaticCreature creature, int x, int y) {
        BufferedImage sprite = getSprite(creature);
        if (sprite != null) {
            g.drawImage(sprite, x + 3, y + 3, CELL_WIDTH - 6, CELL_HEIGHT - 6, null);
            return;
        }

        g.setColor(creature.getColor());
        g.fillOval(x + 8, y + 7, CELL_WIDTH - 16, CELL_HEIGHT - 14);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        g.drawString(creature.getSymbol(), x + CELL_WIDTH / 2 - 5, y + CELL_HEIGHT / 2 + 6);
    }

    private BufferedImage getSprite(AquaticCreature creature) {
        if (sprites == null) {
            return null;
        }
        if (creature instanceof Goldfish) {
            return sprites[0];
        }
        if (creature instanceof Clownfish) {
            return sprites[1];
        }
        if (creature instanceof Shark) {
            return sprites[2];
        }
        if (creature instanceof MoonJellyfish) {
            return sprites[3];
        }
        return null;
    }

    private BufferedImage[] loadSprites() {
        String spriteDirectory = "assets/Sprites/";
        String[] names = {
                "Goldfish Sprite.png",
                "Clownfish Sprite.png",
                "Shark Sprite.png",
                "Jellyfish.png"
        };
        BufferedImage[] loaded = new BufferedImage[names.length];
        boolean allLoaded = true;

        for (int i = 0; i < names.length; i++) {
            try {
                BufferedImage original = readSprite(spriteDirectory, names[i]);
                if (original == null) {
                    System.err.println("Could not read sprite: " + names[i]);
                    allLoaded = false;
                    continue;
                }
                loaded[i] = removeDarkBackground(original);
            } catch (IOException exception) {
                System.err.println("Could not load sprite " + names[i] + ": "
                        + exception.getMessage());
                allLoaded = false;
            }
        }

        if (!allLoaded) {
            System.err.println("One or more individual sprites are missing. "
                    + "Existing sprites will still be displayed.");
        }
        return loaded;
    }

    private BufferedImage readSprite(String spriteDirectory, String name) throws IOException {
        String resourcePath = "/" + spriteDirectory + name;
        try (InputStream resource = AquariumGUI.class.getResourceAsStream(resourcePath)) {
            if (resource != null) {
                return ImageIO.read(resource);
            }
        }

        File[] candidates = {
                new File(spriteDirectory, name),
                new File("Final project", spriteDirectory + name)
        };
        for (File candidate : candidates) {
            if (candidate.isFile()) {
                return ImageIO.read(candidate);
            }
        }

        System.err.println("Sprite not found. Checked: "
                + candidates[0].getAbsolutePath() + " and "
                + candidates[1].getAbsolutePath());
        return null;
    }

    private BufferedImage removeDarkBackground(BufferedImage original) {
        BufferedImage transparent = new BufferedImage(
                original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = transparent.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();

        for (int y = 0; y < transparent.getHeight(); y++) {
            for (int x = 0; x < transparent.getWidth(); x++) {
                int pixel = transparent.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                int alpha = (pixel >> 24) & 0xff;

                if (red <= 4 && green <= 4 && blue <= 4 && alpha <= 8) {
                    transparent.setRGB(x, y, 0);
                } else if (red <= 4 && green <= 4 && blue <= 4) {
                    transparent.setRGB(x, y, 0);
                }
            }
        }
        return transparent;
    }

    private void updateStatus() {
        if (statusLabel != null) {
            statusLabel.setText("Creatures: " + model.getCreatureCount());
        }
    }

    private Color blend(Color start, Color end, float ratio) {
        float boundedRatio = Math.max(0, Math.min(1, ratio));
        int red = (int) (start.getRed() + (end.getRed() - start.getRed()) * boundedRatio);
        int green = (int) (start.getGreen() + (end.getGreen() - start.getGreen()) * boundedRatio);
        int blue = (int) (start.getBlue() + (end.getBlue() - start.getBlue()) * boundedRatio);
        return new Color(red, green, blue);
    }

    public static void showWindow(AquariumModel model) {
        JLabel status = new JLabel("Creatures: " + model.getCreatureCount(), SwingConstants.CENTER);
        AquariumGUI aquarium = new AquariumGUI(model, status);
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton resetButton = new JButton("Reset");
        startButton.addActionListener(event -> aquarium.start());
        pauseButton.addActionListener(event -> aquarium.pause());
        resetButton.addActionListener(event -> aquarium.reset());

        JPanel controls = new JPanel();
        controls.add(startButton);
        controls.add(pauseButton);
        controls.add(resetButton);
        controls.add(status);

        JFrame frame = new JFrame("Aquarium Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(controls, BorderLayout.NORTH);
        frame.add(aquarium, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
