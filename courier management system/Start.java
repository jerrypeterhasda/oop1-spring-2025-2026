package couriermanagementsystem;
// Import the GUI class that builds the main application window
import couriermanagementsystem.gui.CourierGUI;

// Import SwingUtilities to safely launch the GUI on the Event Dispatch Thread (EDT)
import javax.swing.SwingUtilities;

/**
 * Start - Application entry point.
 *
 * This is the first class Java runs when you execute the program.
 * Its only job is to launch the Swing GUI in a thread-safe way.
 *
 * Why SwingUtilities.invokeLater?
 * All Swing component creation and updates must happen on the
 * Event Dispatch Thread (EDT). invokeLater schedules the task
 * to run on that thread, preventing race conditions and visual glitches.
 */
public class Start {

    /**
     * main - Java's standard program entry point.
     *
     * @param args Command-line arguments (not used by this application).
     */
    public static void main(String[] args) {
        // Schedule the CourierGUI constructor to run on the EDT.
        // CourierGUI::new is a method reference equivalent to: () -> new CourierGUI()
        SwingUtilities.invokeLater(CourierGUI::new);
    }
}
