import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;



class Scheduler{

    private RedBlackBST<Integer, Process> readyTree;
    private ProcessQueue blockQueue;
    private ArrayList<Process> newProcesses;
    private int prueba = 1;
    // Variables to control scheduler
    private volatile boolean cpuEmpty = true;
    private volatile boolean finishCPUTime = false;
    private volatile boolean finishIOTime = false;
    private volatile int currentPID = 0;
    private int min = -1; // Menor pid


    public Scheduler(){
        
        //this.newProcesses = new ArrayList<Process>();
        this.blockQueue = new ProcessQueue();
        this.readyTree = new RedBlackBST<Integer, Process>();
        
    }

        // Hilo que simula la ejecucion del proceso en el CPU
        class CPUSimulator extends Thread {
            // Le debo pasar el timeslice que el proceso va a correr
            long timeslice;
            public CPUSimulator(long time){
                this.timeslice = time;
            }
            @Override
            public void run(){
                try{
                    sleep(this.timeslice);
                    StdOut.println("Running process");
                }catch(InterruptedException e){
                }
            }
        }
    
        // Hilo que simula la ejecucion del IO
        class IOSimulator extends Thread{
            // Le debo pasar el timeslice que el proceso va a pasar en io
            long timeslice;
            public IOSimulator(long time){
                this.timeslice = time;
            }
            @Override
            public void run(){
                try{
                    sleep(this.timeslice);
                    StdOut.println("Running IO");
                }catch(InterruptedException e){
                }
            }
        }
    
        // ***************************************** HILOS QUE INTERACTUAN CON EL RBT *******************************************************
        // Hilo que elimina del arbol al iniciar el scheduler o cuando el quantum de tiemo culmina
        class ProcessRemove extends Thread{
            // Le debo pasar el timeslice que el proceso va a correr: YA NO NECESITO ESPERAR, SOLO REVISO QUE LA VARIABLE ESTE EN QUE NO PUEDO SACAR
            @Override
            public void run(){
                // Si no ha terminado el tiempo de CPU y ademas el cpu esta lleno espero
                while(true){
                    if(finishCPUTime || cpuEmpty){
                        min = readyTree.min();
                        readyTree.deleteMin();
                    }
                }

            }
        }
    
        // Hilo que "saca" del cpu y agrega en el arbol cuando el quantum de tiempo del proceso termina
        class ProcessInsert extends Thread {
            // Le debo pasar el timeslice que el proceso va a correr
            @Override
            public void run(){
                while(true){
                    if(finishCPUTime){
                        // Meto el current en el arbol
                        // el min ahora es el current
                    }
                }
                // Debo considerar que el tiempo de io sea 0 para agregar en el arbol
                // Inserto el proceso con el PID de min
                //readyTree.put(p.get_vruntime(), p);
    
            }
        }
    
        // Hilo que "saca" del cpu y agrega en la cola cuando hay una interrupcion de IO
        // DEBEMOS DECIDIR CUANDO HACER ESTAS INTERRUPCIONES
        class BlockingQueueInsert extends Thread {
            // Le debo pasar el timeslice que el proceso va a correr
            @Override
            public void run(){

            }
        }
        // Hilo que elimina de la cola de bloqueados e inserta en el arbol
        class BlockingQueueRemove extends Thread{
            // Le debo pasar el timeslice que el proceso va a correr
            @Override
            public void run(){

            }
        }
        // GenerateProcess deberia ser un hilo que genere procesos cada x tiempo
    
            
    
    class initSchedulerThread extends Thread{
        @Override
        public void run(){
            // Load Processes to New Processes list
            newProcesses = Parser.ParseToProcess("procesos1.json");
    
            for (Process process: newProcesses) {
                System.out.println(newProcesses);
                try{
                    sleep(5000);
                    readyTree.put(process.get_vruntime(), process);
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
    public void initScheduler() { //Aqui corremos el hilo de iniciar el Scheduler
        
        //CPUSimulator t1 = new CPUSimulator();
        //t1.run();
        initSchedulerThread init = new initSchedulerThread(); 
        init.start();
    }

    public void executeScheduler(){
        // Init Threads


    }

}
