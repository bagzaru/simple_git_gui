package gui.branchpanel.component;

import file.GitBranchData;
import gui.branchpanel.component.branch.GitBranchList;
import gui.branchpanel.component.commit.GitCommitFile;
import gui.branchpanel.component.commit.GitCommitModify;
import gui.branchpanel.component.commit.GitCommitStatus;

import javax.swing.*;
import java.awt.*;

public class GitCommit extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitCommit(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        setLayout(new GridLayout(1,2));

        JPanel commit = new JPanel();
        commit.setLayout(new GridLayout(2,1));
        commit.add(new GitCommitStatus(gitBranchData));
        commit.add(new GitCommitFile(gitBranchData));

        add(commit, BorderLayout.CENTER);
        add(new GitCommitModify(gitBranchData), BorderLayout.EAST);
    }
}
