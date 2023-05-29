package file;

public class GitBranchData {
    private String CurrentBranch;
    private String SelectedBranch;
    private String SelectedCommit; //아마 RevCommit 객체로 바뀔 예정
    private String SelectedChangeFile; //아마 File 객체로 바뀔 예정

    GitBranchData() {

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

    public String getSelectedChangeFile() {
        return SelectedChangeFile;
    }

    public void setSelectedChangeFile(String file) {
        SelectedChangeFile = file;
    }
}