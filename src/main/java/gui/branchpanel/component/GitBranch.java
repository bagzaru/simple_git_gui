package gui.branchpanel.component;

import file.GitBranchData;

import javax.swing.*;

public class GitBranch extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranch(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;
    }
}
