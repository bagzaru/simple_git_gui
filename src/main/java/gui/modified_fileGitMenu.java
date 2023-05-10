package gui;

import javax.swing.*;
import java.awt.*;

public class modified_fileGitMenu extends JPanel {//modified파일 패널
    modified_fileGitMenu(){
        setPreferredSize(new Dimension(300, 150));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
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
    }
}

class modified_undo_button extends JButton{

    modified_undo_button(){
        setText("UNDO");
    }
}