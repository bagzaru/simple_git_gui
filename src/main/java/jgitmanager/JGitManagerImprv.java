package jgitmanager;

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
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static String gitDiff(File nowDir, RevCommit commit, File file) throws IOException {
        Repository repository = openRepositoryFromFile(nowDir);
        ObjectId commitId = commit.getId();

        try (Git git = new Git(repository);
             ObjectReader reader = git.getRepository().newObjectReader();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DiffFormatter diffFormatter = new DiffFormatter(outputStream)) {

            CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
            CanonicalTreeParser newTreeIter = new CanonicalTreeParser();

            RevCommit parentCommit = commit.getParent(0);
            if (parentCommit == null) {
                // 최초 커밋인 경우 비교할 이전 커밋이 없습니다.
                return "No previous commit to compare.";
            }

            oldTreeIter.reset(reader, parentCommit.getTree().getId());
            newTreeIter.reset(reader, commitId);

            diffFormatter.setRepository(git.getRepository());
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
            diffFormatter.setDetectRenames(true);

            List<DiffEntry> diffs = diffFormatter.scan(oldTreeIter, newTreeIter);
            for (DiffEntry diff : diffs) {
                if (diff.getNewPath().equals(file.getPath())) {
                    diffFormatter.format(diff);
                    diffFormatter.flush();

                    return outputStream.toString();
                }
            }
        }

        return "No changes found for the file.";
    }
}