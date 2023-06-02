package gui.branchpanel.component.branch.GitBranchListMVC;

import file.BranchDataChangeListener;
import file.GitBranchData;
import file.SelectedFile;
import gui.filepanel.FilePanel;
import gui.filepanel.PanelRefreshUtil;
import gui.filepanel.component.FileTree;
import gui.filepanel.model.FileTableModel;
import jgitmanager.JGitManager;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        gitBranchData.addCurrentBranchChangeEventListeners(this);
        gitBranchData.addGitBranchCommandEventListener(this);

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

        //중앙 파일테이블의 더블클릭을 통해 다른 폴더로 이동을 구현하였습니다.
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        //1. 더블클릭된 브랜치를 확인합니다.
                        int row = view.getSelectionModel().getLeadSelectionIndex();
                        String selected = ((GitBranchListTableModel) view.getModel()).getBranch(row);
                        //2. 확인창을 띄워서 선택할지 확인시킵니다.
                        JPanel confirmPanel = new JPanel(new BorderLayout(3, 3));
                        confirmPanel.add(new JLabel("Do you really want to checkout to '"+selected+"' branch?"), BorderLayout.WEST);
                        int result = JOptionPane.showConfirmDialog(view,confirmPanel,
                                "Branch Checkout", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            try {
                                //체크아웃 시도
                                JGitManagerImprv.gitCheckout(SelectedFile.getInstance().getFile(), selected);
                                gitBranchData.notifyGitBranchCommandCalled();
                            } catch (Throwable t) {
                                JOptionPane.showMessageDialog(null,"Error Occured: Checkout Failed",
                                        "Checkout error", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("GitBranchListTableController: error ocurred");
                            }
                        }


                    } catch (Exception exception) {
                        System.out.println("Something error occurred while mouseDoubleClick Event:" + exception.getMessage());
                    }

                }
            }
        });


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