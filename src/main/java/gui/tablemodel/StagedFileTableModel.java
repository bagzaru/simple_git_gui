package gui.tablemodel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.jgit.api.errors.GitAPIException;

import jgitmanager.JGitManager;
import jgitmanager.StagedFileStatus;

public class StagedFileTableModel extends AbstractTableModel {
    private File[] stagedFiles;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = {
            "Staged File",
            "File Status",
            "Path"
    };

    public StagedFileTableModel() {
        this(new File[0]);
    }

    StagedFileTableModel(File[] files) { this.stagedFiles = files; }

    public Object getValueAt(int row, int column) {
        File file = stagedFiles[row];
        switch (column) {
            case 0:
                return fileSystemView.getSystemDisplayName(file);
            case 1:
                String gitStatusImagePath = getGitStatusImagePath(file);
                if (gitStatusImagePath != null) {
                    URL url = getClass().getResource(gitStatusImagePath);
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        int cellHeight = 23;
                        int cellWidth = cellHeight * 3;
                        Image image = icon.getImage();
                        Image scaledImage = image.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);
                        return scaledIcon;
                    }
                }
                return null;
            case 2:
                String path = "/";
                path += JGitManager.findGitRepositoryName(file);
                path += "/";
                path += JGitManager.findGitRepositoryRelativePath(file);
                return path;
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) {
        switch(column) {
            case 1:
                return ImageIcon.class;
            default:
                return String.class;
        }
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return stagedFiles.length;
    }

    public File getFile(int row) {
        return stagedFiles[row];
    }

    public void setFiles(File[] files) {
        this.stagedFiles = files;
        fireTableDataChanged();
    }

    public String getGitStatusImagePath(File file) {
        StagedFileStatus stagedFileStatus;
        String imagePath;

        try {
            stagedFileStatus = JGitManager.gitCheckStagedFileStatus(file);

            switch (stagedFileStatus) {
                case ADDED:
                    return "/git_status_icons/Added.png";
                case STAGED:
                    return "/git_status_icons/Staged.png";
                case DELETED:
                    return "/git_status_icons/Removed.png";
                default:
                    imagePath = null;

            }
        } catch (IOException | GitAPIException e) {
            imagePath = null;
        }
        return imagePath;
    }
}
