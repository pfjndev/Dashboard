package dashboard.panels;

import javax.swing.*;
import java.awt.*;

public class CSATOverMonthAreaChart extends JPanel {
    private final int[] csatScores = {80, 82, 78, 85, 90, 87, 85, 80, 75, 78, 76, 79}; // Example CSAT data

    public CSATOverMonthAreaChart() {
        setPreferredSize(new Dimension(400, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int gap = width / (csatScores.length - 1);
        int maxCsat = 100;

        int[] xPoints = new int[csatScores.length + 2];
        int[] yPoints = new int[csatScores.length + 2];

        // Create area points
        for (int i = 0; i < csatScores.length; i++) {
            xPoints[i] = i * gap;
            yPoints[i] = height - (csatScores[i] * height / maxCsat);
        }
        xPoints[csatScores.length] = width;
        yPoints[csatScores.length] = height;
        xPoints[csatScores.length + 1] = 0;
        yPoints[csatScores.length + 1] = height;

        // Draw filled area
        g2d.setColor(new Color(0x28A745, true));
        g2d.fillPolygon(xPoints, yPoints, csatScores.length + 2);

        // Draw line on top of the area
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < csatScores.length - 1; i++) {
            int x1 = i * gap;
            int y1 = height - (csatScores[i] * height / maxCsat);
            int x2 = (i + 1) * gap;
            int y2 = height - (csatScores[i + 1] * height / maxCsat);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}
