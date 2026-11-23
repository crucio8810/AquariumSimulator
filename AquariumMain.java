import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;

public class AquariumMain {
    public static void main(String[] args) {
        AquariumModel model = new AquariumModel();
        System.out.println("Welcome to the Aquarium Simulator!");
        System.out.println(model);

        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("\nNo graphical environment is available; the initial aquarium is shown above.");
            return;
        }
        SwingUtilities.invokeLater(() -> AquariumGUI.showWindow(model));
    }
}
