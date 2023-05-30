package gui.branchpanel.component.branch.button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GitBranchDeleteButton extends JButton {
    public GitBranchDeleteButton(){
        setText("-");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
