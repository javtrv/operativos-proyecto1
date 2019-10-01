/**
 * main
 */
public class main {

        /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        RedBlackBST<String, Process> st = new RedBlackBST<String, Process>();
        /*
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        */
        // get me da el process
        // cada que agregue un elemento debo actualizar el min runtime
        Process p = new Process(10, 10.0, 10.0, 9, 1);
        st.put(Double.toString(p.get_vruntime()), p);
        for (String s : st.keys())
            StdOut.println(st.get_min_vruntime());

        StdOut.println();
    }
}