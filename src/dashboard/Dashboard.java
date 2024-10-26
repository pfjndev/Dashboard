package dashboard;

import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dashboard.panels.*;

public class Dashboard extends JFrame {

    private static final String FONT_NAME = "Arial";
    private static final int FONT_WEIGHT = Font.BOLD;
    private static final int FONT_SIZE = 24;
    private static final Font FONT = new Font(FONT_NAME, FONT_WEIGHT, FONT_SIZE);
    
    private static final Color FOREGROUND_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(0x1F2A44);
    
    private static final String APP_TITLE = "Dashboard";

    public Dashboard() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("PFJN - 1705125");               // Set the title of the dashboard
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when clicking the close button
        setExtendedState(JFrame.MAXIMIZED_BOTH);        // Fullscreen
        setLayout(new GridBagLayout());                 // Use GridBagLayout for the dashboard layout

        getMbMenu(this);

        // Application Title
/*         JLabel titleLabel = new JLabel(APP_TITLE, SwingConstants.CENTER);
        titleLabel.setFont(FONT);                   // Custom font
        titleLabel.setBackground(BACKGROUND_COLOR); // Dark background
        titleLabel.setOpaque(true);        // Ensure background color is visible
        titleLabel.setForeground(FOREGROUND_COLOR); // White text
        add(titleLabel);                            // Add title to the dashboard panel */

        // First Row
        add((new ProductNamePanel("Coca-Cola", "https://purepng.com/public/uploads/medium/purepng.com-coca-cola-bottlecoca-colacokecarbonated-soft-drinksoft-drinkcoke-bottle-1411527233225nl5vr.png", "Beverages", 0.99, 100)), gbc(0, 1));           // First element
        add((new KPIAvgRespTimePanel()), gbc(1, 1));   // Second element
        add((new CSATKPIPanel()), gbc(2, 1));     // Third element

        // Second Row
        add(new CSATCircularKPIPanel(), gbc(0, 2));            // First element
        add(new CESGaugePanel(), gbc(1, 2));     // Second element
        add(new CSATOverMonthAreaChartPanel(APP_TITLE), gbc(2, 2));                // Third element

        // Third Row
        add(new VerticalStackedBarChartPanel(APP_TITLE), gbc(0, 3)); // First element
        add(new KPIAvgRespTimePanel(), gbc(1, 3));        // Second element
        add(new NPSGaugePanel(), gbc(2, 3));          // Third element
        
        pack();
        setVisible(true);
    }

    private static GridBagConstraints gbc(int col, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        return gbc;
    }

    private static void getMbMenu(JFrame frame) {
        // Add a menu bar to the dashboard.
        JMenuBar mb = new JMenuBar();
        frame.setJMenuBar(mb);

        // Add menus and menu items
        JMenu dashboardMenu = new JMenu(APP_TITLE);
        mb.add(dashboardMenu);
        dashboardMenu.add("About");
        dashboardMenu.add("Help");
        dashboardMenu.add("Check For Updates");
        dashboardMenu.add("Exit");

        JMenu fileMenu = new JMenu("File");
        mb.add(fileMenu);
        fileMenu.add("New File");
        fileMenu.add("Open File");
        fileMenu.add("Save File");
        fileMenu.add("Save File As");
        fileMenu.add("Close");

        JMenu editMenu = new JMenu("Edit");
        mb.add(editMenu);
        editMenu.add("Copy");
        editMenu.add("Cut");
        editMenu.add("Paste");
        editMenu.add("Delete");

        JMenu viewMenu = new JMenu("View");
        mb.add(viewMenu);
        viewMenu.add("Zoom In");
        viewMenu.add("Zoom Out");
        viewMenu.add("Full Screen");

        JMenu settingsMenu = new JMenu("Settings");
        mb.add(settingsMenu);
        settingsMenu.add("Tools");
        settingsMenu.add("Customize");
        settingsMenu.add("Preferences");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}
