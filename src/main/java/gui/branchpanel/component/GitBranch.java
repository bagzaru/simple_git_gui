package gui.branchpanel.component;

import gui.branchpanel.component.branch.GitBranchList;
import gui.branchpanel.component.branch.GitBranchMenu;

import javax.swing.*;
import java.awt.*;

public class GitBranch extends JPanel {
    private static GitBranch instance;
    public static GitBranch getInstance(){
        if(instance==null){
            instance=new GitBranch();
        }
        return instance;
    }
    public GitBranch() {
        add(GitBranchMenu.getInstance(), BorderLayout.NORTH);
        add(GitBranchList.getInstance(),BorderLayout.SOUTH);

    }
}
