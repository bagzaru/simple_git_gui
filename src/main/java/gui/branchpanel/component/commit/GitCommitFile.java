package gui.branchpanel.component.commit;

import file.GitBranchData;

import javax.swing.*;

public class GitCommitFile extends JScrollPane {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitCommitFile(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;
    }
}