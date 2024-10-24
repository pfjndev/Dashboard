package dashboard.panels;

import javax.swing.*;
import java.awt.*;

public class CustomerEffortGaugePanel extends JPanel {
    private int cesScore = 65; // Example value out of 100

    public CustomerEffortGaugePanel() {
        setPreferredSize(new Dimension(200, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 2 - 20;

        // Draw the circular gauge
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Draw the scale
        g2d.setColor(Color.BLACK);
        for (int i = 0; i <= 100; i += 20) {
            double angle = Math.toRadians(135 - (270 * i / 100.0));
            int x1 = centerX + (int) (Math.cos(angle) * (radius - 10));
            int y1 = centerY - (int) (Math.sin(angle) * (radius - 10));
            int x2 = centerX + (int) (Math.cos(angle) * radius);
            int y2 = centerY - (int) (Math.sin(angle) * radius);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Draw the needle
        double needleAngle = Math.toRadians(135 - (270 * cesScore / 100.0));
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        int needleX = centerX + (int) (Math.cos(needleAngle) * (radius - 20));
        int needleY = centerY - (int) (Math.sin(needleAngle) * (radius - 20));
        g2d.drawLine(centerX, centerY, needleX, needleY);

        // Draw the CES score in the center
        String cesText = cesScore + "%";
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int stringWidth = fm.stringWidth(cesText);
        g2d.setColor(Color.BLACK);
        g2d.drawString(cesText, centerX - stringWidth / 2, centerY + fm.getAscent() / 2);
    }
}
