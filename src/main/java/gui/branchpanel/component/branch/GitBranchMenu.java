package gui.branchpanel.component.branch;

import gui.branchpanel.component.branch.button.*;

import javax.swing.*;

public class GitBranchMenu  extends JPanel {
    GitBranchMenu(){
        GitBranchCreateButton CreateButton=new GitBranchCreateButton();
        CreateButton.setBounds(0,100,50,30);
        add(CreateButton);

        GitBranchDeleteButton DeleteButton=new GitBranchDeleteButton();
        DeleteButton.setBounds(50,100,50,30);
        add(DeleteButton);

        GitBranchRenameButton RenameButton=new GitBranchRenameButton();
        RenameButton.setBounds(100,100,50,30);
        add(RenameButton);

        GitBranchMergeButton MergeButton=new GitBranchMergeButton();
        MergeButton.setBounds(150,100,50,30);
        add(MergeButton);
    }
}
