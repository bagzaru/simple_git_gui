package file;

import javax.swing.*;
import java.io.File;

import gui.GitGUI;

public class SelectedFile {
    private static SelectedFile instance = null;
    private File selectedFile;
    private int gitStatus;

    SelectedFile() {
    }

    public static SelectedFile getInstance() {
        if(instance == null) {
            instance = new SelectedFile();
        }
        return instance;
    }

    public File getFile() {
        return selectedFile;
    }

    public void setFile(File file) {
        selectedFile = file;

        if(file.isDirectory())
            gitStatus = -1;
        else {
            //gitStatus = Git.gitCheckFileStatus(selectedFile, GitRepoDirectory.getInstance().getRepoDirectory());
            gitStatus = 1; //임시
        }

        JFrame f = (JFrame) GitGUI.gui.getTopLevelAncestor();
        if (f!=null) {
            f.setTitle(
                    GitGUI.APP_TITLE +
                            " :: " +
                            GitGUI.fileSystemView.getSystemDisplayName(file));
        }
        GitGUI.gui.repaint();
    }

    public int getGitStatus() {
        return gitStatus;
    }
}