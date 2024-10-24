package dashboard.panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Arrays;

public class LineGraphPanel extends JPanel {
    private List<Integer> data;
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;

    private int padding = 50;
    private int labelPadding = 25;
    private int pointWidth = 4;
    private int numberYDivisions = 6;

    public LineGraphPanel() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.WHITE);
        data = Arrays.asList(10, 20, 30, 40, 30, 20, 10);
        title = "Sample Line Graph";
        xAxisLabel = "X Axis";
        yAxisLabel = "Y Axis";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Max score (compute once)
        double maxScore = getMaxScore();

        // Draw grid lines and hatch marks for Y axis
        for (int i = 0; i <= numberYDivisions; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = height - ((i * (height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;

            if (data.size() > 0) {
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawLine(padding + labelPadding + 1 + pointWidth, y0, width - padding, y1);
                g2d.setColor(Color.BLACK);
                String yLabel = String.format("%.2f", (maxScore * ((i * 1.0) / numberYDivisions)));
                FontMetrics metrics = g2d.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2d.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2d.drawLine(x0, y0, x1, y1);
        }

        // Draw grid lines and hatch marks for X axis
        for (int i = 0; i < data.size(); i++) {
            if (data.size() > 1) {
                int x0 = i * (width - padding * 2 - labelPadding) / (data.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = height - padding - labelPadding;
                int y1 = y0 - pointWidth;

                if ((i % ((int) ((data.size() / 20.0)) + 1)) == 0) {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawLine(x0, height - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2d.setColor(Color.BLACK);
                    String xLabel = String.valueOf(i);
                    FontMetrics metrics = g2d.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2d.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2d.drawLine(x0, y0, x1, y1);
            }
        }

        // Draw axes
        g2d.drawLine(padding + labelPadding, height - padding - labelPadding, padding + labelPadding, padding);
        g2d.drawLine(padding + labelPadding, height - padding - labelPadding, width - padding, height - padding - labelPadding);

        // Draw the line graph
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2f));
        for (int i = 0; i < data.size() - 1; i++) {
            int x1 = i * (width - padding * 2 - labelPadding) / (data.size() - 1) + padding + labelPadding;
            int y1 = height - padding - labelPadding - (int) ((data.get(i) * 1.0 / maxScore) * (height - padding * 2 - labelPadding));
            int x2 = (i + 1) * (width - padding * 2 - labelPadding) / (data.size() - 1) + padding + labelPadding;
            int y2 = height - padding - labelPadding - (int) ((data.get(i + 1) * 1.0 / maxScore) * (height - padding * 2 - labelPadding));
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Draw data points
        g2d.setColor(Color.RED);
        for (int i = 0; i < data.size(); i++) {
            int x = i * (width - padding * 2 - labelPadding) / (data.size() - 1) + padding + labelPadding;
            int y = height - padding - labelPadding - (int) ((data.get(i) * 1.0 / maxScore) * (height - padding * 2 - labelPadding));
            g2d.fillOval(x - pointWidth / 2, y - pointWidth / 2, pointWidth, pointWidth);
        }

        // Draw labels
        g2d.setColor(Color.BLACK);
        FontMetrics metrics = g2d.getFontMetrics();
        g2d.drawString(xAxisLabel, width / 2 - metrics.stringWidth(xAxisLabel) / 2, height - padding + metrics.getHeight() + 5);
        g2d.drawString(yAxisLabel, padding / 2, padding + metrics.getHeight() / 2);
        g2d.drawString(title, width / 2 - metrics.stringWidth(title) / 2, padding - metrics.getHeight() / 2);
    }

    private double getMaxScore() {
        return data.stream().max(Integer::compare).orElse(1);  // Avoid division by zero
    }
}
