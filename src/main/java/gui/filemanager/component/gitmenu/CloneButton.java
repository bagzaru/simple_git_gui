package gui.filemanager.component.gitmenu;

import file.SelectedFile;
import gui.filemanager.PanelRefreshUtil;
import jgitmanager.JGitManagerImprv;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class CloneButton extends JButton {
    private JOptionPane ConflictBox;

    public CloneButton() {
        this.setText("CLONE");
        ConflictBox = new JOptionPane();


        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CloneUrl = new CloneUrlMessageBox().showCloneUrldialog();
                File targetDir = SelectedFile.getInstance().getFile();

                //사용자가 Cancel을 눌렀을 경우
                if(CloneUrl==null){
                    return;
                }


                //Git Clone이 다른 스레드에서 진행됨
                SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
                    @Override
                    protected Void doInBackground()  {
                        try {
                            JGitManagerImprv.gitClone(targetDir, CloneUrl);
                            System.out.println("Git Clone ended: "+CloneUrl);
                        } catch (GitAPIException | IOException ex) {
                            //실패 시 메시지박스를 띄웁니다.
                            JOptionPane.showMessageDialog(null,
                                    ex.toString(),"Git Clone failed", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Git Clone failed: "+CloneUrl);
                            return null;
                            //throw new RuntimeException(ex);
                        }
                        //성공 시 성공 메시지를 띄웁ㄴ디ㅏ.
                        JOptionPane.showMessageDialog(
                                null, "Git clone from ["+CloneUrl+"] on ["+targetDir.getAbsolutePath()+"] completed",
                                "Git Clone", JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }
                };
                worker.execute();
                //클론 완료 시 알려준다고 친절히 알려줌
                JOptionPane.showMessageDialog(
                        null,"When the git clone is ended, we will alert again.",
                        "Git clone", JOptionPane.INFORMATION_MESSAGE);


                PanelRefreshUtil.refreshAll();
            }
        });
    }
}

class CloneUrlMessageBox extends JOptionPane {

    CloneUrlMessageBox() {

    }

    String showCloneUrldialog() {
        return showInputDialog("원격 REPO URL입력하세요");
    }
}
