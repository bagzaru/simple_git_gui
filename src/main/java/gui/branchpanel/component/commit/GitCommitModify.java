package gui.branchpanel.component.commit;

import file.BranchDataChangeListener;
import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.CommitInfo;
import jgitmanager.JGitManagerImprv;

import javax.swing.*;
import java.awt.*;

public class GitCommitModify extends JScrollPane implements BranchDataChangeListener {

    private String Diff;
    private JTextArea Differences;
    private GitBranchData gitBranchData;
    private CommitInfo commitInfo;

    public GitCommitModify(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        gitBranchData.addCurrentBranchChangeEventListeners(this);
        gitBranchData.addCommitSelectionEventListeners(this);
        gitBranchData.addBranchSelectionEventListener(this);
        gitBranchData.addFileSelectionEventListeners(this);
        gitBranchData.addGitBranchCommandEventListener(this);

        Differences=new JTextArea();
        setViewportView(Differences);
        //this.add(Differences);
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
            //GitDiff failed, selectedChangeFile이 잘못 선택되면 발생
            // 브랜치, 커밋 선택 등 파일 목록이 없어져도 발생
            // Diff 값을 초기화
            Diff = "";
            System.out.println(e.toString());
        }

        System.out.println("Test Diff: " + Diff);
        Differences.setText(Diff);
    }
}
