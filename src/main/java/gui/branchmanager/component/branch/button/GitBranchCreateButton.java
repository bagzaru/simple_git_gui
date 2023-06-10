package gui.branchmanager.component.branch.button;

import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class GitBranchCreateButton extends JButton {
    private JOptionPane ConflictBox;
    private GitBranchData gitBranchData;
    public GitBranchCreateButton(GitBranchData gitBranchData){
        this.gitBranchData=gitBranchData;
        ConflictBox=new JOptionPane();
        setText("+");
        MessageBox CreateBranchName=new MessageBox();//messageBox 브랜치 이름
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BranchName=CreateBranchName.ShowMessageBox();
                try {
                    JGitManagerImprv.gitCreateBranch(SelectedFile.getInstance().getFile(),BranchName);
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
class MessageBox extends JOptionPane{

    MessageBox(){

    }

    String ShowMessageBox(){
        return showInputDialog("Branch NAME을 입력하세요");
    }
}
