package gui.filepanel.component.gitmenu;

import gui.filepanel.FilePanel;

import javax.swing.*;

import gui.filepanel.PanelRefreshUtil;
import org.eclipse.jgit.api.errors.GitAPIException;

import file.SelectedFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import jgitmanager.*;

public class modified_fileGitMenu extends JPanel {//modified파일 패널
    public modified_fileGitMenu(){
        //setPreferredSize(new Dimension(300, 150));
        //setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel titleLabel=new JLabel("MODIFIED");
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel);

        modified_add_button add_button=new modified_add_button();//Add button 추가
        add_button.setBounds(0, 200,100,30);
        add(add_button);

        modified_undo_button undo_button=new modified_undo_button();//undo button 추가
        undo_button.setBounds(0, 200,100,30);
        add(undo_button);
    }
}

//modified button
class modified_add_button extends JButton{

    modified_add_button(){
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
                FilePanel.gui.repaint();
            }
        });
    }
}

class modified_undo_button extends JButton{

    modified_undo_button(){
        setText("UNDO");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    JGitManager.gitRestore(SelectedFile.getInstance().getFile());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (GitAPIException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                PanelRefreshUtil.refreshAll();
                FilePanel.gui.repaint();
            }
        });
    }
}