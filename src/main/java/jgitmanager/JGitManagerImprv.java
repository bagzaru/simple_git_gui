package jgitmanager;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

import static jgitmanager.JGitManager.openRepositoryFromFile;

public class JGitManagerImprv {
    // gitClone
    // git clone을 실행
    public static void gitClone(File fileToClone, String cloneURL, String ID, String accessToken) throws GitAPIException {
        try {
            // ID와 access token 객체 생성
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(ID, accessToken);

            //git clone
            Git.cloneRepository()
                    .setURI(cloneURL)
                    .setDirectory(fileToClone)
                    .setCredentialsProvider(credentialsProvider)
                    .call();

            System.out.println("clone success");
        } catch (GitAPIException e) {
            System.out.println("clone fail");

            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // gitCreateBranch
    // git branch <branchName> 실행
    public static void gitCreateBranch(File nowDir, String branchName) throws GitAPIException, IOException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // branch 생성
            git.branchCreate()
                    .setName(branchName)
                    .call();

            // Git 저장소 닫기
            git.close();

            System.out.println("branch create success");
        } catch (GitAPIException | IOException e) {
            System.out.println("branch create fail");

            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // gitDeleteBranch
    // git branch -d <branchName> 실행
    public static void gitDeleteBranch(File nowDir, String branchName) throws GitAPIException, IOException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // branch 삭제
            git.branchDelete()
                    .setBranchNames(branchName)
                    .call();

            // Git 저장소 닫기
            git.close();

            System.out.println("branch delete success");
        } catch (GitAPIException | IOException e) {
            System.out.println("branch delete fail");

            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // gitRenameBranch
    // git branch -m <oldName> <newName>
    public static void gitRenameBranch(File nowDir, String oldName, String newName) throws GitAPIException, IOException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // branch rename
            git.branchRename()
                    .setOldName(oldName)
                    .setNewName(newName)
                    .call();

            // Git 저장소 닫기
            git.close();

            System.out.println("branch rename success");
        } catch (GitAPIException | IOException e) {
            System.out.println("branch rename fail");

            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // git checkout <targetBranch>
    // git merge <sourceBranch>
    public static void gitMerge(File nowDir, String sourceBranch, String targetBranch) throws GitAPIException, IOException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // 병합을 위해 타겟 브랜치로 체크아웃
            git.checkout()
                    .setName(targetBranch)
                    .call();

            // 소스 브랜치를 타겟 브랜치로 병합
            git.merge()
                    .include(repository.findRef(sourceBranch))
                    .setFastForward(MergeCommand.FastForwardMode.FF)
                    .call();

            // Git 저장소 닫기
            git.close();

            System.out.println("merge success");
        } catch (GitAPIException | IOException e) {
            System.out.println("merge fail");

            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    public static void gitCheckout(File nowDir, String branchName) throws GitAPIException, IOException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // checkout
            git.checkout()
                    .setName(branchName)
                    .call();

            // Git 저장소 닫기
            git.close();

            System.out.println("merge success");
        } catch (GitAPIException | IOException e) {
            System.out.println("merge fail");

            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    public static void gitCurrentBranch() {

    }

    public static void gitClickBranch() {

    }

    public static void gitCommitInfo() {

    }

    public static void gitChangedFileList() {

    }

    public static void gitDiff() {

    }
}
