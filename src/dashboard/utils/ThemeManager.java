package dashboard.utils;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ThemeManager{

    public ThemeManager() {
        // Change look and feel to a dark look and feel.
        try {
            // Pick from Metal, Nimbus, CDE/Motif, Mac OS X.
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error setting theme", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setTheme(String theme) {
        // Change look and feel to the specified theme.
        try {
            UIManager.setLookAndFeel(theme);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error setting theme", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void getThemes() {
        // List all the available themes.
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo look : looks) {
            System.out.println(look.getClassName());
        }
    }
}
