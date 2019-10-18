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
    private volatile boolean ioSet;
    private volatile boolean ioEmpty;
    private volatile boolean noMoreProcess;
    private volatile boolean needDecision;


    // Variables related to current process


    // Variables related to current process in IO

    private volatile boolean currentIO_Updated;
    private volatile boolean currentIOChanged;
    private volatile Process currentIOProcess;
    private volatile boolean canIOCurrentChange;


    // Variables related to current process in CPU

    private volatile boolean currentCPU_Updated;
    private volatile boolean canCPUCurrentChange;
    private volatile boolean currentCPUChanged;
    private volatile boolean currentToBQ;
    private volatile boolean currentToTree;
    private volatile Process currentProcess;
    
    
    // Timeslices 

    private volatile int CPUtimeslice;
    private volatile int IOtimeslice;

    // Process Table

    private volatile HashMap<String, Process> processTable;


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
        this.processTable = new HashMap<>();
        this.currentCPU_Updated = false;
        this.currentIO_Updated = false;
        this.canCPUCurrentChange = false;
        this.currentCPUChanged = false;
        this.currentIOChanged = false;
        this.currentToBQ = false;
        this.currentToTree = false;
        this.needDecision = false;
        
        
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
                    while(processHaveCPU() || processHaveIO()){

                        if(minSet){

                            writeToScreen("Running process in CPU: " + currentPID);


                            sleep(CPUtimeslice);

                            writeToScreen("Termino de correr en el cpu: " +  currentProcess.get_pid());

                            finishCPUTime = true;

                            minSet = false;

                            needDecision = true;

                            
                        }

                    }

                    System.out.println("Im not running (cpu)");
                    
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
                    while(processHaveCPU() || processHaveIO()){
                        
                            if(ioSet){

                                writeToScreen("IO SIM con: " + currentIOProcess.get_pid() + " WITH TIMESLICE : " + Integer.toString(currentIOProcess.get_tiempo_io()));

                                sleep(IOtimeslice);

                                writeToScreen("Finish IO time");
                                
                                finishIOTime = true;

                                ioSet = false;   
                            }
                            


                        }

                    

                    System.out.println("Im not running (IO)");
                    
                }catch(InterruptedException e){
                }
            }
        }
    
        class ProcessUpdaterCPU extends Thread{
            // Aqui actualizo el tiempo de los procesos (IO y CPU)
            @Override
            public void run(){
                while(processHaveCPU() || processHaveIO())
                {
                    
                    if(finishCPUTime && !currentCPU_Updated){

                        writeToScreen("Vruntime antes de correr: " + Integer.toString(currentProcess.get_vruntime())+ "de " + currentProcess.get_pid());

                        currentProcess.update_vruntime(CPUtimeslice);

                        writeToScreen("Vruntime updated: " + Integer.toString(currentProcess.get_vruntime()) + "de " + currentProcess.get_pid());

                        currentCPU_Updated = true;
    
                    }
                }
                writeToScreen("Im not running ProcessUpdaterCPU");
            }
        }

        class ProcessProducerCPU extends Thread{
            @Override
            public void run(){

                while(processHaveCPU() || processHaveIO())
                {
                    if(currentToTree){
                        
                        readyTree.insert(currentProcess.get_vruntime(),currentProcess);

                        canCPUCurrentChange = true;

                        currentToTree = false;

                        writeToScreen("Process added to tree: " + currentProcess.get_pid());

                    }

                }
                writeToScreen("Im not running ProcessProducerCPU");
                
            }
        }

        // ***************************************** HILOS QUE INTERACTUAN CON EL RBT *******************************************************
        // Elimina el min del arbol
        class ProcessConsumerCPU extends Thread{

            @Override
            public void run(){
                

                while(processHaveCPU() || processHaveIO()){

                    
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
                    else if(finishCPUTime && currentCPU_Updated && canCPUCurrentChange){

                        
                        writeToScreen("Finish CPU time");
                        writeToScreen("Value of tree: " + Boolean.toString(readyTree.isEmpty()));
                        writeToScreen("Value of queue: " + Boolean.toString(blockQueue.isEmpty()));
                        for (Process var : blockQueue) {
                            writeToScreen("In block queue: " + var.get_pid());
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

                            finishCPUTime = false;

                            currentToTree = false;

                            currentToBQ = false;

                            currentCPU_Updated = false;

                            canCPUCurrentChange = false;
    
                            minSet = true;
                        }

                        


                    }
                }
                System.out.println("Im not running ProcessConsumerCPU");
                
            }
        }
    
        class Decider extends Thread {
            @Override
            public void run(){
                while(processHaveCPU() || processHaveIO())
                {

                    if(finishCPUTime && currentCPU_Updated && needDecision){

                        if(currentProcess.get_tiempo_io() > 0){

                            currentToBQ = true;

                        }else if(currentProcess.get_vruntime() > 0){

                            currentToTree = true;

                        }else{
                            canCPUCurrentChange = true;
                        }

                        needDecision = false;
                    }
                }
            }
        }
        class ProcessProducerBQ extends Thread {
            // Le debo pasar el timeslice que el proceso va a correr
            @Override
            public void run(){

            while(processHaveCPU() || processHaveIO()){

                if(currentToBQ){
                    // Put en la cola
                    
                    blockQueue.add(currentProcess);

                    writeToScreen("PROCESS ADDED TO BLOCK QUEUE: " + currentProcess.get_pid() + "Con tiempo IO: " + Integer.toString(currentProcess.get_tiempo_io()));

                    canCPUCurrentChange = true;

                    currentToBQ = false;
                    
                }
                
            }    
                writeToScreen("Im not running ProcessProducerBQ");
            }
        }

        class ProcessUpdaterBQ extends Thread{

            @Override
            public void run(){

                while(processHaveCPU() || processHaveIO())
                {

                    if(finishIOTime && !currentIO_Updated){

                        currentIOProcess.update_iotime(IOtimeslice);

                        writeToScreen("IO TIME UPDATED DE : " + currentIOProcess.get_pid() + " " + Integer.toString(currentIOProcess.get_tiempo_io()));
    
                        // Verifico que haya procesos con tiempo de IO
                        currentIO_Updated = true;
                    }

                }
                writeToScreen("Im not running ProcessUpdaterBQ");

            }
        }
        
        class ProcessProducerTree_fromBQ extends Thread{
            @Override
            public void run(){

                while(processHaveCPU() || processHaveIO())
                {

                    if(finishIOTime && currentIO_Updated){

                        if(currentIOProcess.get_vruntime() > 0){
    
                            readyTree.insert(currentIOProcess.get_vruntime(),currentIOProcess);
    
                            writeToScreen("PROCESS INSERTED TO TREE FROM BQ: " + currentIOProcess.get_pid() + " with vruntime: " + currentIOProcess.get_vruntime());
                        }
                        canIOCurrentChange = true;
                    }

                }
                writeToScreen("Im not running ProcessProducerTree_fromBQ");
            }
        }

        class ProcessConsumerBQ extends Thread {
            @Override
            public void run(){
            
            while(processHaveCPU() || processHaveIO()){
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

                }else if(finishIOTime && currentIO_Updated && canIOCurrentChange){

                    if(processHaveIO()){

                        Process p = new Process("0",0,0,0,0,0);

                        writeToScreen("Me quede aqui");
                        try
                        {
                            p = blockQueue.take();
                        }
                        catch(InterruptedException e){
        
                        }
                        writeToScreen("Sali de aqui");

                        IOtimeslice = p.get_tiempo_io();
    
                        currentIOProcess = p;
    
                        writeToScreen("PROCESS DELETED FROM BLOCK QUEUE: " + currentIOProcess.get_pid() + " WITH TIMESLICE: " + Integer.toString(IOtimeslice));
    
                        finishIOTime = false;

                        currentIO_Updated = false;

                        canIOCurrentChange = false;
    
                        ioSet = true;

                        
                    }


                    

                }

            }
            writeToScreen("Im not running ProcessConsumerBQ");   

            }
        }

    
    class initSchedulerThread extends Thread{
        @Override
        public void run(){
            // Load Processes to New Processes list
            
            for (Process process: newProcesses) {
                
                try{
                    sleep(1000);
                    
                    readyTree.insert(process.get_vruntime(), process);
                    processTable.put(process.get_pid(), process);
                    System.out.println("Process added to tree from new list: ");
                    System.out.println(process.get_pid());
                    //sleep(1000);
                    
                }catch(InterruptedException e){
                }
                System.out.print("Arbol de listos: ");
                readyTree.prettyPrint();

            }
            System.out.println("Outside initSsched");
            readyTree.prettyPrint();
            //System.out.println(readyTree.keys());
            noMoreProcess = true;

        }
    }


    public void executeScheduler() { //Aqui corremos el hilo de iniciar el Scheduler
        
        // Init threads
        initSchedulerThread init = new initSchedulerThread(); 
        ProcessConsumerCPU processConsumerCPU = new ProcessConsumerCPU();
        ProcessProducerCPU processProducerCPU = new ProcessProducerCPU();
        ProcessUpdaterCPU processUpdaterCPU = new ProcessUpdaterCPU();
        ProcessProducerTree_fromBQ producesToTree = new ProcessProducerTree_fromBQ();
        ProcessConsumerBQ processConsumerBQ = new ProcessConsumerBQ();
        ProcessProducerBQ processProducerBQ = new ProcessProducerBQ();
        ProcessUpdaterBQ processUpdaterBQ = new ProcessUpdaterBQ();
        Decider decider = new Decider();
        CPUSimulator cpuSim = new CPUSimulator();
        IOSimulator ioSim = new IOSimulator();
        //ThreadChecker  threadChecker = new ThreadChecker();

        newProcesses = Parser.ParseToProcess("procesos1.json");
        for (Process process : newProcesses) {
            processTable.put(process.get_pid(), process);
        }
        
        init.start();
        //sleep(10000);
        processConsumerCPU.start();
        processProducerCPU.start();
        processUpdaterCPU.start();
        decider.start();
        producesToTree.start();
        processConsumerBQ.start();
        processProducerBQ.start();
        processUpdaterBQ.start();
        cpuSim.start();
        ioSim.start();

        //System.out.println("?");
        

    }


}
