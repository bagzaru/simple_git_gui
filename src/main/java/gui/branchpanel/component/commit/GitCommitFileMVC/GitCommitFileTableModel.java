package gui.branchpanel.component.commit.GitCommitFileMVC;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GitCommitFileTableModel extends AbstractTableModel {
    private List<File> commitFiles;
    private String[] columns = {"File"};

    public GitCommitFileTableModel() {
        commitFiles = new ArrayList<File>();
    }

    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return commitFiles.get(row).getName();
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getColumnCount() {
        return columns.length;
    }

    public int getRowCount() {
        return commitFiles.size();
    }

    public File getCommitFile(int row) {
        return commitFiles.get(row);
    }

    public void setCommitFiles(List<File> commitFiles) {
        this.commitFiles = commitFiles;
        fireTableDataChanged();
    }
}
