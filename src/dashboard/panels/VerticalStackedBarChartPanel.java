package dashboard.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VerticalStackedBarChartPanel extends JPanel {
    private List<BarData> bars;
    private int maxValue;
    private static final int PADDING = 40;
    private static final int BAR_WIDTH = 60;
    private static final int LABEL_HEIGHT = 20;
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color HOVER_COLOR = new Color(60, 60, 60);
    private static final Color ACTIVE_COLOR = new Color(80, 80, 80);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    private static final Color[] COLORS = {
        new Color(33, 150, 243),  // Blue
        new Color(76, 175, 80),   // Green
        new Color(255, 193, 7),   // Yellow
        new Color(244, 67, 54)    // Red
    };
    private static final String[] COLOR_LABELS = {"Satisfied", "Neutral", "Dissatisfied", "Very Dissatisfied"};

    private String title;
    private String currentPeriod = "Quarterly";
    private JPanel chartPanel;
    private JPanel legendPanel;

    public VerticalStackedBarChartPanel(String title) {
        this.title = title;
        bars = new ArrayList<>();
        setPreferredSize(new Dimension(600, 400));
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(createTogglePanel(), BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        add(topPanel, BorderLayout.NORTH);

        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setOpaque(false);

        legendPanel = createLegendPanel();
        legendPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(chartPanel, BorderLayout.CENTER);
        centerPanel.add(legendPanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void addBar(String label, List<Integer> values) {
        bars.add(new BarData(label, values));
        updateMaxValue();
        repaint();
    }

    private void updateMaxValue() {
        maxValue = bars.stream()
                       .mapToInt(bar -> bar.values.stream().mapToInt(Integer::intValue).sum())
                       .max()
                       .orElse(0);
        maxValue = (int) Math.ceil(maxValue / 10.0) * 10;
    }

    private void drawChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        int chartHeight = height - PADDING * 2 - LABEL_HEIGHT;

        // Draw title
        g2d.setFont(TITLE_FONT);
        g2d.setColor(TEXT_COLOR);
        FontMetrics titleFm = g2d.getFontMetrics();
        int titleWidth = titleFm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, PADDING);

        // Draw Y-axis
        g2d.setColor(TEXT_COLOR);
        g2d.drawLine(PADDING, PADDING + LABEL_HEIGHT, PADDING, height - PADDING);

        // Draw X-axis
        g2d.drawLine(PADDING, height - PADDING, width - PADDING, height - PADDING);

        // Draw Y-axis labels and grid lines
        g2d.setFont(LABEL_FONT);
        FontMetrics fm = g2d.getFontMetrics();
        
        for (int i = 0; i <= 10; i++) {
            int value = i * maxValue / 10;
            int y = height - PADDING - (i * chartHeight / 10);
            String label = value + "%";
            g2d.drawString(label, PADDING - fm.stringWidth(label) - 5, y + fm.getAscent() / 2);
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawLine(PADDING, y, width - PADDING, y);
            g2d.setColor(TEXT_COLOR);
        }

        // Draw bars
        int barSpacing = (width - PADDING * 2) / (bars.size() + 1);
        int x = PADDING + barSpacing;

        for (BarData bar : bars) {
            int y = height - PADDING;
            int totalHeight = 0;

            for (int i = 0; i < bar.values.size(); i++) {
                int value = bar.values.get(i);
                int barHeight = (int) ((double) value / maxValue * chartHeight);
                totalHeight += barHeight;

                Rectangle2D.Double rect = new Rectangle2D.Double(x - BAR_WIDTH / 2, y - totalHeight, BAR_WIDTH, barHeight);
                g2d.setColor(COLORS[i % COLORS.length]);
                g2d.fill(rect);
                g2d.setColor(Color.BLACK);
                g2d.draw(rect);

                // Draw value on the bar segment
                String valueStr = DECIMAL_FORMAT.format((double) value / maxValue * 100) + "%";
                g2d.setFont(VALUE_FONT);
                g2d.setColor(TEXT_COLOR);
                int textWidth = fm.stringWidth(valueStr);
                int textHeight = fm.getHeight();
                g2d.drawString(valueStr, x - textWidth / 2, y - totalHeight + barHeight / 2 + textHeight / 4);
            }

            // Draw bar label
            g2d.setFont(LABEL_FONT);
            g2d.setColor(TEXT_COLOR);
            g2d.drawString(bar.label, x - g2d.getFontMetrics().stringWidth(bar.label) / 2, height - PADDING + fm.getAscent() + 5);
            x += barSpacing;
        }

        g2d.dispose();
    }

    private JPanel createLegendPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 2, 5);

        for (int i = 0; i < COLOR_LABELS.length; i++) {
            JPanel colorPanel = new JPanel();
            colorPanel.setBackground(COLORS[i]);
            colorPanel.setPreferredSize(new Dimension(15, 15));
            panel.add(colorPanel, gbc);

            gbc.gridx = 1;
            JLabel label = new JLabel(COLOR_LABELS[i]);
            label.setForeground(TEXT_COLOR);
            label.setFont(LABEL_FONT);
            panel.add(label, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
        }

        return panel;
    }

    private JPanel createTogglePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel.setOpaque(false);
        
        String[] periods = {"Daily", "Monthly", "Quarterly", "Yearly"};
        ButtonGroup group = new ButtonGroup();

        for (String period : periods) {
            JToggleButton button = new JToggleButton(period) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isPressed()) {
                        g2d.setColor(ACTIVE_COLOR);
                    } else if (getModel().isRollover()) {
                        g2d.setColor(HOVER_COLOR);
                    } else if (isSelected()) {
                        g2d.setColor(ACTIVE_COLOR);
                    } else {
                        g2d.setColor(BACKGROUND_COLOR);
                    }
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2d.setColor(TEXT_COLOR);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(getText());
                    int textHeight = fm.getHeight();
                    g2d.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - fm.getDescent());
                    g2d.dispose();
                }
            };
            button.setForeground(TEXT_COLOR);
            button.setBackground(BACKGROUND_COLOR);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setFont(LABEL_FONT);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> updateDataForPeriod(period));
            group.add(button);
            panel.add(button);

            if (period.equals(currentPeriod)) {
                button.setSelected(true);
            }
        }

        return panel;
    }
   
    private void updateDataForPeriod(String period) {
        currentPeriod = period;
        bars.clear();
        switch (period) {
            case "Daily":
                addBar("Day1", List.of(20, 30, 40, 10));
                addBar("Day2", List.of(30, 20, 35, 15));
                addBar("Day3", List.of(50, 25, 15, 10));
                break;
            case "Monthly":
                addBar("Jan", List.of(30, 40, 20, 10));
                addBar("Feb", List.of(40, 30, 20, 10));
                addBar("Mar", List.of(50, 25, 15, 10));
                break;
            case "Quarterly":
                addBar("Q1", List.of(30, 40, 20, 10));
                addBar("Q2", List.of(40, 30, 20, 10));
                addBar("Q3", List.of(50, 25, 15, 10));
                addBar("Q4", List.of(45, 30, 15, 10));
                break;
            case "Yearly":
                addBar("2023", List.of(70, 20, 5, 5));
                break;
        }
        repaint();
    }

    private static class BarData {
        String label;
        List<Integer> values;

        BarData(String label, List<Integer> values) {
            this.label = label;
            this.values = values;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Vertical Stacked Bar Chart");
            VerticalStackedBarChartPanel chart = new VerticalStackedBarChartPanel("Customer Satisfaction Breakdown");
            chart.updateDataForPeriod("Quarterly");

            frame.add(chart);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}