package gitmenu;

import gui.GitGUI;

import javax.swing.*;

import file.SelectedFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jgitmanager.*;

public class commited_fileGitMenu extends JPanel{
    public commited_fileGitMenu(){
        setPreferredSize(new Dimension(300, 150));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
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
                //gitRmCached(SelectedFile.getInstance().getFile());
                GitGUI.gui.repaint();
            }
        });
    }
}

class commit_delete_button extends JButton{

    commit_delete_button(){

        setText("COMMIT_DELETE");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                
                //gitRm(SelectedFile.getInstance().getFile());
                GitGUI.gui.repaint();
            }
        });
    }
}

class commit_rename_button extends JButton{

    commit_rename_button(){

        setText("COMMIT_RENAME");

        message_box newName=new message_box();
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //String newName=newName.show_message_dialog();//->입력받은 새로운 이름
                //gitMv(SelectedFile.getInstance().getFile(),newName);
                GitGUI.gui.repaint();
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