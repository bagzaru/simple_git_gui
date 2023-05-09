package jgitmanager;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

//JGit의 기능을 직접 사용하여 Git init, add, ... 등으로 단순화하는 클래스입니다.
//git의 특정 명령어를 직접 사용하려면 이 클래스를 사용합니다.
public class JGitManager {
    //path에 git-Init을 실행합니다.
    public void gitInit(File path) throws GitAPIException {
        // 디렉토리 생성 및 git init
        try(Git git = Git.init().setDirectory(path).call()){}
    }

    //gitRestore:
    // modified 상태인 커밋을 가장 최근 상태로 되돌립니다.
    public void gitRestore(File fileToRestore, File dotGit) throws IOException, GitAPIException {
        //JGit doesn't have git restore method. So we used 'git checkout <filename>', ref to our OSS class Lecture5-part3
        try (Repository repository = openRepository(dotGit)) {
            //git checkout <filename>의 filename은 상대경로이므로, 상대 경로를 추출합니다.
            String relativeFilePath;
            try {
                relativeFilePath = extractRepositoryRelativePath(fileToRestore, repository);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to 'git restore " + fileToRestore.getPath() + " : invalid file path");
            }

            //실제 Git 명령어를 수행합니다.
            // git checkout <filename>
            try (Git git = new Git(repository)) {
                git.checkout().addPath(relativeFilePath).call();
            }
        }

    }

    //Modified 상태인 특정 file을 Unmodified로 되돌립니다.
    //fileToRestore
    //ref: https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/RevertChanges.java
    //ref: https://www.tabnine.com/code/java/methods/org.eclipse.jgit.api.Git/reset
    public void gitRestoreStaged(File fileToRestore, File dotGit) throws IOException, GitAPIException {
        //JGit doesn't have git restore method. So we used 'git reset HEAD <filename>', ref to our OSS class Lecture5-part3
        try (Repository repository = openRepository(dotGit)) {
            //git reset HEAD <filename>의 filename은 상대경로이므로, 상대 경로를 추출합니다.
            String relativeFilePath;
            try {
                relativeFilePath = extractRepositoryRelativePath(fileToRestore, repository);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to 'git restore --staged " + fileToRestore.getPath() + " : invalid file path");
            }

            //git reset HEAD <staged file>
            try (Git git = new Git(repository)) {
                git.reset().setRef("HEAD").addPath(relativeFilePath).call();
            }
        }
    }

    //git mv:
    // git mv A B를 실행합니다.
    // 이름을 변경할 때 사용할 수 있습니다.
    public void gitMv(File fileToRename, String newName, File dotGit) throws IOException, GitAPIException {
        //jgit api에서는 git mv를 지원하지 않습니다. 따라서 mv, git add, git rm으로 대체합니다.
        //git mv <oldname> <newname>은, mv oldname newname, git add newname, git rm oldname과 같습니다.
        //  source: https://stackoverflow.com/questions/1094269/whats-the-purpose-of-git-mv
        try (Repository repository = openRepository(dotGit)) {
            //mv <oldname> <newname>을 실행합니다.
            //JAVA File의 파일명 변경을 수행합니다.
            File newFile = new File(fileToRename.getParent() + "\\" + newName);
            if (!fileToRename.renameTo(newFile)) {
                throw new IOException("Failed to 'git mv, failed to Rename from " + fileToRename.getName() + " to " + newName);
            }

            //파일명 변경이 수행되었으니, 두 파일의 상대 경로를 구합니다.
            String relativeOldFilePath, relativeNewFilePath;
            try {
                relativeOldFilePath = extractRepositoryRelativePath(fileToRename, repository);
                relativeNewFilePath = extractRepositoryRelativePath(newFile, repository);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to 'git mv: invalid file path");
            }

            // git add newName, git rm oldName
            try (Git git = new Git(repository)){
                git.add().addFilepattern(relativeNewFilePath).call();
                git.rm().addFilepattern(relativeOldFilePath).call();
            }
        }

    }

    //git rm:
    // git rm <filename>을 실행합니다.
    //파일 삭제 및 삭제 사항을 stage합니다.
    public void gitRm(File fileToRemove, File dotGit) throws IOException, GitAPIException {
        try (Repository repository = openRepository(dotGit)) {
            //git rm을 실행하기 위한 repository부터의 상대경로를 구합니다.
            String relativeFilePath;
            try {
                relativeFilePath = extractRepositoryRelativePath(fileToRemove, repository);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to 'git mv: invalid file path");
            }

            // git add newName, git rm oldName
            try (Git git = new Git(repository)){
                git.rm().addFilepattern(relativeFilePath).call();
            }
        }

    }

    //openRepository:
    // 현재 열려있는 git repo를 가져옵니다.
    // .git까지의 경로를 가지는 file을 받아옵니다.
    public Repository openRepository(File dotGit) throws IOException {
        // now open the resulting repository with a FileRepositoryBuilder
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(dotGit)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()) {
            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            Ref head = repository.exactRef("refs/heads/master");

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
            throw new IOException("failed to delete temporary file.");
        }
        return localPath;
    }

    //extractRepositoryRelativePath:
    // repository의 worktree부터 target까지의 상대경로를 추출하여 반환합니다.
    // git 명령어에서는 파일의 workTree부터의 경로가 필요합니다. 해당 경로를 추출하는데 사용합니다.
    String extractRepositoryRelativePath(File target, Repository repository) throws IllegalArgumentException {
        Path targetPath = target.toPath();
        Path workTreePath = repository.getWorkTree().toPath();
        try {
            String relativePath = workTreePath.relativize(targetPath).toString().replace('\\','/');
            return relativePath;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get relative Exception");
        }
    }

}
