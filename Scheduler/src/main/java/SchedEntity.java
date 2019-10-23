public class SchedEntity{

    private String pid;
    private int vRuntime;
    private int weight;

    public SchedEntity(String pid_, int _vRuntime, int _weight){
        this.pid = pid_;
        this.vRuntime = _vRuntime;
        this.weight = _weight;
    }

    public void set_vruntime(int newVruntime){
        this.vRuntime = newVruntime;
    }

    public String getPid(){
        return this.pid;
    }

    public int get_weight(){
        return this.weight;
    }

    public int getVruntime(){
        return this.vRuntime;
    }


}