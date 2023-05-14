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
    public static void gitInit(File path) throws GitAPIException {
        // 디렉토리 생성 및 git init
        try (Git git = Git.init().setDirectory(path).call()) {
        }
    }

    //gitRestore:
    // modified 상태인 커밋을 가장 최근 상태로 되돌립니다.
    public static void gitRestore(File fileToRestore) throws IOException, GitAPIException, NullPointerException {
        //JGit doesn't have git restore method. So we used 'git checkout <filename>', ref to our OSS class Lecture5-part3

        //git checkout <filename>의 filename은 상대경로이므로, 상대 경로를 추출합니다.
        Repository repository = openRepositoryFromFile(fileToRestore);
        if (repository == null) {
            throw new NullPointerException("failed to open Repository from the file or directory");
        }
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

    //Modified 상태인 특정 file을 Unmodified로 되돌립니다.
    //fileToRestore
    //ref: https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/RevertChanges.java
    //ref: https://www.tabnine.com/code/java/methods/org.eclipse.jgit.api.Git/reset
    public static void gitRestoreStaged(File fileToRestore) throws IOException, GitAPIException, NullPointerException {
        //JGit doesn't have git restore method. So we used 'git reset HEAD <filename>', ref to our OSS class Lecture5-part3
        Repository repository = openRepositoryFromFile(fileToRestore);
        if (repository == null) {
            throw new NullPointerException("failed to open Repository from the file or directory");
        }
        //git reset HEAD <filename>의 filename은 상대경로이므로, 상대 경로를 추출합니다.
        String relativeFilePath;
        try {
            relativeFilePath = extractRepositoryRelativePath(fileToRestore, repository);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to 'git restore --staged " + fileToRestore.getPath() + " : invalid file path");
        }

        //git reset HEAD <staged file>

        try (Git git = new Git(repository)) {
            try {
                git.reset().setRef("HEAD").addPath(relativeFilePath).call();
            } catch (Exception e) {
                //HEAD가 없으면 오류가 난다.
                git.reset().addPath(relativeFilePath).call();
                System.out.println("HEAD 없다!!");
            }
        }


    }

    //git mv:
    // git mv A B를 실행합니다.
    // 이름을 변경할 때 사용할 수 있습니다.
    public static void gitMv(File fileToRename, String newName) throws IOException, GitAPIException, NullPointerException {
        //jgit api에서는 git mv를 지원하지 않습니다. 따라서 mv, git add, git rm으로 대체합니다.
        //git mv <oldname> <newname>은, mv oldname newname, git add newname, git rm oldname과 같습니다.
        //  source: https://stackoverflow.com/questions/1094269/whats-the-purpose-of-git-mv
        Repository repository = openRepositoryFromFile(fileToRename);
        if (repository == null) {
            throw new NullPointerException("failed to open Repository from the file or directory");
        }
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
        try (Git git = new Git(repository)) {
            git.add().addFilepattern(relativeNewFilePath).call();
            git.rm().addFilepattern(relativeOldFilePath).call();

        }


    }

    //git rm:
    // git rm <filename>을 실행합니다.
    //파일 삭제 및 삭제 사항을 stage합니다.
    public static void gitRm(File fileToRemove) throws IOException, GitAPIException, NullPointerException {
        Repository repository = openRepositoryFromFile(fileToRemove);
        if (repository == null) {
            throw new NullPointerException("failed to open Repository from the file or directory");
        }
        //git rm을 실행하기 위한 repository부터의 상대경로를 구합니다.
        String relativeFilePath;
        try {
            relativeFilePath = extractRepositoryRelativePath(fileToRemove, repository);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to 'git mv: invalid file path");
        }

        // git add newName, git rm oldName
        try (Git git = new Git(repository)) {
            git.rm().addFilepattern(relativeFilePath).call();
        }

    }

    //git rm --cached:
    // git rm --cached <filename>을 실행합니다.
    // 삭제 사항을 stage합니다.
    public static void gitRmCached(File fileToRemove) throws IOException, GitAPIException, NullPointerException {
        Repository repository = openRepositoryFromFile(fileToRemove);
        if (repository == null) {
            throw new NullPointerException("failed to open Repository from the file or directory");
        }
        //git rm --cached을 실행하기 위한 repository부터의 상대경로를 구합니다.
        String relativeFilePath;
        try {
            relativeFilePath = extractRepositoryRelativePath(fileToRemove, repository);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to 'git mv: invalid file path");
        }

        // git rm --cached
        try (Git git = new Git(repository)) {
            git.rm().setCached(true).addFilepattern(relativeFilePath).call();

        }

    }

    //openRepository:
    // 현재 열려있는 git repo를 가져옵니다.
    // .git까지의 경로를 가지는 file을 받아옵니다.
//    public Repository openRepository(File dotGit) throws IOException {
//        // now open the resulting repository with a FileRepositoryBuilder
//        FileRepositoryBuilder builder = new FileRepositoryBuilder();
//        try (Repository repository = builder.setGitDir(dotGit)
//                .readEnvironment() // scan environment GIT_* variables
//                .findGitDir() // scan up the file system tree
//                .build()) {
//            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
//            Ref head = repository.exactRef("refs/heads/master");
//
//            return repository;
//        }
//    }

    //특정 파일을 관리하는 repository를 open합니다.
    public static Repository openRepositoryFromFile(File file) throws IOException {
        // now open the resulting repository with a FileRepositoryBuilder
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        builder.findGitDir(file);
        if (builder.getGitDir() == null) {
            return null;
        }
        try (Repository repository =
                     builder.setGitDir(builder.getGitDir())
                             .readEnvironment()
                             .findGitDir()
                             .build()) // scan up the file system tree
        {
            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            Ref head = repository.exactRef("refs/heads/master");

            return repository;
        }
    }

    //createTempPath:
    // 테스트용 임시 경로를 생성합니다.
    // C:\Users\%AppData%\Temp\에 생성됩니다.
    public static File createTempPath() throws IOException {
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
    static String extractRepositoryRelativePath(File target, Repository repository) throws IllegalArgumentException {
        Path targetPath = target.toPath();
        Path workTreePath = repository.getWorkTree().toPath();
        try {
            String relativePath = workTreePath.relativize(targetPath).toString().replace('\\', '/');
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
    public static void gitAdd(File fileToAdd) throws IOException, GitAPIException, NullPointerException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(fileToAdd);
            Git git = new Git(repository);

            //파일 경로
            String relativeFilePath;
            relativeFilePath = extractRepositoryRelativePath(fileToAdd, repository);

            // 파일 추가
            AddCommand add = git.add();
            add.addFilepattern(relativeFilePath).call();

            // Git 저장소 닫기
            git.close();

            System.out.println("add success");
        } catch (IOException | GitAPIException | NullPointerException e) {
            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // gitDoCommit:
    // commit을 실행함
    // success: 1 / fail: 0
    public static void gitDoCommit(File dir, String commitMessage) throws IOException, GitAPIException, NullPointerException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(dir);
            if (repository == null) {
                throw new NullPointerException("failed to open Repository from the file or directory");
            }
            Git git = new Git(repository);

            // 파일 경로
            String relativeFilePath;
            relativeFilePath = extractRepositoryRelativePath(dir, repository);

            // commit 실행
            CommitCommand commit = git.commit();
            commit.setMessage(commitMessage).call();

            // Git 저장소 닫기
            git.close();

            System.out.println("commit success");
        } catch (IOException | GitAPIException | NullPointerException e) {
            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // checkFileStatus:
    // 파일의 상태 확인
    public static FileStatus gitCheckFileStatus(File fileToCheck) throws IOException, GitAPIException, NullPointerException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(fileToCheck);
            if (repository == null) {
                throw new NullPointerException("failed to open Repository from the file or directory");
            }
            Git git = new Git(repository);

            // 파일 경로
            String relativeFilePath;
            relativeFilePath = extractRepositoryRelativePath(fileToCheck, repository);

            // 상태 확인
            Status status = git.status().addPath(relativeFilePath).call();

            // 파일의 상태 확인
            FileStatus returnValue;
            if (status.getUntracked().contains(relativeFilePath)) {
                returnValue = FileStatus.UNTRACKED;
            } else if (status.getModified().contains(relativeFilePath)) {
                returnValue = FileStatus.MODIFIED;
            } else if (status.getAdded().contains(relativeFilePath) || status.getChanged().contains(relativeFilePath)) {
                returnValue = FileStatus.STAGED;
            } else {
                returnValue = FileStatus.COMMITTED;
            }

            // Git 저장소 닫기
            git.close();

            return returnValue;
        } catch (IOException | GitAPIException | NullPointerException e) {
            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    public static StagedFileStatus gitCheckStagedFileStatus(File fileToCheck) throws IOException, GitAPIException, NullPointerException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(fileToCheck);
            if (repository == null) {
                throw new NullPointerException("failed to open Repository from the file or directory");
            }
            Git git = new Git(repository);

            // 파일 경로
            String relativeFilePath;
            relativeFilePath = extractRepositoryRelativePath(fileToCheck, repository);

            // 상태 확인
            Status status = git.status().addPath(relativeFilePath).call();

            StagedFileStatus returnValue;

            if (status.getAdded().contains(relativeFilePath)
                    || status.getChanged().contains(relativeFilePath)
                    || status.getRemoved().contains(relativeFilePath)
            ) {
                if (status.getModified().contains(relativeFilePath)
                        || status.getMissing().contains(relativeFilePath)) {
                    returnValue = StagedFileStatus.STAGED_MODIFIED;
                } else if (status.getRemoved().contains(relativeFilePath)) {
                    //만약 removed 상태관련 오류난다면 여기가 유력함 잘 봐주세요
                    returnValue = StagedFileStatus.REMOVED;
                } else {
                    returnValue = StagedFileStatus.STAGED;
                }
            } else if (status.getMissing().contains(relativeFilePath)) {
                returnValue = StagedFileStatus.REMOVED;
            } else {
                returnValue = StagedFileStatus.NULL;
            }
            System.out.println(relativeFilePath + ": " + returnValue.toString());
            // Git 저장소 닫기
            git.close();

            return returnValue;
        } catch (IOException | GitAPIException | NullPointerException e) {
            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // gitStagedList:
    // Staged된 파일 리스트
    // return : Set<String> 
    public static Set<String> gitStagedList(File dir) throws IOException, GitAPIException, NullPointerException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(dir);
            if (repository == null) {
                throw new NullPointerException("failed to open Repository from the file or directory");
            }
            Git git = new Git(repository);

            // 상태 확인
            Status status = git.status().call();

            // Staged File 리스트
            Set<String> stagedAdded = status.getAdded();
            Set<String> stagedChanged = status.getChanged();
            Set<String> stagedRemoved = status.getRemoved();
            Set<String> stagedFiles = new HashSet<>();
            stagedFiles.addAll(stagedAdded);
            stagedFiles.addAll(stagedChanged);
            stagedFiles.addAll(stagedRemoved);

            // Git 저장소 닫기
            git.close();

            return stagedFiles;
        } catch (IOException | GitAPIException | NullPointerException e) {
            //e.printStackTrace();
            throw e;
        }
    }

    // findGitRepository:
    // 폴더의 상태를 확인
    // managed by git: 1 / not managed by git: 0
    public static int findGitRepository(File folder) {
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

    public static String findGitRepositoryName(File folder) {
        // Git 경로 찾기
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.findGitDir(folder);

        if (repositoryBuilder.getGitDir() != null) {
            // managed by git
            return repositoryBuilder.getGitDir().getParentFile().getName();
        } else {
            // not managed by git
            return "";
        }
    }

    public static String findGitRepositoryRelativePath(File folder) {
        // Git 경로 찾기
        try {
            Repository repo = openRepositoryFromFile(folder);
            if (repo == null) {
                return "";
            }
            return extractRepositoryRelativePath(folder, repo);

        } catch (Exception e) {
            return "";
        }

    }

    //file 또는 dir을 입력받아, 해당 repo를 관리하는 .git 파일의 절대경로를 반환합니다.
    //반환값은 "...//.git"이 됩니다.
    //해당 파일이 실제로 존재하지 않거나, 오류 발생 시 ""을 반환합니다.
    public static String getRepositoryAbsolutePath(File file) {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.findGitDir(file);

        if (repositoryBuilder.getGitDir() == null) {
            // not managed by git
            return "";
        } else {
            // managed by git
            return repositoryBuilder.getGitDir().getAbsolutePath();
        }
    }
}
