/**
 * Process
 */
import java.util.*; 

public class Process implements Comparable<Process> {

    private String pid;
    private int tiempoLlegada;
    private int tiempoCPU;
    private int tiempoIO;
    private int prioridad;
    private int execTime;
    private SchedEntity schedEntity;


    public Process(String pid_, int tiempo_llegada_, int tiempo_io_, int tiempo_cpu_, int prioridad_){
        this.pid = pid_;
        this.tiempoLlegada = tiempo_llegada_;
        this.tiempoIO = tiempo_io_; // Veces que pasa por IO
        this.tiempoCPU = tiempo_cpu_; // Veces que pasa por el CPU
        this.prioridad = prioridad_;
        this.execTime = 0;
        this.schedEntity = null;
    }

    public SchedEntity createSchedEntity(int weight){
        this.schedEntity = new SchedEntity(this.pid, 0, weight);
        return this.schedEntity;
    }

    public SchedEntity get_schedEntity(){
        return this.schedEntity;
    }

    public String get_pid(){
        return pid;
    }

    public void update_iotime(){
        this.tiempoIO = this.tiempoIO - 1;
    }

    public void update_cputime(){
        this.tiempoCPU = this.tiempoCPU - 1;
    }

    public void update_execTime(int time){
        this.execTime = execTime + time;
    }

    public int get_tiempo_llegada(){
        return this.tiempoLlegada;
    }


    public int get_tiempo_cpu(){
        return this.tiempoCPU;
    }


    public int get_prioridad(){
        return this.prioridad;
    }


    public int get_tiempo_io(){
        return this.tiempoIO;
    }

    public int get_execTime(){
        return this.execTime;
    }

        @Override
    public String toString() {
        return "PID: " + pid + ", CPU: " + tiempoCPU+ ", I/O: " + tiempoIO + ", EXC: " + execTime ;
    }

    @Override
    public int compareTo(Process p){
        if(p.get_tiempo_llegada()>tiempoLlegada){
            return -1;
        }else if(p.get_tiempo_llegada()<tiempoLlegada){
            return 0;
        }else{
            return 1;
        }
    }
}