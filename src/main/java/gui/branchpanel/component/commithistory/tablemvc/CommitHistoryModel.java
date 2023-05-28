package gui.branchpanel.component.commithistory.tablemvc;

import jgitmanager.JGitManager;
import org.eclipse.jgit.revwalk.RevCommit;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;

public class CommitHistoryModel extends AbstractTableModel {
    //String에서 Commit으로 교체 필요
    //branch를 입력받아, parent를 따라가며 모델 데이터를 생성하는 함수 필요함. 완료 후 view에 refresh 요청
    private ArrayList<RevCommit> logs;

    //현재 로컬 Repo의 위치를 저장합니다.
    private File currentRepository = null;
    //현재 branch의 정보를 저장합니다.
    private String currentBranch = null;

    private enum Column {
        GRAPH(0, "Graph"),
        MESSAGE(1, "Message"),
        AUTHOR(2, "Author"),
        CHECKSUM(3, "Checksum")
        ;

        private final int key;
        private final String value;
        Column(int key, String value) {
            this.key = key;
            this.value = value;
        }
        public int getKey(){
            return key;
        }
        public String getValue(){
            return value;
        }
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
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RevCommit value = logs.get(rowIndex);
        Column[] columns = Column.values();
        if(columnIndex==Column.GRAPH.key){
            //그래프 그리는 알고리즘 작성
        }else if(columnIndex==Column.MESSAGE.key) {
            return value.getFullMessage();
        }else if(columnIndex==Column.AUTHOR.key){
            return value.getAuthorIdent().getName();
        }else if(columnIndex==Column.CHECKSUM.key){
            return value.getId().getName();
        }
        return null;
    }

    //UpdateLogsByBranch: branch가 입력되면, 그에 맞게 모델의 데이터를 업데이트한 후, UI에 업데이트 신호를 보냅니다.
    public boolean UpdateModelByBranch(File repositoryDir, String branch){
        currentBranch = branch;
        currentRepository = repositoryDir;
        try{
            logs.clear();
            Iterable<RevCommit> tlogs = JGitManager.gitLog(repositoryDir, branch);
            for(RevCommit r : tlogs){
                logs.add(r);
            }
        }catch(Exception e){
            System.out.println("Something error happened on CommitHistoryModel.UpdateModelByBranch");
            e.printStackTrace();
            return false;
        }
        //View.PostUpdateMessage(); //아직 미구현
        return true;
    }

    //
    public RevCommit getCommitByIndex(int tableIndex){
        try{
            return logs.get(tableIndex);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
