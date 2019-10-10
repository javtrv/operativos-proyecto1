import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Scheduler{

    private RedBlackBST<Integer, Process> readyTree;
    private ProcessQueue blockQueue;
    private ArrayList<Process> newProcesses;
    private int prueba = 1;
    // Variables to control scheduler
    private boolean cpuEmpty = true;
    private finishCPUTime = false;
    private finishIOTime = false;
    private currentPID = 0;


    public Scheduler(){
        
        //this.newProcesses = new ArrayList<Process>();
        this.blockQueue = new ProcessQueue();
        this.readyTree = new RedBlackBST<Integer, Process>();
        
    }

    // Hilo que simula la ejecucion del proceso en el CPU
    class CPUSimulator implements Runnable {
        // Le debo pasar el timeslice que el proceso va a correr

        public void run(){
            StdOut.println(prueba);
            prueba = 2;
            StdOut.println(prueba);
        }
    }

    // Hilo que simula la ejecucion del IO
    class IOSimulator implements Runnable {
        // Le debo pasar el timeslice que el proceso va a pasar en io
        
        public void run(){
            StdOut.println(prueba);
            prueba = 2;
            StdOut.println(prueba);
        }
    }

    // Hilo que elimina del arbol al iniciar el scheduler o cuando el quantum de tiemo culmina
    class ProcessRemove implements Runnable {
        // Le debo pasar el timeslice que el proceso va a correr
        
        public void run(){
            StdOut.println(prueba);
            prueba = 2;
            StdOut.println(prueba);
        }
    }

    // Hilo que "saca" del cpu y agrega en el arbol cuando el quantum de tiempo del proceso termina
    class ProcessInsert implements Runnable {
        // Le debo pasar el timeslice que el proceso va a correr
        
        public void run(){
            StdOut.println(prueba);
            prueba = 2;
            StdOut.println(prueba);
        }
    }

    // Hilo que "saca" del cpu y agrega en la cola cuando hay una interrupcion de IO
    // DEBEMOS DECIDIR CUANDO HACER ESTAS INTERRUPCIONES
    class BlockingQueueInsert implements Runnable {
        // Le debo pasar el timeslice que el proceso va a correr
        
        public void run(){
            StdOut.println(prueba);
            prueba = 2;
            StdOut.println(prueba);
        }
    }
    // Hilo que elimina de la cola de bloqueados e inserta en el arbol
    class BlockingQueueRemove implements Runnable {
        // Le debo pasar el timeslice que el proceso va a correr
        
        public void run(){
            StdOut.println(prueba);
            prueba = 2;
            StdOut.println(prueba);
        }
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
        
        CPUSimulator t1 = new CPUSimulator();
        t1.run();
    }

    public void executeScheduler(){
        // Init Threads


    }

}