package gui.branchpanel.component.branch.button;

import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GitBranchDeleteButton extends JButton {
    private GitBranchData gitBranchData;
    public GitBranchDeleteButton(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;
        setText("-");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JGitManagerImprv.gitDeleteBranch(SelectedFile.getInstance().getFile(),
                            gitBranchData.getSelectedBranch());
                } catch (GitAPIException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //Branch Command Call
                gitBranchData.notifyGitBranchCommandCalled();
            }
        });
    }
}
