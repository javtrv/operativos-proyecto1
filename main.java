/**
 * main
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.*;
import java.io.*;

public class main {

        /**
     * Unit tests the {@code Scheduler} class.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    	System.out.println("ARGS 0: " + args[0]);
        Scheduler sc = new Scheduler();
        sc.executeScheduler(args[0]);

    }
}
