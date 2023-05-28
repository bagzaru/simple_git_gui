package gui.branchpanel;

import gui.branchpanel.component.commithistory.CommitLogPane;

import javax.swing.*;

public class BranchPanel extends JPanel {
    public CommitLogPane commitHistoryPanel;
    public BranchPanel() {
        commitHistoryPanel = new CommitLogPane();
        add(commitHistoryPanel);
    }
}