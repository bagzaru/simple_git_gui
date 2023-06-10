package gui.branchmanager.component.branch;

import datamodel.GitBranchData;
import gui.branchmanager.component.branch.button.*;

import javax.swing.*;
import java.awt.*;

public class GitBranchMenu  extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchMenu(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        this.setLayout(new GridLayout(1,4));
        GitBranchCreateButton CreateButton=new GitBranchCreateButton(gitBranchData);
        CreateButton.setSize(10,30);
        add(CreateButton);

        GitBranchDeleteButton DeleteButton=new GitBranchDeleteButton(gitBranchData);
        DeleteButton.setSize(50,30);
        add(DeleteButton);

        GitBranchRenameButton RenameButton=new GitBranchRenameButton(gitBranchData);
        RenameButton.setSize(50,30);
        add(RenameButton);

        GitBranchMergeButton MergeButton=new GitBranchMergeButton(gitBranchData);
        MergeButton.setSize(50,30);
        add(MergeButton);
    }
}
