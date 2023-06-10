package gui.branchmanager.component.branch.GitBranchListMVC;

import file.GitBranchData;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class GitBranchListTableModel extends AbstractTableModel {
    private List<String> branchList;
    private String[] columns = {"", "Branch List"};
    private GitBranchData gitBranchData;

    public GitBranchListTableModel(GitBranchData gitBranchData) {
        branchList = new ArrayList<String>();
        this.gitBranchData = gitBranchData;
    }

    public Object getValueAt(int row, int column) {
        String branch = branchList.get(row);
        String currentBranch = gitBranchData.getCurrentBranch();

        switch (column) {
            case 0:
                if(branch.equals(currentBranch)) {
                    return "V";
                }
                else {
                    return "";
                }
            case 1:
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
        return branchList.size();
    }

    public String getBranch(int row) {
        return branchList.get(row);
    }

    public void setBranchList(List<String> branchList) {
        this.branchList = branchList;
        fireTableDataChanged();

        //모델 리스트를 갱신하면, 선택 데이터가 초기화됩니다. 따라서 selected를 초기화합니다.
        gitBranchData.setSelectedBranch("");
    }
}
