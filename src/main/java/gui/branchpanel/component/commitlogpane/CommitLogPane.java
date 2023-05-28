package gui.branchpanel.component.commitlogpane;

import file.SelectedFile;
import gui.branchpanel.component.commitlogpane.tablemvc.CommitLogTableModel;
import gui.branchpanel.component.commitlogpane.tablemvc.CommitLogTableView;
import gui.branchpanel.component.commitlogpane.tablemvc.CommitLogTableController;

import javax.swing.*;
import java.awt.*;

public class CommitLogPane extends JScrollPane {
    //git table mvc(model, view, controller)
    //model: 표출할 데이터를 관리
    //view: 데이터를 가공하여 GUI로 표현
    //controller: 클릭 등 외부와의 상호작용 처리
    CommitLogTableModel model;
    CommitLogTableView view;
    CommitLogTableController controller;

    public CommitLogPane(){
        //FileTable처럼 JScrollPane에 UI 등록하기
        model = new CommitLogTableModel();
        view = new CommitLogTableView(model);
        controller = new CommitLogTableController(model, view);

        setViewportView(view);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(),(int)d.getHeight()/2));
    }

    //임시적으로 String 타입으로 생성합니다.
    //외부에서 접근 가능하며, branch를 입력받아 CommitLogTable을 업데이트합니다.
    public void Update(String branch){
        //Model Update 구현하
        controller.UpdateCommitLogTable(SelectedFile.getInstance().getFile(), branch);
    }


}
