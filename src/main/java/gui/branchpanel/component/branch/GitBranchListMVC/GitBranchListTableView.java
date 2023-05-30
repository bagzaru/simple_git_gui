package gui.branchpanel.component.branch.GitBranchListMVC;

import javax.swing.*;

public class GitBranchListTableView extends JTable {
    public GitBranchListTableView() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        setShowVerticalLines(false);
    }

    public void setTableModel(GitBranchListTableModel model) {
        setModel(model);
    }
}