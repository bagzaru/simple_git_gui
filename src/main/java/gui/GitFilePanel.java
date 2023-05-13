package gui;

import file.SelectedFile;
import gitmenu.modified_fileGitMenu;
import gitmenu.staged_fileGitMenu;
import gitmenu.untracked_fileGitMenu;
import jgitmanager.*;

import javax.swing.*;

public class GitFilePanel extends JPanel {
    public GitFilePanel(){
        removeAll();
        if(JGitManager.findGitRepository(SelectedFile.getInstance().getFile())==1){
        switch(SelectedFile.getInstance().getGitStatus()){
            case FOLDER :
                break;
            case UNTRACKED :
                add(new untracked_fileGitMenu());
                break;
            case MODIFIED :
                add(new modified_fileGitMenu());
                break;
            case STAGED_MODIFIED :
                add(new modified_fileGitMenu());
                break;
            case STAGED :
                add(new staged_fileGitMenu());
                break;
            case UNMODIFIED :
                add(new staged_fileGitMenu());
                break;
            case DELETED :
                break;     
            default :
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