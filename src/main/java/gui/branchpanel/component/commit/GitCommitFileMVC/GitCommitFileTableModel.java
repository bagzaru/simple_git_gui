package gui.branchpanel.component.commit.GitCommitFileMVC;

import javax.swing.table.AbstractTableModel;
import java.io.File;

public class GitCommitFileTableModel extends AbstractTableModel {
    private File[] commitFiles;
    private String[] columns = {"File"};

    public GitCommitFileTableModel() {
        this(new File[0]);
    }

    GitCommitFileTableModel(File[] files) {
        this.commitFiles = files;
    }

    public Object getValueAt(int row, int column) {
        File file = commitFiles[row];
        switch (column) {
            case 0:
                return file;
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
        return commitFiles.length;
    }

    public File getCommitFile(int row) {
        return commitFiles[row];
    }

    public void setCommitFiles(File[] commitFiles) {
        this.commitFiles = commitFiles;
        fireTableDataChanged();
    }
}
