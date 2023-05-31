package gui.branchpanel.component.commit.GitCommitFileMVC;

import file.BranchDataChangeListener;
import file.GitBranchData;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;

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
        gitBranchData.addBranchDataChangeListener(this);

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

    @Override
    public void updateData() {
        //File[] commitFiles = (jgit과 연동해서 선택한 커밋의 파일 목록 반환하는 메소드)
        File[] commitFiles = {new File("a")}; //테스트용 배열
        model.setCommitFiles(commitFiles);
    }
}