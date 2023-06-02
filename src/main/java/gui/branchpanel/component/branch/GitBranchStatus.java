package gui.branchpanel.component.branch;

import javax.swing.*;
import file.*;

import java.awt.*;

public class GitBranchStatus extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    private String CurrentBranch;
    private String SelectedBranch;

    public GitBranchStatus(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        CurrentBranch=gitBranchData.getCurrentBranch();
        JLabel printCurrentBranch=new JLabel(CurrentBranch);
        SelectedBranch=gitBranchData.getSelectedBranch();
        JLabel printSelectedBranch=new JLabel(SelectedBranch);



        add(printCurrentBranch, BorderLayout.NORTH);
        add(printSelectedBranch,BorderLayout.CENTER);

    }
}
