import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.*; 



class Scheduler{

    //                                                      Scheduler Structures

    private volatile RedBlackTree readyTree;
    private volatile LinkedBlockingQueue<Process> blockQueue;
    private ArrayList<Process> newProcesses;
    private volatile HashMap<String, Object> schedulerTable;
    

    // Clocks

    private volatile int cpu_on;
    private volatile int cpu_off;
    private volatile int clock;

    // **************************************************** Variables to control scheduler ****************************************************

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


    // ************************** Variables related to current process **************************


    // ************** Variables related to current process in IO **************

    private volatile boolean currentIO_Updated;
    private volatile boolean currentIOChanged;
    private volatile Process currentIOProcess;
    private volatile boolean canIOCurrentChange;
    private volatile boolean producedToTree;


    // ************** Variables related to current process in CPU **************

    private volatile boolean currentCPU_Updated;
    private volatile boolean canCPUCurrentChange;
    private volatile boolean currentCPUChanged;
    private volatile boolean currentToBQ;
    private volatile boolean currentToTree;
    private volatile Process currentProcess;
    
    
    // ************************** Timeslices **************************

    private volatile int CPUtimeslice;
    private volatile int IOtimeslice;

    // ************************** Process Table **************************

    private volatile HashMap<String, Process> processTable;
    private volatile HashMap<Integer, Integer> prio_to_wight;
    private volatile HashMap<String, SchedEntity> schedEntityTable;

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
        this.processTable = new HashMap<String, Process>();
        this.currentCPU_Updated = false;
        this.currentIO_Updated = false;
        this.canCPUCurrentChange = false;
        this.currentCPUChanged = false;
        this.currentIOChanged = false;
        this.currentToBQ = false;
        this.currentToTree = false;
        this.needDecision = false;
        this.prio_to_wight = new HashMap<Integer, Integer>();
        this.producedToTree = false;
        this.cpu_off = 0;
        this.cpu_on = 0;
        this.clock = 0;
        this.schedulerTable = new HashMap<String, Object>();
        this.schedEntityTable = new HashMap<String, SchedEntity>();
        this.currentIOProcess = null;
        this.currentProcess = null;
        
        
    }

        public void fillWeight(){
            int[] weight = {
                /* -20 */     88761,     71755,     56483,     46273,     36291,
                /* -15 */     29154,     23254,     18705,     14949,     11916,
                /* -10 */      9548,      7620,      6100,      4904,      3906,
                /*  -5 */      3121,      2501,      1991,      1586,      1277,
                /*   0 */      1024,       820,       655,       526,       423,
                /*   5 */       335,       272,       215,       172,       137,
                /*  10 */       110,        87,        70,        56,        45,
                /*  15 */        36,        29,        23,        18,        15,
            };
            for(int i=0; i < 40; i++){
                this.prio_to_wight.put(i-20, weight[i]);
            }
        }


        synchronized void writeToScreen(String str){
            System.out.println(str);
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException e){

            }
        }

        public boolean processHaveCPU(){
            for (Process i : processTable.values()) {
                if(i.getTiempoCpu() > 0){
                    return true;
                }
            }
            return false;
        }

        public boolean processHaveIO(){
            for (Process i : processTable.values()) {
                if(i.getTiempoIo() > 0){
                    return true;
                }
            }
            return false;
        }

        public int updateVruntime(){
            int size = readyTree.size();
            if(size != 0){
                return (int) currentProcess.get_execTime() / readyTree.size();
            }
            return (int) currentProcess.get_execTime();
        }

        public int calculateTimeslice(Process p){
            int weights = readyTree.weights();
            if(weights != 0){
                return (int) p.getSchedEntity().get_weight() / readyTree.weights();
                //return 1000;
            }
            return 1000;
        }


        public HashMap<String, Object> getSchedulerData(){
            this.schedulerTable.clear();
            //LinkedList<String> keysTree = readyTree.keys();
            LinkedList<String> keysBQ = new LinkedList<String>();
            for(Process i : blockQueue){
                keysBQ.add(i.getPid());
            }
            //this.schedulerTable.put("tree", keysTree);
            this.schedulerTable.put("blockQueue", keysBQ);
            this.schedulerTable.put("processTable", processTable);
            this.schedulerTable.put("schedEntityTable", schedEntityTable);
            this.schedulerTable.put("currentInCPU", currentProcess);
            this.schedulerTable.put("currentInIO", currentIOProcess);
            return this.schedulerTable;
        }
        public HashMap<String, Process> getProcessTable(){
            return this.processTable;
        }

        public HashMap<String, SchedEntity> getSchedEntity(){
            return  this.readyTree.keys();
        }

        public LinkedList<String> getBlockQueue(){
            LinkedList<String> keysBQ = new LinkedList<String>();
            for(Process i : blockQueue){
                keysBQ.add(i.getPid());
            }
            return  keysBQ;
        }

        public String getCurrent(){
            if(this.currentProcess != null){
                return this.currentProcess.getPid();
            }else{
                return("EMPTY");
            }

        }

        public String getCurrentIO(){
            if(this.currentIOProcess != null){
                return this.currentIOProcess.getPid();
            }else{
                return("EMPTY");
            }

        }

        public int calculateTimeProm(){
            int aux=0;
            int prom = 0;
            for (Process i : processTable.values()) {
                aux++;
                prom = prom + i.get_execTime();
            }
            return prom / aux;
        }

        public int processTableSize(){
            return  this.processTable.size();
        }

        public int getCpuOn(){
            int sum = cpu_off + cpu_on;
            int porc = (cpu_on * 100) / sum;
            return porc;
        }

        public int getCpuOff(){
            int sum = cpu_off + cpu_on;
            int porc = (cpu_off * 100) / sum;
            return porc;
        }

        public int getTotalTime(){
            return cpu_on + cpu_off;
        }

        // Debo actualizar el CPUTimeslice para que se asigne a esto y no al vruntime
        // Tambien debo asignar un iotimeslice constante y parametrizable 

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

                            writeToScreen("Termino de correr en el cpu: " +  currentProcess.getPid());

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

                                writeToScreen("IO SIM con: " + currentIOProcess.getPid() + " WITH TIMESLICE : " + Integer.toString(currentIOProcess.getTiempoIo()));

                                sleep(1000);

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

                        currentProcess.update_cputime();

                        writeToScreen("CPU time updated: " + Integer.toString(currentProcess.getTiempoCpu()));

                        currentProcess.update_execTime(CPUtimeslice);

                        writeToScreen("Exec time: " + Integer.toString(currentProcess.get_execTime()));

                        currentProcess.getSchedEntity().set_vruntime(updateVruntime());

                        writeToScreen("Vruntime updated: " + Integer.toString(currentProcess.getSchedEntity().getVruntime()) + "de " + currentProcess.getPid());

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
                        
                        readyTree.insert(currentProcess.getSchedEntity().getVruntime(),currentProcess.getSchedEntity());

                        canCPUCurrentChange = true;

                        currentToTree = false;

                        writeToScreen("Process added to tree: " + currentProcess.getPid());

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

                        SchedEntity min = min_aux.schedEntity;

                        Process min_process = processTable.get(min.getPid());

                        readyTree.deleteMin();

                        writeToScreen("Process deleted from tree: " + min.getPid());

                        
                        currentPID = min_process.getPid();

                        currentProcess = min_process;

                        CPUtimeslice = calculateTimeslice(currentProcess);

                        cpuEmpty = false;

                        minSet = true;

                        writeToScreen("Timeslice: " + CPUtimeslice);

                    }
                    else if(finishCPUTime && currentCPU_Updated && canCPUCurrentChange){


                        if(processHaveCPU()){

                            Node min_aux = readyTree.min();

                            SchedEntity min = min_aux.schedEntity;

                            Process min_process = processTable.get(min.getPid());
        
                            readyTree.deleteMin();
        
                            writeToScreen("Process deleted from tree: " + min.getPid());
        
                            currentPID = min_process.getPid();
        
                            currentProcess = min_process;
    
                            CPUtimeslice = calculateTimeslice(currentProcess);
    
                            minSet = true;
                        }

                        finishCPUTime = false;

                        currentToTree = false;

                        currentToBQ = false;

                        currentCPU_Updated = false;

                        canCPUCurrentChange = false;
                        


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

                        if(currentProcess.getTiempoIo() > 0){

                            currentToBQ = true;

                        }else if(currentProcess.getTiempoCpu() > 0){

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

                    writeToScreen("PROCESS ADDED TO BLOCK QUEUE: " + currentProcess.getPid() + "Con tiempo IO: " + Integer.toString(currentProcess.getTiempoIo()));

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

                        currentIOProcess.update_iotime();

                        writeToScreen("IO TIME UPDATED DE : " + currentIOProcess.getPid() + " " + Integer.toString(currentIOProcess.getTiempoIo()));
    
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
                    if(finishIOTime && currentIO_Updated && !producedToTree){

                        if(currentIOProcess.getTiempoCpu() > 0){
    
                            readyTree.insert(currentIOProcess.getSchedEntity().getVruntime(),currentIOProcess.getSchedEntity());
    
                            writeToScreen("PROCESS INSERTED TO TREE FROM BQ: " + currentIOProcess.getPid() + " with vruntime: " + currentIOProcess.getSchedEntity().getVruntime());
                        }
                        canIOCurrentChange = true;
                        producedToTree = true;
                        //finishIOTime = false;
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
                //writeToScreen("Process have io: " + processHaveIO());
                if(ioEmpty){
                    // Put en el arbol si aun tieme io timef()
                    writeToScreen("IO EMPTY");

                    Process p = new Process("0",0,0,0,0);

                    try
                    {
                        p = blockQueue.take();
                    }
                    catch(InterruptedException e){

                    }
                
                    currentIOProcess = p;

                    //IOtimeslice = p.getTiempoIo();

                    writeToScreen("IO EMPTY PROCESS DELETED FROM BLOCK QUEUE: " + currentIOProcess.getPid() + " WITH TIMESLICE: " + Integer.toString(currentIOProcess.getTiempoIo()));

                    ioEmpty = false;

                    ioSet = true;

                }else if(finishIOTime && currentIO_Updated && canIOCurrentChange){
                    writeToScreen("FINISH IO TIME AND UP AND CAN CHANGE");
                    if(processHaveIO()){

                        Process p = new Process("0",0,0,0,0);

                        writeToScreen("Me quede aqui");
                        try
                        {
                            p = blockQueue.take();
                        }
                        catch(InterruptedException e){
        
                        }
                        writeToScreen("Sali de aqui");

                        //IOtimeslice = p.getTiempoIo();
    
                        currentIOProcess = p;
    
                        writeToScreen("PROCESS DELETED FROM BLOCK QUEUE: " + currentIOProcess.getPid() + " WITH IOTIME: " + Integer.toString(currentIOProcess.getTiempoIo()));
    
                        finishIOTime = false;
    
                        ioSet = true;

                        
                    }
                    finishIOTime = false;

                    currentIO_Updated = false;

                    canIOCurrentChange = false;

                    producedToTree = false;

                    

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

                    SchedEntity schedEntity = process.createSchedEntity(prio_to_wight.get(process.get_prioridad()));

                    //schedEntityTable.put(schedEntity.getPid(), schedEntity);

                    readyTree.insert(0, schedEntity);

                    System.out.println("Process added to tree from new list: ");

                    System.out.println(process.getPid());
                    //sleep(1000);
                    
                }catch(InterruptedException e){
                }
                System.out.print("Arbol de listos: ");
                readyTree.prettyPrint();

            }
            System.out.println("Outside initSsched");
            readyTree.prettyPrint();
            //System.out.println("KEYS");
            //System.out.println(readyTree.keys());
            //System.out.println(readyTree.keys());
            noMoreProcess = true;
            


        }
    }


    class ClockCPU extends Thread{
        @Override
        public void run(){

            while(processHaveCPU() || processHaveIO()){
                try{
                    sleep(1);
                }catch(InterruptedException e){
                }
                if(minSet){
                    cpu_on = cpu_on + 1;
                }else{
                    cpu_off = cpu_off + 1;
                }
            }
            System.out.println("El tiempo del CPU activo fue de: " + cpu_on + " ms");
            System.out.println("El tiempo del CPU inactivo fue de: " + cpu_off + " ms");
        }
    }

    class Clock extends Thread{
        @Override
        public void run(){

            while(processHaveCPU() || processHaveIO()){
                try{
                    sleep(1);
                }catch(InterruptedException e){
                }
                clock = clock + 1;
                
            }
            System.out.println("El tiempo total fue de: " + clock + " ms");
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
        Clock clock = new Clock();
        ClockCPU clockCPU = new ClockCPU();

        fillWeight();
        System.out.println("Weights filled");
        System.out.println(prio_to_wight);
        newProcesses = Parser.ParseToProcess("src/main/resources/procesos1.json");
        for (Process process : newProcesses) {
            processTable.put(process.getPid(), process);
        }
        
        init.start();
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
        clock.start();
        clockCPU.start();
        //System.out.println("?");
        
        

    }



}
