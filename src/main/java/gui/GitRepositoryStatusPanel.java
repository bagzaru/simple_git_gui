package gui;

import file.SelectedFile;
import jgitmanager.JGitManager;
import org.eclipse.jgit.lib.Repository;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GitRepositoryStatusPanel extends JPanel {
    private static GitRepositoryStatusPanel instance;
    public static GitRepositoryStatusPanel getInstance(){
        if(instance==null){
            instance = new GitRepositoryStatusPanel();
        }
        return instance;
    }

    GitRepositoryStatusPanel(){
        UpdateMenu();
    }

    public void UpdateMenu(){
        removeAll();
        //Git repo의 위치
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //test
        //setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        //add(Box.createHorizontalGlue());
        String gitRepoText = "";
        String gitRelativeText = "";
        try {
            gitRepoText += "Git repository: ";
            //String gitRepoPath = JGitManager.findGitRepositoryPath(SelectedFile.getInstance().getFile());
            String gitRepoPath = JGitManager.findGitRepositoryName(SelectedFile.getInstance().getFile());
            if(gitRepoPath.equals("")){
                gitRepoText="현재 디렉토리는 Git repository가 아닙니다.";
            }else{
                gitRepoText+=gitRepoPath;
                gitRelativeText += "상대 경로: ";
                gitRelativeText += "/";
                gitRelativeText += JGitManager.findGitRepositoryRelativePath(SelectedFile.getInstance().getFile());
            }
        }catch (Exception e){
            System.out.println("Git repo 위치 탐색 중 오류 발생");
        }
        JLabel gitRepoJLabel = new JLabel(gitRepoText);
        gitRepoJLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel gitRelativeJLabel = new JLabel(gitRelativeText);
        gitRelativeJLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(gitRepoJLabel, BorderLayout.NORTH);
        add(gitRelativeJLabel,BorderLayout.CENTER);
    }



}
