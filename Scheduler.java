import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.*; 



class Scheduler{

    // Scheduler Structures
    private volatile RedBlackTree readyTree;
    private volatile LinkedBlockingQueue<Process> blockQueue;
    private ArrayList<Process> newProcesses;
    // Variables to control scheduler
    private volatile boolean cpuEmpty;
    private volatile boolean finishCPUTime;
    private volatile boolean finishIOTime;
    private volatile String currentPID;
    private volatile boolean minSet;
    private volatile Process min; 
    private volatile boolean currentExamined;
    private volatile Process currentProcess;
    private volatile Process currentIOProcess;
    private volatile boolean ioSet;
    private volatile boolean ioEmpty;
    private volatile int CPUtimeslice;
    private volatile int IOtimeslice;
    private volatile boolean noMoreProcess;
    private volatile boolean addedToBQ;
    private volatile HashMap<String, Process> processTable;
    private volatile boolean currentCPU_Updated;
    private volatile boolean currentIO_Updated;

    
    
    


    public Scheduler(){
        
        this.blockQueue = new LinkedBlockingQueue<Process>();
        this.readyTree = new RedBlackTree();
        this.minSet = false;
        this.ioSet = false;
        this.cpuEmpty = true;
        this.ioEmpty = true;
        this.finishCPUTime = false;
        this.finishIOTime = false;
        this.CPUtimeslice = 0;
        this.IOtimeslice = 0;
        this.noMoreProcess = false;
        this.currentExamined = false;
        this.addedToBQ = false;
        this.processTable = new HashMap<>();
        this.currentCPU_Updated = false;
        this.currentIO_Updated = false;

        
        
    }

        synchronized void writeToScreen(String str){
            System.out.println(str);
            try
            {
                Thread.sleep(5000);
            }
            catch(InterruptedException e){

            }
        }

        synchronized boolean processHaveCPU(){
            for (Process i : processTable.values()) {
                if(i.get_vruntime() > 0){
                    return true;
                }
            }
            return false;
        }

        synchronized boolean processHaveIO(){
            for (Process i : processTable.values()) {
                if(i.get_tiempo_io() > 0){
                    return true;
                }
            }
            return false;
        }

        // Hilo que simula la ejecucion del proceso en el CPU
        class CPUSimulator extends Thread {
            // Le debo pasar el timeslice que el proceso va a correr

            @Override
            public void run(){
                try{
                    while(!noMoreProcess || !readyTree.isEmpty() || !blockQueue.isEmpty() ){

                        if(minSet){

                            writeToScreen("Running process in CPU: " + currentPID);


                            sleep(CPUtimeslice);
                            writeToScreen("Termino de correr en el cpu: " +  currentProcess.get_pid());
                            finishCPUTime = true;
                            //CPUtimeslice = 1000;

                            minSet = false;

                            
                        }

                    }

                    StdOut.print("Im not running (cpu)");
                    
                }catch(InterruptedException e){
                }
            }
        }
    
        // Hilo que simula la ejecucion del IO
        class IOSimulator extends Thread{
            // Le debo pasar el timeslice que el proceso va a pasar en io

            @Override
            public void run(){
                try{
                    while(!noMoreProcess || !readyTree.isEmpty() || !blockQueue.isEmpty()){
                        
                            if(ioSet){
                            writeToScreen("IO SIM con: " + currentIOProcess.get_pid());

                                sleep(IOtimeslice);
                                finishIOTime = true;
                                ioSet = false;
                                
                            }
                            


                        }

                    

                    StdOut.print("Im not running (IO)");
                    
                }catch(InterruptedException e){
                }
            }
        }
    
        // ***************************************** HILOS QUE INTERACTUAN CON EL RBT *******************************************************
        // Hilo que elimina del arbol al iniciar el scheduler o cuando el quantum de tiemo culmina
        class ProcessHandler extends Thread{
            // Le debo pasar el timeslice que el proceso va a correr: YA NO NECESITO ESPERAR, SOLO REVISO QUE LA VARIABLE ESTE EN QUE NO PUEDO SACAR
            @Override
            public void run(){
                // Si no ha terminado el tiempo de CPU y ademas el cpu esta lleno espero

                while(!noMoreProcess || !readyTree.isEmpty() || !blockQueue.isEmpty()){

                    
                    if(cpuEmpty){

                        writeToScreen("cpu epmty");

                        Node min_aux = readyTree.min();

                        Process min = min_aux.process;


                        readyTree.deleteMin();
                        writeToScreen("Process deleted from tree: " + min.get_pid());

                        
                        currentPID = min.get_pid();

                        currentProcess = min;

                        CPUtimeslice = currentProcess.get_vruntime();

                        cpuEmpty = false;

                        minSet = true;

                        writeToScreen("Timeslice: " + CPUtimeslice);




                    }
                    else if(finishCPUTime && currentExamined){

                        
                        writeToScreen("Finish CPU time");

                        if(!currentCPU_Updated){

                            writeToScreen("Vruntime antes de correr: " + Integer.toString(currentProcess.get_vruntime())+ "de " + currentProcess.get_pid());

                            currentProcess.update_vruntime(CPUtimeslice);
    
                            writeToScreen("Vruntime updated: " + Integer.toString(currentProcess.get_vruntime()) + "de " + currentProcess.get_pid());
    
                            if(!addedToBQ && currentProcess.get_vruntime() > 0)
                            {
                                readyTree.insert(currentProcess.get_vruntime(),currentProcess);
                                writeToScreen("Process added to tree: " + currentProcess.get_pid());
    
                            }
                            writeToScreen("Before min");

                            currentCPU_Updated = true;
                        }

                        // Verifico si aun hay procesos para correr y no me voy a quedar esperando por siempre

                        if(processHaveCPU()){

                            Node min_aux = readyTree.min();

                            min = min_aux.process;
        
                            readyTree.deleteMin();
        
                            writeToScreen("Process deleted from tree: " + min.get_pid());
        
                            currentPID = min.get_pid();
        
                            currentProcess = min;
    
                            CPUtimeslice = currentProcess.get_vruntime();
        
                            currentExamined = false;

                            currentCPU_Updated = false;

                            finishCPUTime = false;
    
                            minSet = true;
                        }

                        


                    }
                }
                StdOut.print("Im not running (handler)");
                
            }
        }
    
        // Hilo que "saca" del cpu y agrega en la cola cuando hay una interrupcion de IO
        // DEBEMOS DECIDIR CUANDO HACER ESTAS INTERRUPCIONES
        class BlockingQueueIn extends Thread {
            // Le debo pasar el timeslice que el proceso va a correr
            @Override
            public void run(){

            while(!noMoreProcess || !readyTree.isEmpty() || !blockQueue.isEmpty()){

                if(finishCPUTime && !currentExamined){
                    // Put en la cola
 
                    if(currentProcess.get_tiempo_io() > 0)
                    {
                        blockQueue.add(currentProcess);

                        writeToScreen("PROCESS ADDED TO BLOCK QUEUE: " + currentProcess.get_pid() + "Con tiempo IO: " + Integer.toString(currentProcess.get_tiempo_io()));
                        
                        addedToBQ = true;

                    }else{

                        addedToBQ = false;

                    }
                    currentExamined = true;
                }
                
            }    
            writeToScreen("Im not running BlockingQueueIN");
            }
        }


        class BlockingQueueOut extends Thread {
            @Override
            public void run(){
            
            while(!noMoreProcess || !readyTree.isEmpty() || !blockQueue.isEmpty()){
                //writeToScreen("BQ STATUS: " + blockQueue.size());
                if(ioEmpty){
                    // Put en el arbol si aun tieme io timef()
                    //writeToScreen("IO EMPTY");

                    Process p = new Process("0",0,0,0,0,0);

                    try
                    {
                        p = blockQueue.take();
                    }
                    catch(InterruptedException e){

                    }
                
                    currentIOProcess = p;

                    IOtimeslice = p.get_tiempo_io();

                    writeToScreen("IO EMPTY PROCESS DELETED FROM BLOCK QUEUE: " + currentIOProcess.get_pid() + " WITH TIMESLICE: " + Integer.toString(IOtimeslice));

                    ioEmpty = false;

                    ioSet = true;

                }else if(finishIOTime){

                    if(currentIO_Updated){

                        currentIOProcess.update_iotime(IOtimeslice);

                        writeToScreen("IO TIME UPDATED DE : " + currentIOProcess.get_pid() + " " + Integer.toString(currentIOProcess.get_tiempo_io()));
    
    
                        if(currentIOProcess.get_vruntime() > 0){
    
                            readyTree.insert(currentIOProcess.get_vruntime(),currentIOProcess);
    
                            writeToScreen("PROCESS INSERTED TO TREE FROM BQ: " + currentIOProcess.get_pid() + " with vruntime: " + currentIOProcess.get_vruntime());
                        }
    
                        // Verifico que haya procesos con tiempo de IO
                        currentIO_Updated = true;
                    }

                    if(processHaveIO()){

                        Process p = new Process("0",0,0,0,0,0);

                        try
                        {
                            p = blockQueue.take();
                        }
                        catch(InterruptedException e){
        
                        }
    
                        IOtimeslice = p.get_tiempo_io();
    
                        currentIOProcess = p;
    
                        writeToScreen("PROCESS DELETED FROM BLOCK QUEUE: " + currentIOProcess.get_pid() + " WITH TIMESLICE: " + Integer.toString(IOtimeslice));
    
                        finishIOTime = false;

                        currentIO_Updated = false;
    
                        ioSet = true;
                    }


                    

                }

            }
            writeToScreen("Im not running BlockingQueueOut");   

            }
        }

    
    class initSchedulerThread extends Thread{
        @Override
        public void run(){
            // Load Processes to New Processes list
            newProcesses = Parser.ParseToProcess("procesos1.json");
            for (Process process: newProcesses) {
                
                try{
                    sleep(1000);
                    processTable.put(process.get_pid(), process);
                    readyTree.insert(process.get_vruntime(), process);
                    processTable.put(process.get_pid(), process);
                    StdOut.println("Process added to tree from new list: ");
                    StdOut.println(process.get_pid());
                    //sleep(1000);
                    
                }catch(InterruptedException e){
                }
                System.out.print("Arbol de listos: ");
                readyTree.prettyPrint();

            }
            StdOut.println("Outside initSsched");
            readyTree.prettyPrint();
            //System.out.println(readyTree.keys());
            noMoreProcess = true;

        }
    }


    public void executeScheduler() { //Aqui corremos el hilo de iniciar el Scheduler
        
        // Init threads
        initSchedulerThread init = new initSchedulerThread(); 
        ProcessHandler processHandler = new ProcessHandler();
        CPUSimulator cpuSim = new CPUSimulator();
        BlockingQueueIn bloqIn = new BlockingQueueIn();
        BlockingQueueOut bloqOut = new BlockingQueueOut();
        IOSimulator ioSim = new IOSimulator();
        //ThreadChecker  threadChecker = new ThreadChecker();

        init.start();
        processHandler.start();
        cpuSim.start();
        bloqIn.start();
        bloqOut.start();
        ioSim.start();

        //StdOut.println("?");
        

    }


}
