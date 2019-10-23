import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Collections;

class Parser
{

  @SuppressWarnings("unchecked")
  private static ArrayList<Process> procesosNuevos = new ArrayList<Process>();
  private static int contador_procesos = 0;

  public static ArrayList<Process> ParseToProcess(String file) 
  {

    //JSON parser object to parse read file
    JSONParser jsonParser = new JSONParser();
    try (FileReader reader = new FileReader(file))
    {
      //Read JSON file
            System.out.println(reader);
            Object obj = jsonParser.parse(reader);
            JSONArray listaProcesos = (JSONArray) obj;
            listaProcesos.forEach( proc ->  parseProcessObject( (JSONObject) proc ) 
              );
            System.out.println("Lista de procesos nuevos");
            System.out.println(procesosNuevos);
            
            Collections.sort(procesosNuevos);
            
            System.out.println("Lista de procesos ordenados");
            System.out.println(procesosNuevos);
            for (Process var : procesosNuevos) {
              System.out.println(var.get_pid());
            }
            

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return procesosNuevos;
  }

  private static void parseProcessObject(JSONObject proc) 
  {
    Integer aux;
    String id = "P" + contador_procesos;
    Integer tiempoLlegada = (int) (long) proc.get("tiempo_llegada");
    Integer tiempoCPU = (int) (long) proc.get("tiempo_CPU");
    Integer tiempoIO = (int) (long) proc.get("tiempo_IO");
    Integer prioridad = (int) (long) proc.get("prioridad");
    if (tiempoIO > tiempoCPU) {
      aux = tiempoIO;
      tiempoIO = tiempoCPU;
      tiempoCPU = tiempoIO;
    }

    //Integer vRuntime = (int) (long) proc.get("v_runtime");
    Process p = new Process(id, tiempoLlegada, tiempoIO, tiempoCPU, prioridad);
    procesosNuevos.add(p);
    contador_procesos++;

  }



}