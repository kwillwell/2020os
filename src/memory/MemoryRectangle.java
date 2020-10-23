package memory;

import controller.ScheduiUIController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import process.PCB;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MemoryRectangle
        extends Pane
{
    private static Pane memorypane=ScheduiUIController.getInstance().memorypane;
    private Color[] colors = new Color[] { Color.AQUA, Color.BURLYWOOD, Color.CORNFLOWERBLUE, Color.RED, Color.YELLOW, Color.YELLOWGREEN, Color.GREEN };
    private Rectangle rectangle;
    private Pane pane;
    private ProgressIndicator pi;
    private Label pidLabel;

    public MemoryRectangle( double x ,double width, double height, final PCB pcb) {
        final double true_x=x/512D;
        final double true_width=width/512D;
        this.layoutXProperty().bind(memorypane.widthProperty().multiply(true_x));
        this.prefHeightProperty().bind(memorypane.heightProperty());
        this.prefWidthProperty().bind(memorypane.widthProperty().multiply(true_width));
        this.rectangle = new Rectangle(width, height);
        this.rectangle.setStroke(Color.BLACK);
        this.rectangle.setFill(getRandomColor());
        this.rectangle.setArcWidth(15.0D);
        this.rectangle.setArcHeight(15.0D);
        getChildren().add(this.rectangle);
        this.pi = new ProgressIndicator();
        this.pi.setLayoutY(10.0D);
        this.pi.setPrefSize(true_width, true_width);
        this.pi.prefWidthProperty().bind(this.prefWidthProperty());
        this.pi.setProgress(0.0D);
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                while (pcb.getResttime() != 0.0D) {


                    Platform.runLater(new Runnable()
                    {
                        public void run() {
                            MemoryRectangle.this.pi.setProgress((pcb.getRuntime() - pcb.getResttime()) / pcb.getRuntime());//圆圈进度条
                        }
                    });
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MemoryRectangle.class.getName()).log(Level.SEVERE, (String)null, ex);
                    }
                }
            }
        });
        thread.setDaemon(true);thread.setName("memoryractangle-run");
        thread.start();

//        if (!pcb.getPID().equals("OS")) {
//            getChildren().add(this.pi);
//        }
        this.pidLabel = new Label(pcb.getName());
        this.pidLabel.setLayoutY(100.0D);
        this.pidLabel.setLayoutX(width / 2.0D - 10.0D);
        getChildren().add(this.pidLabel);
    }

    private Color getRandomColor() {
        return this.colors[(int)(Math.random() * this.colors.length)];
    }
}