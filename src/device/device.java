package device;

public class device {
    private Character name;
    private String process;
    private boolean occupied;
    private double occupiedTime;

    public device(char name) {
        this.name = name;
        this.occupied=false;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public double getOccupiedTime() {
        return occupiedTime;
    }

    public void setOccupiedTime(double occupiedTime) {
        this.occupiedTime = occupiedTime;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public boolean requestDevice(String name,int time){
        if (isOccupied()){
            return false;
        }else {
            this.process=name;
            this.occupiedTime=time;
            setOccupied(true);
            return true;
        }
    }
}
