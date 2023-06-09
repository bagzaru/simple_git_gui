package gui.filemanager.component.gitmenu;

import gui.filemanager.FileManager;

import javax.swing.*;

import eventmanager.PanelRefreshUtil;
import org.eclipse.jgit.api.errors.GitAPIException;

import datamodel.SelectedFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import jgitmanager.*;

public class staged_fileGitMenu extends JPanel {//staged파일 패널
    public staged_fileGitMenu(){
        //setPreferredSize(new Dimension(300, 150));
        //setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel titleLabel=new JLabel("STAGED");
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel);

        staged_unstage_button unstage_button=new staged_unstage_button();//unstage button 추가
        unstage_button.setBounds(0, 200,100,30);
        add(unstage_button);
    }
}

//staged button
class staged_unstage_button extends JButton{

    staged_unstage_button(){
        setText("UNSTAGE");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    JGitManager.gitRestoreStaged(SelectedFile.getInstance().getFile());
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