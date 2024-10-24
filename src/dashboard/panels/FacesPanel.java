package dashboard.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FacesPanel extends JPanel {
    public FacesPanel() {
        setBackground(new Color(0x2A3F5F));
        setLayout(new GridLayout(3, 1));

        add(createMetricPanel("67.76%", "Promoters", new Color(0x28A745))); // Green smiley
        add(createMetricPanel("20.43%", "Passives", new Color(0xFFC107)));  // Yellow face
        add(createMetricPanel("11.81%", "Detractors", new Color(0xDC3545))); // Red sad face
    }

    private JPanel createMetricPanel(String value, String label, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel percentageLabel = new JLabel(value, SwingConstants.CENTER);
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        percentageLabel.setForeground(color);

        JLabel labelText = new JLabel(label, SwingConstants.CENTER);
        labelText.setFont(new Font("Arial", Font.PLAIN, 16));
        labelText.setForeground(Color.WHITE);

        panel.setBackground(new Color(0x2A3F5F));
        panel.add(percentageLabel, BorderLayout.CENTER);
        panel.add(labelText, BorderLayout.SOUTH);

        return panel;
    }
}
