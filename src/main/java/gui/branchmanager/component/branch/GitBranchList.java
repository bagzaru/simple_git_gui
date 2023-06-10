package gui.branchmanager.component.branch;

import datamodel.GitBranchData;
import gui.branchmanager.component.branch.GitBranchListMVC.GitBranchListTableController;
import gui.branchmanager.component.branch.GitBranchListMVC.GitBranchListTableModel;
import gui.branchmanager.component.branch.GitBranchListMVC.GitBranchListTableView;

import javax.swing.*;

public class GitBranchList extends JScrollPane {
    /** GitBranchList table MVC */
    private GitBranchListTableModel model;
    private GitBranchListTableView view;
    private GitBranchListTableController controller;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchList(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        model = new GitBranchListTableModel(gitBranchData);
        view = new GitBranchListTableView();
        controller = new GitBranchListTableController(model, view, gitBranchData);

        view.setTableModel(model);
        setViewportView(view);
    }
}