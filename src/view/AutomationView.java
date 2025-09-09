package view;

import javax.swing.*;
import java.awt.*;

public class AutomationView extends JPanel 
{
    public JButton automatedSearchButton = new JButton("Automated Search Jobs");
    public JButton manualSearchButton = new JButton("Manual Search Jobs");

    public AutomationView() 
    {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(727, 467));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Automated Search Button
        automatedSearchButton.setBackground(Color.decode("#4CAF50")); // green
        automatedSearchButton.setForeground(Color.WHITE);
        automatedSearchButton.setFocusPainted(false);
        gbc.gridy = 0;
        add(automatedSearchButton, gbc);

        // Manual Search Button
        manualSearchButton.setBackground(Color.decode("#2196F3")); // blue
        manualSearchButton.setForeground(Color.WHITE);
        manualSearchButton.setFocusPainted(false);
        gbc.gridy = 1;
        add(manualSearchButton, gbc);
    }
    
}
