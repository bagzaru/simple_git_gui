package gui.filemanager;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

import file.SelectedFile;
import gui.filemanager.component.*;
import gui.filemanager.FileManager;

public class PanelRefreshUtil {
    /** class variable */
    public static DefaultMutableTreeNode lastTreeNode;
    public static File currentDirectory;

    public static void refreshAll(){
        refreshFileTree();
        refreshFileTable();
        refreshStagedList();
        refreshGitMenu();
        refreshGitFilePanel();
        refreshGitRepoStatusPanel();
        refreshTitle();
    }

    //좌측의 root부터 시작되는 파일 트리를 새로고칩니다.
    public static void refreshFileTree(){
        JTree tree = FileTree.getInstance().getTree();
        if(lastTreeNode!=null){
            FileTree.getInstance().showChildren(lastTreeNode);
        }
    }

    //중앙 상단의 현재 선택된 폴더의 파일들을 보여주는 테이블을 새로고칩니다.
    public static void refreshFileTable(){
        if(currentDirectory!=null){
            if (currentDirectory.isDirectory()) {
                File[] files = FileSystemView.getFileSystemView().getFiles(currentDirectory, true);
                FileTable.getInstance().setTableData(files);
            }
        }
        else{
            System.out.println("there is no selected dir");
        }
        FileManager.gui.repaint();
    }

    //중앙 하단의 현재 git repo의 staged list를 새로고칩니다.
    public static void refreshStagedList(){
        StagedFileList.getInstance().setStagedFileTableData();
        FileManager.gui.repaint();
    }

    //우측 상단의 현재 선택된 파일에 대한 깃 명령어 모음 패널을 새로고침합니다.
    public static void refreshGitFilePanel(){
        SelectedFile selectedFile = SelectedFile.getInstance();
        if(selectedFile!=null){
            File sfile = selectedFile.getFile();
            if(sfile!=null){
                GitFilePanel.getInstance().UpdatePanel();
            }
        }
    }
    public static void refreshGitRepoStatusPanel(){
        SelectedFile selectedFile = SelectedFile.getInstance();
        if(selectedFile!=null){
            File sfile = selectedFile.getFile();
            if(sfile!=null){
                GitRepositoryStatusPanel.getInstance().UpdateMenu();
            }
        }
    }

    //우측 하단의 현재 git repo에 대한 깃 명령어 모음 패널을 새로고침합니다.
    public static void refreshGitMenu(){
        SelectedFile selectedFile = SelectedFile.getInstance();
        if(selectedFile!=null){
            File sfile = selectedFile.getFile();
            if(sfile!=null){
                GitMenu.getInstance().UpdateMenu();
            }
        }
    }

    //파일, 폴더 클릭시 좌측 상단의 제목 옆 현재 파일, 폴더 이름 변경
    public static void refreshTitle() {
        JFrame f = (JFrame) FileManager.gui.getTopLevelAncestor();
        if (f!=null) {
            f.setTitle(
                    FileManager.APP_TITLE + " :: "
                            + FileSystemView.getFileSystemView().getSystemDisplayName(SelectedFile.getInstance().getFile()));
        }
    }
}
