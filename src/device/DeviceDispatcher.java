package device;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;

public class DeviceDispatcher {

    private static int size = 8;
    public static ObservableList<device> deviceList;
    private static Timer[] equipTime = new Timer[size];
    private static Timer[] clockTime = new Timer[size];
    private static DeviceDispatcher deviceDispatcher;


    static {
        deviceList= FXCollections.observableArrayList();
        deviceList.add(new device('A'));
        deviceList.add(new device('A'));
        deviceList.add(new device('B'));
        deviceList.add(new device('B'));
        deviceList.add(new device('B'));
        deviceList.add(new device('C'));
        deviceList.add(new device('C'));
        deviceList.add(new device('C'));

    }
    public static DeviceDispatcher getInstance(){
        if (deviceDispatcher==null){
            deviceDispatcher=new DeviceDispatcher();
        }
        return deviceDispatcher;
    }


    public static boolean checkEquipment(char name){
        if(name=='A'||name=='a'){
            if(!deviceList.get(0).isOccupied()||!deviceList.get(1).isOccupied())
                return true;
        }
        else if(name=='B'||name=='b'){
            if(!deviceList.get(2).isOccupied()||!deviceList.get(3).isOccupied()||!deviceList.get(4).isOccupied())
                return true;
        }
        else if(name=='C'||name=='c'){
            if(!deviceList.get(5).isOccupied()||!deviceList.get(6).isOccupied()||!deviceList.get(7).isOccupied())
                return true;
        }
        return false;
    }
    public static boolean requestDevice(String processname,char name, int time) {
        if (name == 'A') {
            if (!deviceList.get(0).isOccupied()) {
                deviceList.get(0).requestDevice(processname,time);
                setTimerState( time,  0);
                return true;
            } else if(!deviceList.get(1).isOccupied()){
                deviceList.get(1).requestDevice(processname,time);
                setTimerState( time,  1);
                return true;
            }else{
                return false;
            }

        } else if (name == 'B') {
            if (!deviceList.get(2).isOccupied()) {
                deviceList.get(2).requestDevice(processname,time);
                setTimerState( time,  2);
                return true;
            } else if(!deviceList.get(3).isOccupied()){
                deviceList.get(3).requestDevice(processname,time);
                setTimerState( time,  3);
                return true;
            }
            else if (!deviceList.get(4).isOccupied()){
                deviceList.get(4).requestDevice(processname,time);
                setTimerState( time,  4);
                return true;
            }else {
                return false;
            }
        } else if (name == 'C') {
            if (!deviceList.get(5).isOccupied()) {
                deviceList.get(5).requestDevice(processname,time);
                setTimerState( time,  5);
                return true;
            } else if(!deviceList.get(6).isOccupied()){
                deviceList.get(6).requestDevice(processname,time);
                setTimerState( time,  6);
                return true;
            }
            else if (!deviceList.get(7).isOccupied()){
                deviceList.get(7).requestDevice(processname,time);
                setTimerState( time,  7);
                return true;
            }else {
                return false;
            }
        }else
            return false;

    }
    public static void setTimerState( int time, int index) {


        equipTime[index] = new Timer(1000 * time, e -> {//延时time秒
            deviceList.get(index).setOccupied(false);
            deviceList.get(index).setProcess("");
            deviceList.get(index).setOccupiedTime(0);
            equipTime[index].stop();
            clockTime[index].stop();
        });

        clockTime[index] = new Timer(1000, e -> {//更新剩余时间
            deviceList.get(index).setOccupiedTime( deviceList.get(index).getOccupiedTime()-1);

        });
        equipTime[index].start();
        clockTime[index].start();
    }

    public static ObservableList<device> getDeviceList() {
        return deviceList;
    }

    public static void setDeviceList(ObservableList<device> deviceList) {
        deviceDispatcher.deviceList = deviceList;
    }
}
