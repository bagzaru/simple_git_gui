package gui.branchpanel.component.commithistory;

import file.SelectedFile;
import gui.branchpanel.component.commithistory.tablemvc.CommitHistoryModel;
import gui.branchpanel.component.commithistory.tablemvc.CommitHistoryTableView;
import gui.branchpanel.component.commithistory.tablemvc.CommitHistoryTableController;

import javax.swing.*;
import java.awt.*;

public class GitCommitHistory extends JScrollPane {
    //git table mvc(model, view, controller)
    //model: 표출할 데이터를 관리
    //view: 데이터를 가공하여 GUI로 표현
    //controller: 클릭 등 외부와의 상호작용 처리
    CommitHistoryModel model;
    CommitHistoryTableView view;
    CommitHistoryTableController controller;

    public GitCommitHistory(){
        //FileTable처럼 JScrollPane에 UI 등록하기
        model = new CommitHistoryModel();
        view = new CommitHistoryTableView(model);
        controller = new CommitHistoryTableController(model, view);

        setViewportView(view);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(),(int)d.getHeight()/2));
    }

    //임시적으로 String 타입으로 생성함
    //외부에서 접근 가능하며, branch를 입력받아 GitCommitHistory를 업데이트함
    public void Update(String branch){
        //Model Update 구현하
        model.UpdateModelByBranch(SelectedFile.getInstance().getFile(), branch);
    }


}
