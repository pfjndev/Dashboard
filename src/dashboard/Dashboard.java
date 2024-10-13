package dashboard;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.WindowConstants;
import javax.swing.JMenuBar;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class Dashboard extends JFrame {

    /** createAndShowGui method to run the dashboard.
     * 
     * The createAndShowGui method will create a new JFrame object and set the title of the dashboard.
     * The default close operation is set to exit the application.
     * The size of the dashboard is set to full screen.
     * 
     * A menu bar is added to the dashboard.
     * 
     * Menus: Dashboard, File, Edit, View, Settings.
     * 
     * Menu items:
     * 
     * Dashboard - About, Help, Check For Updates, Exit.
     * 
     * File - New File, Open File, Save File, Save File As, Close.
     * 
     * Edit - Copy, Cut, Paste, Delete.
     * 
     * View - Zoom In, Zoom Out, Full Screen.
     * 
     * Settings - Tools, Customize, Preferences.

    * The main panel is added to the frame, then the pack method is called
    * to resize the frame to fit the panel and finally the frame is set to visible.

    * @param args The command line arguments.
    * @version 1.0
    * @author Pedro Nunes, 1705125
    * @return 
    **/
    private static void createAndShowGui() {
        // Create a new JFrame object.
        JFrame frame = new Dashboard();
        // Set the title of the dashboard.
        frame.setTitle("Dashboard - PFJN - 1705125");
        // Set the default close operation to exit the application.
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Set the dashboard to full screen.
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        // Set the size of the dashboard.
        Dimension fullScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(fullScreen);
        
        // Set the layout of the dashboard to GridBagLayout.
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;                              // Column
        gbc.gridy = 0;                              // Row
        gbc.weightx = 1.0;                          // Weight of the column
        gbc.weighty = 1.0;                          // Weight of the row
        gbc.fill = GridBagConstraints.BOTH;         // Fill the column and row
        gbc.anchor = GridBagConstraints.CENTER;     // Center the column and row

        // Add a menu bar to the dashboard.
        JMenuBar mb = new JMenuBar();
        mb.setPreferredSize(new Dimension(fullScreen.width, 30));
        frame.setJMenuBar(mb);

        // Add menus to the menu bar.
        // Add a main menu.
        JMenu mbMenu = new JMenu("Dashboard");
        mb.add(mbMenu);
        // Add menu items to the help menu.
        mbMenu.add("About");
        mbMenu.add("Help");
        mbMenu.add("Check For Updates");
        mbMenu.add("Exit");
        
        // Add a file menu.
        mbMenu = new JMenu("File");
        mb.add(mbMenu);
        // Add menu items to the file menu.
        mbMenu.add("New File");
        mbMenu.add("Open File");
        mbMenu.add("Save File");
        mbMenu.add("Save File As");
        mbMenu.add("Close");

        // Add a edit menu.
        mbMenu = new JMenu("Edit");
        mb.add(mbMenu);
        // Add menu items to the edit menu.
        mbMenu.add("Copy");
        mbMenu.add("Cut");
        mbMenu.add("Paste");
        mbMenu.add("Delete");

        // Add a view menu.
        mbMenu = new JMenu("View");
        mb.add(mbMenu);
        // Add menu items to the view menu.
        mbMenu.add("Zoom In");
        mbMenu.add("Zoom Out");
        mbMenu.add("Full Screen");

        // Add a tools menu.
        mbMenu = new JMenu("Settings");
        mb.add(mbMenu);
        // Add menu items to the settings menu.
        mbMenu.add("Tools");
        mbMenu.add("Customize");
        mbMenu.add("Preferences");

        /**
        * TODO - Add all menus and menu items kekyboard shortcuts.
        * TODO - Add all menus and menu items eventListeners.
        **/

        /** Grid layout
        * TODO - Add all panels to the grid layout.
        *
        *JPanel lineGraphPanel = new LineGraphPanel();
        *JPanel barChartPanel = new BarChartPanel();
        *JPanel pieChartPanel = new PieChartPanel();
        *JPanel tablePanel = new TablePanel();
        *JPanel mapPanel = new MapPanel();
        *
        * TODO - Add all panels to the frame.
        *frame.getContentPane().add(lineGraphPanel, gbc);
        *frame.getContentPane().add(barChartPanel, gbc);
        *frame.getContentPane().add(pieChartPanel, gbc);
        *frame.getContentPane().add(tablePanel, gbc);
        *frame.getContentPane().add(mapPanel, gbc);
        */
        
        // Display the frame.
        frame.pack();
        frame.setResizable(true);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
}