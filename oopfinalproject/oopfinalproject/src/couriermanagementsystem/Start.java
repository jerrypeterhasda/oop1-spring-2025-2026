package couriermanagementsystem;
import couriermanagementsystem.gui.CourierGUI;
import javax.swing.SwingUtilities;
public class Start {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CourierGUI::new);
    }
}
