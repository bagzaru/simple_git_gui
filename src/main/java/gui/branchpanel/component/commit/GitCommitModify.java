package gui.branchpanel.component.commit;

import file.BranchDataChangeListener;
import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.CommitInfo;
import jgitmanager.JGitManagerImprv;

import javax.swing.*;
import java.awt.*;

public class GitCommitModify extends JPanel implements BranchDataChangeListener {
    private String Diff;
    private JTextArea Differences;
    private GitBranchData gitBranchData;
    private CommitInfo commitInfo;

    public GitCommitModify(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        gitBranchData.addBranchDataChangeListener(this);

        Differences=new JTextArea();
        this.add(Differences, BorderLayout.CENTER);
    }

    @Override
    public void updateData() {
        try {
            Diff = JGitManagerImprv.gitDiff(SelectedFile.getInstance().getFile(),
                    gitBranchData.getSelectedCommit(), gitBranchData.getSelectedChangeFile());
            System.out.println("Test1: " + SelectedFile.getInstance().getFile().getName());
            System.out.println("Test2: " + gitBranchData.getSelectedCommit().getName());
            System.out.println("Test3: " + gitBranchData.getSelectedChangeFile().getName());
            System.out.println("Test4: " + gitBranchData.getSelectedChangeFile().getAbsolutePath());
        }catch (Exception e){
            System.out.println(e.toString());
        }

        System.out.println("Test Diff: " + Diff);
        Differences.setText(Diff);
    }
}
