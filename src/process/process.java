package process;

import memory.MemoryDispatcher;

import java.util.Map;

public class process {
    private PCB pcb;
    private int size;//进程大小;指条数
    private int PSW;
    private static MemoryDispatcher memoryDispatcher=MemoryDispatcher.getInstance();
    public static process createProcess(String[] instructions,String name ){
        process proc = new process();
        proc.size=instructions.length;
        proc.pcb= MemoryDispatcher.applyPCB(name);
        if (proc.pcb==null){
            System.out.println("进程过多");
            return null;
        }else {
            setVariableArea(instructions,proc.pcb.getVariableArea());
            int testaddress;
            if (name.equals("闲逛进程")){
                testaddress=49;
            }else{
                testaddress=memoryDispatcher.findBlankAddress(instructions.length);
            }
            if (testaddress==-1) {
                System.out.println("内存空间不足");
                return null;
            }else {
                proc.pcb.setStartpointer(testaddress);
                proc.pcb.setPC(testaddress);
                proc.pcb.setEndpointer(testaddress+instructions.length-1);
                MemoryDispatcher.storeInstruction(instructions,proc.pcb);
               // proc.pcb.setName(name);
                proc.pcb.setRuntime(instructions.length*1.0);
                proc.pcb.setName(name);
                proc.pcb.setLength(instructions.length);
                return proc;
            }


        }


    }
    private static void setVariableArea(String[] program,Map<Character,Integer >VariableArea) {
       for (String str:program){
           if (str.charAt(1)=='='){
               VariableArea.put(new Character(str.charAt(0)),Integer.valueOf(str.substring(2)));
           }

       }

    }

    public PCB getPcb() {
        return pcb;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPSW() {
        return PSW;
    }

    public void setPSW(int PSW) {
        this.PSW = PSW;
    }
}
