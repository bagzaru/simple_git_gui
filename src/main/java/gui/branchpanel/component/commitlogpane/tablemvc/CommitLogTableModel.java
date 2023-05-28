package gui.branchpanel.component.commitlogpane.tablemvc;

import jgitmanager.JGitManager;
import org.eclipse.jgit.revwalk.RevCommit;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;

public class CommitLogTableModel extends AbstractTableModel {
    //String에서 Commit으로 교체 필요
    //branch를 입력받아, parent를 따라가며 모델 데이터를 생성하는 함수 필요함. 완료 후 view에 refresh 요청
    private ArrayList<RevCommit> logs;

    //현재 로컬 Repo의 위치를 저장합니다.
    private File currentRepository = null;
    //현재 branch의 정보를 저장합니다.
    private String currentBranch = null;

    private final String[] columns = {
            "Graph",
            "Message",
            "Author",
            "Checksum"
    };


    public CommitLogTableModel(){
        logs = new ArrayList<>();
    };

    @Override
    public int getRowCount(){
        //override 함수
        //row의 수 구현
        if(logs==null)
            return 0;
        return logs.size();
    }

    @Override
    public int getColumnCount(){
        //override 함수
        //column의 length 구현
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RevCommit value = logs.get(rowIndex);
        if(columnIndex==0){
            //그래프 그리는 알고리즘 작성
        }else if(columnIndex==1) {
            return value.getFullMessage();
        }else if(columnIndex==2){
            return value.getAuthorIdent().getName();
        }else if(columnIndex==3){
            return value.getId().getName().substring(0,6);
        }
        return null;
    }

    @Override
    public String getColumnName(int column){
        return columns[column];
    }

    //UpdateLogsByBranch: branch가 입력되면, 그에 맞게 모델의 데이터를 업데이트한 후, UI에 업데이트 신호를 보냅니다.
    //기본적으로 ModelController에서 호출됩니다.
    boolean UpdateModelByBranch(File repositoryDir, String branch){
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
        //View에 변경사항 반영
        fireTableDataChanged();
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
