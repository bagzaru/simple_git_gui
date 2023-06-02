package gui.branchpanel.component.commit;

import file.GitBranchData;
import gui.branchpanel.component.commit.GitCommitFileMVC.GitCommitFileTableController;
import gui.branchpanel.component.commit.GitCommitFileMVC.GitCommitFileTableModel;
import gui.branchpanel.component.commit.GitCommitFileMVC.GitCommitFileTableView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(),(int)d.getHeight()/2));
    }
}