package gui.filepanel.component;

import file.SelectedFile;
import gui.filepanel.component.gitmenu.commited_fileGitMenu;
import gui.filepanel.component.gitmenu.modified_fileGitMenu;
import gui.filepanel.component.gitmenu.staged_fileGitMenu;
import gui.filepanel.component.gitmenu.untracked_fileGitMenu;
import jgitmanager.*;

import javax.swing.*;
import java.awt.*;

//GitFilePanel: 우측 깃 패널 중 위쪽에 나오는 버튼 관련 패널

public class GitFilePanel extends JPanel {

    private static GitFilePanel instance;
    public static GitFilePanel getInstance(){
        if(instance==null){
            instance = new GitFilePanel();
        }
        return instance;
    }

    public GitFilePanel() {
        UpdatePanel();
    }

    public void UpdatePanel() {
        removeAll();
        setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        setLayout(new GridLayout(1,1));
        if (JGitManager.findGitRepository(SelectedFile.getInstance().getFile()) == 1) {
            switch (SelectedFile.getInstance().getGitStatus()) {
                case UNTRACKED:
                    add(new untracked_fileGitMenu());
                    break;
                case MODIFIED:
                    add(new modified_fileGitMenu());
                    break;
                case STAGED:
                    add(new staged_fileGitMenu());
                    break;
                case COMMITTED:
                    add(new commited_fileGitMenu());
                    break;
                default:
                    break;
            }
        }
/*
        switch(SelectedFile.getInstance().getGitStatus()){
            case 0 :
                add(new modified_fileGitMenu());
                break;

            case 1 :
                add(new staged_fileGitMenu());
                break;

            default:
                add(new untracked_fileGitMenu());
                break;
        }
*/
        revalidate();
        repaint();

    }
}