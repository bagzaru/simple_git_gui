package jgitmanager;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;

import static jgitmanager.JGitManager.openRepositoryFromFile;

public class JGitImprvTester {
    public static String testPath = "/Users/minjeong-in/민정인/open_prj/testPath";

    static JGitManagerImprv jGitManagerImprv;

    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitImprvTester tester = new JGitImprvTester();

        tester.gitDiffTest(testPath);
    }

    public void gitCloneTest(String filePath){
        try{
            // "min-jp" "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"
            jGitManagerImprv.gitClone(new File(filePath), "https://github.com/min-jp/testRepo.git");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitCloneTest2(String filePath){
        try{
            jGitManagerImprv.gitClone(new File(filePath+"2"), "https://github.com/min-jp/simple_git_gui.git");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitCreateBranchTest(String filePath){
        try{
            jGitManagerImprv.gitCreateBranch(new File(filePath), "branch1");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitDeleteBranchTest(String filePath){
        try{
            jGitManagerImprv.gitDeleteBranch(new File(filePath), "branch1");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitRenameBranchTest(String filePath){
        try{
            jGitManagerImprv.gitRenameBranch(new File(filePath), "branch1", "branch_re");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitMergeTest(String filePath){
        try{
            jGitManagerImprv.gitMerge(new File(filePath), "branch_re");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitCheckoutTest(String filePath){
        try{
            jGitManagerImprv.gitCheckout(new File(filePath), "branch_re");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitCurrentBranchTest(String filePath){
        try{
            String branchName = jGitManagerImprv.gitCurrentBranch(new File(filePath));
            System.out.println(branchName);
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitCommitInfoTest(String filePath){
        try{
            Repository repository = openRepositoryFromFile(new File(filePath));
            RevCommit latestCommit = getLatestCommit(repository);
            CommitInfo info = jGitManagerImprv.gitCommitInfo(latestCommit);

            System.out.println(info.getCheckSum());
            System.out.println(info.getCommitTime());
            System.out.println(info.getCommitMessage());
            System.out.println(info.getAuthorName());
            System.out.println(info.getAuthorEMail());

        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitChangedFileListTest(String filePath){
        try{
            Repository repository = openRepositoryFromFile(new File(filePath));
            RevWalk revWalk = new RevWalk(repository);
            ObjectId head = repository.resolve("HEAD"); // 현재 HEAD 커밋의 ObjectId를 가져옴
            RevCommit headCommit = revWalk.parseCommit(head);
            RevCommit previousCommit = revWalk.parseCommit(headCommit.getParent(0));

            RevCommit latestCommit = getLatestCommit(repository);

            jGitManagerImprv.gitChangedFileList(new File(filePath), previousCommit);
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitDiffTest(String filePath){
        try{
            Repository repository = openRepositoryFromFile(new File(filePath));
            RevWalk revWalk = new RevWalk(repository);
            ObjectId head = repository.resolve("HEAD"); // 현재 HEAD 커밋의 ObjectId를 가져옴
            RevCommit headCommit = revWalk.parseCommit(head);
            RevCommit previousCommit = revWalk.parseCommit(headCommit.getParent(0));

            RevCommit latestCommit = getLatestCommit(repository);

            File file = new File(testPath + "/무제.txt");

            ///jGitManagerImprv.gitDiff(new File(filePath), latestCommit, new File(testPath + "/무제.txt"));
            String str = jGitManagerImprv.gitDiff(new File(filePath), previousCommit, new File(testPath + "/무제.txt"));

            System.out.println("-------");
            System.out.println(str);
            System.out.println("-------");
        } catch(Exception e){
            System.out.println("eeee");
            System.out.println(e.toString());
        }
    }







    public static RevCommit getLatestCommit(Repository repository) throws IOException {
        try (RevWalk revWalk = new RevWalk(repository)) {
            ObjectId head = repository.resolve("HEAD");
            return revWalk.parseCommit(head);
        }
    }
}
