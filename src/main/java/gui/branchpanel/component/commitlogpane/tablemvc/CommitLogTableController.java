package gui.branchpanel.component.commitlogpane.tablemvc;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;

public class CommitLogTableController {
    //클릭 처리 필요(모델에서 해당 커밋 데이터 받아오기, Panel3에 전달하기)
    //UI에 대한 외부 입력(함수)에 대한 모델의 상태 변화 처리를 담당하는 클래스
    CommitLogTableModel model;
    CommitLogTableView table;

    ListSelectionListener listSelectionListener;

    public CommitLogTableController(CommitLogTableModel model, CommitLogTableView table){
        this.model = model;
        this.table = table;
        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //테이블이 클릭되었을 때 발동
                //1. 선택한 부분의 정보를 모델에서 받아옵니다.
                //2. 선택한 Commit을 <3번패널>로 보냅니다.
                if(!e.getValueIsAdjusting()){ //false: 마우스가 떼어진 순간만 적용

                    //1. 선택한 부분의 정보를 모델에서 받아옵니다.
                    int clickedRow = table.getSelectedRow();
                    String checksum = model.getCommitByRowIndex(clickedRow).getId().getName().substring(0,6);
                    System.out.println("Table Clicked: "+checksum);
                }
                //선택한 Commit을 3번 패널로 보냅니다.
                //merge 후에 구현 예정
            }
        };
        table.getSelectionModel().addListSelectionListener(listSelectionListener);

    }

    //기본적으로 외부에서 호출됩니다.
    //CommitLogTable의 값 변화에 대한 Update를 호출합니다.
    public void UpdateCommitLogTable(File repositoryDir, String branch){
        model.UpdateModelByBranch(repositoryDir,branch);
    }

}