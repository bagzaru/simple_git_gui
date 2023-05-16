package gui;

import file.SelectedFile;
import org.eclipse.jgit.api.Git;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;

public class PanelRefreshUtil {
    //좌측의 root부터 시작되는 파일 트리를 새로고칩니다.

    public static void refreshAll(){
        refreshFileTree();
        refreshFileTable();
        refreshStagedList();
        refreshGitMenu();
        refreshGitFilePanel();
        refreshGitRepoStatusPanel();
    }

    public static DefaultMutableTreeNode lastTreeNode;
    public static void refreshFileTree(){
        JTree tree = Tree.getInstance();
        if(lastTreeNode!=null){
            GitGUI.showChildren(lastTreeNode);
        }
    }

    //중앙 상단의 현재 선택된 폴더의 파일들을 보여주는 테이블을 새로고칩니다.
    public static File currentDirectory;
    public static void refreshFileTable(){
        if(currentDirectory!=null){
            if (currentDirectory.isDirectory()) {
                File[] files = GitGUI.fileSystemView.getFiles(currentDirectory, true);
                GitGUI.setTableData(files);
            }
        }
        else{
            System.out.println("there is no selected dir");
        }
        GitGUI.gui.repaint();
    }

    //중앙 하단의 현재 git repo의 staged list를 새로고칩니다.
    public static void refreshStagedList(){
        StagedFileList.getInstance().setStagedFileTableData();
        GitGUI.gui.repaint();
    }

    //우측 상단의 현재 선택된 파일에 대한 깃 명령어 모음 패널을 새로고침합니다.
    public static void refreshGitFilePanel(){
        SelectedFile selectedFile = SelectedFile.getInstance();
        if(selectedFile!=null){
            File sfile = selectedFile.getFile();
            if(sfile!=null){
                selectedFile.setFile(sfile);
                GitFilePanel.getInstance().UpdatePanel();
            }
        }
    }
    public static void refreshGitRepoStatusPanel(){
        SelectedFile selectedFile = SelectedFile.getInstance();
        if(selectedFile!=null){
            File sfile = selectedFile.getFile();
            if(sfile!=null){
                selectedFile.setFile(sfile);
                GitRepositoryStatusPanel.getInstance().UpdateMenu();
            }
        }
    }

    //우측 하단의 현재 git repo에 대한 깃 명령어 모음 패널을 새로고침합니다.
    public static void refreshGitMenu(){
        GitMenu.getInstance().UpdateMenu();
        GitRepositoryStatusPanel.getInstance().UpdateMenu();
    }
}
