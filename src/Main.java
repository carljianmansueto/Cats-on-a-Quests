import LoginAndRegistration.LoginFrame;
import DataAndModels.DataStore;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize DataStore (loads data from files)
        DataStore.initialize();

        // Launch the login screen
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}