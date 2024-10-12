package dashboard;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Dashboard extends JFrame {
    private static final String M_FILLER = "<!############ DASHBOARD LOGGER ############!>";
    private static Logger myLogger = Logger.getLogger(Dashboard.class.getName());

    public static void main(String[] args){
        boolean shouldLogOSInfo = true;
        boolean shouldLog = true;
        
        if (shouldLogOSInfo) {
            // Log OS information.
            myLogger.info(String.format("Debug Informations%n"));
            myLogger.info(String.format(
                "OS%nName: %s%nVersion: %s%nArch: %s%n%s",
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch"),
                M_FILLER
            ));
            myLogger.info(String.format(
                "Java%nVendor: %s%nVersion: %s%nVM Name: %s%n%s",
                System.getProperty("java.vendor"),
                System.getProperty("java.version"),
                System.getProperty("java.vm.name"),
                M_FILLER
            ));
        }

        
        // Create a new dashboard frame.
        JFrame frame = new Dashboard();
        // Set the frame title
        frame.setTitle("Dashboard - PFJN - 1705125");
        // Set the default close operation
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create a new panel.
        JPanel panel = new Panel();
        // Add the panel to the frame.
        frame.getContentPane().add(panel);
        // Pack the frame. This causes the window to be sized to
        // fit the preferred size and layouts of its subcomponents.
        frame.pack();
        // Set the frame to be visible.
        frame.setVisible(true);

        if (shouldLog) {
            // Log the successful creation of the dashboard.
            myLogger.info(String.format("Dashboard created successfully%n%s", M_FILLER));
        }
    }
}

class Panel extends JPanel {
    private static final String M_FILLER = "<!############ PANEL LOGGER ############!>";
    private static Logger myLogger = Logger.getLogger(Panel.class.getName());
    boolean shouldLog = true;
    
    public Panel() {            
        // Set the panel size to the user's screen size.
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(screenSize);

        if (shouldLog) {
            // Log the successful creation of the panel.
            myLogger.info(String.format("Panel size set to screen size%n%s", M_FILLER));
        }
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
