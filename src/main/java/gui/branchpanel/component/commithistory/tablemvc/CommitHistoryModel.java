package gui.branchpanel.component.commithistory.tablemvc;

import file.SelectedFile;
import jgitmanager.JGitManager;
import org.eclipse.jgit.revwalk.RevCommit;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CommitHistoryModel extends AbstractTableModel {
    //String에서 Commit으로 교체 필요
    //branch를 입력받아, parent를 따라가며 모델 데이터를 생성하는 함수 필요함. 완료 후 view에 refresh 요청
    private ArrayList<RevCommit> logs;

    private String[] columns = {
            "Graph",
            "Message",
            "Author",
            "Checksum"
    };
    public CommitHistoryModel(){

    };

    @Override
    public int getRowCount(){
        //override 함수
        //row의 수 구현
        return logs.size();
    }

    @Override
    public int getColumnCount(){
        //override 함수
        //column의 length 구현
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
    
    //UpdateLogsByBranch: branch가 입력되면, 그에 맞게 모델의 데이터를 업데이트한 후, UI에 업데이트 신호를 보냅니다.
    public void UpdateModelByBranch(String branch){
        try{
            logs.clear();
            Iterable<RevCommit> tlogs = JGitManager.gitLog(SelectedFile.getInstance().getFile(), branch);
            for(RevCommit r : tlogs){
                logs.add(r);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
