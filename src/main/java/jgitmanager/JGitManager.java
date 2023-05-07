package jgitmanager;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

//JGit의 기능을 직접 사용하여 Git init, add, ... 등으로 단순화하는 클래스입니다.
//git의 특정 명령어를 직접 사용하려면 이 클래스를 사용합니다.
public class JGitManager {
    //path에 git-Init을 실행합니다.
    public void gitInit(File path) {
        // 디렉토리 생성 및 git init
        try (Git git = Git.init().setDirectory(path).call()) {
            System.out.println("git init: Repository initialized: " + git.getRepository().getDirectory());
        } catch (GitAPIException e) {
            System.out.println("An error occurred while initializing git: " + e.toString());
        }
    }

    //gitRestore:
    // modified 상태인 커밋을 가장 최근 상태로 되돌립니다.
    public void gitRestore(File fileToRestore, File dotGit) throws IOException, GitAPIException{
        //JGit doesn't have git restore method. So we used 'git checkout <filename>', ref to our OSS class Lecture5-part3
        System.out.println("Start: gitRestore");
        try (Repository repository = openRepository(dotGit)) {
            //git checkout <filename>의 filename은 상대경로이므로, 상대 경로를 추출합니다.
            String relativeFilePath;
            try{
                relativeFilePath = extractRepositoryRelativePath(fileToRestore, repository);
            }catch (IllegalArgumentException e){
                System.out.println("Failed to 'git restore "+fileToRestore.getPath()+" : invalid file path");
                return;
            }

            //실제 Git 명령어를 수행합니다.
            try (Git git = new Git(repository)) {
                // revert the changes
                // git checkout <filename>
                System.out.println("try: git checkout "+relativeFilePath);
                git.checkout().addPath(relativeFilePath).call();
                System.out.println("End: gitRestore");
            }
        }

    }

    //Modified 상태인 특정 file을 Unmodified로 되돌립니다.
    //fileToRestore
    //ref: https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/RevertChanges.java
    //ref: https://www.tabnine.com/code/java/methods/org.eclipse.jgit.api.Git/reset
    public void gitRestoreStaged(File fileToRestore, File dotGit) throws IOException, GitAPIException{
        //JGit doesn't have git restore method. So we used 'git reset HEAD <filename>', ref to our OSS class Lecture5-part3
        System.out.println("Start: gitRestoreStaged ");
        try (Repository repository = openRepository(dotGit)) {
            //git reset HEAD <filename>의 filename은 상대경로이므로, 상대 경로를 추출합니다.
            String relativeFilePath;
            try{
                relativeFilePath = extractRepositoryRelativePath(fileToRestore, repository);
            }catch (IllegalArgumentException e){
                System.out.println("Failed to 'git restore --staged "+fileToRestore.getPath()+" : invalid file path");
                return;
            }

            try (Git git = new Git(repository)) {
                // revert the changes
                //git reset HEAD <staged file>

                System.out.println("try: git reset HEAD "+relativeFilePath);
                git.reset().setRef("HEAD").addPath(relativeFilePath).call();
                // git.checkout().addPath(fileName).call();

                System.out.println("End: gitRestoreStaged");
            }
        }
    }

    //git mv:
    // git mv A B를 실행합니다.
    // 이름을 변경할 때 사용할 수 있습니다.
    public void gitMv(File fileToRename, String newName, File dotGit) throws IOException, GitAPIException {
        //jgit api에서는 git mv를 지원하지 않습니다.
        //git mv <oldname> <newname>은, mv oldname newname, git add newname, git rm oldname과 같습니다.
        //  source: https://stackoverflow.com/questions/1094269/whats-the-purpose-of-git-mv

        System.out.println("Start: gitMv ");
        try (Repository repository = openRepository(dotGit)) {
            //fileToRename의 workTree부터의 상대경로를 추출합니다.
            File newFile = new File(repository.getWorkTree().toString()+"\\"+newName);
            System.out.println("newFIle.path: "+newFile.getPath());
            if(fileToRename.renameTo(newFile)){
                System.out.println("Rename completed, path: "+fileToRename.getPath()+", name: "+fileToRename.getName());
            }
            else{
                throw new IOException("Failed to 'git mv, failed to Rename from"+fileToRename.getName()+" to "+newName);
            }

            try (Git git = new Git(repository)) {
                // rename the file
                // git add newName
                // git rm oldName

                System.out.println("try: git mv "+newFile.getName()+" "+newFile.getName());
                git.add().addFilepattern(newFile.getName()).call();
                git.rm().addFilepattern(fileToRename.getName()).call();

                System.out.println("End: git mv "+fileToRename.getName()+" "+newFile.getName());
            }
        }

    }

    //openRepository:
    // 현재 열려있는 git repo를 가져옵니다.
    // .git까지의 경로를 가지는 file을 받아옵니다.
    public Repository openRepository(File dotGit) throws IOException {
        // now open the resulting repository with a FileRepositoryBuilder
        System.out.println("Start: try to open repository: "+dotGit.getPath());
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(dotGit)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()) {
            System.out.println("...Having repository: " + repository.getDirectory());

            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            Ref head = repository.exactRef("refs/heads/master");
            //System.out.println("...Ref of refs/heads/master: " + head);
            System.out.println("End: finished to get repository" + head);

            return repository;
        }
    }

    //createTempPath:
    // 테스트용 임시 경로를 생성합니다.
    // C:\Users\%AppData%\Temp\에 생성됩니다.
    public File createTempPath() throws IOException {
        //임시 파일 생성
        File localPath = File.createTempFile("TestGitRepo", "");
        //임시 파일 삭제, 경로만 남김
        if (!localPath.delete()) {
            throw new IOException("임시 파일 삭제 실패.");
        }
        return localPath;
    }

    //extractRepositoryRelativePath:
    // repository의 worktree부터 target까지의 상대경로를 추출하여 반환합니다.
    // git 명령어에서는 파일의 workTree부터의 경로가 필요합니다. 해당 경로를 추출하는데 사용합니다.
    String extractRepositoryRelativePath(File target, Repository repository) throws IllegalArgumentException {
        Path targetPath = target.toPath();
        Path workTreePath = repository.getWorkTree().toPath();
        try{
            Path relativePath = workTreePath.relativize(targetPath);
            return relativePath.toString();
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Failed to get relative Exception");
        }
    }

}
