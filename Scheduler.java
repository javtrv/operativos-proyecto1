import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;



class initSchedulerThread extends Thread{
    @Override
    public void run(){
        // Load Processes to New Processes list
        newProcesses = Parser.ParseToProcess("procesos1.json");

        for (Process process: newProcesses) {
            System.out.println(newProcesses);
            try{
                sleep(5000);
                readyTree.put(p.get_vruntime(), p);
            }catch(InterruptedException e){
            }
        }

        // Load Processes to RBT 
        
        /*for (Process p : newProcesses) {
            readyTree.put(p.get_vruntime(), p);
        }
        // Processes in RBT
        StdOut.println(readyTree.keys());*/
    }
}

class Scheduler{
    public static RedBlackBST<Integer, Process> readyTree;
    public static ProcessQueue blockQueue;
    public static ArrayList<Process> newProcesses;

    public Scheduler(){
        
        //this.newProcesses = new ArrayList<Process>();
        blockQueue = new ProcessQueue();
        readyTree = new RedBlackBST<Integer, Process>();
        
    }
    public void initScheduler() { //Aqui corremos el hilo de iniciar el Scheduler
        
        initSchedulerThread init = new initSchedulerThread(); 
        init.start();
    }

    public void executeScheduler(){
        // Init Threads

    }

}
