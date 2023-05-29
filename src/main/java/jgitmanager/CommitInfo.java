package jgitmanager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CommitInfo {
    // commit 체크썸
    private final String checkSum;

    // commit 생성 시간
    private final String commitTime;

    // commit message
    private final String commitMessage;

    // author 이름
    private final String authorName;

    // author 이메일
    private final String authorEMail;

    public CommitInfo (String checkSum, int commitTime, String commitMessage, String authorName, String authorEMail) {
        this.checkSum = checkSum;

        //timestamp 시간 변환
        long longCommitTime = commitTime * 1000L;
        Timestamp timestamp = new Timestamp(longCommitTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초");
        this.commitTime = simpleDateFormat.format(timestamp);

        this.commitMessage = commitMessage;
        this.authorName = authorName;
        this.authorEMail = authorEMail;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEMail() {
        return authorEMail;
    }
}
