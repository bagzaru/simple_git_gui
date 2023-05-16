# simple_git_gui

![./img/v1.1/1_main.png](./img/v1.1/1_main.png)

Simple-git-gui는 Git과 연동하여 파일을 탐색하는 GUI 파일브라우저입니다.

## 주요 특징

- 파일 탐색 기능 제공
- Git을 통한 파일 관리 기능 제공

## 지원 플랫폼

- Windows 10 x64 또는 그 이상

## 요구사항

- 자바 개발 키트(JDK) 17 설치
- 버전 17의 자바 런타임 환경(JRE) 설치

## 실행 방법

[https://github.com/bagzaru/simple_git_gui/releases/latest](https://github.com/bagzaru/simple_git_gui/releases/latest)

위의 링크에서 simple_git_gui-1.1-all.jar파일을 다운받습니다.

명령 프롬프트에서 파일의 위치로 이동합니다.

```bash
java -jar simple_git_gui-1.1-all.jar

```

위 명령어를 통해 simple_git_gui-1.1-all.jar을 실행합니다.
(자바 17버전에서 실행해야 합니다.)

## 파일 탐색 안내

![./img/v1.1/9_main.png](./img/v1.1/9_main.png)

[좌측 패널]

- 좌측 패널에서 파일과 디렉토리를 클릭을 통해 탐색할 수 있습니다.

[중앙 상단 패널(]

- Current Directory에서는 현재 폴더 내의 파일과 폴더를 탐색할 수 있습니다.
- 각 파일의 Git status를 아이콘을 통해 확인할 수 있습니다.

[중앙 툴바]

- 파일의 실행, 생성, 삭제를 할 수 있습니다.
- StageAll 버튼을 누르면 현재 Git Repository의 변경사항을 모두 Stage할 수 있습니다. (git add .)
- Refresh 버튼을 누르면 파일 브라우저 외부에서 일어난 일들도 새로고침 할 수 있습니다.

[중앙 하단 패널(Staged List)]

- Staged List에서는 현재 Git에 staged된 파일 목록을 확인할 수 있습니다.

[우측 패널]

- Git repository가 아닐 경우, 해당 디렉토리를 Git repository로 만들 수 있습니다.

[우측 패널 - 파일 선택]

- Git repository일 경우, 선택된 파일을 Git 명령어를 통해 관리할 수 있습니다.

## Git을 통한 파일 관리 안내

파일을 선택하여 해당 파일을 Git을 통해 관리할 수 있습니다.

### 파일의 상태 - Directory

![./img/v1.1/2_directory.png](./img/v1.1/2_directory.png)

중앙 상단의 테이블에서 파일의 상태를 볼 수 있습니다.
우측 상단의 패널로 각 상태에 맞는 명령을 실행할 수 있습니다.

- Committed: 파일이 마지막 Commit의 상태와 동일함을 나타냅니다.
- Modified: 파일이 Git에서 관리하고 있는 내용과 다름을 의미합니다.
- Staged: 파일의 변경 사항이 Stage되었고, Staged 파일과 로컬 파일의 내용이 동일함을 의미합니다.
- Untracked: 파일이 Git에 의해 관리되고 있지 않음을 나타냅니다.

### 파일의 상태 - Staged List

![./img/v1.1/3_StagedList.png](./img/v1.1/3_StagedList.png)

중앙 하단 테이블에서는 Staged 상태인 파일들을 나타냅니다.
더블클릭으로 Staged 상태인 파일은 Unstage할 수 있습니다.

- Added: 파일이 Git에 새로 추가되어 Stage되었습니다.
- Staged: 파일이 이전 Commit에서 수정되어 Stage되었습니다.
- Deleted: git rm 등을 통해 파일이 삭제되었다는 내용이 Staged 된 상태입니다.

### Git을 통한 버전 관리

![./img/v1.1/4_Untracked.png](./img/v1.1/4_Untracked.png)

Untracked

- Add: 선택한 파일을 Staged List에 추가합니다.

![./img/v1.1/5_Modified.png](./img/v1.1/5_Modified.png)

Modified

- Add: 선택한 파일을 Staged List에 추가합니다.
- Undo: 선택한 파일을 마지막 Commit의 상태로 되돌립니다.

![./img/v1.1/6_Staged.png](./img/v1.1/6_Staged.png)

Staged

- Unstage: 선택한 파일을 Staged List에서 제거합니다. (파일의 내용은 변경되지 않습니다.)

![./img/v1.1/7_Committed.png](./img/v1.1/7_Committed.png)

Committed

- Delete: 파일을 삭제하고 Git에 반영합니다.
- Untrack: 파일을 Untracked모드로 변경합니다.
- Rename: 파일의 이름을 변경하고 Git에 반영합니다.

![./img/v1.1/8_commit_msg.png](./img/v1.1/8_commit_msg.png)

- Commit: 현재 Staged List의 내용을 Git에 Commit합니다.
