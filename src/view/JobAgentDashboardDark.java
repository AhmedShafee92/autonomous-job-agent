package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JobAgentDashboardDark extends JFrame {

    // --- COLORS & STYLING ---
    private final Color COLOR_BG_DARK = new Color(16, 24, 32);      // Navy Background
    private final Color COLOR_ACCENT = new Color(0, 229, 255);      // Electric Teal
    private final Color COLOR_CARD_BG = Color.WHITE;                // Central Card
    private final Color COLOR_TEXT_PRIMARY = new Color(33, 37, 41);
    private final Color COLOR_TEXT_SECONDARY = new Color(108, 117, 125);
    private final Color COLOR_DISABLED = new Color(200, 200, 200);
    private final Color COLOR_SUCCESS = new Color(40, 167, 69);     // Green for success

    // --- COMPONENT STATE ---
    private boolean hasCV = false;
    private boolean isAnalysed = false;

    // --- UI COMPONENTS ---
    private JLabel statusIconLabel;
    private JLabel statusTextLabel;
    private JProgressBar progressBar;
    
    private ActionButton btnAttach;
    private ActionButton btnAnalyse;
    private ActionButton btnSearch;
    private JButton btnProfile;

    // Background Animation
    private List<Point> networkNodes = new ArrayList<>();

    public JobAgentDashboardDark() {
        // 1. Frame Setup
        setUndecorated(true);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

        // 2. Initialize Background Animation
        initNetworkNodes();

        // 3. Main Layout
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawNetworkBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout()); // Centers the card
        setContentPane(mainPanel);

        // 4. Create the Central "Workflow Card"
        JPanel cardPanel = createWorkflowCard();
        mainPanel.add(cardPanel);

        // 5. Add Drag Logic
        addWindowDragLogic(mainPanel);
    }

    private JPanel createWorkflowCard() {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450, 580));
        card.setBackground(COLOR_CARD_BG);
        card.setLayout(null); // Absolute positioning inside the card for precision
        
        // Rounded border for the card itself (visual trick using BorderFactory)
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // --- Close Button ---
        JLabel closeBtn = new JLabel("✕");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setForeground(Color.LIGHT_GRAY);
        closeBtn.setBounds(410, 15, 30, 30);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { System.exit(0); }
        });
        card.add(closeBtn);

        // --- Header Section ---
        JLabel title = new JLabel("Agent Workflow", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setBounds(0, 30, 450, 30);
        card.add(title);

        // --- Status Icon (Big Circle) ---
        statusIconLabel = new JLabel("📎", SwingConstants.CENTER);
        statusIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        statusIconLabel.setForeground(COLOR_ACCENT);
        statusIconLabel.setBounds(175, 70, 100, 100);
        
        // Draw a circle behind the icon
        JPanel circleBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(240, 248, 255)); // Light blue circle
                g2d.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        circleBg.setBounds(175, 70, 100, 100);
        circleBg.setOpaque(false);
        card.add(statusIconLabel);
        card.add(circleBg); // Add circle behind text

        // --- Status Text ---
        statusTextLabel = new JLabel("Step 1: Attach your CV", SwingConstants.CENTER);
        statusTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusTextLabel.setForeground(COLOR_TEXT_SECONDARY);
        statusTextLabel.setBounds(0, 180, 450, 25);
        card.add(statusTextLabel);

        // --- Progress Bar ---
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setBounds(50, 215, 350, 6);
        progressBar.setForeground(COLOR_ACCENT);
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorderPainted(false);
        card.add(progressBar);

        // --- BUTTONS ---
        
        // 1. Attach CV
        btnAttach = new ActionButton("Attach Resume (PDF/DOCX)", "📂", true);
        btnAttach.setBounds(50, 250, 350, 55);
        btnAttach.addActionListener(e -> performAttachCV());
        card.add(btnAttach);

        // 2. Analyse Data
        btnAnalyse = new ActionButton("Analyse Data & Agree", "🔍", false);
        btnAnalyse.setBounds(50, 315, 350, 55);
        btnAnalyse.addActionListener(e -> performAnalysis());
        card.add(btnAnalyse);

        // 3. Search Jobs
        btnSearch = new ActionButton("Run Job Search Agent", "🚀", false);
        btnSearch.setBounds(50, 380, 350, 55);
        btnSearch.addActionListener(e -> performSearch());
        card.add(btnSearch);

        // 4. View Profile (Secondary)
        btnProfile = new JButton("View Personal Data Profile");
        btnProfile.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnProfile.setForeground(COLOR_TEXT_SECONDARY);
        btnProfile.setContentAreaFilled(false);
        btnProfile.setBorderPainted(false);
        btnProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProfile.setBounds(50, 460, 350, 40);
        
        // Hover underline effect
        btnProfile.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnProfile.setForeground(COLOR_ACCENT); }
            public void mouseExited(MouseEvent e) { btnProfile.setForeground(COLOR_TEXT_SECONDARY); }
        });
        card.add(btnProfile);

        return card;
    }

    // --- ACTIONS ---

    private void performAttachCV() {
        // Simulate File Chooser
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Update State
            hasCV = true;
            btnAttach.setText("Attached: " + selectedFile.getName());
            btnAttach.setSuccessStyle();
            
            // Update UI for Next Step
            progressBar.setValue(33);
            statusIconLabel.setText("🧠");
            statusTextLabel.setText("Step 2: Let AI Analyse your data");
            
            // Enable Next Button
            btnAnalyse.setEnabledState(true);
        }
    }

    private void performAnalysis() {
        if (!hasCV) return;

        // Simulate Processing delay
        btnAnalyse.setText("Analysing...");
        Timer t = new Timer(1000, e -> {
            // Update State
            isAnalysed = true;
            btnAnalyse.setText("Analysis Complete");
            btnAnalyse.setSuccessStyle();

            // Update UI for Next Step
            progressBar.setValue(66);
            statusIconLabel.setText("⚡");
            statusTextLabel.setText("Step 3: Ready to Launch Agent");
            statusIconLabel.setForeground(COLOR_SUCCESS);

            // Enable Final Button
            btnSearch.setEnabledState(true);
            // Highlight it visually
            btnSearch.setBackground(COLOR_ACCENT); 
            btnSearch.setForeground(Color.WHITE);
        });
        t.setRepeats(false);
        t.start();
    }

    private void performSearch() {
        if (!isAnalysed) return;

        progressBar.setValue(100);
        statusTextLabel.setText("Agent is Hunting for Jobs...");
        btnSearch.setText("Agent Running...");
        // Here you would switch frames or start the backend process
        JOptionPane.showMessageDialog(this, "Automation Agent Started Successfully!");
    }

    // --- CUSTOM COMPONENTS ---

    /**
     * A Custom Button that handles Disabled/Enabled/Success states visually
     */
    class ActionButton extends JButton {
        private boolean isEnabledCustom;
        private Color currentColor;

        public ActionButton(String text, String icon, boolean startEnabled) {
            super(icon + "  " + text);
            this.isEnabledCustom = startEnabled;
            
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            
            if (startEnabled) {
                currentColor = COLOR_BG_DARK; // Default active color
                setForeground(Color.WHITE);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                currentColor = new Color(240, 240, 240); // Disabled Gray
                setForeground(Color.GRAY);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        public void setEnabledState(boolean enable) {
            this.isEnabledCustom = enable;
            if (enable) {
                currentColor = COLOR_BG_DARK;
                setForeground(Color.WHITE);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                currentColor = new Color(240, 240, 240);
                setForeground(Color.GRAY);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            repaint();
        }

        public void setSuccessStyle() {
            currentColor = COLOR_SUCCESS; // Green
            setForeground(Color.WHITE);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(currentColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            super.paintComponent(g);
        }
    }

    // --- BACKGROUND GRAPHICS ---

    private void initNetworkNodes() {
        Random rand = new Random();
        for(int i=0; i<60; i++) {
            networkNodes.add(new Point(rand.nextInt(1000), rand.nextInt(650)));
        }
    }

    private void drawNetworkBackground(Graphics2D g2d, int w, int h) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dark Background
        g2d.setColor(COLOR_BG_DARK);
        g2d.fillRect(0, 0, w, h);

        // Draw Nodes
        g2d.setColor(new Color(255, 255, 255, 20)); // Faint white
        for (int i = 0; i < networkNodes.size(); i++) {
            Point p1 = networkNodes.get(i);
            g2d.fillOval(p1.x, p1.y, 4, 4);
            
            // Connect close nodes
            for (int j = i + 1; j < networkNodes.size(); j++) {
                Point p2 = networkNodes.get(j);
                double dist = p1.distance(p2);
                if (dist < 120) {
                    // Opacity based on distance (closer = brighter)
                    int alpha = (int) (40 * (1 - dist / 120));
                    g2d.setColor(new Color(255, 255, 255, alpha));
                    g2d.setStroke(new BasicStroke(1f));
                    g2d.drawLine(p1.x + 2, p1.y + 2, p2.x + 2, p2.y + 2);
                }
            }
        }
    }

    // --- DRAG LOGIC ---
    private int mouseX, mouseY;
    private void addWindowDragLogic(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> new JobAgentDashboardDark().setVisible(true));
    }
}