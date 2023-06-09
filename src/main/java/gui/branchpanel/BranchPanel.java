package gui.branchpanel;

import file.GitBranchData;
import gui.branchpanel.component.*;
import gui.branchpanel.component.commitlogpane.CommitLogPane;

import javax.swing.*;
import java.awt.*;

public class BranchPanel extends JPanel{
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public BranchPanel(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        setLayout(new BorderLayout(3, 3));

        JPanel status = new JPanel(new GridLayout(2, 1));
        status.add(new CommitLogPane(gitBranchData));
        status.add(new GitCommit(gitBranchData));

        add(status, BorderLayout.CENTER);
        add(new GitBranch(gitBranchData), BorderLayout.WEST);
    }
}