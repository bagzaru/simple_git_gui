package gui.branchpanel.component.commitlogpane.tablemvc;

import java.awt.*;

public class BranchColorGenerator {
    static int delta = -1;
    static int[] rgb = {
            0x02ee02,
            0x0202ee,
            0x02eeee,
            0xee02ee,
            0xee3d02,
            0x3d02ee,
            0xb3ee02,
            0xee8002,
            0xe6ee02,
            0xb50046,
            0xf1e94b,
            0x009984,
            0xf0645e,
            0x7246fe,
            0x009cff,
            0x1202ee,
            0x5d00da,
            0x8c0054,
            0x8bcfff,
            0xee02d6,
            0x9002ee,
            0xd6ee02,
            0x8f8500,
            0x8902ee,
            0x68ee02,
            0x1202ee,
            0xee02de,
            0xee0268,
            0xee8802,
            0xee02cc,
            0x9b02ee,
            0xcbee02,
            0xee2502,
            0x68ee02,
            0xee0267,
            0x02ee88,
            0xee02de,
            0xee1202,
            0xee8802,
    };

    public static Color getGraphColor(int i) {
        if(delta==-1){
            //처음 색상을 호출했을 경우, 랜덤 값을 부여합니다.
            //이후부터는 해당 값에 맞게 출력됩니다.
            delta = (int)(Math.random()*rgb.length);
        }
        if (i < 0||rgb.length==0) {
            return Color.BLUE;
        }
        else{
            return new Color(rgb[(i+delta)%rgb.length]);
        }

    }
}
