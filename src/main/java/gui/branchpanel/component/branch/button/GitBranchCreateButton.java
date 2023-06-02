package gui.branchpanel.component.branch.button;

import file.SelectedFile;
import jgitmanager.JGitManager;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class GitBranchCreateButton extends JButton {
    public GitBranchCreateButton(){
        setText("+");
        MessageBox CreateBranchName=new MessageBox();//messageBox 브랜치 이름
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BranchName=CreateBranchName.ShowMessageBox();
                try {
                    JGitManagerImprv.gitCreateBranch(SelectedFile.getInstance().getFile(),BranchName);
                } catch (GitAPIException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }
}
class MessageBox extends JOptionPane{

    MessageBox(){

    }

    String ShowMessageBox(){
        return showInputDialog("Branch NAME을 입력하세요");
    }
}
