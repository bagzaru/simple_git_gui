package jgitmanager;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;

public class JGitManagerImprv {
    // ID와 access token을 저장하는 파일
    private static final String FILE_PATH = "credentials.txt";

    // gitClone
    // git clone을 실행
    public static void gitClone(File fileToClone, String cloneURL) throws GitAPIException, IOException {
        try {
            // ID와 access token 객체 생성
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(getID(), getAccessToken());

            //git clone
            Git.cloneRepository()
                    .setURI(cloneURL)
                    .setDirectory(fileToClone)
                    .setCredentialsProvider(credentialsProvider)
                    .call();

            System.out.println("Clone Success");
        } catch (GitAPIException | IOException e) {
            System.out.println("Clone Fail");
            e.printStackTrace();
            // 예외 발생
            throw e;
        }
    }

    // setIdToken
    // ID와 access token을 파일에 저장
    public static void setIdToken(String ID, String accessToken) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(ID + ":" + accessToken);
        }
    }

    // getID
    // ID를 리턴
    public static String getID() throws IOException {
        try {
            String[] credentials = getCredentials();
            return credentials[0];
        } catch (IOException e) {
            throw e;
        }
    }

    // getAccessToken
    // access token을 리턴
    public static String getAccessToken() throws IOException {
        try {
            String[] credentials = getCredentials();
            return credentials[1];
        } catch (IOException e) {
            throw e;
        }
    }

    // getCredentials
    // ID와 access token을 리턴
    private static String[] getCredentials() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] parts = line.split(":");
                String username = "";
                String password = "";
                if (parts.length > 0) {
                    username = parts[0];
                }
                if (parts.length > 1) {
                    password = parts[1];
                }
                return new String[]{username, password};
            }
        }
        return new String[0];
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
