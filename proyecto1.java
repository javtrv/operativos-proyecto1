import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class Planificador 
{
  @SuppressWarnings("unchecked")
  public static void main(String[] args) 
  {
    //JSON parser object to parse read file
    JSONParser jsonParser = new JSONParser();
    try (FileReader reader = new FileReader(args[0]))
    {
      //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray listaProcesos = (JSONArray) obj;
            System.out.println(listaProcesos);
            
            listaProcesos.forEach( proc -> parseProcessObject( (JSONObject) proc ) );

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
    System.out.println(id);

    Long tiempoLlegada = (Long) proc.get("tiempo_llegada");  
    System.out.println(tiempoLlegada);

  }
}