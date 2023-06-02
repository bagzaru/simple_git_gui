package gui.branchpanel.component.branch;

import file.GitBranchData;
import gui.branchpanel.component.branch.button.*;

import javax.swing.*;

public class GitBranchMenu  extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchMenu(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;

        GitBranchCreateButton CreateButton=new GitBranchCreateButton(gitBranchData);
        CreateButton.setBounds(0,100,50,30);
        add(CreateButton);

        GitBranchDeleteButton DeleteButton=new GitBranchDeleteButton(gitBranchData);
        DeleteButton.setBounds(50,100,50,30);
        add(DeleteButton);

        GitBranchRenameButton RenameButton=new GitBranchRenameButton(gitBranchData);
        RenameButton.setBounds(100,100,50,30);
        add(RenameButton);

        GitBranchMergeButton MergeButton=new GitBranchMergeButton(gitBranchData);
        MergeButton.setBounds(150,100,50,30);
        add(MergeButton);
    }
}
