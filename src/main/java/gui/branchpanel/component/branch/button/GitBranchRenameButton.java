package gui.branchpanel.component.branch.button;

import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GitBranchRenameButton extends JButton {
    private JOptionPane ConflictBox;
    private GitBranchData gitBranchData;
    public GitBranchRenameButton(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        ConflictBox =new JOptionPane();
        setText("R");
        MessageBox RenameBranchName=new MessageBox();//messageBox new branch name 추가 예정
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String NewBranchName=RenameBranchName.ShowMessageBox();
                try {
                    boolean isCurrentBranch = (gitBranchData.getCurrentBranch().equals(gitBranchData.getSelectedBranch()));
                    JGitManagerImprv.gitRenameBranch(SelectedFile.getInstance().getFile(),
                            gitBranchData.getSelectedBranch(),NewBranchName);
                    if(isCurrentBranch){
                        gitBranchData.setCurrentBranch();
                    }
                } catch (GitAPIException ex) {
                    ConflictBox.showMessageDialog(null,ex.toString());
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    ConflictBox.showMessageDialog(null,ex.toString());
                    throw new RuntimeException(ex);
                }
                //Branch Command Call
                gitBranchData.notifyGitBranchCommandCalled();
            }
        });
    }
}


