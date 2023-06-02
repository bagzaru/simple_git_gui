package gui.branchpanel.component.branch.button;

import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GitBranchMergeButton extends JButton {
    private GitBranchData gitBranchData;
    public GitBranchMergeButton(){
        setText("M");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JGitManagerImprv.gitMerge(SelectedFile.getInstance().getFile(),
                            gitBranchData.getSelectedBranch());
                } catch (GitAPIException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
