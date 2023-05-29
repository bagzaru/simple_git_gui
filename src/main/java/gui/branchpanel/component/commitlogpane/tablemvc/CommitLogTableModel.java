package gui.branchpanel.component.commitlogpane.tablemvc;

import jgitmanager.JGitManager;
import org.eclipse.jgit.revwalk.RevCommit;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;

public class CommitLogTableModel extends AbstractTableModel {
    //Commit data, Update가 호출될 때마다 업데이트됩니다. 처음에는 길이가 0입니다.
    private ArrayList<RevCommit> logs;

    //테이블의 열 제목을 지정합니다. 추후에 enum으로 변경될 수 있습니다.
    private final String[] columns = {
            "Graph",
            "Message",
            "Author",
            "Checksum"
    };


    //생성자입니다.
    public CommitLogTableModel(){
        logs = new ArrayList<>();
    };

    @Override
    public int getRowCount(){
        //row의 수 반환
        if(logs==null)
            return 0;
        return logs.size();
    }

    @Override
    public int getColumnCount(){
        //column의 수 반환
        return columns.length;
    }

    //선택한 영역의 데이터를 반환합니다.
    //Model의 데이터가 TableView에 저장될 때 Swing.JTable 내부적으로 호출됩니다.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(logs.size()<=rowIndex){
            return null;
        }
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

    //각 Column의 제목을 반환합니다.
    @Override
    public String getColumnName(int column){
        return columns[column];
    }

    //UpdateLogsByBranch: branch가 입력되면, 그에 맞게 모델의 데이터를 업데이트한 후, UI에 업데이트 신호를 보냅니다.
    //기본적으로 ModelController에서 호출됩니다.
    public boolean UpdateModelByBranch(File repositoryDir, String branch){
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

    //선택한 행을 입력받아 해당 행의 Commit 값을 반환합니다.
    //기본적으로 외부에서 호출됩니다.
    public RevCommit getCommitByRowIndex(int rowIndex){
        try{
            return logs.get(rowIndex);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
