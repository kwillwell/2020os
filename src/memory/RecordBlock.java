package memory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class RecordBlock {

    private StringProperty name;
    private IntegerProperty startpointer;//进程指令在程序区的起始地址
    private IntegerProperty endpointer;//进程指令在程序区的起始地址
    private int size;
    public RecordBlock(StringProperty name, IntegerProperty startpointer, IntegerProperty endpointer) {
        this.name = name;
        this.startpointer = startpointer;
        this.endpointer = endpointer;
        this.size=endpointer.get()-startpointer.get()+1;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getStartpointer() {
        return startpointer.get();
    }

    public IntegerProperty startpointerProperty() {
        return startpointer;
    }

    public void setStartpointer(int startpointer) {
        this.startpointer.set(startpointer);
    }

    public int getEndpointer() {
        return endpointer.get();
    }

    public IntegerProperty endpointerProperty() {
        return endpointer;
    }

    public void setEndpointer(int endpointer) {
        this.endpointer.set(endpointer);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
