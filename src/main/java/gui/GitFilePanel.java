package gui;

import file.SelectedFile;
import gitmenu.modified_fileGitMenu;
import gitmenu.staged_fileGitMenu;
import gitmenu.untracked_fileGitMenu;

import javax.swing.*;

public class GitFilePanel extends JPanel {
    public GitFilePanel(){
        removeAll();

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

        revalidate();
        repaint();
    }
}