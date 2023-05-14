package file;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import jgitmanager.FileStatus;
import jgitmanager.JGitManager;
import org.eclipse.jgit.api.errors.GitAPIException;

/** A TableModel to hold File[]. */
public class FileTableModel extends AbstractTableModel {
    private File[] files;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = {
            "Icon",
            "File",
            "Size",
            "Last Modified",
            "File Status"
    };

    public FileTableModel() {
        this(new File[0]);
    }

    FileTableModel(File[] files) {
        this.files = files;
    }

    public Object getValueAt(int row, int column) {
        File file = files[row];
        switch (column) {
            case 0:
                return fileSystemView.getSystemIcon(file);
            case 1:
                return fileSystemView.getSystemDisplayName(file);
            case 2:
                return file.length();
            case 3:
                return file.lastModified();
            case 4:
                String gitStatusImagePath = getGitStatusImagePath(file);
                if (gitStatusImagePath != null) {
                    URL url = getClass().getResource(gitStatusImagePath);
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        int cellHeight = fileSystemView.getSystemIcon(file).getIconHeight() + 6;
                        int cellWidth = cellHeight * 3;
                        Image image = icon.getImage();
                        Image scaledImage = image.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);
                        return scaledIcon;
                    }
                }
                return null;
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            case 2:
                return Long.class;
            case 3:
                return Date.class;
            case 4:
                return ImageIcon.class;
        }
        return String.class;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return files.length;
    }

    public File getFile(int row) {
        return files[row];
    }

    public void setFiles(File[] files) {
        this.files = files;
        fireTableDataChanged();
    }

    public String getGitStatusImagePath(File file) {
        FileStatus fileStatus;
        String imagePath;

        if(file.isDirectory()) {
            imagePath = null;
        }
        else {
            try {
                if(JGitManager.findGitRepository(file) == 1) {
                    fileStatus = JGitManager.gitCheckFileStatus(file);
                }
                else {
                    fileStatus = FileStatus.UNTRACKED;
                }

                switch (fileStatus) {
                    case DIRECTORY:
                        imagePath = null;
                        break;
                    case UNTRACKED:
                        imagePath = "/git_status_icons/Untracked.png";
                        break;
                    case MODIFIED:
                        imagePath = "/git_status_icons/Modified.png";
                        break;
                    case STAGED:
                        imagePath = "/git_status_icons/Staged.png";
                        break;
                    case COMMITTED:
                        imagePath = "/git_status_icons/Committed.png";
                        break;
                    default:
                        imagePath = null;
                }
            } catch(IOException | GitAPIException e) {
                imagePath = null;
            };
        }
        return imagePath;
    }
}