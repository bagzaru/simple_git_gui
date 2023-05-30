package gui.branchpanel.component.commit.GitCommitFileMVC;

import file.GitBranchData;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;

public class GitCommitFileTableController {
    private GitCommitFileTableModel model;
    private GitCommitFileTableView view;
    private ListSelectionListener listSelectionListener;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitCommitFileTableController(GitCommitFileTableModel model, GitCommitFileTableView view, GitBranchData gitBranchData) {
        this.model = model;
        this.view = view;
        this.gitBranchData = gitBranchData;

        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    File selectedCommitFile = model.getCommitFile(view.getSelectedRow());
                    gitBranchData.setSelectedChangeFile(selectedCommitFile); //이름 불일치 수정해야할 듯
                }
            }
        };
        view.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void updateData(File[] files) {
        model.setCommitFiles(files);
    }
}