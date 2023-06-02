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

        setLayout(new BorderLayout(3,3));

        JPanel commit = new JPanel();
        commit.setLayout(new BorderLayout(3,3));
        commit.add(new GitCommitStatus(gitBranchData), BorderLayout.NORTH);
        commit.add(new GitCommitFile(gitBranchData), BorderLayout.CENTER);

        add(commit, BorderLayout.CENTER);
        add(new GitCommitModify(gitBranchData), BorderLayout.EAST);
    }
}
