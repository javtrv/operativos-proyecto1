/**
 * Process
 */
public class Process {

    private int pid;
    private double tiempo_llegada;
    private double tiempo_cpu;
    private int prioridad;
    private double v_runtime;


    public Process(int pid_, double tiempo_llegada_, double tiempo_cpu_, int prioridad_, double vruntime){
        this.pid = pid_;
        this.tiempo_llegada = tiempo_llegada_;
        this.tiempo_cpu = tiempo_cpu_;
        this.prioridad = prioridad_;
        this.v_runtime = vruntime;
    }

    public int get_pid(){
        return pid;
    }


    public double get_tiempo_llegada(){
        return tiempo_llegada;
    }


    public double get_tiempo_cpu(){
        return tiempo_cpu;
    }


    public int get_prioridad(){
        return prioridad;
    }

    public double get_vruntime(){
        return v_runtime;
    }
}