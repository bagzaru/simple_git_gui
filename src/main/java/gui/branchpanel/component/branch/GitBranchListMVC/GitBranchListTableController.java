package gui.branchpanel.component.branch.GitBranchListMVC;

import file.GitBranchData;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GitBranchListTableController {
    private GitBranchListTableModel model;
    private GitBranchListTableView view;
    private ListSelectionListener listSelectionListener;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchListTableController(GitBranchListTableModel model, GitBranchListTableView view, GitBranchData gitBranchData) {
        this.model = model;
        this.view = view;
        this.gitBranchData = gitBranchData;

        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    String selectedBranch = model.getBranch(view.getSelectedRow());
                    gitBranchData.setSelectedBranch(selectedBranch);
                }
            }
        };
        view.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void updateData(String[] branchList) {
        model.setBranchList(branchList);
    }
}