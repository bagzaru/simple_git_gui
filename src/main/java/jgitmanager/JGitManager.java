package jgitmanager;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

//JGit의 기능을 직접 사용하여 Git init, add, ... 등으로 단순화하는 클래스입니다.
//git의 특정 명령어를 직접 사용하려면 이 클래스를 사용합니다.
public class JGitManager {
    public static String testPath = "C:\\Users\\BAEKSE~1\\AppData\\Local\\Temp\\TestGitRepo7066676561043817265\\.git";
    public static void main(String args[]) {
        System.out.println("Hello World from JGitManager");
        JGitManager tester = new JGitManager();
        //tester.gitInitTest();
        try {
            Scanner sc = new Scanner(System.in);

            tester.openRepository(new File(testPath));

        }catch(Exception e){System.out.println(e.toString());}
    }

    public void gitInitTest(){
        File tempPath;
        try{
            tempPath = createTempPath();
        }
        catch(IOException e){
            System.out.println("Cannot create temp path: "+e.toString());
            return;
        }
        gitInit(tempPath);
    }

    //테스트용 임시 경로를 생성합니다.
    //C:\Users\%AppData%\Temp\에 생성됩니다.

    public File createTempPath() throws IOException{
        //임시 파일 생성
        File localPath = File.createTempFile("TestGitRepo","");
        //임시 파일 삭제, 경로만 남김
        if(!localPath.delete()){
            throw new IOException("임시 파일 삭제 실패.");
        }
        return localPath;
    }

    //path에 git-Init을 실행합니다.
    public boolean gitInit(File path) {
        // 디렉토리 생성 및 git init
        try (Git git = Git.init().setDirectory(path).call()) {
            System.out.println("Repository initialized: " + git.getRepository().getDirectory());
        }
        catch(GitAPIException e){
            System.out.println("An error occurred while initializing git: "+ e.toString());
        }

        //FileUtils.deleteDirectory(localPath);
        return true;
    }


    //현재 열려있는 git repo를 가져옵니다.
    //.git까지의 경로를 가지는 file을 받아옵니다.
    public Repository openRepository(File dotGit) throws IOException{
        // now open the resulting repository with a FileRepositoryBuilder
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(dotGit)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()) {
            System.out.println("Having repository: " + repository.getDirectory());

            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            Ref head = repository.exactRef("refs/heads/master");
            System.out.println("Ref of refs/heads/master: " + head);
            return repository;
        }
    }
}
