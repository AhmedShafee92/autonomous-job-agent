import controller.AuthController;
import view.AuthView;

import javax.swing.*;

public class MainApp 
{
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Client App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            AuthView authView = new AuthView();
            frame.setContentPane(authView);
            new AuthController(authView, frame);

            frame.setVisible(true);
        });
    }
}
