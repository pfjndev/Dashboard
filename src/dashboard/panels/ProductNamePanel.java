package dashboard.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;

public class ProductNamePanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Font PRODUCT_NAME_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font STAT_VALUE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final int STAT_SPACING = 5; // Reduced spacing between stats
    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 300;

    private String productName;
    private BufferedImage productImage;
    private String category;
    private double price;
    private int stockQuantity;

    public ProductNamePanel(String productName, String imageUrl, String category, double price, int stockQuantity) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;

        setPreferredSize(new Dimension(500, 300));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        setLayout(new BorderLayout());

        loadImage(imageUrl);
        setupComponents();
    }

    private void loadImage(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            productImage = ImageIO.read(uri.toURL());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            productImage = createPlaceholderImage(this.getWidth(), this.getHeight());
        }
    }

    private BufferedImage createPlaceholderImage(int width, int height) {
        BufferedImage placeholderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholderImage.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "No Image";
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, (width - textWidth) / 2, height / 2);
        g2d.dispose();
        return placeholderImage;
    }

    private void setupComponents() {
        // Image panel
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (productImage != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    // Crop the image to fit the panel
                    productImage = productImage.getSubimage(0, 0, Math.min(productImage.getWidth(), this.getWidth()), Math.min(productImage.getHeight(), this.getHeight()));
                    g2d.drawImage(productImage, 0, 0, this.getWidth(), this.getHeight(), null);
                    g2d.dispose();
                    
                    // Stretch to fill
                    //g2d.drawImage(productImage, 0, 0, this.getWidth(), this.getHeight(), null);
                    //g2d.dispose();
                }
            }
        };
        
        imagePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Centering the content
        JLabel nameLabel = new JLabel(productName);
        nameLabel.setFont(PRODUCT_NAME_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        
        infoPanel.add(nameLabel);

        // Add stats with spacing
        addStat(infoPanel, "Category:", category);
        addStat(infoPanel, "Price:", String.format("$%.2f", price));
        addStat(infoPanel, "In Stock:", String.format("$%d", stockQuantity));

        add(imagePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST); 
     } 

     private void addStat(JPanel panel, String label, String value) { 
         JLabel statLabel = new JLabel(label + "\s" + value); 
         statLabel.setFont(STAT_VALUE_FONT); 
         statLabel.setForeground(TEXT_COLOR); 
         panel.add(statLabel);
     } 

     public static void main(String[] args) { 
         SwingUtilities.invokeLater(() -> { 
             JFrame frame = new JFrame("Product Panel"); 
             ProductNamePanel panel = new ProductNamePanel( 
                 "Wireless Noise-Cancelling Headphones with Extra Long Battery Life", 
                 "https://as1.ftcdn.net/v2/jpg/02/19/10/16/1000_F_219101680_apYiquOiG4wqijDxktNq5R6CGTwqPWXp.jpg", 
                 "Electronics", 
                 99.99, 
                 50 
             ); 
             frame.add(panel); 
             frame.pack(); 
             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
             frame.setLocationRelativeTo(null); 
             frame.setVisible(true); 
         }); 
     } 
}