package file;

import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitBranchData {
    private List<BranchDataChangeListener> listeners;

    private String CurrentBranch;
    private String SelectedBranch;
    private RevCommit SelectedCommit; //아마 RevCommit 객체로 바뀔 예정
    private File SelectedChangeFile;

    public GitBranchData() {
        listeners = new ArrayList<>();
    }

    public void addBranchDataChangeListener(BranchDataChangeListener listener) {
        listeners.add(listener);
    }

    public void notifyBranchDataChange() {
        for(BranchDataChangeListener listener : listeners) {
            listener.updateData();
        }
    }

    public void initGitBranchData() {
        SelectedBranch = null;
        SelectedCommit = null;
        SelectedChangeFile = null;
        setCurrentBranch();
    }

    public String getCurrentBranch() {
        return CurrentBranch;
    }

    public void setCurrentBranch() {
        try {
            CurrentBranch = JGitManagerImprv.gitCurrentBranch(SelectedFile.getInstance().getFile());
        } catch (IOException e) {
        }
        notifyBranchDataChange();
    }

    public String getSelectedBranch() {
        return SelectedBranch;
    }

    public void setSelectedBranch(String branch) {
        SelectedBranch = branch;
        SelectedCommit = null;
        SelectedChangeFile = null;
        notifyBranchDataChange();
    }

    public RevCommit getSelectedCommit() {
        return SelectedCommit;
    }

    public void setSelectedCommit(RevCommit commit) {
        SelectedCommit = commit;
        SelectedChangeFile = null;
        notifyBranchDataChange();
    }

    public File getSelectedChangeFile() {
        return SelectedChangeFile;
    }

    public void setSelectedChangeFile(File file) {
        SelectedChangeFile = file;
        notifyBranchDataChange();
    }
}