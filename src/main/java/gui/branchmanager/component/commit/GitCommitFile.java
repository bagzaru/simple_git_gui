package gui.branchmanager.component.commit;

import datamodel.GitBranchData;
import gui.branchmanager.component.commit.GitCommitFileMVC.GitCommitFileTableController;
import gui.branchmanager.component.commit.GitCommitFileMVC.GitCommitFileTableModel;
import gui.branchmanager.component.commit.GitCommitFileMVC.GitCommitFileTableView;

import javax.swing.*;

public class GitCommitFile extends JScrollPane {
    /** GitCommitFile table MVC */
    private GitCommitFileTableModel model;
    private GitCommitFileTableView view;
    private GitCommitFileTableController controller;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitCommitFile(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        model = new GitCommitFileTableModel();
        view = new GitCommitFileTableView();
        controller = new GitCommitFileTableController(model, view, gitBranchData);

        view.setTableModel(model);

        setViewportView(view);
    }
}