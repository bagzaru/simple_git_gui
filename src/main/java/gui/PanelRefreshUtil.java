package gui;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class PanelRefreshUtil {
    //좌측의 root부터 시작되는 파일 트리를 새로고칩니다.

    public static void refreshAll(){
        refreshFileTree();
        refreshFileTable();
        refreshGitMenu();
        refreshGitFilePanel();
        refreshStagedList();
    }

    public static void refreshFileTree(){
        //FileTree.UpdateTreeModel();
        //오류로 인해 아직 미구현
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
        GitGUI.gui.repaint();
    }

    //중앙 하단의 현재 git repo의 staged list를 새로고칩니다.
    public static void refreshStagedList(){
        //현재 구현되어있지 않음
    }

    //우측 상단의 현재 선택된 파일에 대한 깃 명령어 모음 패널을 새로고침합니다.
    public static void refreshGitFilePanel(){
        GitFilePanel.getInstance().UpdatePanel();
    }
    //우측 하단의 현재 git repo에 대한 깃 명령어 모음 패널을 새로고침합니다.
    public static void refreshGitMenu(){
        GitMenu.getInstance().UpdateMenu();
    }
}
