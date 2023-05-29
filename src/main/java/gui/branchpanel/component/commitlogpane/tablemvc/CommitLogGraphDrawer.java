package gui.branchpanel.component.commitlogpane.tablemvc;

import org.eclipse.jgit.revwalk.RevCommit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


//1. setGraphNodes: 그려질 그래프의 노드의 정보 저장. (노드의 위치(row,col), 연결된 부모노드)
//2. drawGraph: graphNode를 기반으로 그려냄

public class CommitLogGraphDrawer {

    double circleSize = 0.6;

    static class CommitNode {
        int column;
        int row;
        RevCommit commit;
        ArrayList<CommitNode> parents;

        CommitNode() {
            parents = new ArrayList<>();
        }
    }

    //CommitLog의 Graph를 그릴 때 필요한 데이터입니다.
    ArrayList<CommitNode> graphNodes;

    CommitLogGraphDrawer() {
        graphNodes = new ArrayList<>();
    }

    //1. Model의 logs 참조하여 각 행에 대한 commit 열 저장하기, 최대값도 저장하기
    //2. 해당 열 참조하여 각 graph draw하기
    public void setGraphNodes(ArrayList<RevCommit> logs) {
        graphNodes.clear();
        //실제 Model의 logs를 입력받아, Graph를 그리기 위한 준비를 한다.
        //그려질 그래프의 Node를 제작한다. 후에 drawGraph에서 사용된다.
        PriorityQueue<Integer> enableList = new PriorityQueue<>(); //현재 가능한 줄 번호 중 가장 작은 번호를 호출
        Map<String, CommitNode> nextList = new HashMap<>();
        //enableList 초기화, 최대 탐색할 node의 수는 전체 log의 크기를 넘을 수 없다.
        for (int i = 0; i <= logs.size(); i++) {
            enableList.add(i);
        }
        //돌면서 Node 정보를 저장
        //initial node
        CommitNode initialNode = new CommitNode();
        initialNode.commit = logs.get(0);
        initialNode.column = enableList.poll();
        nextList.put(initialNode.commit.getId().getName(), initialNode);
        for (int i = 0; i < logs.size(); i++) {
            CommitNode cur = nextList.remove(logs.get(i).getId().getName());
            cur.row = i;
            graphNodes.add(cur);
            enableList.add(cur.column);     //현재 컬럼을 추가한다.
            for (RevCommit p : cur.commit.getParents()) {
                if (nextList.containsKey(p.getId().getName())) {
                    //이미 누군가가 추가하였다면(동일한 부모를 가진 사람이 있다면)
                    CommitNode c = nextList.get(p.getId().getName());
                    //현재 가능한 값보다 더 큰 값을 사용하였을 경우, 교체
                    if (c.column > enableList.peek()) {
                        enableList.add(c.column);
                        c.column = enableList.poll();
                    }
                    cur.parents.add(c);
                } else {
                    //아무도 추가하지 않았다면
                    CommitNode c = new CommitNode();
                    c.commit = p;
                    c.column = enableList.poll();
                    nextList.put(p.getId().getName(), c);
                    cur.parents.add(c);
                }
            }
        }
    }

    //graphNode가 잘 작동하나 test용 함수
    public void printGraphNodes() {
        System.out.println("printGraphNodes: ");
        for (CommitNode i : graphNodes) {
            System.out.print("commit: " + i.commit.getId().getName().substring(0, 4) + ", parent:");
            for (CommitNode p : i.parents) {
                System.out.print(" " + p.commit.getId().getName().substring(0, 4) + "[" + p.column + "] ");
            }
            System.out.println();
        }
    }

    public void drawGraph(Graphics g, JTable table) {
        int h = table.getRowHeight();
        int i = 0;
        for (CommitNode n : graphNodes) {
            g.setColor(BranchColorGenerator.getGraphColor(n.column));
            g.fillOval(
                    n.column * h + (int) (h * (1.0 - circleSize) * (0.5)),
                    i * h + (int) (h * (1.0 - circleSize) * (0.5)),
                    (int) (h * circleSize),
                    (int) (h * circleSize)
            );
            for(CommitNode p: n.parents){
                //부모를 향한 선을 그린다.
                g.setColor(BranchColorGenerator.getGraphColor(p.column));
                g.drawLine(
                        n.column*h+(int)(h*0.5),i*h+(int)(h*0.5),
                        p.column*h+(int)(h*0.5),p.row*h+(int)(h*0.5)
                        );
            }
            i++;
        }
    }
}