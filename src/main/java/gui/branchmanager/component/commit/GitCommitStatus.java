package gui.branchmanager.component.commit;

import datamodel.BranchDataChangeListener;
import datamodel.GitBranchData;
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
    private JPanel CommitDetailLabel;
    private JPanel CommitDetailValue;

    public GitCommitStatus(GitBranchData gitBranchData){
        super(new GridLayout(1, 2));
        this.gitBranchData = gitBranchData;
        gitBranchData.addCurrentBranchChangeEventListeners(this);
        gitBranchData.addCommitSelectionEventListeners(this);
        gitBranchData.addBranchSelectionEventListener(this);
        gitBranchData.addFileSelectionEventListeners(this);
        gitBranchData.addGitBranchCommandEventListener(this);

        CommitDetailLabel=new JPanel(new GridLayout(0,1,2,2));
        CommitDetailValue=new JPanel(new GridLayout(0,1,2,2));

        CommitDetailLabel.add(new JLabel("CHECKSUM   ",JLabel.TRAILING));
        /*COMMIT 내용 입력*/
        CheckSum=new JLabel();
        CommitDetailValue.add(CheckSum);

        CommitDetailLabel.add(new JLabel("COMMIT MESSAGE   ",JLabel.TRAILING));
        //COMMIT MESSAGE
        CommitMessage=new JLabel();
        CommitDetailValue.add(CommitMessage);

        CommitDetailLabel.add(new JLabel("AUTHOR   ",JLabel.TRAILING));
        //AUTHOR
        AuthorName=new JLabel();
        CommitDetailValue.add(AuthorName);
        CommitDetailLabel.add(new JLabel("AUTHOR EMAIL   ",JLabel.TRAILING));
        //EMAIL
        AuthorEmail=new JLabel();
        CommitDetailValue.add(AuthorEmail);

        CommitDetailLabel.add(new JLabel("DATE   ",JLabel.TRAILING));
        Date=new JLabel();
        CommitDetailValue.add(Date);

        //왼쪽 부분 길이 지정
        this.add(CommitDetailLabel);
        this.add(CommitDetailValue);
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

        CommitMessage.setText(commitInfo.getCommitMessage());

        AuthorName.setText(commitInfo.getAuthorName());
        AuthorEmail.setText(commitInfo.getAuthorEMail());
        Date.setText(commitInfo.getCommitTime());
    }

    public String getAdaptiveText(JPanel parentPanel, JLabel label) {
        int panelWidth = parentPanel.getWidth();
        String labelText = label.getText();
        Font labelFont = label.getFont();
        FontMetrics labelFontMetrics = label.getFontMetrics(labelFont);

        int horizontalPadding = 5; //좌우 여백 길이(pixel)
        int availableWidth = panelWidth - 2 * horizontalPadding; //실제 가능한 좌우 길이
        int textWidth = labelFontMetrics.stringWidth(labelText);    //Label에 적용되는 텍스트의 좌우길이를 가져옴

        System.out.println("텍스트: "+labelText+", 가용길이: "+availableWidth);

        //만약 가용길이보다 더 길다면 잘라냄
        if (textWidth > availableWidth) {
            int preferredTextWidth = availableWidth - 2 * labelFontMetrics.stringWidth("...");
            String trimmedText = labelText;
            while (labelFontMetrics.stringWidth(trimmedText) > preferredTextWidth&&trimmedText.length()>=1) {
                trimmedText = trimmedText.substring(0, trimmedText.length() - 1);
            }
            labelText = trimmedText + "...";
        }
        return labelText;
    }
}
