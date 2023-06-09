package gui.branchpanel.component.commit.GitCommitFileMVC;

import file.BranchDataChangeListener;
import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.JGitManager;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitCommitFileTableController implements BranchDataChangeListener {
    private GitCommitFileTableModel model;
    private GitCommitFileTableView view;
    private ListSelectionListener listSelectionListener;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitCommitFileTableController(GitCommitFileTableModel model, GitCommitFileTableView view, GitBranchData gitBranchData) {
        this.model = model;
        this.view = view;
        this.gitBranchData = gitBranchData;
        gitBranchData.addCurrentBranchChangeEventListeners(this);
        gitBranchData.addCommitSelectionEventListeners(this);
        gitBranchData.addBranchSelectionEventListener(this);
        gitBranchData.addFileSelectionEventListeners(this);
        gitBranchData.addGitBranchCommandEventListener(this);

        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int clickedRow = view.getSelectedRow();
                if(!e.getValueIsAdjusting() && clickedRow != -1) {
                    File selectedCommitFile = model.getCommitFile(clickedRow);
                    gitBranchData.setSelectedChangeFile(selectedCommitFile); //이름 불일치 수정해야할 듯
                }
            }
        };
        view.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    @Override
    public void updateData() {
        File currentDir = SelectedFile.getInstance().getFile();
        RevCommit selectedCommit = gitBranchData.getSelectedCommit();
        List<File> commitFiles;

        if(JGitManager.findGitRepository(currentDir) == 1) {
            if(selectedCommit == null) {
                commitFiles = new ArrayList<File>();
                model.setCommitFiles(commitFiles);
            }
            else {
                try {
                    commitFiles = JGitManagerImprv.gitChangedFileList(currentDir, selectedCommit);
                    model.setCommitFiles(commitFiles);
                } catch(Exception e){
                    commitFiles = new ArrayList<>();
                    model.setCommitFiles(commitFiles);
                }
            }
        }
    }
}