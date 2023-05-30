package gui.branchpanel;

import file.GitBranchData;
import gui.branchpanel.component.*;
import gui.branchpanel.component.commitlogpane.CommitLogPane;

import javax.swing.*;
import java.awt.*;

public class BranchPanel extends JPanel{
    private GitBranchData gitBranchData=new GitBranchData();

    public BranchPanel() {
        setLayout(new BorderLayout(3, 3));

        JPanel status = new JPanel(new BorderLayout(3, 3));
        status.add(new CommitLogPane(gitBranchData), BorderLayout.CENTER);
        status.add(new GitCommit(gitBranchData), BorderLayout.SOUTH);

        add(status, BorderLayout.CENTER);
        add(new GitBranch(gitBranchData), BorderLayout.WEST);
    }
}