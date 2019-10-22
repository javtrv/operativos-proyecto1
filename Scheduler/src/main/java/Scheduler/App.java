package Scheduler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public final class App{
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Scheduler sc = new Scheduler();
        sc.executeScheduler();

        /*Interface ic = new Interface();
        ic.start();

        System.out.println("guachiman");*/
    }
}
