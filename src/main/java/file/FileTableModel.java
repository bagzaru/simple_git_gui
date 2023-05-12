package file;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.Date;

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
        int status;
        if(file.isDirectory())
            status = -1;
        else {
            //status = jgitmanager.gitCheckFileStatus(file, GitRepoDirectory.getInstance().getRepoDirectory());
            status = 1; //임시
        }

        switch (status) {
            case 0:
                return "Fail";
            case 1:
                return "Untracked";
            case 2:
                return "Modified";
            case 3:
                return "Staged & Modified";
            case 4:
                return "Deleted";
            case 5:
                return "Staged";
            case 6:
                return "Unmodified(committed)";
            default:
                return "";
        }
    }
}