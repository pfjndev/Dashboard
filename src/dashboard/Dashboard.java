package dashboard;

import javax.swing.*;
import java.awt.*;

import dashboard.panels.FacesPanel;
import dashboard.panels.ProductNamePanel;
import dashboard.panels.KPIAvgRespTimePanel;
import dashboard.panels.CSATOverMonthAreaChart;
import dashboard.panels.CircularCSATPanel;
import dashboard.panels.CustomerEffortGaugePanel;
import dashboard.panels.NPSGaugePanel;
import dashboard.panels.SatisfactionBreakdownChartPanel;

public class Dashboard extends JFrame {

    private static final String FONT_NAME = "Arial";
    private static final int FONT_WEIGHT = Font.BOLD;
    private static final int FONT_SIZE = 24;
    private static final Font FONT = new Font(FONT_NAME, FONT_WEIGHT, FONT_SIZE);
    
    private static final Color FOREGROUND_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(0x1F2A44);
    
    private static final String APP_TITLE = "Support Team Slide Dashboard";

    public Dashboard() {
        setTitle("PFJN - 1705125");               // Set the title of the dashboard
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when clicking the close button
        setExtendedState(JFrame.MAXIMIZED_BOTH);        // Fullscreen
        setLayout(new GridBagLayout());                 // Use GridBagLayout for the dashboard layout

        getMbMenu(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Application Title
        JLabel titleLabel = new JLabel(APP_TITLE, SwingConstants.CENTER);
        titleLabel.setFont(FONT);                   // Custom font
        titleLabel.setBackground(BACKGROUND_COLOR); // Dark background
        titleLabel.setOpaque(true);        // Ensure background color is visible
        titleLabel.setForeground(FOREGROUND_COLOR); // White text
        gbc.gridwidth = 4;                          // Span across all columns
        gbc.gridx = 0;                              // Start at first column
        gbc.gridy = 0;                              // First row
        gbc.weighty = 0.1;                          // Title takes 10% of the height
        add(titleLabel, gbc);                       // Add title to the dashboard panel

        gbc.gridwidth = 1;                          // Reset grid width for further components
        gbc.weighty = 1;                            // Reset weight for the rest of the components

        // First Row
        add((new ProductNamePanel()), gbc(0, 1));           // First element
        add((new KPIAvgRespTimePanel()), gbc(1, 1));   // Second element
        add((new CSATOverMonthAreaChart()), gbc(2, 1));     // Third element
        gbc.gridheight = 2;                                     // Last element spans 2 rows
        add((new FacesPanel()), gbc(3, 1));                 // Fourth element

        // Reset gridheight for the second row
        gbc.gridheight = 1;

        // Second Row
        add(new CircularCSATPanel(), gbc(0, 2));            // First element
        add(new CustomerEffortGaugePanel(), gbc(1, 2));     // Second element
        add(new NPSGaugePanel(), gbc(2, 2));                // Third element

        // Third Row
        add(new SatisfactionBreakdownChartPanel(), gbc(0, 3)); // First element
        add(new KPIAvgRespTimePanel(), gbc(1, 3));        // Second element
        add(new CSATOverMonthAreaChart(), gbc(2, 3));          // Third element
        
        pack();
        setVisible(true);
    }

    private GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
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
