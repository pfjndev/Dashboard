package dashboard.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class CircularCSATPanel extends JPanel {
    private int csatPercentage = 85; // Example value

    public CircularCSATPanel() {
        setPreferredSize(new Dimension(200, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arcAngle = (int) (360 * (csatPercentage / 100.0));

        // Draw background circle
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(new Arc2D.Double(10, 10, width - 20, height - 20, 0, 360, Arc2D.OPEN));

        // Draw CSAT arc
        g2d.setColor(new Color(0x28A745)); // Green color
        g2d.fill(new Arc2D.Double(10, 10, width - 20, height - 20, 90, -arcAngle, Arc2D.OPEN));

        // Draw percentage label in the center
        String csatText = csatPercentage + "%";
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        int stringWidth = fm.stringWidth(csatText);
        g2d.setColor(Color.BLACK);
        g2d.drawString(csatText, (width - stringWidth) / 2, height / 2 + fm.getAscent() / 2);
    }
}
