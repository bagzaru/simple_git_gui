package file;

import java.io.File;

public class GitBranchData {
    private String CurrentBranch;
    private String SelectedBranch;
    private String SelectedCommit; //아마 RevCommit 객체로 바뀔 예정
    private File SelectedChangeFile;

    public GitBranchData() {

    }

    public String getCurrentBranch() {
        return CurrentBranch;
    }

    public void setCurrentBranch(String branch) {
        CurrentBranch = branch;
    }

    public String getSelectedBranch() {
        return SelectedBranch;
    }

    public void setSelectedBranch(String branch) {
        SelectedBranch = branch;
    }

    public String getSelectedCommit() {
        return SelectedCommit;
    }

    public void setSelectedCommit(String commit) {
        SelectedCommit = commit;
    }

    public File getSelectedChangeFile() {
        return SelectedChangeFile;
    }

    public void setSelectedChangeFile(File file) {
        SelectedChangeFile = file;
    }
}