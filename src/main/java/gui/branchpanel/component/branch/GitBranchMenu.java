package gui.branchpanel.component.branch;

import file.GitBranchData;
import gui.branchpanel.component.branch.button.*;

import javax.swing.*;
import java.awt.*;

public class GitBranchMenu  extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchMenu(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        this.setLayout(new FlowLayout());
        GitBranchCreateButton CreateButton=new GitBranchCreateButton(gitBranchData);
        CreateButton.setSize(50,30);
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
