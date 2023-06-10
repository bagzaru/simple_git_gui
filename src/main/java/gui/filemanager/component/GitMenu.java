package gui.filemanager.component;

import javax.swing.*;

import gui.filemanager.FileManager;
import eventmanager.PanelRefreshUtil;
import gui.filemanager.component.gitmenu.CloneButton;
import org.eclipse.jgit.api.errors.GitAPIException;

import datamodel.SelectedFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import jgitmanager.*;

public class GitMenu extends JPanel {//커밋 같은 일반적인 깃 버튼을 위한 패널

    private static GitMenu instance;
    public static GitMenu getInstance(){
        if(instance==null){
            instance = new GitMenu();
        }
        return instance;
    }

    GitMenu(){
        UpdateMenu();
    }

    public void UpdateMenu(){
        removeAll();
        //setPreferredSize(new Dimension(300, 200));

        //test
        //setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
       // add(Box.createHorizontalGlue());

        //테두리 표시
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //git repo가 아닐 때만 init합니다.
        File selected = SelectedFile.getInstance().getFile();
        if(JGitManager.findGitRepository(selected)!=0){
            commit_button commit_button=new commit_button();//commit button 추가
            commit_button.setBounds(0, 100, 100, 30);
            add(commit_button);


        }
        else{
            //git repo가 아닌 경우
            init_button init_button=new init_button();//init button 추가
            init_button.setBounds(0, 0, 100, 30);
            add(init_button);
            CloneButton CloneButton =new CloneButton();
            CloneButton.setBounds(0,50,100,30);
            add(CloneButton);
        }


    }
}


//commit button
class commit_button extends JButton{

    commit_button(){
        setText("COMMIT");

        message_box commit_message_box=new message_box();

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String commitMessage=commit_message_box.show_message_dialog();//->입력받은 커밋 스트링

                if(commitMessage==null){
                    System.out.println("commit canceled");
                    return;
                }
                try {
                    JGitManager.gitDoCommit(SelectedFile.getInstance().getFile(),commitMessage);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (GitAPIException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }//->커밋 함수
                PanelRefreshUtil.refreshAll();
                FileManager.gui.repaint();
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
                try {
                    JGitManager.gitInit(SelectedFile.getInstance().getFile());
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
        return showInputDialog("COMMIT MESSAGE를 입력하세요");
    }
}