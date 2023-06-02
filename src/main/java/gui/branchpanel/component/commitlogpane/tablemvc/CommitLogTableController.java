package gui.branchpanel.component.commitlogpane.tablemvc;

import file.BranchDataChangeListener;
import file.GitBranchData;
import file.SelectedFile;
import org.eclipse.jgit.revwalk.RevCommit;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;

public class CommitLogTableController implements BranchDataChangeListener {
    //클릭 처리 필요(모델에서 해당 커밋 데이터 받아오기, Panel3에 전달하기)
    //UI에 대한 외부 입력(함수)에 대한 모델의 상태 변화 처리를 담당하는 클래스
    CommitLogTableModel model;
    CommitLogTableView table;

    ListSelectionListener listSelectionListener;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public CommitLogTableController(CommitLogTableModel model, CommitLogTableView table, GitBranchData gitBranchData){
        this.model = model;
        this.table = table;
        this.gitBranchData = gitBranchData;
        gitBranchData.addBranchDataChangeListener(this);

        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //테이블이 클릭되었을 때 발동
                //1. 선택한 부분의 정보를 모델에서 받아옵니다.
                //2. 선택한 Commit을 <3번패널>로 보냅니다.
                int clickedRow = table.getSelectedRow();
                if(!e.getValueIsAdjusting() && clickedRow != -1){ //false: 마우스가 떼어진 순간만 적용
                    //1. 선택한 부분의 정보를 모델에서 받아옵니다.
                    String checksum = model.getCommitByRowIndex(clickedRow).getId().getName().substring(0,6);

                    RevCommit selectCommit = model.getCommitByRowIndex(clickedRow);
                    gitBranchData.setSelectedCommit(selectCommit);
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

    //위 메소드를 이벤트 리스너로 사실상 이름이랑 인자만 바꾼거인데 내(승재)가 아니라 승훈이가 만든거라 그냥 지우진 않고 일단 남겨둠
    @Override
    public void updateData() {
        File repositoryDir = SelectedFile.getInstance().getFile();
        String selectedBranch = gitBranchData.getSelectedBranch();

        if(repositoryDir != null && selectedBranch != null) {
            //git에 관리 안되는 폴더거나 브랜치 선택 안했을때 null 에러 뜨는 것 때문에 임시로 추가한건데
            //나중에 예외 처리 위치 수정할 예정
            model.UpdateModelByBranch(repositoryDir, selectedBranch);
        }
    }
}
