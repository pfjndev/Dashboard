package dashboard;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Dashboard extends JFrame {

    private static final String M_TITLE = "Dashboard - PFJN - 1705125";

    public Dashboard() {
        setTitle(String.format("%s", M_TITLE));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /* Set the layout of the frame to BorderLayout. This layout manager is used to
        * arrange the components in the five regions of the frame: NORTH, SOUTH, EAST,
        * WEST, and CENTER.*/
        setLayout(new BorderLayout());

        // Logger object for the dashboard.
        MyLogger dashboardLogger = new MyLogger("Dashboard.DashboardLogger", null);

        /**
         * 
         * @param mainPanel The main panel of the dashboard.
         * 
         * @param myLogger The logger object for the dashboard.
         * 
         * MainPanel is the main panel of the dashboard. It is the panel that will be
         * displayed to the user. It will contain all the other panels and components
         * of the dashboard.
         * 
         * By extending JFrame, the Dashboard class itself becomes a frame, and there
         * is no need to create a separate JFrame instance.
         * The constructor of Dashboard sets up the frame's properties and adds components to it.
         * 
         * Panels and other components can be added to the frame using the add() method.
         */
        try {
            JPanel mainPanel = new MainPanel();
            add(mainPanel, BorderLayout.CENTER);
            dashboardLogger.logDashboardCreation();
        } catch (Exception e) {
            // Log the exception message.
            dashboardLogger.info(e.getMessage());
        }
        
        // pack() method is used to resize the frame so that all its contents are at or above their preferred sizes.
        pack();
        // Set the frame to be visible.
        setVisible(true);
        
    }

    public static void main(String[] args) {
        // The main method uses SwingUtilities.invokeLater to ensure that the GUI creation is done
        // on the Event Dispatch Thread (EDT), which is the proper way to create and update the GUI in Swing.
        SwingUtilities.invokeLater(Dashboard::new);
    }
}

class MainPanel extends JPanel {

    public MainPanel() {
        // Set the panel size to the user's screen size.
        Dimension fullScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(fullScreen);
    }

    // Override the paintComponent method to draw the panel.
    @Override
    public void paintComponent(Graphics g) {
        // Call the parent paintComponent method.
        super.paintComponent(g);
        // Cast the Graphics object to Graphics2D.
        Graphics2D g2 = (Graphics2D) g;

        // Dashboard begins here.
    }
}

class MyLogger extends Logger {
    private static final boolean V_LOGS_ENABLED = true;
    private static final boolean V_CONSOLE_LOGS_ENABLED = true;
    private static final String M_LOG_DISABLED = "Logging disabled";
    private static final String M_CONSOLE_LOG_DISABLED = "Console logging disabled";
    private static final String M_FILLER = String.format(
        "######%-5s######%-5s######\t%-20s\t######%-5s######%-5s######",
        "\s", "\s",
        MyLogger.class.getName(),
        "\s", "\s"
    );

    // Constructor
    public MyLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);

        try {
            // Create a console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            this.addHandler(consoleHandler);

            // Create a file handler
            String logDir = "./logs";
            FileHandler fileHandler = new FileHandler(logDir + "/dashboard.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            this.addHandler(fileHandler);

            // Set the logger level
            this.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Log OS information.
        logOsInfo();
        // Log Java information.
        logJavaInfo();
    }

    @Override
    public void info(String msg) {
        // Log the message.
        try {
            assert V_LOGS_ENABLED;
            super.info(String.format("%n%s%n%s%n", M_FILLER, msg));
        } catch (Exception e) {
            // Log the exception message.
            super.info(e.getMessage());
            super.info(String.format("%s%n%s%n", M_LOG_DISABLED, M_FILLER));
        }

        // Log the message to the console.
        try {
            assert V_CONSOLE_LOGS_ENABLED;
            super.info(String.format("%n%s%n%s%n", M_FILLER, msg));
        } catch (Exception e) {
            // Log the exception message.
            super.info(e.getMessage());
            super.info(String.format("%s%n%s%n", M_CONSOLE_LOG_DISABLED, M_FILLER));
        }
    }

    // OS information.
    public void logOsInfo() {
        info(
            // Table formated. All Legends first then on the second row centered values.
            String.format(
                "|\t%-18s\t|\t%-18s\t|\t%-18s\t|%n|\t%-18s\t|\t%-18s\t|\t%-18s\t|",
                "OS Name", "OS Version", "OS Architecture",
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch")
            )
        );
    }

    // Java information.
    public void logJavaInfo() {
        info(
            // Table formated. All Legends first then on the second row centered values.
            String.format(
                "|\t%-18s\t|\t%-18s\t|\t%-18s\t|%n|\t%-18s\t|\t%-18s\t|\t%-18s\t|",
                "Java Vendor", "Java Version", "Java Runtime Version",
                System.getProperty("java.vendor"),
                System.getProperty("java.version"),
                System.getProperty("java.runtime.version")
            )
        );
    }

    // Dashboard creation.
    public void logDashboardCreation() {
        info(
            String.format(
                "%n\t- Dashboard created successfully -%n"
            )
        );
    }
}