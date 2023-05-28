package gui.branchpanel;

import gui.branchpanel.component.commitlogpane.CommitLogPane;

import javax.swing.*;

public class BranchPanel extends JPanel {
    public CommitLogPane commitHistoryPanel;
    public BranchPanel() {
        commitHistoryPanel = new CommitLogPane();
        add(commitHistoryPanel);
    }
}