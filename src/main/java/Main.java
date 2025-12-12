import ui.TextUI;

/**
 * Entry point for the Labyrinth of Glory application.
 * <p>
 * This class is responsible only for instantiating the user interface (TextUI)
 * and starting the application lifecycle.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Main {

    /** Hidden constructor to prevent instantiation. */
    private Main() { }

    /**
     * Main method that starts the program.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        TextUI textUI = new TextUI();

        textUI.run();
    }
}