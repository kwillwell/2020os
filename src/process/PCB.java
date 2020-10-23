package process;

import javafx.beans.property.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;

public class PCB {

    private int PID;
    private StringProperty name;
    private StringProperty status;
    private DoubleProperty runtime;
    private DoubleProperty resttime;
    private int PC;
    private int startpointer;//进程指令在程序区的起始地址
    private int endpointer;//进程指令在程序区的起始地址
    private IntegerProperty length;
    private StringProperty needdevice;//进程临时所需设备
    private IntPredicate holdTime;//进程占用设备时间
    private Map<Character, Integer> VariableArea;
    private StringProperty result;
    private SimpleDoubleProperty progress;
    private int PSW;
    public PCB(String name) {
        this.name=new SimpleStringProperty();
        this.status=new SimpleStringProperty();
        this.resttime=new SimpleDoubleProperty();
        this.runtime=new SimpleDoubleProperty();
        this.length=new SimpleIntegerProperty();
        this.needdevice=new SimpleStringProperty();
        this.result=new SimpleStringProperty();
        this.progress=new SimpleDoubleProperty();
        this.VariableArea=new HashMap<>();
        this.name.set(name);
    }

    public void OutputResuit(){
        if (VariableArea.isEmpty()){
            return;
        }
        String resultstr=new String();
        for (Map.Entry<Character,Integer> entry:VariableArea.entrySet()){
            resultstr+=entry.getKey()+"="+entry.getValue()+";";
        }
        setResult(resultstr);
    }


    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public java.lang.String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name.set(name);
    }

    public java.lang.String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status.set(status);
    }

    public double getRuntime() {
        return runtime.get();
    }

    public DoubleProperty runtimeProperty() {
        return runtime;
    }

    public void setRuntime(double runtime) {
        this.runtime.set(runtime);
    }

    public double getResttime() {
        return resttime.get();
    }

    public DoubleProperty resttimeProperty() {
        return resttime;
    }

    public void setResttime(double resttime) {
        this.resttime.set(resttime);
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getStartpointer() {
        return startpointer;
    }

    public void setStartpointer(int startpointer) {
        this.startpointer = startpointer;
    }

    public int getEndpointer() {
        return endpointer;
    }

    public void setEndpointer(int endpointer) {
        this.endpointer = endpointer;
    }

    public int getLength() {
        return length.get();
    }

    public IntegerProperty lengthProperty() {
        return length;
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    public java.lang.String getNeeddevice() {
        return needdevice.get();
    }

    public StringProperty needdeviceProperty() {
        return needdevice;
    }

    public void setNeeddevice(java.lang.String needdevice) {
        this.needdevice.set(needdevice);
    }

    public IntPredicate getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(IntPredicate holdTime) {
        this.holdTime = holdTime;
    }

    public Map<Character, Integer> getVariableArea() {
        return VariableArea;
    }

    public void setVariableArea(Map<Character, Integer> variableArea) {
        VariableArea = variableArea;
    }

    public java.lang.String getResult() {
        return result.get();
    }

    public StringProperty resultProperty() {
        return result;
    }

    public void setResult(java.lang.String result) {
        this.result.set(result);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public int getPSW() {
        return PSW;
    }

    public void setPSW(int PSW) {
        this.PSW = PSW;
    }
}
