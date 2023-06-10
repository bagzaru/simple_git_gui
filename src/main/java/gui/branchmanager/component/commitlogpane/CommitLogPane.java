package gui.branchmanager.component.commitlogpane;

import datamodel.GitBranchData;
import datamodel.SelectedFile;
import gui.branchmanager.component.commitlogpane.tablemvc.CommitLogTableModel;
import gui.branchmanager.component.commitlogpane.tablemvc.CommitLogTableView;
import gui.branchmanager.component.commitlogpane.tablemvc.CommitLogTableController;

import javax.swing.*;

public class CommitLogPane extends JScrollPane {
    //git table mvc(model, view, controller)
    //model: 표출할 데이터를 관리
    //view: 데이터를 가공하여 GUI로 표현
    //controller: 클릭 등 외부와의 상호작용 처리
    CommitLogTableModel model;
    CommitLogTableView view;
    CommitLogTableController controller;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public CommitLogPane(GitBranchData gitBranchData){
        this.gitBranchData = gitBranchData;

        //FileTable처럼 JScrollPane에 UI 등록하기
        model = new CommitLogTableModel();
        view = new CommitLogTableView(model);
        controller = new CommitLogTableController(model, view, gitBranchData);

        setViewportView(view);
    }

    //임시적으로 String 타입으로 생성합니다.
    //외부에서 접근 가능하며, branch를 입력받아 CommitLogTable을 업데이트합니다.
    public void Update(String branch){
        //Model Update 구현하
        controller.UpdateCommitLogTable(SelectedFile.getInstance().getFile(), branch);
    } //updataCommitLogTable 메소드를 인터페이스 상속받은 메소드로 바꾸면서 인자 조건 달라져서 이거 필요 없을듯

}
