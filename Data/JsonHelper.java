package Data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import netscape.javascript.JSObject;

public class JsonHelper {

    public String path;

    @SuppressWarnings("unchecked")
    public JsonHelper(String path) {
        this.path = path;
    }

    public JSONObject readJsonFromFile() {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try (FileReader reader = new FileReader(path)) {
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void writeJsonToFile(JSONObject jsonObject) {
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
