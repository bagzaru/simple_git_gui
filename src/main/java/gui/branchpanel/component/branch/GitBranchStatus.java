package gui.branchpanel.component.branch;

import javax.swing.*;
import file.*;

import java.awt.*;

public class GitBranchStatus extends JPanel {

    private static GitBranchStatus instance;
    public static GitBranchStatus getInstance(){
        if(instance==null){
            instance=new GitBranchStatus();
        }
        return instance;
    }
    private String CurrentBranch;
    private String SelectedBranch;
    public GitBranchStatus() {
        //CurrentBranch=GitBranchData.getCurrentBranch();
        JLabel printCurrentBranch=new JLabel(CurrentBranch);
        //SelectedBranch=GitBranchData.getSelectedBranch();
        JLabel printSelectedBranch=new JLabel(SelectedBranch);

        add(printCurrentBranch, BorderLayout.NORTH);
        add(printSelectedBranch,BorderLayout.SOUTH);
    }
}
