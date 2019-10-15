/**
 * main
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class main {

        /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        RedBlackBST<Integer, Process> st = new RedBlackBST<Integer, Process>();
        // **********************Proximo: Agregar Monitores, crear Threads y crear clase Planificador
        // Tambien: Agregar de la lista de procesos nuevos al arbol
        

        Process p2 = new Process("10", 10, 10, 10, 9, 2);
        Process p27 = new Process("10", 10, 10, 10, 9, 27);
        Process p19 = new Process("10", 10, 10, 10 ,9, 19);
        Process p7 = new Process("10", 10, 10, 10 ,9, 7);
        Process p25 = new Process("10", 10, 10, 10 ,9, 25);
        Process p31 = new Process("10", 10, 10, 10,9, 31);
        Process p34 = new Process("10", 10, 10, 10,9, 34);
        Process p65 = new Process("10", 10, 10, 10,9, 65);
        Process p49 = new Process("10", 10, 10, 10,9, 49);
        Process p98 = new Process("10", 10, 10, 10,9, 19);

        
        st.put(27, p2);
        
        st.put(19, p27);
        st.put(19, p19);
        st.put(19, p7);
        st.put(19, p25);
        st.put(19, p31);
        st.put(19, p34);
        st.put(19, p65);
        st.put(19, p49);
        st.put(19, p98);
        
        
        StdOut.print("ESTE ES EL MINIMO: ");
        StdOut.println(st.keys());

        StdOut.println(st.min());

        // LISTA DE PROCESOS NUEVOS
        //ArrayList<Process> procesosNuevos = Parser.ParseToProcess("procesos1.json");

        // PRUEBAS SCHEDULER

        Scheduler sc = new Scheduler();
        sc.executeScheduler();

    }
}