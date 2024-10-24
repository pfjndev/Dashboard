package dashboard.panels;

import javax.swing.*;
import java.awt.*;


public class SatisfactionBreakdownChartPanel extends JPanel {
    private final int[] satisfactionData = {60, 25, 15}; // Example data for Promoters, Passives, and Detractors
    private final String[] labels = {"Promoters", "Passives", "Detractors"};
    private final Color[] barColors = {new Color(0x28A745), new Color(0xFFC107), new Color(0xDC3545)};

    public SatisfactionBreakdownChartPanel() {
        setPreferredSize(new Dimension(400, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / satisfactionData.length;

        for (int i = 0; i < satisfactionData.length; i++) {
            int barHeight = (int) (height * (satisfactionData[i] / 100.0));
            int x = i * barWidth;
            int y = height - barHeight;

            g2d.setColor(barColors[i]);
            g2d.fillRect(x, y, barWidth - 10, barHeight);

            // Draw percentage label above each bar
            String label = satisfactionData[i] + "%";
            g2d.setColor(Color.BLACK);
            g2d.drawString(label, x + (barWidth - 10) / 2 - g2d.getFontMetrics().stringWidth(label) / 2, y - 5);
        }

        // Draw the labels below each bar
        for (int i = 0; i < satisfactionData.length; i++) {
            int x = i * barWidth;
            g2d.drawString(labels[i], x + (barWidth - 10) / 2 - g2d.getFontMetrics().stringWidth(labels[i]) / 2, height + 20);
        }
    }
}

