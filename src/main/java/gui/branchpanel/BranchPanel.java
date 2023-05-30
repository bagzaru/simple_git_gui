package gui.branchpanel;

import file.GitBranchData;
import gui.branchpanel.component.GitBranch;
import gui.branchpanel.component.branch.*;

import javax.swing.*;
import java.awt.*;

public class BranchPanel extends JPanel{
    private GitBranchData gitBranchData=new GitBranchData();
    public BranchPanel(){
       add(GitBranch.getInstance(),BorderLayout.WEST);

    }

}