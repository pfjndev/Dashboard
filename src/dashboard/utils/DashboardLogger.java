package dashboard.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

public class DashboardLogger extends Logger {
    private static final boolean V_LOGS_ENABLED = true;
    private static final boolean V_CONSOLE_LOGS_ENABLED = true;
    private static final String M_LOG_DISABLED = "Logging disabled";
    private static final String M_CONSOLE_LOG_DISABLED = "Console logging disabled";
    private static final String M_FILLER = String.format(
        "######%-5s######%-5s######\t%-20s\t######%-5s######%-5s######",
        "\s", "\s",
        DashboardLogger.class.getName(),
        "\s", "\s"
    );

    // Constructor
    public DashboardLogger(String name, String resourceBundleName) {
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
            JOptionPane.showMessageDialog(null, "Error setting logger", "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Error logging message", "Error", JOptionPane.ERROR_MESSAGE);
            super.info(String.format("%s%n%s%n", M_LOG_DISABLED, M_FILLER));
        }

        // Log the message to the console.
        try {
            assert V_CONSOLE_LOGS_ENABLED;
            super.info(String.format("%n%s%n%s%n", M_FILLER, msg));
        } catch (Exception e) {
            // Log the exception message.
            super.info(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error logging message to console", "Error", JOptionPane.ERROR_MESSAGE);
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