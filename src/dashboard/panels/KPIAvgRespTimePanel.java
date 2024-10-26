package dashboard.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

public class KPIAvgRespTimePanel extends JPanel {
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    
    private static final int SQUARE_SIZE = 60;
    private static final int INDICATOR_SIZE = 20;
    private static final int CORNER_RADIUS = 15;
    private static final int INFO_ICON_SIZE = 20;
    private static final int UPDATE_INTERVAL = 1000; // 1 second
    
    private static final Dimension MINIMUM_SIZE = new Dimension(250, 100);
    private static final Dimension PREFERRED_SIZE = new Dimension(300, 120);

    private final Timer updateTimer;
    private final JLabel infoIcon;
    private final JPopupMenu tooltipPopup;
    private final ResponseTimeIndicator indicator;
    
    private double currentValue;

    public KPIAvgRespTimePanel() {
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

        indicator = new ResponseTimeIndicator();
        add(indicator, BorderLayout.CENTER);

        tooltipPopup = createTooltipPopup();

        updateTimer = new Timer(UPDATE_INTERVAL, e -> {
            updateKPI();
            updateTooltipContent();
        });
        updateTimer.start();

        updateKPI(); // Initial display
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
        int size = INFO_ICON_SIZE;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(100, 100, 100));
        g2.fillOval(0, 0, size, size);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, size * 3 / 4));
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
        updateTooltipContent(); // Ensure content is up-to-date
        tooltipPopup.show(infoIcon, -tooltipPopup.getPreferredSize().width + infoIcon.getWidth(), infoIcon.getHeight());
    }

    private void hideTooltip() {
        tooltipPopup.setVisible(false);
    }

    @Override
    public Dimension getMinimumSize() {
        return MINIMUM_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            configureGraphics(g2d);
            paintBackground(g2d);
            paintBorder(g2d);
        } finally {
            g2d.dispose();
        }
    }

    private void configureGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void paintBackground(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
    }

    private void paintBorder(Graphics2D g2d) {
        g2d.setColor(BORDER_COLOR);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Average Response Time");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        return titleLabel;
    }

    private void updateKPI() {
        currentValue = simulateResponseTime();
        indicator.updateValue(currentValue);
    }

    private double simulateResponseTime() {
        return 200 + new Random().nextDouble() * 1800; // Random value between 200 and 2000 ms
    }

    private String createTooltipText() {
        return
            String.format("\nResponse time: %.2f ms", currentValue) +
            String.format("\nStatus: %s", getStatusDescription()) +
            String.format("\nLast updated: %s", new java.util.Date().toString());
    }

    private String getStatusDescription() {
        if (currentValue < 500) return "Good";
        if (currentValue < 1000) return "Average";
        return "Poor";
    }

    private class ResponseTimeIndicator extends JPanel {
        private double value;
        private IndicatorState state;

        public ResponseTimeIndicator() {
            setOpaque(false);
        }

        public void updateValue(double newValue) {
            this.value = newValue;
            this.state = determineState(newValue);
            repaint();
        }

        private IndicatorState determineState(double value) {
            if (value < 500) return IndicatorState.GOOD;
            if (value < 1000) return IndicatorState.AVERAGE;
            return IndicatorState.BAD;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                configureGraphics(g2d);
                int totalWidth = getWidth();
                int totalHeight = getHeight();
                
                // Calculate positions
                String valueStr = DECIMAL_FORMAT.format(value) + " ms";
                FontMetrics fm = g2d.getFontMetrics(VALUE_FONT);
                int textWidth = fm.stringWidth(valueStr);
                int textHeight = fm.getHeight();
                int spacing = 20; // Space between text and square
                
                int textX = (totalWidth - (textWidth + spacing + SQUARE_SIZE)) / 2;
                int textY = (totalHeight + textHeight) / 2 - fm.getDescent();
                int squareX = textX + textWidth + spacing;
                int squareY = (totalHeight - SQUARE_SIZE) / 2;

                // Draw components
                drawValueText(g2d, valueStr, textX, textY);
                drawIndicatorSquare(g2d, squareX, squareY);
                drawIndicator(g2d, squareX, squareY);
            } finally {
                g2d.dispose();
            }
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

        private void drawIndicator(Graphics2D g2d, int squareX, int squareY) {
            int centerX = squareX + SQUARE_SIZE / 2;
            int centerY = squareY + SQUARE_SIZE / 2;
            
            switch (state) {
                case GOOD:
                    drawTriangle(g2d, new Color(76, 175, 80), true, centerX, centerY);
                    break;
                case AVERAGE:
                    drawDash(g2d, new Color(255, 193, 7), centerX, centerY);
                    break;
                case BAD:
                    drawTriangle(g2d, new Color(244, 67, 54), false, centerX, centerY);
                    break;
            }
        }

        private void drawTriangle(Graphics2D g2d, Color color, boolean pointUp, int centerX, int centerY) {
            g2d.setColor(color);
            int[] xPoints = {centerX - INDICATOR_SIZE / 2, centerX, centerX + INDICATOR_SIZE / 2};
            int[] yPoints = pointUp
                ? new int[]{centerY + INDICATOR_SIZE / 3, centerY - INDICATOR_SIZE / 3, centerY + INDICATOR_SIZE / 3}
                : new int[]{centerY - INDICATOR_SIZE / 3, centerY + INDICATOR_SIZE / 3, centerY - INDICATOR_SIZE / 3};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }

        private void drawDash(Graphics2D g2d, Color color, int centerX, int centerY) {
            g2d.setColor(color);
            g2d.fillRoundRect(centerX - INDICATOR_SIZE / 2, centerY - INDICATOR_SIZE / 6,
                              INDICATOR_SIZE, INDICATOR_SIZE / 3, 5, 5);
        }
    }

    enum IndicatorState {
        GOOD, AVERAGE, BAD
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            JFrame frame = new JFrame("KPI Dashboard");
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
                    dashboard.add(new KPIAvgRespTimePanel(), gbc);
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