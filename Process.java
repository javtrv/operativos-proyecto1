/**
 * Process
 */
public class Process {

    private String pid;
    private int tiempoLlegada;
    private int tiempoCPU;
    private int tiempoIO;
    private int prioridad;
    private int vRuntime;


    public Process(String pid_, int tiempo_llegada_, int tiempo_io_, int tiempo_cpu_, int prioridad_, int vRuntime){
        this.pid = pid_;
        this.tiempoLlegada = tiempo_llegada_;
        this.tiempoIO = tiempo_io_;
        this.tiempoCPU = tiempo_cpu_;
        this.prioridad = prioridad_;
        this.vRuntime = vRuntime;
    }

    public String get_pid(){
        return pid;
    }

    public void update_vruntime(int time){
        this.vRuntime = this.vRuntime - time;
    }

    public void update_iotime(int time){
        this.tiempoIO = this.tiempoIO - time;
    }

    public int get_tiempo_llegada(){
        return tiempoLlegada;
    }


    public int get_tiempo_cpu(){
        return tiempoCPU;
    }


    public int get_prioridad(){
        return prioridad;
    }

    public int get_vruntime(){
        return vRuntime;
    }

    public int get_tiempo_io(){
        return tiempoIO;
    }
}