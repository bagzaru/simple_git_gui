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

        try {
            gitBranchData.setCurrentBranch(JGitManagerImprv.gitCurrentBranch(SelectedFile.getInstance().getFile()));
        }catch (Exception e){
            System.out.println(e.toString());
        }
        
        String CurrentBranch=gitBranchData.getCurrentBranch();
        JLabel printCurrentBranch=new JLabel(CurrentBranch);
        String SelectedBranch=gitBranchData.getSelectedBranch();
        JLabel printSelectedBranch=new JLabel(SelectedBranch);
        setLayout(new BorderLayout(2,2));
        add(printCurrentBranch);
        add(printSelectedBranch);


    }
}
