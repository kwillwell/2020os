package CPU;

import controller.ScheduiUIController;
import device.DeviceDispatcher;
import memory.Memory;
import memory.MemoryDispatcher;
import process.PCB;

import java.util.Map;

public class CPU {
    private static final long serialVersionUID = 1L;
    public static int PC;
    public static int PSW;
    public static PCB pcb;
    public static final int NONE_INTERMIT=0;  //无中断
    public static final int TIME_INTERMIT=1;  //时间片到中断
    public static final int NORMAL_INTERMIT=2;  //正常中断
    public static final int EQUIP_INTERMIT=3;   //设备中断
    public static final char[] IR=new char[3];
    public static ScheduiUIController UI_CONTROLLER= ScheduiUIController.getInstance();
    //public static DataTable table;   //数据在内存中的索引表
    private static int timeSize=6;   //时间片大小
    private static CPU cpu;
    private boolean stopFlag;
    public static CPU getInstance() {
        if (cpu == null) {
            cpu = new CPU();
        }
        return cpu;
    }
    public static void run(){
        int k=0;
        int hasExecuteOrderNum=1;
        String Instruction=new String();//指令
        int time=timeSize;
        while(true){

            //输出到控制台
            k++;
            //System.out.println("k:"+k);

            if(PSW==CPU.NORMAL_INTERMIT){
                return;
            } else if(CPU.PSW==CPU.TIME_INTERMIT){

                return;
            } else if(PSW==CPU.EQUIP_INTERMIT){
                return;
            }
            //取指令
            System.out.println("pc值"+PC);
            Instruction=MemoryDispatcher.fetchInstruction(PC);
            System.out.println("正在执行的指令： "+Instruction);
            //当前正在执行的指令
//            if (Instruction.equals(null)||UI_CONTROLLER.equals(null)){
//                System.out.println("null");
//            }
            System.out.println(UI_CONTROLLER.getRunningOrder());
            UI_CONTROLLER.setorder(Instruction);
            UI_CONTROLLER.getRunnningProcess().setText(pcb.getName());
            for(int i=0;i<3;i++)IR[i]=Instruction.charAt(i);
            //执行指令
            char v=executeOrder();
            //显示中间结果
            String resultString="";
            if(!pcb.getVariableArea().isEmpty()){
                for (Map.Entry<Character,Integer> entry:pcb.getVariableArea().entrySet()){
                    resultString+=entry.getKey()+"="+entry.getValue()+";";
                }
            }
            UI_CONTROLLER.getTemporaryResults().setText(resultString);

            time--;
            //如果是设备中断，则保存这一条指令方便检查
            if(CPU.PSW==CPU.EQUIP_INTERMIT) return ;


            /*如果时间片到了而程序还未正常结束就把PSW的状态设置成时间片到中断,
             *但是如果时间片到了此时程序正好结束就把PSW设置成程序正常结束软中
             *断而忽略时间片到中断*/
            if(time==0&&CPU.PSW!=CPU.NORMAL_INTERMIT)
                CPU.PSW=CPU.TIME_INTERMIT;

            PC++;
        }
    }

    private static char executeOrder(){

        if(IR[0]=='e'&&IR[1]=='n'&&IR[2]=='d'){
            PSW=CPU.NORMAL_INTERMIT;
            return '&';
        }
        else if(IR[0]=='!'){
                Memory.setPCBToBlock(pcb.getName());
                if (DeviceDispatcher.requestDevice(pcb.getName(),IR[1],IR[2]-'0')) {
                    PSW=CPU.EQUIP_INTERMIT;
                    PC++;
                }else {
                    PSW=CPU.EQUIP_INTERMIT;        //设备中断
                }


            return '&';
        }//设备管理
        else{
            char variable_name;
            int variable=0;
             variable_name=IR[0];
            if(IR[1]=='='){
                variable=IR[2]-48;
            }
            else if(IR[1]=='+'){
                variable=pcb.getVariableArea().get(variable_name)+1;

            }
            else if(IR[1]=='-'){
                variable=pcb.getVariableArea().get(variable_name)-1;
            }
            pcb.getVariableArea().put(variable_name,variable);
            return IR[0];
        }
    }

}
