package gui.branchpanel.component.branch.GitBranchListMVC;

import file.BranchDataChangeListener;
import file.GitBranchData;
import file.SelectedFile;
import jgitmanager.JGitManager;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GitBranchListTableController implements BranchDataChangeListener {
    private GitBranchListTableModel model;
    private GitBranchListTableView view;
    private ListSelectionListener listSelectionListener;

    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData;

    public GitBranchListTableController(GitBranchListTableModel model, GitBranchListTableView view, GitBranchData gitBranchData) {
        this.model = model;
        this.view = view;
        this.gitBranchData = gitBranchData;
        gitBranchData.addBranchDataChangeListener(this);

        this.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int clickedRow = view.getSelectedRow();
                if(!e.getValueIsAdjusting() && clickedRow != -1){
                    String selectedBranch = model.getBranch(clickedRow);
                    gitBranchData.setSelectedBranch(selectedBranch);
                }
            }
        };
        view.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    @Override
    public void updateData() {
        File currentDir = SelectedFile.getInstance().getFile();
        if(JGitManager.findGitRepository(currentDir) == 1) {
            try {
                List<String> branchList = JGitManagerImprv.gitBranchList(currentDir);
                model.setBranchList(branchList);
            } catch (IOException | GitAPIException e) {
            }
        }
    }
}