package dashboard.panels;

import java.text.DecimalFormat;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.FontMetrics;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.Shape;

import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CSATCircularKPIPanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final int CORNER_RADIUS = 15;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    private static final Dimension MINIMUM_SIZE = new Dimension(250, 200);
    private static final Dimension PREFERRED_SIZE = new Dimension(300, 250);
    private static final int UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int CHART_PADDING = 40;
    private static final int LINE_LENGTH = 30;
    private static final float LINE_THICKNESS = 2.0f;

    private final List<Segment> segments;
    private final Timer updateTimer;

    public CSATCircularKPIPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);
        setMinimumSize(MINIMUM_SIZE);
        setPreferredSize(PREFERRED_SIZE);

        add(createTitleLabel(), BorderLayout.NORTH);

        segments = initializeSegments();

        updateTimer = new Timer(UPDATE_INTERVAL, e -> updateKPI());
        updateTimer.start();

        updateKPI(); // Initial display
    }

    private List<Segment> initializeSegments() {
        return Arrays.asList(
            new Segment("Satisfied", 0, new Color(76, 175, 80)),
            new Segment("Neutral", 0, new Color(255, 235, 59)),
            new Segment("Dissatisfied", 0, new Color(244, 67, 54))
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            renderPanel(g2d);
        } finally {
            g2d.dispose();
        }
    }

    private void renderPanel(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBackground(g2d);
        drawBorder(g2d);
        drawCircularChart(g2d);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
    }

    private void drawBorder(Graphics2D g2d) {
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("CSAT Distribution");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        return titleLabel;
    }

    private void updateKPI() {
        distributePercentages();
        repaint();
    }

    private void distributePercentages() {
        double total = 100.0;
        for (int i = 0; i < segments.size() - 1; i++) {
            double value = ThreadLocalRandom.current().nextDouble(0, total);
            segments.get(i).setPercentage(value);
            total -= value;
        }
        segments.get(segments.size() - 1).setPercentage(total);
    }

    private void drawCircularChart(Graphics2D g2d) {
        Dimension size = getSize();
        Point center = new Point(size.width / 2, (size.height / 2) + 10);
        int chartSize = Math.min(size.width, size.height) - CHART_PADDING * 2;

        double startAngle = 0;
        for (Segment segment : segments) {
            drawSegment(g2d, center, chartSize, startAngle, segment);
            startAngle += segment.getPercentage() * 3.6; // Convert percentage to degrees
        }
    }

    private void drawSegment(Graphics2D g2d, Point center, int size, double startAngle, Segment segment) {
        double arcAngle = segment.getPercentage() * 3.6;
        Shape arc = createArc(center, size, startAngle, arcAngle);
        
        g2d.setColor(segment.getColor());
        g2d.fill(arc);

        Point lineEnd = calculateLineEndPoint(center, size / 2 + LINE_LENGTH, startAngle + arcAngle / 2);
        drawLine(g2d, center, size / 2, lineEnd, segment.getColor());

        drawText(g2d, center.x, lineEnd.x, lineEnd.y, 
                 DECIMAL_FORMAT.format(segment.getPercentage()) + "%", 
                 segment.getLabel(), segment.getColor());
    }

    private Shape createArc(Point center, int size, double startAngle, double arcAngle) {
        return new Arc2D.Double(
            center.x - size / 2.0, center.y - size / 2.0, 
            size, size, startAngle, arcAngle, Arc2D.PIE
        );
    }

    private Point calculateLineEndPoint(Point center, int radius, double angleInDegrees) {
        double angleInRadians = Math.toRadians(angleInDegrees);
        int x = (int) (center.x + radius * Math.cos(angleInRadians));
        int y = (int) (center.y + radius * Math.sin(angleInRadians));
        return new Point(x, y);
    }

    private void drawLine(Graphics2D g2d, Point center, int innerRadius, Point end, Color color) {
        Point start = calculateLineEndPoint(center, innerRadius, Math.toDegrees(Math.atan2(end.y - center.y, end.x - center.x)));
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(LINE_THICKNESS));
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }

    private void drawText(Graphics2D g2d, int centerX, int lineEndX, int lineEndY, String percentageText, String label, Color color) {
        boolean isRightSide = lineEndX > centerX;
        g2d.setColor(color);
        
        drawString(g2d, VALUE_FONT, percentageText, lineEndX, lineEndY, isRightSide);
        drawString(g2d, LABEL_FONT, label, lineEndX, lineEndY + g2d.getFontMetrics(VALUE_FONT).getHeight(), isRightSide);
    }

    private void drawString(Graphics2D g2d, Font font, String text, int x, int y, boolean isRightSide) {
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = isRightSide ? x + 5 : x - fm.stringWidth(text) - 5;
        g2d.drawString(text, textX, y + (isRightSide ? -5 : 5));
    }

    private static class Segment {
        private final String label;
        private double percentage;
        private final Color color;

        public Segment(String label, double percentage, Color color) {
            this.label = label;
            this.percentage = percentage;
            this.color = color;
        }

        public String getLabel() { return label; }
        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }
        public Color getColor() { return color; }
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
                    dashboard.add(new CSATCircularKPIPanel(), gbc);
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