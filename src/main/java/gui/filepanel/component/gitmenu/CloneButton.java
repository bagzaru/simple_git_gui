package gui.filepanel.component.gitmenu;

import file.SelectedFile;
import gui.filepanel.PanelRefreshUtil;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CloneButton extends JButton {
    public CloneButton(){
        this.setText("CLONE");
         CloneUrlMessageBox CloneRepoUrl=new CloneUrlMessageBox();

         this.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                String CloneUrl =new CloneUrlMessageBox().showCloneUrldialog();
                 try {
                     JGitManagerImprv.gitClone(SelectedFile.getInstance().getFile(), CloneUrl);
                 } catch (GitAPIException ex) {
                     throw new RuntimeException(ex);
                 } catch (IOException ex) {
                     throw new RuntimeException(ex);
                 }
                 PanelRefreshUtil.refreshAll();
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
