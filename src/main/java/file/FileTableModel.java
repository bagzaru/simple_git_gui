package file;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.IOException;
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
                return gitStatus(file);
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

    public String gitStatus(File file) {
        FileStatus fileStatus;

        if(file.isDirectory())
            return "";
        else {
            try {
                if(JGitManager.findGitRepository(file) == 1) {
                    fileStatus = JGitManager.gitCheckFileStatus(file);
                }
                else {
                    fileStatus = FileStatus.UNTRACKED;
                }

                switch (fileStatus) {
                    case FOLDER:
                        return "";
                    case UNTRACKED:
                        return "Untracked";
                    case MODIFIED:
                        return "Modified";
                    case STAGED_MODIFIED:
                        return "Staged & Modified";
                    case DELETED:
                        return "Deleted";
                    case STAGED:
                        return "Staged";
                    case UNMODIFIED:
                        return "Unmodified(committed)";
                    default:
                        return "UNKNOWN FILE";
                }
            } catch(IOException | GitAPIException e) {
            };
        }
        return "UNKNOWN FILE";
    }
}