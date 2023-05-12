package file;

import javax.swing.*;
import java.io.File;

import gui.GitGUI;
import jgitmanager.FileStatus;
import jgitmanager.JGitManager;

public class SelectedFile {
    private static SelectedFile instance = null;
    private File selectedFile;
    private FileStatus gitStatus;

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
            gitStatus = FileStatus.FOLDER;
        else {
            gitStatus = JGitManager.gitCheckFileStatus(selectedFile);
        }

        JFrame f = (JFrame) GitGUI.gui.getTopLevelAncestor();
        if (f!=null) {
            f.setTitle(
                    GitGUI.APP_TITLE + " :: " + GitGUI.fileSystemView.getSystemDisplayName(file));
        }
        GitGUI.gui.repaint();
    }

    public FileStatus getGitStatus() {
        return gitStatus;
    }
}