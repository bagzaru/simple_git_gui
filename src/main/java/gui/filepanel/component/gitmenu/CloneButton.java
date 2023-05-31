package gui.filepanel.component.gitmenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloneButton extends JButton {
    public CloneButton(){
        this.setText("CLONE");
         message_box CloneRepoUrl=new message_box();

         this.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                String CloneUrl =new message_box().show_message_dialog();
             }
         });
    }
}
