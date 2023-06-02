package gui.branchpanel.component.commit;

import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.CommitInfo;
import jgitmanager.JGitManagerImprv;

import javax.swing.*;
import java.awt.*;

public class GitCommitModify extends JPanel {
    private String Diff;
    private JTextArea Differences;
    private GitBranchData gitBranchData;
    private CommitInfo commitInfo;
    public GitCommitModify(GitBranchData gitBranchData){
        try {
            Diff = JGitManagerImprv.gitDiff(SelectedFile.getInstance().getFile(),
                    gitBranchData.getSelectedCommit(), gitBranchData.getSelectedChangeFile());
        }catch (Exception e){
            System.out.println(e.toString());
        }
        Differences=new JTextArea(Diff);
        this.add(Differences, BorderLayout.CENTER);

    }
}
