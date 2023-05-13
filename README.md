# README

# simple_git_gui

Simple-git-gui는 Git과 연동하여 파일을 탐색하는 GUI 파일브라우저입니다.

## 주요 특징

- 파일 탐색 기능 제공
- Git을 통한 파일 관리 기능 제공

## 지원 플랫폼

- Windows 10 x64 또는 그 이상

## 요구사항

- 자바 개발 키트(JDK) 설치
- 버전 17의 자바 런타임 환경(JRE) 설치

## 실행 방법

(Release 링크)

위의 링크에서 릴리즈 버전의 파일을 다운받습니다.

```java
java -jar <jar_filename>.jar
```

(실행 파일명)을 실행합니다.

## 파일 탐색 안내

[사진]

- 좌측 패널에서 파일과 디렉토리를 클릭을 통해 탐색할 수 있습니다.

[중앙 상단 패널 사진]

- Current Directory에서는 현재 폴더 내의 파일과 폴더를 탐색할 수 있습니다.
- 각 파일의 Git status를 아이콘을 통해 확인할 수 있습니다.

[중앙 툴바 사진]

- 파일의 실행, 생성, 삭제, 파일명 변경, 복사를 할 수 있습니다.
기존 파일 브라우저 오픈소스에서 기능이 구현 안되어 정상적으로 작동하지 않을 수 있습니다.
- Refresh 버튼을 누르면 파일 브라우저 외부에서 일어난 일들도 새로고침 할 수 있습니다.

[중앙 하단 패널 사진]

- Staged List에서는 현재 Git에 staged된 파일 목록을 확인할 수 있습니다.

[우측 패널 사진]

- Git repository가 아닐 경우, 해당 디렉토리를 Git repository로 만들 수 있습니다.

[우측 패널 사진 - 파일 선택]

- Git repository일 경우, 선택된 파일을 Git 명령어를 통해 관리할 수 있습니다.

## Git을 통한 파일 관리 안내

파일을 선택하여 해당 파일을 Git을 통해 관리할 수 있습니다.

### 파일의 상태 - Directory

[각 상태에 대한 사진 또는 각 상태가 다 존재하는 디렉토리 사진]

- Untracked: 파일이 Git에 의해 관리되고 있지 않음을 나타냅니다.
- Unmodified: 파일이 마지막 Commit의 상태와 동일함을 나타냅니다.
- Modified(Green): 파일이 마지막 Commit 상태에서 수정되었고, Staged된 것을 나타냅니다.
- Modified(Yellow): 파일이 마지막 Commit 상태에서 수정되었고, 현재 변경사항이 Staged되지 않았음을 나타냅니다.

### 파일의 상태 - Staged List

[Staged List사진]

- (Green): 파일이 Staged되었음을 나타냅니다.
- (Yellow): 파일이 Staged되었으나 그 내용이 현재 디렉토리의 파일과 다름을 나타냅니다.

### Git을 통한 버전 관리

[버튼 패널 사진]

- Add: 선택한 파일을 Staged List에 추가합니다.
- Undo: 선택한 파일을 마지막 Commit의 상태로 되돌립니다.
- Unstage: 선택한 파일을 Staged List에서 제거합니다. (파일의 내용은 변경되지 않습다.)
- Untrack: 파일을 Untracked모드로 변경합니다.
- Delete: 파일을 삭제하고 Git에 반영합니다.
- Rename: 파일의 이름을 변경하고 Git에 반영합니다.
- Commit: 현재 Staged List의 내용을 Git에 Commit합니다.

### 라이센스

(License.md 링크)