package gui.branchpanel.component.commit.GitCommitFileMVC;

import javax.swing.*;

public class GitCommitFileTableView extends JTable {
    public GitCommitFileTableView() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setShowVerticalLines(false);
    }

    public void setTableModel(GitCommitFileTableModel model) {
        setModel(model);
    }
}
