package memory;

import javafx.scene.layout.Pane;
import process.PCB;
import process.process;
import CPU.CPU;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MemoryDispatcher
{
    private static final  Memory memory=Memory.getInstance() ;
    private static MemoryDispatcher memoryDispatcher;
    private Pane memoryPane;
    private Map<RecordBlock, MemoryRectangle> oldRecords;

    public static MemoryDispatcher getInstance() {
        if (memoryDispatcher == null) {
            memoryDispatcher = new MemoryDispatcher(Memory.getInstance());
        }
        return memoryDispatcher;
    }

    public static PCB applyPCB(String name){
        return memory.applyPCB(name);

    }

    public static void storeInstruction(String[] instructions,PCB pcb){
        memory.storeInstruction(instructions,pcb);
        return;
    }

    public static String fetchInstruction(int address){
        return memory.fetchInstruction(address);

    }
    private MemoryDispatcher(Memory memory) {

        this.oldRecords = new LinkedHashMap<>();
    }



//    public void loadOS() {
//        MemoryRectangle rect = new MemoryRectangle(0.0D, 50.0D, this.memoryPane.getPrefHeight(), new PCB("OS", 5, -1.0D));
//        this.memoryPane.getChildren().add(rect);
//    }

    public void mergeFragments() {//整理内存分区(界面展示就是进程记录块整合合并)
        CPU cpu = CPU.getInstance();
        //cpu.stop();
        updateUI();

        List<RecordBlock> RecordBlocks = this.memory.getMAT();
        for (int i = 0; i < RecordBlocks.size(); i++) {
            int newStartAddress;
            RecordBlock record = RecordBlocks.get(i);
            if (i == 0) {
                newStartAddress = 50;
            } else {
                newStartAddress = ((RecordBlocks.get(i - 1).getEndpointer()) + 1);
            }
            record.setStartpointer(newStartAddress);
            record.setEndpointer(newStartAddress+record.getSize()-1);
            MemoryRectangle rect = this.oldRecords.get(record);
            double x=(newStartAddress/512)*rect.getParent().prefWidth(-1);
            rect.setLayoutX(x);
        }


        updateUI();
        //cpu.play();
    }







    public int findBlankAddress(int size) {//寻找新的空白块地址,找到则返回,没有空白块就返回null
        List<RecordBlock> RecordBlocks = this.memory.getMAT();
        int startAddress = 0, endAddress = 0;
        if (RecordBlocks.size() == 0) {
            return 50;
        }

        for (int i = 0; i < RecordBlocks.size(); i++) {
            if (i == 0) {
                startAddress = 50;
                endAddress = ((RecordBlock)RecordBlocks.get(i)).getStartpointer()- 1;
            } else {
                startAddress = ((RecordBlock)RecordBlocks.get(i - 1)).getEndpointer() + 1;
                endAddress = ((RecordBlock)RecordBlocks.get(i)).getStartpointer() - 1;
            }
            if (endAddress - startAddress + 1 >= size) {
                return startAddress;
            }
        }
        if (RecordBlocks.size() > 0) {
            startAddress = ((RecordBlock)RecordBlocks.get(RecordBlocks.size() - 1)).getEndpointer() + 1;
            endAddress = 511;
            if (endAddress - startAddress + 1 >= size) {
                return startAddress;
            }
        }
        return -1;
    }

//    public boolean hasBlankPCB() {
//        /* 118 */     return (this.memory.getPCBList().size() < 10);
//        /*     */   }
//
//    public void store(process process) {
//        PCB pcb = process.getPCB();
//        Address address = process.getAddress();
//        this.memory.getPCBList().add(pcb);
//        this.memory.getMAT().add(new RecordBlock(pcb.getPID(), address, process.isUser()));
//    }

    public static void freePCB(process process) {
        PCB pcb = process.getPcb();
        memory.freePCB(pcb);
        return ;
    }

    public static void freeRecordBlock(process process) {
        String name = process.getPcb().getName();
        memory.freeRecordBlock(name);
        return ;
    }

    public void setMemoryPane(Pane pane) {
            this.memoryPane = pane;
          }

    public void updateUI() {
        List<RecordBlock> RecordBlocks = this.memory.getMAT();
        for (int i = 0; i < RecordBlocks.size(); i++) {
            RecordBlock newRecord = RecordBlocks.get(i);
            if (!this.oldRecords.keySet().contains(newRecord)) {//界面添加新的进程记录快(UI)
                PCB pcb =Memory.getPCBByName(newRecord.getName());
                MemoryRectangle rect = new MemoryRectangle(newRecord.getStartpointer(), newRecord.getSize(), this.memoryPane.getPrefHeight(), pcb);
                this.memoryPane.getChildren().add(rect);
                this.oldRecords.put(newRecord, rect);
            }
        }
        for (Map.Entry<RecordBlock, MemoryRectangle> entry : this.oldRecords.entrySet()) {//界面删除内存记录表中的进程(即运行完的进程)
            if (!RecordBlocks.contains(entry.getKey()))
                this.memoryPane.getChildren().remove(entry.getValue());
        }
    }
}
