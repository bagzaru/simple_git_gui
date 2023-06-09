package gui.branchpanel.component.commit;

import file.BranchDataChangeListener;
import file.GitBranchData;
import jgitmanager.CommitInfo;
import jgitmanager.JGitManagerImprv;

import javax.swing.*;
import java.awt.*;

public class GitCommitStatus extends JPanel implements BranchDataChangeListener {
    private JLabel CheckSum;
    private JLabel CommitMessage;
    private JLabel AuthorName;
    private JLabel AuthorEmail;
    private JLabel Date;
    private CommitInfo commitInfo;
    private GitBranchData gitBranchData;

    public GitCommitStatus(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        gitBranchData.addCurrentBranchChangeEventListeners(this);
        gitBranchData.addCommitSelectionEventListeners(this);
        gitBranchData.addBranchSelectionEventListener(this);
        gitBranchData.addFileSelectionEventListeners(this);
        gitBranchData.addGitBranchCommandEventListener(this);

        JPanel CommitDetailLabel=new JPanel(new GridLayout(0,1,2,2));
        JPanel CommitDetailValue=new JPanel(new GridLayout(0,1,2,2));

        CommitDetailLabel.add(new JLabel("CHECKSUM",JLabel.TRAILING));
        /*COMMIT 내용 입력*/
        CheckSum=new JLabel();
        CommitDetailValue.add(CheckSum);

        CommitDetailLabel.add(new JLabel("COMMIT MESSAGE",JLabel.TRAILING));
        //COMMIT MESSAGE
        CommitMessage=new JLabel();
        CommitDetailValue.add(CommitMessage);

        CommitDetailLabel.add(new JLabel("AUTHOR",JLabel.TRAILING));
        //AUTHOR
        AuthorName=new JLabel();
        CommitDetailValue.add(AuthorName);
        CommitDetailLabel.add(new JLabel("AUTHOR EMAIL",JLabel.TRAILING));
        //EMAIL
        AuthorEmail=new JLabel();
        CommitDetailValue.add(AuthorEmail);

        CommitDetailLabel.add(new JLabel("DATE",JLabel.TRAILING));
        Date=new JLabel();
        CommitDetailValue.add(Date);
        //DATE

        this.add(CommitDetailLabel,BorderLayout.WEST);
        this.add(CommitDetailValue,BorderLayout.CENTER);
    }

    @Override
    public void updateData() {
        try{
            commitInfo = JGitManagerImprv.gitCommitInfo(gitBranchData.getSelectedCommit());
        }catch (Exception e){
            System.out.println(e.toString());
        }

        if(commitInfo.getCheckSum().length() > 6)
            CheckSum.setText(commitInfo.getCheckSum().substring(0,6));
        else
            CheckSum.setText(commitInfo.getCheckSum());

        if(commitInfo.getCommitMessage().length() > 30)
            CommitMessage.setText(commitInfo.getCommitMessage().substring(0, 30) + "...");
        else
            CommitMessage.setText(commitInfo.getCommitMessage());

        AuthorName.setText(commitInfo.getAuthorName());
        AuthorEmail.setText(commitInfo.getAuthorEMail());
        Date.setText(commitInfo.getCommitTime());
    }
}
