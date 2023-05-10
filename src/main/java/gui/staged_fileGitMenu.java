package gui;

import javax.swing.*;
import java.awt.*;

public class staged_fileGitMenu extends JPanel {//staged파일 패널
    staged_fileGitMenu(){
        setPreferredSize(new Dimension(300, 150));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
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
    }
}