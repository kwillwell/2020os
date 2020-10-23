package memory;

import CPU.CPU;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import process.PCB;

import java.util.ArrayList;

public class Memory {
    private static String[] instructionArea;
    private static ObservableList<PCB> PCBlist;
    private static ArrayList<RecordBlock> MAT;
    private static Memory memory;
    private static final int instructionAreaSize=512;
    private static final int PCBlistSize=100;

    public Memory(String[] instructionArea) {
        PCBlist=FXCollections.observableArrayList();
        this.instructionArea = instructionArea;
        this.PCBlist= FXCollections.observableArrayList();
        this.MAT=new ArrayList<>();

    }
    public static Memory getInstance() {//内存单例
        if (memory == null) {
            memory = new Memory(new String[512]);
        }
        return memory;
    }

    public PCB applyPCB( String name){
        if (PCBlist.size()==10){
            return null;
        }else {
            for (PCB pcb:PCBlist){
                if (pcb.getName().equals(name)){
                    System.out.println("进程名以被占用");
                    return null;
                }
            }
            PCB pcb=new PCB(name);
            PCBlist.add(pcb);
            return pcb;
        }

    }
    public  void storeInstruction(String[] instructions,PCB pcb){
        System.arraycopy(instructions,0,this.instructionArea,pcb.getStartpointer(),instructions.length);

    }
    public String fetchInstruction(int address){
        return this.instructionArea[address];
    }

    public void freePCB(PCB pcb){
        if (PCBlist.size()>0){
            PCBlist.remove(pcb);
        }
    }

    public void freeRecordBlock(String name){
        for (RecordBlock recordBlock:MAT){
            if (recordBlock.getName().equals(name)){
                MAT.remove(recordBlock);
                return;
            }
        }
    }
    public static void  setPCBToBlock(String name){

        for (PCB pcb:PCBlist){
            if (pcb.getName().equals(name)){
                pcb.setPSW(CPU.EQUIP_INTERMIT);
            }
        }

    }

    public static  PCB getPCBByName(String  name){
        for (PCB pcb:PCBlist){
            if(pcb.getName().equals(name)){
                return pcb;
            }
        }
        return null;
    }


    public String[] getInstructionArea() {
        return instructionArea;
    }

    public void setInstructionArea(String[] instructionArea) {
        this.instructionArea = instructionArea;
    }

    public ObservableList<PCB> getPCBlist() {
        return PCBlist;
    }

    public void setPCBlist(ObservableList<PCB> PCBlist) {
        this.PCBlist = PCBlist;
    }

    public  ArrayList<RecordBlock> getMAT() {
        return MAT;
    }

    public void setMAT(ArrayList<RecordBlock> MAT) {
        this.MAT = MAT;
    }
}
