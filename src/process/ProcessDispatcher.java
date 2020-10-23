package process;

import CPU.CPU;
import controller.ScheduiUIController;
import device.DeviceDispatcher;
import device.device;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import memory.MemoryDispatcher;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessDispatcher {

    private process runningProcess;
    private static boolean isDispatcher=false;
    private static process hangOutProcess;
    private static process processRegister;
    private static ObservableList<PCB> endProcessList;
    private static ObservableList<PCB> readyProcessList;
    private static ObservableList<PCB> blockedProcessList;
    private static ObservableList<device> deviceList;
    //private final Thread dispatchThread;D
    private static ProcessDispatcher processDispatcher;
    public static final ScheduiUIController UI_CONTROLLER= ScheduiUIController.getInstance();
    private static final DeviceDispatcher DEVICE_DISPATCHER=DeviceDispatcher.getInstance();
    private static Path pathrunToEnd;
    private static Path pathreadyToRun;
    private static Path pathblockTodevice;
    private static Path pathdeviceToReady;
    private static PathTransition runToEnd;
    private static PathTransition readyToRun;
    private static PathTransition blockTodevice;
    private static PathTransition deviceToReady;
    private static Pane panerunToEnd;
    private static Pane panereadyToRun;
    private static Pane paneblockTodevice;
    private static Pane panedeviceToReady;

    public static ProcessDispatcher getInstance(){
        if (processDispatcher==null){
            processDispatcher=new ProcessDispatcher();
        }
        return processDispatcher;
    }

    static {
            deviceList= FXCollections.observableArrayList();
            blockedProcessList= FXCollections.observableArrayList();
            readyProcessList= FXCollections.observableArrayList();
            endProcessList= FXCollections.observableArrayList();
            runToEnd=new PathTransition();
            readyToRun=new PathTransition();
            blockTodevice=new PathTransition();
            deviceToReady=new PathTransition();
            processRegister=new process();
            String[] end=new String[]{"end"};
            hangOutProcess = process.createProcess(end,"闲逛进程");
            deviceList= DeviceDispatcher.deviceList;
    }
    // 调度进程
    public static void dispatchProcess() {
        Thread thread=new Thread(){
            public void run(){
                //在运行时时刻检查阻塞队列中的能不能调进就绪队列
                while (true) {
                    //System.out.println("CPU.PSW："+CPU.PSW);
                    /* 初始时刻或由于进程正常结束引发的调度 */
                    System.out.println(isDispatcher);
                    if (isDispatcher){
                        if (CPU.PSW == 0 || CPU.PSW == 2) {
                            if (CPU.PSW == 2 &&  processRegister!= null) {
                                endProcessList.add(processRegister.getPcb());
                                MemoryDispatcher.freePCB(processRegister);
                                MemoryDispatcher.freeRecordBlock(processRegister);
                            }
                            if (readyProcessList.size()!=0){
                                processRegister.setPcb(readyProcessList.remove(0));
                                setCPURegister(processRegister);
                            }else{
                                processRegister=null;
                                setCPURegister(hangOutProcess);
                            }
                            playreadyToRun();
                            CPU.run();

                        }
                        /* 由于时间片到进程被打断引发的调度 */
                        else if (CPU.PSW == 1) {
                            /* 如果被打断的是闲逛进程,将CPU的PSW设置为0,使之继续运行 */
                            if (processRegister == null) {
                                CPU.PSW = 0;
                                CPU.run();
                            } else {
                                processRegister.setPSW(0); // 保存现场
                                processRegister.setPcb(CPU.pcb);
                                processRegister.getPcb().setPC(CPU.PC);
                                readyProcessList.add(processRegister.getPcb());
                                /* 调度新的进程占用处理器CPU */
                                if (readyProcessList.size()!=0){
                                    processRegister.setPcb(readyProcessList.remove(0));
                                    setCPURegister(processRegister);
                                }else{
                                    processRegister=null;
                                    setCPURegister(hangOutProcess);
                                }
                                playreadyToRun();
                                CPU.run();
                            }
                        }
                        //设备中断
                        else {
                            /* 如果被打断的是闲逛进程,将CPU的PSW设置为0,使之继续运行 */
                            if (processRegister == null) {
                                CPU.PSW = 0;
                                CPU.run();
                            } else {
                                processRegister.setPSW(0); // 保存现场
                                processRegister.setPcb(CPU.pcb);
                                processRegister.getPcb().setPC(CPU.PC);
                                blockedProcessList.add(processRegister.getPcb());                    //保存到阻塞队列
                                /* 调度新的进程占用处理器CPU */
                                if (readyProcessList.size()!=0){
                                    processRegister.setPcb(readyProcessList.remove(0));
                                    setCPURegister(processRegister);
                                }else{
                                    processRegister=null;
                                    setCPURegister(hangOutProcess);
                                }
                                playreadyToRun();
                                CPU.run();
                            }

                        }
                    }
                }
            }

        };
        thread.setDaemon(true);
        thread.start();

    }
    public static void setCPURegister(process p) {
        CPU.PC = p.getPcb().getPC();
        CPU.PSW = CPU.NONE_INTERMIT;
        CPU.pcb = p.getPcb();
    }

    public void setPanerunToEnd(Pane panerunToEnd) {
        this.panerunToEnd = panerunToEnd;
    }

    public void setPanereadyToRun(Pane panereadyToRun) {
        this.panereadyToRun = panereadyToRun;
    }

    public void setPaneblockTodevice(Pane paneblockTodevice) {
        this.paneblockTodevice = paneblockTodevice;
    }

    public void setPanedeviceToReady(Pane panedeviceToReady) {
        this.panedeviceToReady = panedeviceToReady;
    }

    public void setReadyTableView(TableView<PCB> tableView , TableColumn<PCB, StringProperty> Readyname, TableColumn<PCB, DoubleProperty>ReadyRt, TableColumn<PCB, DoubleProperty>ReadyRestTime, TableColumn<PCB, IntegerProperty> Readylength, TableColumn<PCB, Double> processBar) {
        Readyname.setCellValueFactory(new PropertyValueFactory<>("name"));
        ReadyRt.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        ReadyRestTime.setCellValueFactory(new PropertyValueFactory<>("resttime"));
        Readylength.setCellValueFactory(new PropertyValueFactory<>("length"));
        processBar.setCellValueFactory(new PropertyValueFactory<>("progress"));
        processBar.setCellFactory(ProgressBarTableCell.forTableColumn());
        tableView.setItems(this.readyProcessList);
    }

    public void setBlockedTableView(TableView<PCB> BlockedProcess,TableColumn<PCB, StringProperty> BlockedName,TableColumn<PCB, StringProperty> BlockedDeviceType,TableColumn<PCB, DoubleProperty> BlockeddeviceTime,TableColumn<PCB, DoubleProperty> BlockedTRT,TableColumn<PCB, DoubleProperty> BlockedRRT){
        BlockedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        BlockedTRT.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        BlockedRRT.setCellValueFactory(new PropertyValueFactory<>("resttime"));
        BlockedDeviceType.setCellValueFactory(new PropertyValueFactory<>("needdevice"));
        BlockeddeviceTime.setCellValueFactory(new PropertyValueFactory<>("holdTime"));
        BlockedProcess.setItems(this.blockedProcessList);

    }

    public void setEndTableView(TableView<PCB> EndedTable,TableColumn<PCB, DoubleProperty> endedRT,TableColumn<PCB, StringProperty> endedName,TableColumn<PCB, StringProperty> endedresult){
        endedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        endedRT.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        endedresult.setCellValueFactory(new PropertyValueFactory<>("result"));
        EndedTable.setItems(this.endProcessList);

    }

    public void setDeviceTableView(TableView<device> DeviceTable,TableColumn<device, Character> device,TableColumn<device, String> DevicePName,TableColumn<device, Double> DeviceRT){
        device.setCellValueFactory(new PropertyValueFactory<>("name"));
        DevicePName.setCellValueFactory(new PropertyValueFactory<>("process"));
        DeviceRT.setCellValueFactory(new PropertyValueFactory<>("occupiedTime"));
        DeviceTable.setItems(this.deviceList);

    }
    public void setPathrunToEnd(Path pathrunToEnd) {
        this.pathrunToEnd = pathrunToEnd;
        runToEnd.setPath(pathrunToEnd);
        runToEnd.setCycleCount(1);
        runToEnd.setDuration(Duration.seconds(1));
    }

    public void setPathreadyToRun(Path pathreadyToRun) {
        this.pathreadyToRun = pathreadyToRun;
        readyToRun.setPath(pathreadyToRun);
        readyToRun.setCycleCount(1);
        readyToRun.setDuration(Duration.seconds(1));
    }

    public void setPathblockTodevice(Path pathblockTodevice) {
        this.pathblockTodevice = pathblockTodevice;
        blockTodevice.setPath(pathblockTodevice);
        blockTodevice.setCycleCount(1);
        blockTodevice.setDuration(Duration.seconds(1));
    }

    public void setPathdeviceToReady(Path pathdeviceToReady) {
        this.pathdeviceToReady = pathdeviceToReady;
        deviceToReady.setPath(pathdeviceToReady);
        deviceToReady.setCycleCount(1);
        deviceToReady.setDuration(Duration.seconds(1));
    }
    public void playrunToEnd() {//队列之间的小球转移
        final Rectangle R = new Rectangle(20,20);
        R.setFill(Paint.valueOf("#48D1CC"));
        this.panerunToEnd.getChildren().add(R);
        runToEnd.setNode(R);
        runToEnd.play();
        (new Thread(new Runnable()
        {
            public void run() {
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcessDispatcher.class.getName()).log(Level.SEVERE, (String)null, ex);
                }
                Platform.runLater(new Runnable()
                {
                    public void run() {
                        /* 428 */                     ProcessDispatcher.this.panerunToEnd.getChildren().remove(R);
                        /*     */                   }
                });
            }
        })).start();
    }
    public static void playreadyToRun() {//队列之间的小球转移
        final Rectangle R = new Rectangle(20,20);
        R.setFill(Paint.valueOf("#FFA500"));
        (new Thread(new Runnable()
        {
            public void run() {
                Platform.runLater(new Runnable()
                {
                    public void run() {
                        panereadyToRun.getChildren().add(R);
                    }
                });
            }
        })).start();

        readyToRun.setNode(R);
        readyToRun.play();
        (new Thread(new Runnable()
        {
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcessDispatcher.class.getName()).log(Level.SEVERE, (String)null, ex);
                }
                Platform.runLater(new Runnable()
                {
                    public void run() {
                                        panereadyToRun.getChildren().remove(R);
                    }
                });
            }
        })).start();
    }
    public void playblockTodevice() {//队列之间的小球转移
        final Rectangle R = new Rectangle(20,20);
        R.setFill(Paint.valueOf("#FF6347"));
        this.paneblockTodevice.getChildren().add(R);
        blockTodevice.setNode(R);
        blockTodevice.play();
        (new Thread(new Runnable()
        {
            public void run() {
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcessDispatcher.class.getName()).log(Level.SEVERE, (String)null, ex);
                }
                Platform.runLater(new Runnable()
                {
                    public void run() {
                        /* 428 */                     ProcessDispatcher.this.paneblockTodevice.getChildren().remove(R);
                        /*     */                   }
                });
            }
        })).start();
    }
    public void playdeviceToReady() {//队列之间的小球转移
        final Rectangle R = new Rectangle(20,20);
        R.setFill(Paint.valueOf("#48D1CC"));
        this.panedeviceToReady.getChildren().add(R);
        deviceToReady.setNode(R);
        deviceToReady.play();
        (new Thread(new Runnable()
        {
            public void run() {
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcessDispatcher.class.getName()).log(Level.SEVERE, (String)null, ex);
                }
                Platform.runLater(new Runnable()
                {
                    public void run() {
                        /* 428 */                     ProcessDispatcher.this.panedeviceToReady.getChildren().remove(R);
                        /*     */                   }
                });
            }
        })).start();
    }

    public static ObservableList<PCB> getReadyProcessList() {
        return readyProcessList;
    }

    public static void setReadyProcessList(ObservableList<PCB> readyProcessList) {
        ProcessDispatcher.readyProcessList = readyProcessList;
    }

    public static boolean isIsDispatcher() {
        return isDispatcher;
    }

    public static void setIsDispatcher(boolean isDispatcher) {
        ProcessDispatcher.isDispatcher = isDispatcher;
    }
}
