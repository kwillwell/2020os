package controller;

import CPU.CPU;
import device.DeviceDispatcher;
import device.device;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Path;
import memory.MemoryDispatcher;
import process.PCB;
import process.ProcessDispatcher;
import Action.createprocess;
import java.net.URL;
import java.util.ResourceBundle;



public class ScheduiUIController implements Initializable{

    private CPU cpu;
    private MemoryDispatcher memoryDispatcher;
    private ProcessDispatcher processDispatcher;
    private DeviceDispatcher deviceDispatcher;

    @FXML // fx:id="BlockedRRT"
    private TableColumn<PCB, DoubleProperty> BlockedRRT; // Value injected by FXMLLoader

    @FXML // fx:id="endedRT"
    private TableColumn<PCB, DoubleProperty> endedRT; // Value injected by FXMLLoader

    @FXML // fx:id="BlockedTRT"
    private TableColumn<PCB, DoubleProperty> BlockedTRT; // Value injected by FXMLLoader

    @FXML // fx:id="CPU"
    private ImageView CPU1; // Value injected by FXMLLoader

    @FXML // fx:id="endedName"
    private TableColumn<PCB, StringProperty> endedName; // Value injected by FXMLLoader

    @FXML // fx:id="DevicePName"
    private TableColumn<device, String> DevicePName; // Value injected by FXMLLoader

    @FXML // fx:id="processBar"
    private TableColumn<PCB, Double> processBar; // Value injected by FXMLLoader

    @FXML // fx:id="DeviceRT"
    private TableColumn<device, Double> DeviceRT; // Value injected by FXMLLoader

    @FXML // fx:id="endedresult"
    private TableColumn<PCB, StringProperty> endedresult; // Value injected by FXMLLoader

    @FXML // fx:id="RemanTime"
    private TextField RemanTime; // Value injected by FXMLLoader

    @FXML // fx:id="RunningOrder"
    private TextField RunningOrder; // Value injected by FXMLLoader

    @FXML // fx:id="BlockedDeviceType"
    private TableColumn<PCB, StringProperty> BlockedDeviceType; // Value injected by FXMLLoader

    @FXML // fx:id="Readylength"
    private TableColumn<PCB, IntegerProperty> Readylength; // Value injected by FXMLLoader

    @FXML // fx:id="ReadyRestTime"
    private TableColumn<PCB, DoubleProperty> ReadyRestTime; // Value injected by FXMLLoader

    @FXML // fx:id="BlockedProcess"
    private TableView<PCB> BlockedProcess; // Value injected by FXMLLoader

    @FXML // fx:id="ReadyRT"
    private TableColumn<PCB, DoubleProperty> ReadyRT; // Value injected by FXMLLoader

    @FXML // fx:id="BlockedName"
    private TableColumn<PCB, StringProperty> BlockedName; // Value injected by FXMLLoader

    @FXML // fx:id="ProcessUsage"
    private TextField ProcessUsage; // Value injected by FXMLLoader

    @FXML // fx:id="playBTN"
    private Button playBTN; // Value injected by FXMLLoader

    @FXML // fx:id="EndedTable"
    private TableView<PCB> EndedTable; // Value injected by FXMLLoader

    @FXML // fx:id="TemporaryResults"
    private TextField TemporaryResults; // Value injected by FXMLLoader

    @FXML // fx:id="RunnningProcess"
    private TextField RunnningProcess; // Value injected by FXMLLoader

    @FXML // fx:id="createBTN"
    private Button createBTN; // Value injected by FXMLLoader

    @FXML // fx:id="stackPane"
    private StackPane stackPane; // Value injected by FXMLLoader

    @FXML // fx:id="MemoryUsage"
    private TextField MemoryUsage; // Value injected by FXMLLoader

    @FXML // fx:id="DeviceUsage"
    private TextField DeviceUsage; // Value injected by FXMLLoader

    @FXML // fx:id="Ready"
    private TableView<PCB> Ready; // Value injected by FXMLLoader

    @FXML // fx:id="Readyname"
    private TableColumn<PCB, StringProperty> Readyname; // Value injected by FXMLLoader

    @FXML // fx:id="BlockeddeviceTime"
    private TableColumn<PCB, DoubleProperty> BlockeddeviceTime; // Value injected by FXMLLoader

    @FXML // fx:id="integrateBTN"
    private Button integrateBTN; // Value injected by FXMLLoader

    @FXML // fx:id="DeviceTable"
    private TableView<device> DeviceTable; // Value injected by FXMLLoader

    @FXML // fx:id="device"
    private TableColumn<device, Character> devicename; // Value injected by FXMLLoader

    @FXML
    private BorderPane borderPane;
    @FXML
    public Pane memorypane;
    @FXML
    private Path pathrunToEnd;
    @FXML
    private Path pathreadyToRun;
    @FXML
    private Path pathdeviceToReady;
    @FXML
    private Path pathblockTodevice;

    @FXML
    private Pane panerunToEnd;
    @FXML
    private Pane panereadyToRun;
    @FXML
    private Pane panedeviceToReady;
    @FXML
    private Pane paneblockTodevice;





    @FXML
    private void playorstop(ActionEvent event) {
        if(ProcessDispatcher.isIsDispatcher()){
            ProcessDispatcher.setIsDispatcher(false);
            playBTN.setText("开始运行");
        }else{
            ProcessDispatcher.setIsDispatcher(true);
            playBTN.setText("暂停运行");
        }
        return;
    }

    @FXML
    private void createprocess(ActionEvent event) {
        new createprocess();
        return;
    }

    @FXML
    private void integrateMemory(ActionEvent event) {
        return;

    }



    private static ScheduiUIController scheduiUIController;
    public static synchronized ScheduiUIController getInstance(){
        if (scheduiUIController==null){
            scheduiUIController=new ScheduiUIController();
        }
        return scheduiUIController;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     this.cpu= CPU.getInstance();
     this.memoryDispatcher=MemoryDispatcher.getInstance();
     this.processDispatcher=ProcessDispatcher.getInstance();
     this.deviceDispatcher= DeviceDispatcher.getInstance();

     this.processDispatcher.setBlockedTableView(this.BlockedProcess,this.BlockedName,this.BlockedDeviceType,this.BlockeddeviceTime,this.BlockedTRT,this.BlockedRRT);
     this.processDispatcher.setDeviceTableView(this.DeviceTable,this.devicename,this.DevicePName,this.DeviceRT);
     this.processDispatcher.setEndTableView(this.EndedTable,this.endedRT,this.endedName,this.endedresult);
     this.processDispatcher.setReadyTableView(this.Ready,this.Readyname,this.ReadyRT,this.ReadyRestTime,this.Readylength,this.processBar);

     this.processDispatcher.setPaneblockTodevice(this.paneblockTodevice);
     this.processDispatcher.setPanedeviceToReady(this.panedeviceToReady);
     this.processDispatcher.setPanereadyToRun(this.panereadyToRun);
     this.processDispatcher.setPanerunToEnd(this.panerunToEnd);

     this.processDispatcher.setPathblockTodevice(this.pathblockTodevice);
     this.processDispatcher.setPathdeviceToReady(this.pathdeviceToReady);
     this.processDispatcher.setPathreadyToRun(this.pathreadyToRun);
     this.processDispatcher.setPathrunToEnd(this.pathrunToEnd);
        this.CPU1.setImage(new Image("/cpu1.png"));
        this.getRunningOrder().setText("zhiling");

    }

    public ImageView getCPU() {
        return CPU1;
    }

    public void setCPU(ImageView CPU) {
        this.CPU1 = CPU;
    }


    public TextField getRemanTime() {
        return RemanTime;
    }

    public void setRemanTime(TextField remanTime) {
        RemanTime = remanTime;
    }

    public TextField getRunningOrder() {
        return RunningOrder;
    }

    public void setRunningOrder(TextField runningOrder) {
        RunningOrder = runningOrder;
    }

    public Button getPlayBTN() {
        return playBTN;
    }

    public void setPlayBTN(Button playBTN) {
        this.playBTN = playBTN;
    }


    public TextField getProcessUsage() {
        return ProcessUsage;
    }

    public void setProcessUsage(TextField processUsage) {
        ProcessUsage = processUsage;
    }


    public TextField getTemporaryResults() {
        return TemporaryResults;
    }

    public void setTemporaryResults(TextField temporaryResults) {
        TemporaryResults = temporaryResults;
    }

    public Button getCreateBTN() {
        return createBTN;
    }

    public void setCreateBTN(Button createBTN) {
        this.createBTN = createBTN;
    }

    public TextField getRunnningProcess() {
        return RunnningProcess;
    }

    public void setRunnningProcess(TextField runnningProcess) {
        RunnningProcess = runnningProcess;
    }

    public TextField getMemoryUsage() {
        return MemoryUsage;
    }

    public void setMemoryUsage(TextField memoryUsage) {
        MemoryUsage = memoryUsage;
    }

    public TextField getDeviceUsage() {
        return DeviceUsage;
    }

    public void setDeviceUsage(TextField deviceUsage) {
        DeviceUsage = deviceUsage;
    }


    public Button getIntegrateBTN() {
        return integrateBTN;
    }

    public void setIntegrateBTN(Button integrateBTN) {
        this.integrateBTN = integrateBTN;
    }



    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public static ScheduiUIController getScheduiUIController() {
        return scheduiUIController;
    }

    public static void setScheduiUIController(ScheduiUIController scheduiUIController) {
        ScheduiUIController.scheduiUIController = scheduiUIController;
    }

    public TableColumn<PCB, DoubleProperty> getBlockedRRT() {
        return BlockedRRT;
    }

    public void setBlockedRRT(TableColumn<PCB, DoubleProperty> blockedRRT) {
        BlockedRRT = blockedRRT;
    }

    public TableColumn<PCB, DoubleProperty> getEndedRT() {
        return endedRT;
    }

    public void setEndedRT(TableColumn<PCB, DoubleProperty> endedRT) {
        this.endedRT = endedRT;
    }

    public TableColumn<PCB, DoubleProperty> getBlockedTRT() {
        return BlockedTRT;
    }

    public void setBlockedTRT(TableColumn<PCB, DoubleProperty> blockedTRT) {
        BlockedTRT = blockedTRT;
    }

    public TableColumn<PCB, StringProperty> getEndedName() {
        return endedName;
    }

    public void setEndedName(TableColumn<PCB, StringProperty> endedName) {
        this.endedName = endedName;
    }


    public TableColumn<PCB, Double> getProcessBar() {
        return processBar;
    }

    public void setProcessBar(TableColumn<PCB, Double> processBar) {
        this.processBar = processBar;
    }

    public TableColumn<device, String> getDevicePName() {
        return DevicePName;
    }

    public void setDevicePName(TableColumn<device, String> devicePName) {
        DevicePName = devicePName;
    }

    public TableColumn<device, Double> getDeviceRT() {
        return DeviceRT;
    }

    public void setDeviceRT(TableColumn<device, Double> deviceRT) {
        DeviceRT = deviceRT;
    }

    public void setDeviceTable(TableView<device> deviceTable) {
        DeviceTable = deviceTable;
    }

    public void setDevice(TableColumn<device, Character> device) {
        this.devicename = device;
    }

    public TableColumn<PCB, StringProperty> getEndedresult() {
        return endedresult;
    }

    public void setEndedresult(TableColumn<PCB, StringProperty> endedresult) {
        this.endedresult = endedresult;
    }

    public TableColumn<PCB, StringProperty> getBlockedDeviceType() {
        return BlockedDeviceType;
    }

    public void setBlockedDeviceType(TableColumn<PCB, StringProperty> blockedDeviceType) {
        BlockedDeviceType = blockedDeviceType;
    }

    public TableColumn<PCB, IntegerProperty> getReadylength() {
        return Readylength;
    }

    public void setReadylength(TableColumn<PCB, IntegerProperty> readylength) {
        Readylength = readylength;
    }

    public TableColumn<PCB, DoubleProperty> getReadyRestTime() {
        return ReadyRestTime;
    }

    public void setReadyRestTime(TableColumn<PCB, DoubleProperty> readyRestTime) {
        ReadyRestTime = readyRestTime;
    }

    public TableView<PCB> getBlockedProcess() {
        return BlockedProcess;
    }

    public void setBlockedProcess(TableView<PCB> blockedProcess) {
        BlockedProcess = blockedProcess;
    }

    public TableColumn<PCB, DoubleProperty> getReadyRT() {
        return ReadyRT;
    }

    public void setReadyRT(TableColumn<PCB, DoubleProperty> readyRT) {
        ReadyRT = readyRT;
    }

    public TableColumn<PCB, StringProperty> getBlockedName() {
        return BlockedName;
    }

    public void setBlockedName(TableColumn<PCB, StringProperty> blockedName) {
        BlockedName = blockedName;
    }

    public TableView<PCB> getEndedTable() {
        return EndedTable;
    }

    public void setEndedTable(TableView<PCB> endedTable) {
        EndedTable = endedTable;
    }

    public TableView<PCB> getReady() {
        return Ready;
    }

    public void setReady(TableView<PCB> ready) {
        Ready = ready;
    }

    public TableColumn<PCB, StringProperty> getReadyname() {
        return Readyname;
    }

    public void setReadyname(TableColumn<PCB, StringProperty> readyname) {
        Readyname = readyname;
    }

    public TableColumn<PCB, DoubleProperty> getBlockeddeviceTime() {
        return BlockeddeviceTime;
    }

    public void setBlockeddeviceTime(TableColumn<PCB, DoubleProperty> blockeddeviceTime) {
        BlockeddeviceTime = blockeddeviceTime;
    }
    public void setorder(String order){
        this.RunningOrder.setText(order);
    }

}
