package file;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;

import gui.PanelRefreshUtil;
import jgitmanager.FileStatus;
import jgitmanager.JGitManager;

public class SelectedFile {
    /** singleton instance variable */
    private static SelectedFile instance = null;

    /** instance variable */
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

        if(file.isDirectory()) {
            gitStatus = FileStatus.DIRECTORY;
        }
        else {
            try {
                if(JGitManager.findGitRepository(file) == 1) {
                    gitStatus = JGitManager.gitCheckFileStatus(file);
                }
                else {
                    gitStatus = FileStatus.UNTRACKED;
                }
            } catch(IOException | GitAPIException e) {
            };
        }

        PanelRefreshUtil.refreshTitle();
    }

    public FileStatus getGitStatus() {
        return gitStatus;
    }
}