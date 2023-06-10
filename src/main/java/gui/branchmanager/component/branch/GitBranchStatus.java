package gui.branchmanager.component.branch;

import javax.swing.*;
import file.*;

import java.awt.*;

public class GitBranchStatus extends JPanel implements BranchDataChangeListener {
    private JLabel PrintCurrentBranch;
    private JLabel PrintSelectedBranch;
    private String CurrentBranch;
    private String SelectedBranch;
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;


    public GitBranchStatus(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        gitBranchData.addBranchSelectionEventListener(this);
        gitBranchData.addCurrentBranchChangeEventListeners(this);
        gitBranchData.addGitBranchCommandEventListener(this);

        this.setLayout(new GridLayout(4,1,4,4));

        JLabel CurrentBranchName=new JLabel("CURRENT BRANCH",JLabel.CENTER);

        PrintCurrentBranch=new JLabel(CurrentBranch,JLabel.CENTER);

        JLabel SelectedBranchName=new JLabel("SELECTED BRANCH",JLabel.CENTER);

        PrintSelectedBranch=new JLabel(SelectedBranch,JLabel.CENTER);

        add(CurrentBranchName);
        add(PrintCurrentBranch);
        add(SelectedBranchName);
        add(PrintSelectedBranch);



    }
    @Override
    public void updateData(){
        try{
            CurrentBranch=gitBranchData.getCurrentBranch();
            SelectedBranch=gitBranchData.getSelectedBranch();
        }catch(Exception e){
            System.out.println(e.toString());
        }
        PrintCurrentBranch.setText(CurrentBranch);
        PrintSelectedBranch.setText(SelectedBranch);
    }
}
