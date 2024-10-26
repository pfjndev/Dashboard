package dashboard.panels;


import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.UIManager;

import java.text.DecimalFormat;
import java.util.Random;


public class CSATKPIPanel extends JPanel {
    private static final int UPDATE_INTERVAL = 1500; // 5 seconds
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final int CORNER_RADIUS = 15;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final Dimension MINIMUM_SIZE = new Dimension(250, 100);
    private static final Dimension PREFERRED_SIZE = new Dimension(300, 120);
    private static final int SQUARE_SIZE = 60;
    private static final int FACE_SIZE = 50;

    private final CSATIndicator indicator;
    private final Timer updateTimer;
    private double currentValue;
    private final JLabel infoIcon;
    private final JPopupMenu tooltipPopup;

    public CSATKPIPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(BACKGROUND_COLOR);
        setMinimumSize(MINIMUM_SIZE);
        setPreferredSize(PREFERRED_SIZE);

        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.setOpaque(false);
        topPanel.add(createTitleLabel(), BorderLayout.CENTER);

        infoIcon = createInfoIcon();
        topPanel.add(infoIcon, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        indicator = new CSATIndicator();
        add(indicator, BorderLayout.CENTER);

        tooltipPopup = createTooltipPopup();

        updateTimer = new Timer(UPDATE_INTERVAL, e -> {
            updateKPI();
            updateTooltipContent();
        });
        updateTimer.start();

        updateKPI(); // Initial display
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("CSAT Score");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        return titleLabel;
    }

    private JLabel createInfoIcon() {
        JLabel icon = new JLabel(createInfoIconImage());
        icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showTooltip(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hideTooltip();
            }
        });
        return icon;
    }

    private ImageIcon createInfoIconImage() {
        int size = 20;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(100, 100, 100));
        g2.fillOval(0, 0, size, size);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("i", (size - fm.stringWidth("i")) / 2, size - fm.getDescent() - 2);

        g2.dispose();
        return new ImageIcon(image);
    }

    private JPopupMenu createTooltipPopup() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(new Color(50, 50, 50));
        popup.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        return popup;
    }

    private void updateTooltipContent() {
        JLabel content = new JLabel(createTooltipText());
        content.setForeground(TEXT_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tooltipPopup.removeAll();
        tooltipPopup.add(content);
    }

    private void showTooltip(MouseEvent e) {
        updateTooltipContent();
        tooltipPopup.show(infoIcon, -tooltipPopup.getPreferredSize().width + infoIcon.getWidth(), infoIcon.getHeight());
    }

    private void hideTooltip() {
        tooltipPopup.setVisible(false);
    }

    private void updateKPI() {
        currentValue = simulateCSATScore();
        indicator.updateValue(currentValue);
    }

    private double simulateCSATScore() {
        return 50 + new Random().nextDouble() * 50; // Random value between 50% and 100%
    }

    private String createTooltipText() {
        return String.format(
            "<html><body style='width: 200px;'>" +
            "<b>Current CSAT Score:</b> %.2f%%<br>" +
            "<b>Status:</b> %s<br>" +
            "<b>Last Updated:</b> %s" +
            "</body></html>",
            currentValue,
            getStatusDescription(),
            new java.util.Date().toString()
        );
    }

    private String getStatusDescription() {
        if (currentValue >= 80) return "Satisfied";
        if (currentValue >= 60) return "Neutral";
        return "Unsatisfied";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Draw background with rounded corners
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

        // Draw white border
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
    
        g2d.dispose();
    }

    private class CSATIndicator extends JPanel {
        private double value;

        public CSATIndicator() {
            setOpaque(false);
        }

        public void updateValue(double newValue) {
            this.value = newValue;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                configureGraphics(g2d);
                int totalWidth = getWidth();
                int totalHeight = getHeight();
                
                String valueStr = DECIMAL_FORMAT.format(value) + "%";
                FontMetrics fm = g2d.getFontMetrics(VALUE_FONT);
                int textWidth = fm.stringWidth(valueStr);
                int textHeight = fm.getHeight();
                int spacing = 20;
                
                int textX = (totalWidth - (textWidth + spacing + SQUARE_SIZE)) / 2;
                int textY = (totalHeight + textHeight) / 2 - fm.getDescent();
                int squareX = textX + textWidth + spacing;
                int squareY = (totalHeight - SQUARE_SIZE) / 2;

                drawValueText(g2d, valueStr, textX, textY);
                drawIndicatorSquare(g2d, squareX, squareY);
                drawFaceIndicator(g2d, squareX, squareY);
            } finally {
                g2d.dispose();
            }
        }

        private void configureGraphics(Graphics2D g2d) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        private void drawValueText(Graphics2D g2d, String valueStr, int x, int y) {
            g2d.setColor(TEXT_COLOR);
            g2d.setFont(VALUE_FONT);
            g2d.drawString(valueStr, x, y);
        }

        private void drawIndicatorSquare(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRoundRect(x, y, SQUARE_SIZE, SQUARE_SIZE, 10, 10);
        }

        private void drawFaceIndicator(Graphics2D g2d, int squareX, int squareY) {
            int centerX = squareX + SQUARE_SIZE / 2;
            int centerY = squareY + SQUARE_SIZE / 2;
            
            Color faceColor;
            if (value >= 80) faceColor = new Color(76, 175, 80);  // Green
            else if (value >= 60) faceColor = new Color(255, 235, 59);  // Yellow
            else faceColor = new Color(244, 67, 54);  // Red

            g2d.setColor(faceColor);
            g2d.fillOval(centerX - FACE_SIZE / 2, centerY - FACE_SIZE / 2, FACE_SIZE, FACE_SIZE);

            // Draw eyes
            g2d.setColor(Color.BLACK);
            int eyeSize = FACE_SIZE / 10;
            g2d.fillOval(centerX - FACE_SIZE / 4, centerY - FACE_SIZE / 5, eyeSize, eyeSize);
            g2d.fillOval(centerX + FACE_SIZE / 4 - eyeSize, centerY - FACE_SIZE / 5, eyeSize, eyeSize);

            // Draw mouth
            g2d.setStroke(new BasicStroke(2));
            if (value >= 80) {
                // Smiley face
                g2d.drawArc(centerX - FACE_SIZE / 3, centerY - FACE_SIZE / 6, FACE_SIZE * 2 / 3, FACE_SIZE / 3, 0, -180);
            } else if (value >= 60) {
                // Neutral face
                g2d.drawLine(centerX - FACE_SIZE / 3, centerY + FACE_SIZE / 5, centerX + FACE_SIZE / 3, centerY + FACE_SIZE / 5);
            } else {
                // Sad face
                g2d.drawArc(centerX - FACE_SIZE / 3, centerY + FACE_SIZE / 6, FACE_SIZE * 2 / 3, FACE_SIZE / 3, 0, 180);
            }
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JFrame frame = new JFrame("CSAT KPI Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel dashboard = new JPanel(new GridBagLayout());
            dashboard.setBackground(new Color(20, 20, 20));
            dashboard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.insets = new Insets(5, 5, 5, 5);

            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;
                    dashboard.add(new CSATKPIPanel(), gbc);
                }
            }

            frame.add(dashboard);
            frame.setSize(800, 600);
            frame.setMinimumSize(new Dimension(600, 400));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}