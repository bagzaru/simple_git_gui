package gui.filemanager.component.gitmenu;

import gui.filemanager.FileManager;

import javax.swing.*;

import gui.filemanager.PanelRefreshUtil;
import org.eclipse.jgit.api.errors.GitAPIException;

import datamodel.SelectedFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import jgitmanager.*;

public class commited_fileGitMenu extends JPanel{
    public commited_fileGitMenu(){
        //setPreferredSize(new Dimension(300, 150));
        //setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel titleLabel=new JLabel("COMMITED");
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel);

        commit_delete_button delete_button=new commit_delete_button();//delete button 추가
        delete_button.setBounds(0, 200,100,30);
        add(delete_button);

        commit_untrack_button untrack_button=new commit_untrack_button();//untrack button 추가
        untrack_button.setBounds(0,150,100,30);
        add(untrack_button);

        commit_rename_button rename_button=new commit_rename_button();//rename button 추가
        rename_button.setBounds(0, 50,100,30);
        add(rename_button);
    }
    
}
//commit status button
class commit_untrack_button extends JButton{

    commit_untrack_button(){

        setText("COMMIT_UNTRACK");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                
                try {
                    JGitManager.gitRmCached(SelectedFile.getInstance().getFile());
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

class commit_delete_button extends JButton{

    commit_delete_button(){

        setText("COMMIT_DELETE");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                
                try {
                    JGitManager.gitRm(SelectedFile.getInstance().getFile());
                    SelectedFile.getInstance().setFile(SelectedFile.getInstance().getFile().getParentFile());

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

class commit_rename_button extends JButton{

    commit_rename_button(){

        setText("COMMIT_RENAME");

        message_box new_Name=new message_box();
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String newName = new_Name.show_message_dialog();//->입력받은 새로운 이름
                try {
                    JGitManager.gitMv(SelectedFile.getInstance().getFile(),newName);
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

//message box class
class message_box extends JOptionPane{

    message_box(){

    }

    String show_message_dialog(){
        return showInputDialog("NEW NAME을 입력하세요");
    }
}