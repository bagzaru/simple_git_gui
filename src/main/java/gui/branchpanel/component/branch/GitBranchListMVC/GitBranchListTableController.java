package gui.branchpanel.component.branch.GitBranchListMVC;

import file.BranchDataChangeListener;
import file.GitBranchData;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GitBranchListTableController implements BranchDataChangeListener {
    private GitBranchListTableModel model;
    private GitBranchListTableView view;
    private ListSelectionListener listSelectionListener;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchListTableController(GitBranchListTableModel model, GitBranchListTableView view, GitBranchData gitBranchData) {
        this.model = model;
        this.view = view;
        this.gitBranchData = gitBranchData;
        gitBranchData.addBranchDataChangeListener(this);

        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    int row = view.getSelectedRow();
                    System.out.println("Test row: " + row);
                    String selectedBranch = model.getBranch(row);
                    gitBranchData.setSelectedBranch(selectedBranch);
                }
            }
        };
        view.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    @Override
    public void updateData() {
        //String[] branchList = (jgit과 연동해서 현재 브랜치 목록을 호출하는 메소드)
        String[] branchList = {"", "project2", "main"}; //테스트용 배열
        model.setBranchList(branchList);
    }
}