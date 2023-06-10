package gui.filemanager.component.gitmenu;

import gui.filemanager.FileManager;

import javax.swing.*;

import gui.filemanager.PanelRefreshUtil;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import jgitmanager.*;

import file.SelectedFile;

//패널 클래스
public class untracked_fileGitMenu extends JPanel {//untracked파일 패널
    public untracked_fileGitMenu(){
        //setPreferredSize(new Dimension(300, 150));
        //setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel titleLabel=new JLabel("UNTRACKED");
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel);

        untracked_add_button add_button=new untracked_add_button();//Add button 추가
        add_button.setBounds(10, 50,100,30);
        add(add_button);
    }

}

//Untracked button
class untracked_add_button extends JButton{

    untracked_add_button(){
        setText("ADD");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    JGitManager.gitAdd(SelectedFile.getInstance().getFile());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (GitAPIException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                PanelRefreshUtil.refreshAll();
                FileManager.gui.repaint();
            }
        });
    }
}