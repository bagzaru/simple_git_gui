package file;

import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitBranchData {
    private List<BranchDataChangeListener> branchSelectionEventListeners;   //GitBranchList의 Branch List selection Event에 대응
    private List<BranchDataChangeListener> currentBranchChangeEventListeners;  //GitBranchList의 Branch 명령 버튼 selection Event에 대응
    private List<BranchDataChangeListener> commitSelectionEventListeners;   //GitCommitPane의 Commit List Selection Event에 대응
    private List<BranchDataChangeListener> fileSelectionEventListeners;   //GitCommitPane의 Commit List Selection Event에 대응
    private List<BranchDataChangeListener> gitBranchCommandEventListeners;   //GitCommitPane의 Commit List Selection Event에 대응


    private String CurrentBranch;
    private String SelectedBranch;
    private RevCommit SelectedCommit; //아마 RevCommit 객체로 바뀔 예정
    private File SelectedChangeFile;

    public GitBranchData() {
        branchSelectionEventListeners = new ArrayList<>();
        currentBranchChangeEventListeners = new ArrayList<>();
        commitSelectionEventListeners = new ArrayList<>();
        fileSelectionEventListeners = new ArrayList<>();
        gitBranchCommandEventListeners = new ArrayList<>();
    }

    public void addBranchSelectionEventListener(BranchDataChangeListener listener) {
        branchSelectionEventListeners.add(listener);
    }

    public void notifyBranchSelectionChanged() {
        for(BranchDataChangeListener listener : branchSelectionEventListeners) {
            listener.updateData();
        }
    }


    public void addCurrentBranchChangeEventListeners(BranchDataChangeListener listener) {
        currentBranchChangeEventListeners.add(listener);
    }

    public void notifyCurrentBranchChanged() {
        for(BranchDataChangeListener listener : currentBranchChangeEventListeners) {
            listener.updateData();
        }
    }


    public void addCommitSelectionEventListeners(BranchDataChangeListener listener) {
        commitSelectionEventListeners.add(listener);
    }

    public void notifyCommitSelectionChanged() {
        for(BranchDataChangeListener listener : commitSelectionEventListeners) {
            listener.updateData();
        }
    }


    public void addFileSelectionEventListeners(BranchDataChangeListener listener) {
        fileSelectionEventListeners.add(listener);
    }

    public void notifyFileSelectionChanged() {
        for(BranchDataChangeListener listener : fileSelectionEventListeners) {
            listener.updateData();
        }
    }

    public void addGitBranchCommandEventListener(BranchDataChangeListener listener) {
        gitBranchCommandEventListeners.add(listener);
    }

    public void notifyGitBranchCommandCalled() {
        for(BranchDataChangeListener listener : gitBranchCommandEventListeners) {
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
        notifyCurrentBranchChanged();
    }

    public String getSelectedBranch() {
        return SelectedBranch;
    }

    public void setSelectedBranch(String branch) {
        SelectedBranch = branch;
        SelectedCommit = null;
        SelectedChangeFile = null;
        notifyBranchSelectionChanged();
    }

    public RevCommit getSelectedCommit() {
        return SelectedCommit;
    }

    public void setSelectedCommit(RevCommit commit) {
        SelectedCommit = commit;
        SelectedChangeFile = null;
        notifyCommitSelectionChanged();
    }

    public File getSelectedChangeFile() {
        return SelectedChangeFile;
    }

    public void setSelectedChangeFile(File file) {
        SelectedChangeFile = file;
        notifyFileSelectionChanged();
    }
}