
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {

    @FXML
    private TableView<Process> tableView;

    @FXML
    private TableView<SchedEntity> tableView_;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label label1;
    @FXML
    private Label label2;

    @FXML
    private Label cpuON;

    @FXML
    private Label cpuOFF;

    @FXML
    private Label timeProm;

    @FXML
    private Label totalProc;

    @FXML
    private Label time;






    ScheduledExecutorService scheduledExecutorService;

    Scheduler sc = new Scheduler();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // PROCESS TABLE COLS
        TableColumn  pid= new TableColumn("PID");
        TableColumn tiempoCPU = new TableColumn("TIEMPO CPU");
        TableColumn tiempoIO = new TableColumn("TIEMPO IO");
        pid.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33333333333));
        tiempoCPU.prefWidthProperty().bind(tableView.widthProperty().multiply(0.333333333333));
        tiempoIO.prefWidthProperty().bind(tableView.widthProperty().multiply(0.333333333));

        tableView.getColumns().addAll(pid, tiempoCPU, tiempoIO);

        // SCHEDENTITY COLS
        TableColumn pid_ = new TableColumn("PID");
        TableColumn vruntime = new TableColumn("VRUNTIME");
        pid_.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        vruntime.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        tableView_.getColumns().addAll(pid_, vruntime);



        //Step : 1# Create a person class that will represtent data

        //Step : 2# Define data in an Observable list and add data as you want to show inside table
        ObservableList<Process> data = FXCollections.observableArrayList();
        ObservableList<SchedEntity> data_ = FXCollections.observableArrayList();
        ObservableList<String> data1 = FXCollections.observableArrayList();

        //Step : 3#  Associate data with columns
        pid.setCellValueFactory(new PropertyValueFactory<Process,String>("pid"));

        tiempoCPU.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tiempoCpu"));

        tiempoIO.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tiempoIo"));


        pid_.setCellValueFactory(new PropertyValueFactory<Process,String>("pid"));

        vruntime.setCellValueFactory(new PropertyValueFactory<Process,Integer>("vruntime"));


        // Boton

        //ActionEvent e;

        ;

        //Step 4: add data inside table


        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        sc.executeScheduler();
        scheduledExecutorService.scheduleAtFixedRate(() -> {


            // Update screen

            Platform.runLater(() -> {

                // Obtener process table
                tableView.getItems().clear();
                HashMap<String, Process> processTable = sc.getProcessTable();
                data.addAll(processTable.values());
                tableView.setItems(data);

                // Obtener sched table
                tableView_.getItems().clear();
                HashMap<String, SchedEntity> schedTable = sc.getSchedEntity();
                data_.addAll(schedTable.values());
                tableView_.setItems(data_);

                // Obtener block list

                listView.getItems().clear();
                LinkedList<String> blockQueue = sc.getBlockQueue();
                data1.addAll(blockQueue);
                listView.setItems(data1);

                // Labels

                // Get current process
                String current = sc.getCurrent();
                label1.setText(current);

                // Get current IO Process
                String currentIO = sc.getCurrentIO();
                label2.setText(currentIO);

                // Get % CPUON

                String cpu_on = Integer.toString(sc.getCpuOn()) + " %";
                cpuON.setText(cpu_on);

                // Get % CPUOFF

                String cpu_Off = Integer.toString(sc.getCpuOff()) + " %";
                cpuOFF.setText(cpu_Off);

                // Get prom

                String prom_ = Integer.toString(sc.calculateTimeProm()) + " ms";
                timeProm.setText(prom_);

                // Get total proc

                String procs = Integer.toString(sc.processTableSize());
                totalProc.setText(procs);

                // Get tiempo

                String time_sim = Integer.toString(sc.getTotalTime()) + " ms";
                time.setText(time_sim);


            });
        }, 0, 1, TimeUnit.MILLISECONDS);



    }

}
