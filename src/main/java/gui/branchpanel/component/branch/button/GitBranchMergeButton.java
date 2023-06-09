package gui.branchpanel.component.branch.button;

import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.JGitManagerImprv;
import jgitmanager.MergeException;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GitBranchMergeButton extends JButton {
    private JOptionPane ConflictBox;
    private GitBranchData gitBranchData;
    public GitBranchMergeButton(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        setText("M");
        ConflictBox=new JOptionPane();
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JGitManagerImprv.gitMerge(SelectedFile.getInstance().getFile(),
                            gitBranchData.getSelectedBranch());
                } catch (GitAPIException | IOException ex) {
                    ConflictBox.showMessageDialog(null,ex.toString());
                    throw new RuntimeException(ex);
                }catch(MergeException ex){
                    ConflictBox.showMessageDialog(null,ex.toString());
                    throw new RuntimeException(ex);
                }
                //Branch Command Call
                gitBranchData.notifyGitBranchCommandCalled();
            }
        });
    }
}
