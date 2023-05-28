package jgitmanager;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

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

            System.out.println("Clone Success");
        } catch (GitAPIException e) {
            System.out.println("Clone Fail");
            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    public static void gitMerge() {

    }

    public static void gitCreateBranch() {

    }

    public static void gitDeleteBranch() {

    }

    public static void gitRenameBranch() {

    }

    public static void gitCheckout() {

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
