package gui.branchpanel.component.branch.button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GitBranchCreateButton extends JButton {
    public GitBranchCreateButton(){
        setText("+");
        MessageBox CreateBranchName=new MessageBox();//messageBox 브랜치 이름
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BranchName=CreateBranchName.ShowMessageBox();

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
