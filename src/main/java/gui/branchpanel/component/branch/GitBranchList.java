package gui.branchpanel.component.branch;

import file.GitBranchData;

import javax.swing.*;
import java.awt.*;

public class GitBranchList extends JPanel{
    private static GitBranchList instance;
    public static GitBranchList getInstance(){
        if(instance==null){
            instance=new GitBranchList();
        }
        return instance;
    }
    private String CurrentBranch;
    private String SelectedBranch;
    public GitBranchList() {
        CurrentBranch=GitBranchData.getCurrentBranch();
        JLabel printCurrentBranch=new JLabel(CurrentBranch);
        SelectedBranch=GitBranchData.getSelectedBranch();
        JLabel printSelectedBranch=new JLabel(SelectedBranch);

        add(printCurrentBranch, BorderLayout.NORTH);
        add(printSelectedBranch,BorderLayout.SOUTH);
    }
}
