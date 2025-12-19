package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserProfileFrame extends JFrame {

    // --- THEME ---
    private final Color COL_BG_MAIN = new Color(16, 24, 32);     
    private final Color COL_BG_CARD = new Color(22, 32, 45);     
    private final Color COL_BG_SIDEBAR = new Color(13, 20, 28);  
    private final Color COL_ACCENT = new Color(0, 229, 255);     
    private final Color COL_TEXT_WHITE = new Color(245, 245, 245);
    private final Color COL_TEXT_GRAY = new Color(110, 130, 150);
    private final Color COL_FIELD_BG = new Color(30, 40, 55);
    private final Color COL_LOG_BG = new Color(10, 15, 20);

    private boolean isEditing = false;
    private List<JTextField> editableFields = new ArrayList<>();
    private List<Point> networkNodes = new ArrayList<>();
    private DefaultListModel<String> logModel;

    public UserProfileFrame() {
        setUndecorated(true);
        setSize(1100, 720); // Slightly wider for log
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

        initNetworkNodes();

        // Background Wrapper
        JPanel mainWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawNetworkBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        setContentPane(mainWrapper);

        // --- MAIN GLASS CARD ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COL_BG_CARD);
        
        JPanel floatContainer = new JPanel(new BorderLayout());
        floatContainer.setOpaque(false);
        floatContainer.setBorder(new EmptyBorder(30, 30, 30, 30));
        floatContainer.add(contentPanel);
        mainWrapper.add(floatContainer, BorderLayout.CENTER);

        // --- LAYOUT ---
        contentPanel.add(createSidebar(), BorderLayout.WEST);
        contentPanel.add(createMainContent(), BorderLayout.CENTER);

        addDrag(mainWrapper);
    }

    // --- LEFT SIDEBAR (Identity) ---
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COL_BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(new EmptyBorder(40, 20, 40, 20));

        // 1. Avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 100;
                int x = (getWidth() - size) / 2;
                g2d.setColor(COL_FIELD_BG);
                g2d.fillOval(x, 0, size, size);
                g2d.setColor(COL_ACCENT);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawOval(x, 0, size, size);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString("JD", x + (size - fm.stringWidth("JD"))/2, 65);
                
                // Status Dot
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillOval(x + size - 25, size - 25, 20, 20);
            }
        };
        avatarPanel.setPreferredSize(new Dimension(240, 110));
        avatarPanel.setMaximumSize(new Dimension(240, 110));
        avatarPanel.setOpaque(false);

        // 2. Identity
        JLabel nameLbl = new JLabel("John Doe");
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLbl.setForeground(COL_TEXT_WHITE);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLbl = new JLabel("Target: Sales Manager"); // Generic
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLbl.setForeground(COL_ACCENT);
        roleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Status Badge
        JPanel statusBadge = new JPanel();
        statusBadge.setBackground(new Color(0, 229, 255, 30));
        statusBadge.setBorder(new LineBorder(COL_ACCENT, 1, true));
        statusBadge.setMaximumSize(new Dimension(140, 30));
        JLabel statusText = new JLabel("Agent Working");
        statusText.setForeground(COL_ACCENT);
        statusBadge.add(statusText);
        statusBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(avatarPanel);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(nameLbl);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(roleLbl);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(statusBadge);
        sidebar.add(Box.createVerticalGlue());
        
        JButton resumeBtn = createButton("Check Resume", "📄");
        sidebar.add(resumeBtn);

        return sidebar;
    }

    // --- RIGHT PANEL (Dashboard) ---
    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COL_BG_CARD);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // A. Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Agent Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COL_TEXT_WHITE);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setOpaque(false);
        
        JToggleButton editToggle = new JToggleButton("Edit Details");
        styleToggle(editToggle);
        editToggle.addActionListener(e -> toggleEditMode(editToggle.isSelected()));
        
        JButton close = new JButton("✕");
        close.setBorderPainted(false); close.setContentAreaFilled(false);
        close.setForeground(COL_TEXT_GRAY); close.setFont(new Font("Arial", Font.BOLD, 18));
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());

        controls.add(editToggle);
        controls.add(Box.createHorizontalStrut(10));
        controls.add(close);
        
        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // B. Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(COL_BG_CARD);

        // 1. Agent Stats (Generic)
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 20, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(2000, 90));
        statsRow.add(createStatCard("Emails Sent", "42", "📤"));
        statsRow.add(createStatCard("Jobs Found", "15", "🔎"));
        statsRow.add(createStatCard("Responses", "3", "📩"));
        
        body.add(Box.createVerticalStrut(20));
        body.add(statsRow);
        body.add(Box.createVerticalStrut(30));

        // 2. Personal Info Grid (Generic)
        JPanel formGrid = new JPanel(new GridLayout(3, 2, 40, 20));
        formGrid.setOpaque(false);
        formGrid.setMaximumSize(new Dimension(2000, 220));

        formGrid.add(createLabeledField("Email Address", "john.doe@email.com"));
        formGrid.add(createLabeledField("Phone Number", "+1 (555) 123-4567"));
        formGrid.add(createLabeledField("Target Location", "New York / Remote"));
        formGrid.add(createLabeledField("Target Salary", "$60k - $80k"));
        // Generic fields for any profession
        formGrid.add(createLabeledField("Portfolio / Reference", "portfolio.com/john"));
        formGrid.add(createLabeledField("Availability", "Immediate Start"));

        body.add(formGrid);
        body.add(Box.createVerticalStrut(20));

        // 3. Agent Activity Log (The "What is it doing?" part)
        JLabel logHeader = new JLabel("Live Agent Activity Log");
        logHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logHeader.setForeground(COL_TEXT_GRAY);
        logHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(logHeader);
        body.add(Box.createVerticalStrut(5));

        body.add(createActivityLog());

        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    // --- COMPONENT: ACTIVITY LOG ---
    private JScrollPane createActivityLog() {
        logModel = new DefaultListModel<>();
        JList<String> logList = new JList<>(logModel);
        logList.setBackground(COL_LOG_BG);
        logList.setForeground(new Color(0, 255, 128)); // Terminal Green
        logList.setFont(new Font("Consolas", Font.PLAIN, 12));
        logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add fake history data
        addLogEntry("System initialized. Agent ID: #8821");
        addLogEntry("Scanning generic job boards for 'Sales Manager'...");
        addLogEntry("Found listing: 'Regional Sales Lead' at Company A.");
        addLogEntry("Analyzing requirements... Match Score: 85%.");
        addLogEntry("Email composed. Sending application to hr@companya.com...");
        addLogEntry("✔ Email Sent Successfully.");
        addLogEntry("Scanning continues...");

        JScrollPane scroll = new JScrollPane(logList);
        scroll.setBorder(new LineBorder(COL_FIELD_BG, 1));
        scroll.setPreferredSize(new Dimension(0, 150)); // Fixed height log
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        return scroll;
    }

    private void addLogEntry(String text) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        logModel.addElement("[" + timestamp + "] " + text);
    }

    // --- COMPONENT FACTORIES ---
    private JPanel createStatCard(String label, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COL_FIELD_BG);
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, COL_ACCENT));
        
        JLabel valLbl = new JLabel(value + " ", SwingConstants.CENTER);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valLbl.setForeground(COL_TEXT_WHITE);
        
        JLabel lblLbl = new JLabel(" " + icon + " " + label, SwingConstants.CENTER);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLbl.setForeground(COL_TEXT_GRAY);
        lblLbl.setBorder(new EmptyBorder(8, 0, 8, 0));

        card.add(valLbl, BorderLayout.CENTER);
        card.add(lblLbl, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createLabeledField(String label, String value) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(COL_ACCENT);

        JTextField t = new JTextField(value);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setForeground(COL_TEXT_WHITE);
        t.setBackground(COL_FIELD_BG);
        t.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COL_BG_MAIN, 1), new EmptyBorder(5, 10, 5, 10)
        ));
        t.setCaretColor(COL_ACCENT);
        t.setEditable(false);
        editableFields.add(t);

        p.add(l, BorderLayout.NORTH);
        p.add(t, BorderLayout.CENTER);
        return p;
    }

    private JButton createButton(String text, String icon) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBackground(COL_FIELD_BG);
        btn.setForeground(COL_TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(COL_TEXT_GRAY, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COL_ACCENT); btn.setForeground(COL_BG_MAIN); }
            public void mouseExited(MouseEvent e) { btn.setBackground(COL_FIELD_BG); btn.setForeground(COL_TEXT_WHITE); }
        });
        return btn;
    }
    
    private void styleToggle(JToggleButton btn) {
        btn.setBackground(COL_BG_CARD);
        btn.setForeground(COL_ACCENT);
        btn.setBorder(new LineBorder(COL_ACCENT, 1));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void toggleEditMode(boolean edit) {
        this.isEditing = edit;
        for (JTextField f : editableFields) {
            f.setEditable(edit);
            f.setBackground(edit ? new Color(40, 50, 65) : COL_FIELD_BG);
            f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(edit ? COL_ACCENT : COL_BG_MAIN, 1), new EmptyBorder(5, 10, 5, 10)
            ));
        }
    }

    // --- GRAPHICS & UTILS ---
    private void addDrag(JComponent c) {
        final Point pos = new Point();
        c.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { pos.setLocation(e.getX(), e.getY()); }});
        c.addMouseMotionListener(new MouseMotionAdapter() { public void mouseDragged(MouseEvent e) { setLocation(e.getXOnScreen()-pos.x, e.getYOnScreen()-pos.y); }});
    }

    private void initNetworkNodes() {
        Random rand = new Random();
        for(int i=0; i<80; i++) networkNodes.add(new Point(rand.nextInt(1100), rand.nextInt(750)));
    }

    private void drawNetworkBackground(Graphics2D g2d, int w, int h) {
        g2d.setColor(COL_BG_MAIN); g2d.fillRect(0, 0, w, h);
        g2d.setColor(new Color(0, 229, 255, 10)); 
        for (int i = 0; i < networkNodes.size(); i++) {
            Point p1 = networkNodes.get(i);
            g2d.fillOval(p1.x, p1.y, 3, 3);
            for (int j = i + 1; j < networkNodes.size(); j++) {
                if (p1.distance(networkNodes.get(j)) < 100) {
                    g2d.drawLine(p1.x, p1.y, networkNodes.get(j).x, networkNodes.get(j).y);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserProfileFrame().setVisible(true));
    }
}