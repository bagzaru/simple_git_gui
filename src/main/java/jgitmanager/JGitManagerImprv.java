package jgitmanager;

import jgitmanager.CommitInfo;
import jgitmanager.MergeException;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.ResolveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

            // 폴더명 파싱
            String tempFileName = cloneURL.substring(cloneURL.lastIndexOf("/") + 1);
            String newFileName;
            // 마지막 네글자가 .git이면 삭제
            if (tempFileName.endsWith(".git")) {
                newFileName = tempFileName.substring(0, tempFileName.length() - 4);
            } else {
                newFileName = tempFileName;
            }

            //git clone
            Git.cloneRepository()
                    .setURI(cloneURL)
                    .setDirectory(new File(fileToClone, newFileName))
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
            // credentials.txt 파일이 없다면 생성
            File credentialsFile = new File(FILE_PATH);
            if (!credentialsFile.exists()) {
                setIdToken("","");
            }

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

            System.out.println(branchName + " branch create success");
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

            System.out.println(branchName + " branch delete success");
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

            System.out.println("branch rename success / old name: " + oldName + " new name: " + newName);
        } catch (GitAPIException | IOException e) {
            System.out.println("branch rename fail");

            // 예외 발생
            throw e;
        }
    }

    // gitMerge
    // 현재 branch에서 source branch를 merge
    // git merge <sourceBranch>
    public static void gitMerge(File nowDir, String sourceBranch) throws GitAPIException, IOException, MergeException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // git staged나 modified가 있다면 merge 안하도록
            Status status = git.status().call();
            int isClean = status.getModified().size() +
                    status.getAdded().size() +
                    status.getChanged().size() +
                    status.getRemoved().size();
            if (isClean != 0) {
                throw new MergeException("Before you merge, please commit");
            }
            // git merge
            MergeResult mergeResult = git.merge()
                    .include(repository.findRef(sourceBranch))
                    .setFastForward(MergeCommand.FastForwardMode.FF)
                    .call();

            // 충돌이 나면 예외 발생
            StringBuilder conflictFiles = new StringBuilder("Conflict in file");
            mergeResult.getFailingPaths();
            if (mergeResult.getConflicts() != null) {
                for (String conflictedFile : mergeResult.getConflicts().keySet()) {
                    conflictFiles.append("\n").append(conflictedFile);
                }

                // git reset --hard
                git.reset().setMode(ResetCommand.ResetType.HARD).call();


                // throw
                throw new MergeException(String.valueOf(conflictFiles));
            }

            // Git 저장소 닫기
            git.close();

            System.out.println("merge success / " + sourceBranch);
        } catch (GitAPIException | IOException | MergeException e) {
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

            System.out.println(branchName + " checkout success");
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

            // current branch 이름 가져옴
            String branchName = repository.getBranch();

            System.out.println("get name of current branch / " + branchName);

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
            if (nowCommit != null) {
                return new CommitInfo(nowCommit.getId().getName(),
                        nowCommit.getCommitTime(),
                        nowCommit.getFullMessage(),
                        nowCommit.getAuthorIdent().getName(),
                        nowCommit.getAuthorIdent().getEmailAddress());
            } else {
                return new CommitInfo(" ", -1, " ", " ", " ");
            }

        } catch (Exception e) {
            System.out.println("Failed to get commit information");
            throw e;
        }
    }

    // gitChangedFileList
    // 특정 commit에서 변경된 파일 리스트를 가져옵니다.
    public static List<File> gitChangedFileList(File nowDir, RevCommit nowCommit) throws IOException, GitAPIException {
        Repository repository = openRepositoryFromFile(nowDir);
        List<File> changedFiles = new ArrayList<>();

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
                        .setOldTree(prepareTreeParser(repository, parentCommit.getId().getName()))
                        .setNewTree(prepareTreeParser(repository, nowCommit.getId().getName()))
                        .call();

                for (DiffEntry diff : diffs) {
                    // 파일에 추가
                    File file = new File(repository.getWorkTree(), diff.getNewPath());
                    //가끔 파일이 잘못들어옴
                    if(!file.getName().equals("null")){
                        changedFiles.add(file);
                    }
                }

                return changedFiles;
            }
        }
    }

    // gitDiff
    // 특정 커밋의 특정 파일의 변경사항을 문자열로 반환
    public static String gitDiff(File nowDir, RevCommit nowCommit, File changedFile) throws IOException, GitAPIException {
        Repository repository = openRepositoryFromFile(nowDir);

        // 현재 커밋과 부모 커밋의 AbstractTreeIterator객체를 구함
        AbstractTreeIterator newTreeParser = prepareTreeParser(repository,nowCommit.getId().getName());
        AbstractTreeIterator oldTreeParser = prepareTreeParser(repository,nowCommit.getParent(0).getId().getName());

        try (Git git = new Git(repository)) {
            // 새 트리와 이전 트리 비교
            List<DiffEntry> diffs = git.diff()
                    .setNewTree(newTreeParser)
                    .setOldTree(oldTreeParser)
                    .call();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(outputStream);
            formatter.setRepository(repository);
            for (DiffEntry entry : diffs) {
                // 비교를 위한 디렉토리
                File newPathFile = new File(nowDir, entry.getNewPath());
                File oldPathFile = new File(nowDir, entry.getOldPath());
                String newPath = newPathFile.getPath();
                String oldPath = oldPathFile.getPath();

                // 파일과 현재 diff가 같으면 변화된 내용 가져옴
                if (changedFile.getPath().equals(newPath) || changedFile.getPath().equals(oldPath))
                    formatter.format(entry);
            }
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }

    // prepareTreeParser
    // TreeParser 준비
    private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        try (RevWalk walk = new RevWalk(repository)) {
            // 커밋의 트리
            RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            // tree paser 생성
            CanonicalTreeParser treeParser = new CanonicalTreeParser();

            // ObjectReader 생성
            try (ObjectReader reader = repository.newObjectReader()) {
                // tree reader, id 설정
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }

    public static List<String> gitBranchList (File nowDir) throws IOException, GitAPIException {
        try {
            // Git 저장소 열기
            Repository repository = openRepositoryFromFile(nowDir);
            Git git = new Git(repository);

            // current branch 이름 가져옴
            List<Ref> call = git.branchList().call();
            List<String> branchList = new ArrayList<>();
            for (Ref ref : call) {
                branchList.add(Repository.shortenRefName(ref.getName()));
            }

            // Git 저장소 닫기
            git.close();

            System.out.println("get list of current branch");

            return branchList;
        } catch (IOException | GitAPIException e) {
            System.out.println("Failed to get list of current branch");

            // 예외 발생
            throw e;
        }
    }
}