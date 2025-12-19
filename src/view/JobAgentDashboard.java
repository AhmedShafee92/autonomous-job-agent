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

public class JobAgentDashboard extends JFrame {

    // --- ALIGNED COLOR PALETTE (Dark Tech Theme) ---
    private final Color COLOR_BG_DARK = new Color(16, 24, 32);      // Deep Navy Background
    private final Color COLOR_CARD_DARK = new Color(26, 38, 52);    // Lighter Navy for Card
    private final Color COLOR_ACCENT = new Color(0, 229, 255);      // Electric Teal
    private final Color COLOR_SUCCESS = new Color(0, 200, 83);      // Vibrant Green
    private final Color COLOR_TEXT_WHITE = new Color(240, 248, 255);
    private final Color COLOR_TEXT_GRAY = new Color(144, 164, 174);
    private final Color COLOR_DIVIDER = new Color(45, 55, 72);

    // --- STATE ---
    private boolean hasCV = false;
    private boolean isAnalysed = false;

    // --- UI COMPONENTS ---
    private JPanel uploadZonePanel;
    private JLabel uploadIconLbl;
    private JLabel uploadTextLbl;
    private JProgressBar progressBar;
    
    private ActionButton btnAnalyse;
    private ActionButton btnSearch;
    private JButton btnProfile;

    // Background Animation
    private List<Point> networkNodes = new ArrayList<>();

    public JobAgentDashboard() {
        // Frame Setup
        setUndecorated(true);
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        initNetworkNodes();

        // Main Container
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawNetworkBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // Add Central Card
        mainPanel.add(createWorkflowCard());

        addWindowDragLogic(mainPanel);
    }

    private JPanel createWorkflowCard() {
        JPanel card = new JPanel(null); // Absolute layout for precision
        card.setPreferredSize(new Dimension(500, 620));
        card.setBackground(COLOR_CARD_DARK);
        // Subtle glowing border
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 229, 255, 60), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        // Header - Close Btn
        JLabel closeBtn = new JLabel("✕");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setForeground(COLOR_TEXT_GRAY);
        closeBtn.setBounds(460, 15, 30, 30);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
             public void mouseClicked(MouseEvent e) { System.exit(0); }
             public void mouseEntered(MouseEvent e) { closeBtn.setForeground(Color.WHITE); }
             public void mouseExited(MouseEvent e) { closeBtn.setForeground(COLOR_TEXT_GRAY); }
        });
        card.add(closeBtn);

        // Header - Title
        JLabel title = new JLabel("New Agent Workflow", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_WHITE);
        title.setBounds(30, 20, 300, 40);
        card.add(title);

        JLabel subtitle = new JLabel("Follow the steps to initialize job searching.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(COLOR_TEXT_GRAY);
        subtitle.setBounds(30, 55, 300, 20);
        card.add(subtitle);

        // Divider
        JPanel divider = new JPanel();
        divider.setBackground(COLOR_DIVIDER);
        divider.setBounds(30, 90, 440, 1);
        card.add(divider);

        // --- STEP 1: CLEAR UPLOAD ZONE ---
        // This is a custom panel that looks like a dashed dropzone
        uploadZonePanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (!hasCV) {
                    // Dashed Border look
                    float[] dash = {10.0f};
                    g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
                    g2d.setColor(new Color(0, 229, 255, 100)); // Faint Teal border
                    g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
                    this.setBackground(COLOR_CARD_DARK);
                } else {
                    // Solid Success look
                    g2d.setColor(new Color(0, 200, 83, 30)); // Faint Green fill
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2d.setColor(COLOR_SUCCESS); // Solid Green border
                    g2d.setStroke(new BasicStroke(2f));
                    g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                }
            }
        };
        uploadZonePanel.setBounds(30, 110, 440, 150);
        uploadZonePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Clicking the zone triggers the file chooser
        uploadZonePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { if(!hasCV) performAttachCV(); }
        });
        
        // Internal Zone Components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        uploadIconLbl = new JLabel("☁\uFE0F", SwingConstants.CENTER); // Cloud icon
        uploadIconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        uploadIconLbl.setForeground(COLOR_ACCENT);
        uploadZonePanel.add(uploadIconLbl, gbc);

        gbc.gridy = 1;
        uploadTextLbl = new JLabel("<html><center>Step 1: <b>Click here to attach Resume</b><br><span style='font-size:11px'>(PDF or DOCX required to proceed)</span></center></html>", SwingConstants.CENTER);
        uploadTextLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        uploadTextLbl.setForeground(COLOR_TEXT_WHITE);
        uploadZonePanel.add(uploadTextLbl, gbc);

        card.add(uploadZonePanel);

        // --- Progress Bar ---
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setBounds(30, 280, 440, 8);
        progressBar.setForeground(COLOR_ACCENT);
        progressBar.setBackground(COLOR_DIVIDER);
        progressBar.setBorderPainted(false);
        card.add(progressBar);


        // --- STEP 2 & 3 BUTTONS ---
        
        // 2. Analyse Data
        btnAnalyse = new ActionButton("Step 2: Analyse Data & Verify", "🔍", false);
        btnAnalyse.setBounds(30, 310, 440, 60);
        btnAnalyse.addActionListener(e -> performAnalysis());
        card.add(btnAnalyse);

        // 3. Search Jobs
        btnSearch = new ActionButton("Step 3: Initialize Job Agent", "🚀", false);
        btnSearch.setBounds(30, 390, 440, 60);
        btnSearch.addActionListener(e -> performSearch());
        card.add(btnSearch);


        // Footer Link
        btnProfile = new JButton("View Personal Data Profile");
        btnProfile.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnProfile.setForeground(COLOR_TEXT_GRAY);
        btnProfile.setContentAreaFilled(false);
        btnProfile.setBorderPainted(false);
        btnProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProfile.setBounds(100, 530, 300, 40);
        btnProfile.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnProfile.setForeground(COLOR_ACCENT); }
            public void mouseExited(MouseEvent e) { btnProfile.setForeground(COLOR_TEXT_GRAY); }
        });
        card.add(btnProfile);

        return card;
    }

    // --- ACTIONS ---

    private void performAttachCV() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Update State
            hasCV = true;
            
            // VISUAL UPDATE: Change the upload zone to success state
            uploadIconLbl.setText("✅"); // Checkmark
            uploadIconLbl.setForeground(COLOR_SUCCESS);
            uploadTextLbl.setText("<html><center><b>Resume Attached!</b><br><span style='font-size:12px'>" + selectedFile.getName() + "</span></center></html>");
            uploadTextLbl.setForeground(COLOR_SUCCESS);
            uploadZonePanel.repaint(); // Re-run paintComponent to change border
            
            // Update Workflow
            progressBar.setValue(33);
            btnAnalyse.setEnabledState(true);
            btnAnalyse.setText("Step 2: Ready to Analyse Data");
        }
    }

    private void performAnalysis() {
        if (!hasCV || isAnalysed) return;

        btnAnalyse.setText("Processing Resume Data...");
        // Simulate Processing
        Timer t = new Timer(1500, e -> {
            isAnalysed = true;
            btnAnalyse.setSuccessStyle("Analysis Complete ✓");
            progressBar.setValue(66);
            btnSearch.setEnabledState(true);
            btnSearch.setText("Step 3: Ready to Launch Agent");
        });
        t.setRepeats(false);
        t.start();
    }

    private void performSearch() {
        if (!isAnalysed) return;
        progressBar.setValue(100);
        btnSearch.setSuccessStyle("Agent Running in Background...");
        JOptionPane.showMessageDialog(this, "Automation Agent Deployed Successfully!");
    }


    // --- HELPER CLASSES (Buttons & Background) ---

    class ActionButton extends JButton {
        private boolean isEnabledCustom;
        private Color bgStateColor;
        private Color textStateColor;

        public ActionButton(String text, String icon, boolean startEnabled) {
            super(icon + "  " + text);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setFocusPainted(false); setBorderPainted(false); setContentAreaFilled(false);
            setEnabledState(startEnabled);
        }

        public void setEnabledState(boolean enable) {
            this.isEnabledCustom = enable;
            if (enable) {
                bgStateColor = COLOR_ACCENT; // Active Teal
                textStateColor = COLOR_BG_DARK; // Dark text on bright button
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                bgStateColor = new Color(45, 55, 72, 100); // Faint dark gray
                textStateColor = new Color(100, 110, 120); // Dim gray text
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            repaint();
        }

        public void setSuccessStyle(String newText) {
            setText(newText);
            isEnabledCustom = false; // Lock it
            bgStateColor = COLOR_SUCCESS; // Green
            textStateColor = Color.WHITE;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(bgStateColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            setForeground(textStateColor);
            super.paintComponent(g);
        }
    }

    private void initNetworkNodes() {
        Random rand = new Random();
        for(int i=0; i<70; i++) networkNodes.add(new Point(rand.nextInt(1050), rand.nextInt(700)));
    }

    private void drawNetworkBackground(Graphics2D g2d, int w, int h) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(COLOR_BG_DARK); g2d.fillRect(0, 0, w, h);
        g2d.setColor(new Color(0, 229, 255, 15)); // Faint teal nodes
        for (int i = 0; i < networkNodes.size(); i++) {
            Point p1 = networkNodes.get(i);
            g2d.fillOval(p1.x, p1.y, 3, 3);
            for (int j = i + 1; j < networkNodes.size(); j++) {
                Point p2 = networkNodes.get(j);
                if (p1.distance(p2) < 110) {
                    g2d.setStroke(new BasicStroke(1f));
                    g2d.drawLine(p1.x+1, p1.y+1, p2.x+1, p2.y+1);
                }
            }
        }
    }

    private void addWindowDragLogic(JPanel panel) {
        final Point pos = new Point();
        panel.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { pos.setLocation(e.getX(), e.getY()); }});
        panel.addMouseMotionListener(new MouseMotionAdapter() { public void mouseDragged(MouseEvent e) { setLocation(e.getXOnScreen()-pos.x, e.getYOnScreen()-pos.y); }});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JobAgentDashboard().setVisible(true));
    }
}