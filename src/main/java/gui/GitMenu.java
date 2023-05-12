package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GitMenu extends JPanel {//커밋 같은 일반적인 깃 버튼을 위한 패널
    GitMenu(){
        setPreferredSize(new Dimension(300, 200));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        commit_delete_button delete_button=new commit_delete_button();//delete button 추가
        delete_button.setBounds(0, 200,100,30);
        add(delete_button);

        commit_untrack_button untrack_button=new commit_untrack_button();//untrack button 추가
        untrack_button.setBounds(0,150,100,30);
        add(untrack_button);

        commit_rename_button rename_button=new commit_rename_button();//rename button 추가
        rename_button.setBounds(0, 50,100,30);
        add(rename_button);

        commit_button commit_button=new commit_button();//commit button 추가
        commit_button.setBounds(0, 100, 100, 30);
        add(commit_button);

        init_button init_button=new init_button();//init button 추가
        init_button.setBounds(0, 0, 100, 30);
        add(init_button);
    }
}

//commit status button
class commit_untrack_button extends JButton{

    commit_untrack_button(){

        setText("COMMIT_UNTRACK");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
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
                GitGUI.gui.repaint();
            }
        });
    }
}

class commit_rename_button extends JButton{

    commit_rename_button(){

        setText("COMMIT_RENAME");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                GitGUI.gui.repaint();
            }
        });
    }
}
//commit button
class commit_button extends JButton{

    commit_button(){
        setText("COMMIT");

        commit_message_box commit_message_box=new commit_message_box();

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //String commitMessage=commit_message_box.show_commit_message_dialog();->입력받은 커밋 스트링
                //gitDoCommit(파일,깃파일,commitMessage);->커밋 함수
                commit_message_box.show_commit_message_dialog();
                GitGUI.gui.repaint();
            }
        });
    }
}
//INIT button
class init_button extends JButton{

    init_button(){
        setText("INIT");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                GitGUI.gui.repaint();
            }
        });
    }
}

//message box class
class commit_message_box extends JOptionPane{

    commit_message_box(){

    }

    String show_commit_message_dialog(){
        return showInputDialog("COMMIT MESSAGE를 입력하세요");
    }
}