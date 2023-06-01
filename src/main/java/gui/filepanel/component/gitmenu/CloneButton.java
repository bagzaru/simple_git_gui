package gui.filepanel.component.gitmenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloneButton extends JButton {
    public CloneButton(){
        this.setText("CLONE");
         CloneUrlMessageBox CloneRepoUrl=new CloneUrlMessageBox();

         this.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                String CloneUrl =new CloneUrlMessageBox().showCloneUrldialog();
             }
         });
    }
}
class CloneUrlMessageBox extends JOptionPane{

    CloneUrlMessageBox(){

    }

    String showCloneUrldialog(){
        return showInputDialog("원격 REPO URL입력하세요");
    }
}
