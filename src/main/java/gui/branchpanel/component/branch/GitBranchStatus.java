package gui.branchpanel.component.branch;

import javax.swing.*;
import file.*;
import jgitmanager.JGitManagerImprv;

import java.awt.*;

public class GitBranchStatus extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;


    public GitBranchStatus(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;
        this.setLayout(new GridLayout(4,1,4,4));
        JLabel CurrentBranchName=new JLabel("CURRENT BRANCH");
        String CurrentBranch=gitBranchData.getCurrentBranch();
        JLabel printCurrentBranch=new JLabel(CurrentBranch);
        JLabel SelectedBranchName=new JLabel("SELECTED BRANCH");
        String SelectedBranch=gitBranchData.getSelectedBranch();
        JLabel printSelectedBranch=new JLabel(SelectedBranch);
        add(CurrentBranchName);
        add(printCurrentBranch);
        add(SelectedBranchName);
        add(printSelectedBranch);


    }
}
