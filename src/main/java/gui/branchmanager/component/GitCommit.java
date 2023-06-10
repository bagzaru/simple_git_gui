package gui.branchmanager.component;

import datamodel.GitBranchData;
import gui.branchmanager.component.commit.GitCommitFile;
import gui.branchmanager.component.commit.GitCommitModify;
import gui.branchmanager.component.commit.GitCommitStatus;

import javax.swing.*;
import java.awt.*;

public class GitCommit extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitCommit(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        setLayout(new GridLayout(1,2));

        JPanel commit = new JPanel();
        commit.setLayout(new BorderLayout(2,1));
        commit.add(new GitCommitStatus(gitBranchData), BorderLayout.NORTH);
        commit.add(new GitCommitFile(gitBranchData), BorderLayout.CENTER);

        add(commit);
        add(new GitCommitModify(gitBranchData));
    }
}
