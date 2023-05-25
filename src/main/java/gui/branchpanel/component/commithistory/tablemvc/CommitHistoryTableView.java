package gui.branchpanel.component.commithistory.tablemvc;

import javax.swing.*;

public class CommitHistoryTableView extends JTable {
    //EventListner 추가하기
    //모델 연결하기

    public void Render(){

        //table UI의 속성을 지정합니다.
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        setShowVerticalLines(false);

        //eventListener를 추가합니다.
        //아직 미구현



    }
}
