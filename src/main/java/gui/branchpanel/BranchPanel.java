package gui.branchpanel;

import gui.branchpanel.component.commithistory.GitCommitHistory;

import javax.swing.*;

public class BranchPanel extends JPanel {
    public GitCommitHistory commitHistoryPanel;
    public BranchPanel() {
        commitHistoryPanel = new GitCommitHistory();
        add(commitHistoryPanel);
    }
}