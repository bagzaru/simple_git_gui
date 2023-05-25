package gui.branchpanel.component.commithistory.tablemvc;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CommitHistoryTableController {
    //클릭 처리 필요(모델에서 해당 커밋 데이터 받아오기, Panel3에 전달하기)
    //UI에 대한 외부 입력(함수)에 대한 모델의 상태 변화 처리를 담당하는 클래스
    CommitHistoryModel model;
    CommitHistoryTableView table;

    ListSelectionListener listSelectionListener;

    public CommitHistoryTableController(CommitHistoryModel model, CommitHistoryTableView table){
        this.model = model;
        this.table = table;
        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //테이블이 클릭되었을 때 발동
                //1. 선택한 부분의 정보를 모델에서 받아옵니다.
                //2. 선택한 Commit을 <3번패널>로 보냅니다.

            }
        };
        table.getSelectionModel().addListSelectionListener(listSelectionListener);

    }

}
