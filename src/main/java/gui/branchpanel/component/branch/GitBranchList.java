package gui.branchpanel.component.branch;

import file.GitBranchData;
import gui.branchpanel.component.branch.GitBranchListMVC.GitBranchListTableController;
import gui.branchpanel.component.branch.GitBranchListMVC.GitBranchListTableModel;
import gui.branchpanel.component.branch.GitBranchListMVC.GitBranchListTableView;

import javax.swing.*;
import java.awt.*;

public class GitBranchList extends JScrollPane {
    /** GitBranchList table MVC */
    private GitBranchListTableModel model;
    private GitBranchListTableView view;
    private GitBranchListTableController controller;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchList(GitBranchData gitBranchData) {
        this.gitBranchData = gitBranchData;

        model = new GitBranchListTableModel();
        view = new GitBranchListTableView();
        controller = new GitBranchListTableController(model, view, gitBranchData);

        view.setTableModel(model);

        //테스트용 임시 코드
        String[] testList = {"a", "b", "c"};
        controller.updateData(testList);

        setViewportView(view);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(),(int)d.getHeight()/2));
    }
}