package gui.branchpanel.component.branch.button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GitBranchCreateButton extends JButton {
    GitBranchCreateButton(){
        setText("+");
        //messageBox 브랜치 이름
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
