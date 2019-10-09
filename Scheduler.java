import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Scheduler{

    private RedBlackBST<Integer, Process> readyTree;
    private ProcessQueue blockQueue;
    private ArrayList<Process> newProcesses;

    public Scheduler(){
        
        //this.newProcesses = new ArrayList<Process>();
        this.blockQueue = new ProcessQueue();
        this.readyTree = new RedBlackBST<Integer, Process>();
        
    }
    public void initScheduler() {
        
        // Load Processes to New Processes list
        this.newProcesses = Parser.ParseToProcess("procesos1.json");

        // Load Processes to RBT 
        
        for (Process p : newProcesses) {
            readyTree.put(p.get_vruntime(), p);
        }
        // Processes in RBT
        StdOut.println(readyTree.keys());
        
    }

    public void executeScheduler(){
        // Init Threads

    }

}