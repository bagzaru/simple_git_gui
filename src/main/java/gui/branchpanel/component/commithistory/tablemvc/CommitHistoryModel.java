package gui.branchpanel.component.commithistory.tablemvc;

import javax.swing.table.AbstractTableModel;

public class CommitHistoryModel extends AbstractTableModel {
    //String에서 Commit으로 교체 필요
    //branch를 입력받아, parent를 따라가며 모델 데이터를 생성하는 함수 필요함. 완료 후 view에 refresh 요청
    private String[] commits;
    private String[] columns = {
            "Graph",
            "Message",
            "Author",
            "Checksum"
    };
    public CommitHistoryModel(){

    };

    public int getRowCount(){
        //override 함수
        //row의 수 구현
        return commits.length;
    }

    public int getColumnCount(){
        //override 함수
        //column의 length 구현
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

}
