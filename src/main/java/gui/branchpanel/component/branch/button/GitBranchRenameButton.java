package gui.branchpanel.component.branch.button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GitBranchRenameButton extends JButton {
    public GitBranchRenameButton(){
        setText("R");
        MessageBox RenameBranchName=new MessageBox();//messageBox new branch name 추가 예정
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BranchName=RenameBranchName.ShowMessageBox();
            }
        });
    }
}

class MessageBox extends JOptionPane{

    MessageBox(){

    }

    String ShowMessageBox(){
        return showInputDialog("NEW NAME을 입력하세요");
    }
}
