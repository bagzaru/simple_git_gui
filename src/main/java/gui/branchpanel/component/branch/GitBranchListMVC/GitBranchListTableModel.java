package gui.branchpanel.component.branch.GitBranchListMVC;

import javax.swing.table.AbstractTableModel;

public class GitBranchListTableModel extends AbstractTableModel {
    private String[] branchList;
    private String[] columns = {"Branch List"};

    public GitBranchListTableModel() {
        this(new String[0]);
    }

    GitBranchListTableModel(String[] list) {
        this.branchList = list;
    }

    public Object getValueAt(int row, int column) {
        String branch = branchList[row];
        switch (column) {
            case 0:
                return branch;
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
        return branchList.length;
    }

    public String getBranch(int row) {
        return branchList[row];
    }

    public void setBranchList(String[] branchList) {
        this.branchList = branchList;
        fireTableDataChanged();
    }
}
