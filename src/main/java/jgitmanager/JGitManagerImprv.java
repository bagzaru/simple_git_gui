package jgitmanager;

import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jgitmanager.JGitManager.openRepositoryFromFile;

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

            System.out.println("clone success");
        } catch (GitAPIException e) {
            System.out.println("clone fail");

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

            // 예외 발생
            throw e;
        }
    }

    // gitMerge
    // 현재 branch에서 source branch를 merge
    // git merge <sourceBranch>
    public static void gitMerge(File nowDir, String sourceBranch) throws GitAPIException, IOException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // git merge
            git.merge()
                    .include(repository.findRef(sourceBranch))
                    .setFastForward(MergeCommand.FastForwardMode.FF)
                    .call();

            // Git 저장소 닫기
            git.close();

            System.out.println("merge success");
        } catch (GitAPIException | IOException e) {
            System.out.println("merge fail");

            // 예외 발생
            throw e;
        }
    }

    // gitCheckout
    // git checkout <branchName>
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

            System.out.println("checkout success");
        } catch (GitAPIException | IOException e) {
            System.out.println("checkout fail");

            // 예외 발생
            throw e;
        }
    }

    // gitCurrentBranch
    // 현재 브랜치의 이름을 가져옴
    public static String gitCurrentBranch(File nowDir) throws IOException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // current branch 이름 가져옴
            String branchName = repository.getBranch();

            // Git 저장소 닫기
            git.close();

            System.out.println("get name of current branch");

            return branchName;
        } catch (IOException e) {
            System.out.println("Failed to get name of current branch");

            // 예외 발생
            throw e;
        }
    }

    // gitCommitInfo
    // 특정 commit의 정보를 리턴합니다
    // CommitInfo 객체를 리턴합니다.
    public static CommitInfo gitCommitInfo(RevCommit nowCommit) throws Exception {
        try {
            return new CommitInfo(nowCommit.getId().getName(),
                    nowCommit.getCommitTime(),
                    nowCommit.getFullMessage(),
                    nowCommit.getAuthorIdent().getName(),
                    nowCommit.getAuthorIdent().getEmailAddress());
        } catch (Exception e) {
            System.out.println("Failed to get commit information");
            throw e;
        }
    }

    // gitChangedFileList
    // 특정 commit에서 변경된 파일 리스트를 가져옵니다.
    public static Set<File> gitChangedFileList(File nowDir, RevCommit nowCommit) throws IOException, GitAPIException {
        Repository repository = openRepositoryFromFile(nowDir);
        Set<File> changedFiles = new HashSet<>();

        try (RevWalk revWalk = new RevWalk(repository)) {
            // nowCommit의 부모 commit
            RevCommit parentCommit = revWalk.parseCommit(nowCommit.getParent(0).getId());

            try (Git git = new Git(repository)) {
                DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
                diffFormatter.setRepository(repository);
                diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
                // 파일명 변경도 감지
                diffFormatter.setDetectRenames(true);

                // nowCommit과 parentCommit의 변경사항
                List<DiffEntry> diffs = git.diff()
                        .setOldTree(prepareTreeParser(repository, parentCommit))
                        .setNewTree(prepareTreeParser(repository, nowCommit))
                        .call();

                for (DiffEntry diff : diffs) {
                    // 파일에 추가
                    File file = new File(repository.getWorkTree(), diff.getNewPath());
                    changedFiles.add(file);
                }

                return changedFiles;
            }
        }
    }

    // prepareTreeParser
    // TreeParser 준비
    private static AbstractTreeIterator prepareTreeParser(Repository repository, RevCommit commit) throws IOException {
        try (RevWalk revWalk = new RevWalk(repository)) {
            // 커밋의 트리
            RevTree tree = revWalk.parseTree(commit.getTree().getId());

            // tree paser 생성
            CanonicalTreeParser treeParser = new CanonicalTreeParser();

            // ObjectReader 생성
            try (ObjectReader reader = repository.newObjectReader()) {
                // tree reader, id 설정
                treeParser.reset(reader, tree.getId());
            }
            revWalk.dispose();
            return treeParser;
        }
    }

    // gitDiff
    // 구현중
    public static void gitDiff(File nowDir, RevCommit commit, File file) throws IOException {
        Repository repository = openRepositoryFromFile(nowDir);
        String commitId = commit.getId().getName();
        Git git = new Git(repository);
        try {
            RevWalk revWalk = new RevWalk(repository);
            RevTree commitTree = commit.getTree();

            ObjectId parentCommitId = commit.getParent(0).getId();
            RevCommit parentCommit = revWalk.parseCommit(parentCommitId);
            RevTree parentTree = parentCommit.getTree();

            DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
            diffFormatter.setRepository(repository);
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);

            List<DiffEntry> diffs = diffFormatter.scan(parentTree, commitTree);
            for (DiffEntry diff : diffs) {
                if (diff.getNewPath().equals(file.getPath())) {
                    // 변경 내용 출력
                    diffFormatter.format(diff);
                    break;
                }
            }

            diffFormatter.close();
            revWalk.dispose();
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }
}