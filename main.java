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
        /*
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        */
        // get me da el process
        // cada que agregue un elemento debo actualizar el min runtime
        Process p2 = new Process(10, 10.0, 10.0, 9, 2);
        Process p27 = new Process(10, 10.0, 10.0, 9, 27);
        Process p19 = new Process(10, 10.0, 10.0, 9, 19);
        Process p7 = new Process(10, 10.0, 10.0, 9, 7);
        Process p25 = new Process(10, 10.0, 10.0, 9, 25);
        Process p31 = new Process(10, 10.0, 10.0, 9, 31);
        Process p34 = new Process(10, 10.0, 10.0, 9, 34);
        Process p65 = new Process(10, 10.0, 10.0, 9, 65);
        Process p49 = new Process(10, 10.0, 10.0, 9, 49);
        Process p98 = new Process(10, 10.0, 10.0, 9, 98);

        /*
        List<Integer> anotherList = Arrays.asList(2, 27, 19, 7, 25, 34, 31, 65, 49, 98);
        for (int var : anotherList) {
            st.put(Integer.toString(var), new Process(10, 10.0, 10.0, 9, var));
            StdOut.println(var);
        }*/
        //st.put("2", p2);
        //st.put("27", p27);
        
        st.put(2, p2);
        
        st.put(27, p27);
        st.put(19, p19);
        st.put(7, p7);
        st.put(25, p25);
        st.put(31, p31);
        st.put(34, p34);
        st.put(65, p65);
        st.put(49, p49);
        st.put(98, p98);
        
        
        StdOut.print("ESTE ES EL MINIMO: ");
        StdOut.println(st.keys());

        StdOut.println(st.min());
    }
}