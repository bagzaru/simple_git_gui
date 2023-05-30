package gui.branchpanel.component;

import file.GitBranchData;
import gui.branchpanel.component.branch.GitBranchList;

import javax.swing.*;
import java.awt.*;

public class GitBranch extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranch(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        setLayout(new BorderLayout(3,3));
        //add(new GitBranchMenu(gitBranchData), BorderLayout.NORTH);
        add(new GitBranchList(gitBranchData), BorderLayout.CENTER);
        //add(new GitBranchStatus(gitBranchData), BorderLayout.SOUTH);
    }
}
