package dashboard.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import java.util.List;

public class LineGraphPanel extends JPanel {
    private static final int PADDING = 25;
    private static final String TITLE = "Line Graph";
    private static final String X_LABEL = "X-Axis";
    private static final String Y_LABEL = "Y-Axis";
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color GRID_COLOR = Color.LIGHT_GRAY;
    private static final Color LINE_COLOR = Color.BLUE;
    private List<Integer> data;

    public LineGraphPanel(List<Integer> data) {
        this.data = data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(LABEL_FONT);

        drawTitle(g2d);
        drawLabels(g2d);
        drawGrid(g2d);
        drawDataPoints(g2d);
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.drawString(TITLE, (getWidth() - g2d.getFontMetrics().stringWidth(TITLE)) / 2, PADDING / 2);
    }

    private void drawLabels(Graphics2D g2d) {
        g2d.drawString(X_LABEL, PADDING, getHeight() - PADDING / 2);
        g2d.drawString(Y_LABEL, PADDING / 2, PADDING);
    }

    private void drawGrid(Graphics2D g2d) {
        int gridWidth = getWidth() - 2 * PADDING;
        int gridHeight = getHeight() - 2 * PADDING;
        int gridX = PADDING;
        int gridY = PADDING;
        g2d.setColor(GRID_COLOR);
        g2d.drawRect(gridX, gridY, gridWidth, gridHeight);
    }

    private void drawDataPoints(Graphics2D g2d) {
        int gridWidth = getWidth() - 2 * PADDING;
        int gridHeight = getHeight() - 2 * PADDING;
        int gridX = PADDING;
        int gridY = PADDING;

        if (data.size() < 2) {
            return; // Not enough data points to draw a line
        }

        int dataWidth = gridWidth / (data.size() - 1);
        int maxDataValue = getMaxDataValue();
        int dataHeight = maxDataValue == 0 ? 0 : gridHeight / maxDataValue;

        g2d.setColor(LINE_COLOR);
        for (int i = 0; i < data.size() - 1; i++) {
            int x1 = gridX + (i * dataWidth);
            int y1 = gridY + gridHeight - (data.get(i) * dataHeight);
            int x2 = gridX + ((i + 1) * dataWidth);
            int y2 = gridY + gridHeight - (data.get(i + 1) * dataHeight);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    private int getMaxDataValue() {
        int max = Integer.MIN_VALUE;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}