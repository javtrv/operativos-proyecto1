import java.util.LinkedList; 
import java.util.Queue; 
import java.util.Iterator;
  
public class ProcessQueue implements Iterable<Process>
{ 
  private Queue<Process> queue;
  public ProcessQueue(){
    this.queue = new LinkedList<Process>();
  }

  public void add(Process p){
    this.queue.add(p);
  }

  public Process remove(){
    return this.queue.remove();
  }

  public int size(){
    return this.queue.size();
  }

  public Iterator<Process> iterator() {
    return this.queue.iterator();
}
  public static void main(String[] args) 
  { 
    

    // inicializamos procesos
    ProcessQueue q = new ProcessQueue();

    Process proceso1 = new Process("1",68,62,1,8,59);
    Process proceso2 = new Process("2",99,78,9,8,68);
    Process proceso3 = new Process("3",67,85,4,8,47);
    Process proceso4 = new Process("4",17,32,5,8,13);
    Process proceso5 = new Process("5",72,40,8,8,88);

    // Añadimos los procesos a la cola
    q.add(proceso1);
    q.add(proceso2);
    q.add(proceso3);
    q.add(proceso4);
    q.add(proceso5);
  
    // Enseñamos los elementos de la cola
    System.out.println("Elementos de la cola-"); 

    for(Process p: q){
        System.out.println(p.get_pid());
      }
  
    // Para remover los elementos de la cola. 
    Process removedele = q.remove(); 
    System.out.println("removed element-" + removedele.get_pid()); 
  
    System.out.println(q); 
  

    int size = q.size(); 
    System.out.println("Tamano de la cola-" + size); 
  } 
} 