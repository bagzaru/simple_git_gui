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

        setText("UNTRACK");
    }
}

class commit_delete_button extends JButton{

    commit_delete_button(){

        setText("DELETE");
    }
}

class commit_rename_button extends JButton{

    commit_rename_button(){

        setText("RENAME");
    }
}
//commit button
class commit_button extends JButton{

    commit_button(){
        setText("COMMIT");

        commit_message_box commit_message_box=new commit_message_box();

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                commit_message_box.showInputDialog("COMMIT MESSAGE를 입력하세요");
            }
        });
    }
}
//INIT button
class init_button extends JButton{

    init_button(){
        setText("INIT");
    }
}

//message box class
class commit_message_box extends JOptionPane{

    commit_message_box(){

    }
}