package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModernLogin extends JFrame {

    // --- COLOR PALETTE ---
    // Deep Navy (Professional/Dark Mode feel)
    private final Color COLOR_BG_LEFT = new Color(16, 24, 32); 
    // Electric Teal (AI/Automation accent)
    private final Color COLOR_ACCENT = new Color(0, 229, 255); 
    // Clean White for form
    private final Color COLOR_BG_RIGHT = Color.WHITE;
    // Light Gray for inputs
    private final Color COLOR_INPUT_BG = new Color(248, 250, 252);
    private final Color COLOR_TEXT_GRAY = new Color(100, 116, 139);

    private JPanel rightPanel;
    private CardLayout cardLayout;
    
    // For the background animation effect
    private List<Point> networkNodes = new ArrayList<>();

    public ModernLogin() {
        setUndecorated(true);
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

        // Layout: 40% Left (Branding), 60% Right (Form)
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- 1. LEFT PANEL (Branding) ---
        initNetworkNodes(); // Generate random dots for the background
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background Color
                g2d.setColor(COLOR_BG_LEFT);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw "Neural Network" Pattern
                g2d.setColor(new Color(255, 255, 255, 30)); // Faint white
                for (int i = 0; i < networkNodes.size(); i++) {
                    Point p1 = networkNodes.get(i);
                    g2d.fillOval(p1.x, p1.y, 4, 4); // Draw node
                    
                    // Connect close nodes
                    for (int j = i + 1; j < networkNodes.size(); j++) {
                        Point p2 = networkNodes.get(j);
                        if (p1.distance(p2) < 100) {
                            g2d.setStroke(new BasicStroke(1f));
                            g2d.drawLine(p1.x + 2, p1.y + 2, p2.x + 2, p2.y + 2);
                        }
                    }
                }
            }
        };
        leftPanel.setPreferredSize(new Dimension(350, 600));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Add Logo/Text to Left Panel
        leftPanel.add(Box.createVerticalGlue());
        
        JLabel iconLabel = new JLabel("⚡"); // Simple unicode icon, or use an ImageIcon
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setForeground(COLOR_ACCENT);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(iconLabel);

        JLabel brandTitle = new JLabel("JobHunter AI");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        brandTitle.setForeground(Color.WHITE);
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(brandTitle);
        
        leftPanel.add(Box.createVerticalStrut(10));

        JLabel brandSub = new JLabel("Automate your career growth.");
        brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        brandSub.setForeground(new Color(173, 181, 189));
        brandSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(brandSub);

        leftPanel.add(Box.createVerticalGlue());

        // --- 2. RIGHT PANEL (Forms) ---
        rightPanel = new JPanel();
        rightPanel.setBackground(COLOR_BG_RIGHT);
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);

        initLoginForm();
        initSignupForm();

        // --- ADD TO FRAME ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.4; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(leftPanel, gbc);

        gbc.gridx = 1; 
        gbc.weightx = 0.6;
        add(rightPanel, gbc);

        // Drag Logic
        addWindowDragLogic(leftPanel);
    }

    // --- GENERATE RANDOM NODES FOR BACKGROUND ---
    private void initNetworkNodes() {
        Random rand = new Random();
        for(int i=0; i<40; i++) {
            networkNodes.add(new Point(rand.nextInt(350), rand.nextInt(600)));
        }
    }

    private void initLoginForm() {
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_BG_RIGHT);

        // Close Button
        JLabel closeBtn = createCloseButton();
        closeBtn.setBounds(480, 15, 30, 30);
        panel.add(closeBtn);

        // Title
        JLabel title = new JLabel("Agent Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(COLOR_BG_LEFT);
        title.setBounds(60, 80, 200, 40);
        panel.add(title);

        JLabel subTitle = new JLabel("Wake up your automation bot");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(COLOR_TEXT_GRAY);
        subTitle.setBounds(60, 120, 250, 20);
        panel.add(subTitle);

        // Inputs
        JTextField email = createModernField("Enter Email");
        email.setBounds(60, 170, 380, 50);
        panel.add(email);

        JPasswordField pass = createModernPassField();
        pass.setBounds(60, 240, 380, 50);
        panel.add(pass);

        // Main Action Button
        JButton actionBtn = createTechButton("INITIALIZE AGENT");
        actionBtn.setBounds(60, 330, 380, 50);
        panel.add(actionBtn);

        // Switcher
        JLabel switchLink = createLink("New user? Configure a new Agent", "SIGNUP");
        switchLink.setBounds(60, 400, 380, 30);
        panel.add(switchLink);

        rightPanel.add(panel, "LOGIN");
    }

    private void initSignupForm() {
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_BG_RIGHT);

        JLabel closeBtn = createCloseButton();
        closeBtn.setBounds(480, 15, 30, 30);
        panel.add(closeBtn);

        JLabel title = new JLabel("New Configuration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(COLOR_BG_LEFT);
        title.setBounds(60, 60, 300, 40);
        panel.add(title);

        JTextField name = createModernField("Agent Name / User Name");
        name.setBounds(60, 130, 380, 50);
        panel.add(name);

        JTextField email = createModernField("Email Address");
        email.setBounds(60, 200, 380, 50);
        panel.add(email);

        JPasswordField pass = createModernPassField();
        pass.setBounds(60, 270, 380, 50);
        panel.add(pass);

        JButton actionBtn = createTechButton("DEPLOY AGENT");
        actionBtn.setBounds(60, 350, 380, 50);
        panel.add(actionBtn);

        JLabel switchLink = createLink("Agent exists? Return to Login", "LOGIN");
        switchLink.setBounds(60, 420, 380, 30);
        panel.add(switchLink);

        rightPanel.add(panel, "SIGNUP");
    }

    // --- CUSTOM COMPONENT FACTORIES ---

    private JTextField createModernField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw bottom border only for a sleek look
                g.setColor(new Color(220, 220, 220));
                g.fillRect(0, getHeight() - 2, getWidth(), 2);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Remove default border
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 0), 
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Focus logic
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(COLOR_BG_LEFT);
                    field.setBackground(new Color(240, 248, 255)); // Slight blue tint on focus
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setBackground(COLOR_INPUT_BG);
                }
            }
        });
        return field;
    }

    private JPasswordField createModernPassField() {
        JPasswordField field = new JPasswordField("Password");
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char)0); // Show text initially

        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 0), 
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals("Password")) {
                    field.setText("");
                    field.setForeground(COLOR_BG_LEFT);
                    field.setEchoChar('●');
                    field.setBackground(new Color(240, 248, 255));
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setText("Password");
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char)0);
                    field.setBackground(COLOR_INPUT_BG);
                }
            }
        });
        return field;
    }

    private JButton createTechButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Tech Gradient: Navy -> Electric Blue
                GradientPaint gp = new GradientPaint(0, 0, COLOR_BG_LEFT, getWidth(), 0, new Color(40, 60, 90));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        btn.setForeground(COLOR_ACCENT); // Text is Electric Blue
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { btn.setForeground(COLOR_ACCENT); }
        });
        return btn;
    }

    private JLabel createLink(String text, String targetCard) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(COLOR_TEXT_GRAY);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { cardLayout.show(rightPanel, targetCard); }
            public void mouseEntered(MouseEvent e) { label.setForeground(COLOR_BG_LEFT); }
            public void mouseExited(MouseEvent e) { label.setForeground(COLOR_TEXT_GRAY); }
        });
        return label;
    }

    private JLabel createCloseButton() {
        JLabel close = new JLabel("✕", SwingConstants.CENTER);
        close.setFont(new Font("Arial", Font.BOLD, 18));
        close.setForeground(Color.LIGHT_GRAY);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { System.exit(0); }
            public void mouseEntered(MouseEvent e) { close.setForeground(Color.BLACK); }
            public void mouseExited(MouseEvent e) { close.setForeground(Color.LIGHT_GRAY); }
        });
        return close;
    }

    // Window Dragging Logic
    private int mouseX, mouseY;
    private void addWindowDragLogic(Component comp) {
        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
        });
        comp.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernLogin().setVisible(true));
    }
}