package gui.branchpanel.component.branch.GitBranchListMVC;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class GitBranchListTableModel extends AbstractTableModel {
    private List<String> branchList;
    private String[] columns = {"Branch List"};

    public GitBranchListTableModel() {
        branchList = new ArrayList<>();
    }

    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return branchList.get(row);
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
        return branchList.size();
    }

    public String getBranch(int row) {
        return branchList.get(row);
    }

    public void setBranchList(List<String> branchList) {
        this.branchList = branchList;
        fireTableDataChanged();
    }
}
