import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


class Planificador 
{
  @SuppressWarnings("unchecked")
  public static ArrayList<Process> procesosNuevos = new ArrayList<Process>();
  public static void main(String[] args) 
  {
    Planificador planificador = new Planificador();
    //JSON parser object to parse read file
    JSONParser jsonParser = new JSONParser();
    try (FileReader reader = new FileReader(args[0]))
    {
      //Read JSON file

            Object obj = jsonParser.parse(reader);

            JSONArray listaProcesos = (JSONArray) obj;


            listaProcesos.forEach( proc ->  parseProcessObject( (JSONObject) proc ) 
              );
            System.out.println("Lista de procesos nuevos");
            System.out.println(procesosNuevos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

  }



  private static void parseProcessObject(JSONObject proc) 
  {

    String id = (String) proc.get("id"); 
    Integer tiempoLlegada = (int) (long) proc.get("tiempo_llegada");
    Integer tiempoCPU = (int) (long) proc.get("tiempo_CPU");
    Integer tiempoIO = (int) (long) proc.get("tiempo_IO");
    Integer prioridad = (int) (long) proc.get("prioridad");
    Integer vRuntime = (int) (long) proc.get("v_runtime");
    Process p = new Process(id, tiempoLlegada, tiempoIO, tiempoCPU, prioridad, vRuntime);
    procesosNuevos.add(p);

  }
}