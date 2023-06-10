import gui.SimpleGitGUIFrame;

import javax.swing.*;

public class SimpleGitGUI {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimpleGitGUIFrame mainFrame = new SimpleGitGUIFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}