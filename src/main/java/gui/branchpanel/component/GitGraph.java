package gui.branchpanel.component;

import file.GitBranchData;

import javax.swing.*;

public class GitGraph extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitGraph(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;
    }
}