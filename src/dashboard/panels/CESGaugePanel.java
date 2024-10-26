package dashboard.panels;

import java.text.DecimalFormat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CESGaugePanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final Color GAUGE_COLOR = new Color(100, 100, 100);
    private static final Color NEEDLE_COLOR = new Color(255, 255, 255);
    private static final int CORNER_RADIUS = 15;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    private static final Dimension MINIMUM_SIZE = new Dimension(150, 150);
    private static final double MIN_VALUE = 0.0;
    private static final double MAX_VALUE = 100.0;
    private static final int GAUGE_PADDING = 15;
    private static final int VALUE_CIRCLE_RADIUS = 30;
    private static final float GAUGE_STROKE_WIDTH = 15.0f;
    private static final float NEEDLE_STROKE_WIDTH = 2.0f;

    private double currentValue;
    private final Arc2D.Double gaugeArc = new Arc2D.Double();
    private final Arc2D.Double gaugeFillArc = new Arc2D.Double();
    private final Ellipse2D.Double valueCircle = new Ellipse2D.Double();
    private final Line2D.Double needleLine = new Line2D.Double();
    private final Point2D.Double gaugeCenter = new Point2D.Double();
    private final BasicStroke gaugeStroke = new BasicStroke(GAUGE_STROKE_WIDTH, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND);
    private final BasicStroke needleStroke = new BasicStroke(NEEDLE_STROKE_WIDTH, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND);
    private String cachedValueText = "";
    private Rectangle2D cachedValueBounds;
    private String minLabel;
    private String maxLabel;
    private Rectangle2D minLabelBounds;
    private Rectangle2D maxLabelBounds;

    public CESGaugePanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setOpaque(false);
        setMinimumSize(MINIMUM_SIZE);

        add(createTitleLabel(), BorderLayout.NORTH);
        setCurrentValue(50); // Set an initial value
        initializeLabels();
    }

    private void initializeLabels() {
        minLabel = DECIMAL_FORMAT.format(MIN_VALUE) + "%";
        maxLabel = DECIMAL_FORMAT.format(MAX_VALUE) + "%";
        FontRenderContext frc = new FontRenderContext(null, true, true);
        minLabelBounds = new TextLayout(minLabel, LABEL_FONT, frc).getBounds();
        maxLabelBounds = new TextLayout(maxLabel, LABEL_FONT, frc).getBounds();
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Customer Effort Score");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        return titleLabel;
    }

    public void setCurrentValue(double value) {
        this.currentValue = Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
        this.cachedValueText = DECIMAL_FORMAT.format(currentValue) + "%";
        FontRenderContext frc = new FontRenderContext(null, true, true);
        this.cachedValueBounds = new TextLayout(cachedValueText, VALUE_FONT, frc).getBounds();
        repaint();
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
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        drawBackground(g2d);
        drawBorder(g2d);
        drawGauge(g2d);
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

    private void drawGauge(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 2 * GAUGE_PADDING;
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        updateGaugeGeometry(x, y, diameter);
        drawGaugeArc(g2d);
        drawNeedle(g2d);
        drawValueCircle(g2d);
        drawLabels(g2d, x, y + diameter / 2, diameter);
    }

    private void updateGaugeGeometry(int x, int y, int diameter) {
        gaugeArc.setArc(x, y, diameter, diameter, 0, 180, Arc2D.OPEN);
        gaugeFillArc.setArc(x, y, diameter, diameter, 0, 180 * (currentValue / MAX_VALUE), Arc2D.OPEN);

        gaugeCenter.setLocation(x + diameter / 2.0, y + diameter / 2.0);
        valueCircle.setFrame(gaugeCenter.x - VALUE_CIRCLE_RADIUS, gaugeCenter.y - VALUE_CIRCLE_RADIUS,
                VALUE_CIRCLE_RADIUS * 2, VALUE_CIRCLE_RADIUS * 2);

        // Calculate angle for needle from left to right
        double angle = Math.toRadians(180 * (currentValue / MAX_VALUE));
        double needleX = gaugeCenter.x + (diameter / 2.0 - GAUGE_STROKE_WIDTH / 2) * Math.cos(angle);
        double needleY = gaugeCenter.y + (diameter / 2.0 - GAUGE_STROKE_WIDTH / 2) * Math.sin(angle);
        needleLine.setLine(gaugeCenter.x, gaugeCenter.y, needleX, needleY);
    }

    private void drawGaugeArc(Graphics2D g2d) {
        g2d.setStroke(gaugeStroke);
        g2d.setColor(GAUGE_COLOR);
        g2d.draw(gaugeArc);
        g2d.setColor(getColorForPercentage(currentValue));
        g2d.draw(gaugeFillArc);
    }

    private Color getColorForPercentage(double percentage) {
        if (percentage < 33) {
            return new Color(255, 0, 0); // Red
        } else if (percentage < 66) {
            return new Color(255, 165, 0); // Orange
        } else {
            return new Color(0, 255, 0); // Green
        }
    }

    private void drawNeedle(Graphics2D g2d) {
        g2d.setColor(NEEDLE_COLOR);
        g2d.setStroke(needleStroke);
        g2d.draw(needleLine);
    }

    private void drawValueCircle(Graphics2D g2d) {
        g2d.setColor(GAUGE_COLOR);
        g2d.fill(valueCircle);

        g2d.setColor(TEXT_COLOR);
        g2d.setFont(VALUE_FONT);
        float textX = (float) (gaugeCenter.x - cachedValueBounds.getWidth() / 2);
        float textY = (float) (gaugeCenter.y + cachedValueBounds.getHeight() / 2);
        g2d.drawString(cachedValueText, textX, textY);
    }

    private void drawLabels(Graphics2D g2d, int x, int y, int width) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(LABEL_FONT);

        int labelY = y + (int) minLabelBounds.getHeight() + 5;
        g2d.drawString(minLabel, x, labelY);
        g2d.drawString(maxLabel, x + width - (float) maxLabelBounds.getWidth(), labelY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JFrame frame = new JFrame("CES Gauge Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            for (int i = 0; i < 4; i++) {
                CESGaugePanel panel = new CESGaugePanel();
                mainPanel.add(panel);

                new Timer(50, e -> {
                    double value = panel.currentValue + 0.5 * Math.random();
                    if (value > MAX_VALUE)
                        value = MIN_VALUE;
                    panel.setCurrentValue(value);
                }).start();
            }

            frame.add(mainPanel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}