package gui.branchmanager;

import datamodel.GitBranchData;
import gui.branchmanager.component.*;
import gui.branchmanager.component.commitlogpane.CommitLogPane;

import javax.swing.*;
import java.awt.*;

public class BranchManager extends JPanel{
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public BranchManager(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        setLayout(new BorderLayout(3, 3));

        JPanel status = new JPanel(new GridLayout(2, 1));
        status.add(new CommitLogPane(gitBranchData));
        status.add(new GitCommit(gitBranchData));

        add(status, BorderLayout.CENTER);
        add(new GitBranch(gitBranchData), BorderLayout.WEST);
    }
}