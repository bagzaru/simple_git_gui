package gui.branchpanel;

import gui.branchpanel.component.commitlogpane.CommitLogPane;

import javax.swing.*;

public class BranchPanel extends JPanel {
    public CommitLogPane commitLogPane;
    public BranchPanel() {
        commitLogPane = new CommitLogPane();
        add(commitLogPane);
    }
}