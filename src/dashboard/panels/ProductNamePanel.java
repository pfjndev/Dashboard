package dashboard.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ProductNamePanel extends JPanel {
    public ProductNamePanel() {
        setBackground(new Color(0x2A3F5F));
        setLayout(new BorderLayout());
        JLabel label = new JLabel("<html>Product Name<br>Product 01</html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label, BorderLayout.CENTER);
    }
}