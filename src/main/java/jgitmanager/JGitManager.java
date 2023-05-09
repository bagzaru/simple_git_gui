package jgitmanager;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.api.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.HashSet;

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
    
    
    /*
     * ------------------------------------------------------------------------------
     * ------------------------------min-jp_part-------------------------------------
     * ------------------------------------------------------------------------------
     * */
    
    // gitAdd:
    // staged area로 올림
    // success: 1 / fail: 0
    public int gitAdd(File fileToRestore, File dotGit) {
        try {
            // Git 저장소 열기
            Repository repository = openRepository(dotGit);
            Git git = new Git(repository);
            
            //파일 경로
            String relativeFilePath;
            relativeFilePath = extractRepositoryRelativePath(fileToRestore, repository);
            
            // 파일 추가
            AddCommand add = git.add();
            add.addFilepattern(relativeFilePath).call();
            
            // Git 저장소 닫기
            git.close();
            
            System.out.println("add success");

            // add 성공
            return 1;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            // 예외 발생
            return 0;
        }
    }
    
    // gitDoCommit:
    // commit을 실행함
    // success: 1 / fail: 0
    public int gitDoCommit(File fileToRestore, File dotGit, String commitMessage) {
        try {
            // Git 저장소 열기
            Repository repository = openRepository(dotGit);
            Git git = new Git(repository);

            // 파일 경로
            String relativeFilePath;
            relativeFilePath = extractRepositoryRelativePath(fileToRestore, repository);

            // commit 실행
            CommitCommand commit = git.commit();
            commit.setMessage(commitMessage).call();

            // Git 저장소 닫기
            git.close();

            System.out.println("commit success");

            // commit 성공
            return 1;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            // 예외 발생
            return 0;
        }
    }

    // checkFileStatus:
    // 파일의 상태 확인
    // fail: 0 / Untracked: 1 / Modified : 2 / Deleted : 3 / Unmodified : 4 / status not found : 5
    public int gitCheckFileStatus(File fileToCheck, File dotGit) {
        try {
            // Git 저장소 열기
        	Repository repository = openRepository(dotGit);
            Git git = new Git(repository);
            
            // 파일 경로
            String relativeFilePath;
            relativeFilePath = extractRepositoryRelativePath(fileToCheck, repository);
            
            // 상태 확인
            Status status = git.status().addPath(relativeFilePath).call();
            
            int returnValue = 0;

            // 파일의 상태 확인
            if (status.getUntracked().contains(relativeFilePath)) {
                System.out.println("Untracked file");
                returnValue = 1;
            } else if (status.getModified().contains(relativeFilePath)) {
                System.out.println("Modified file");
                returnValue = 2;
            } else if (status.getMissing().contains(relativeFilePath)) {
                System.out.println("Deleted file");
                returnValue = 3;
            } else if (status.getAdded().contains(relativeFilePath) || status.getChanged().contains(relativeFilePath)) {
                System.out.println("Staged file");
                returnValue = 4;
            } else {
                System.out.println("Unmodified file");
                returnValue = 5;
            }
            
            // Git 저장소 닫기
            git.close();

            return returnValue;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            // 예외 발생
            return 0;
        }
    }

    // gitStagedList:
    // Staged된 파일 리스트
    // fail: null / success : Set<String> 
    public Set<String> gitStagedList(File dotGit) {
        try {
            // Git 저장소 열기
        	Repository repository = openRepository(dotGit);
            Git git = new Git(repository);
            
            // 상태 확인
            Status status = git.status().call();
            
            // Staged File 리스트
            Set<String> stagedAdded = status.getAdded();
            Set<String> stagedChanged = status.getChanged();
            Set<String> stagedFiles = new HashSet<>();
            stagedFiles.addAll(stagedAdded);
            stagedFiles.addAll(stagedChanged);

            // Git 저장소 닫기
            git.close();

            return stagedFiles;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    // findGitRepository:
    // 폴더의 상태를 확인
    // managed by git: 1 / not managed by git: 0
    public int findGitRepository(File folder) {
        // Git 경로 찾기
            FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
            repositoryBuilder.setMustExist(true);
            repositoryBuilder.findGitDir(folder);

            if (repositoryBuilder.getGitDir() != null) {
                // managed by git
                return 1;
            } else {
                // not managed by git
                return 0;
            }
    }

}
