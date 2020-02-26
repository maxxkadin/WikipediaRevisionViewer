package utils;

import com.google.gson.*;
import domain.Edit;
import domain.WikipediaPage;
import exceptions.ParameterIsNotJsonStringException;
import java.util.ArrayList;
import java.util.List;

public class ParseUtils {

    public static WikipediaPage parseJsonToWikipediaPageDirect(String jsonString) throws ParameterIsNotJsonStringException{
        if (!jsonString.startsWith("{")){
            throw new ParameterIsNotJsonStringException();
        }
        Gson tempGson = new Gson();
        return tempGson.fromJson(jsonString, WikipediaPage.class);
    }

    public static WikipediaPage parseJsonToWikipediaPageManual(String jsonString) throws ParameterIsNotJsonStringException{ //What exception does this throw?
        JsonParser jsonParser = new JsonParser();
        JsonElement rootElement = jsonParser.parse(jsonString);
        JsonObject rootObject = rootElement.getAsJsonObject();
        JsonObject queryObject = rootObject.getAsJsonObject("query");
        JsonObject pagesObject = queryObject.getAsJsonObject("pages");
        int length = pagesObject.keySet().toString().length();
        String pageIdObjectName = pagesObject.keySet().toString().substring(1, length - 1);
        JsonObject pageidObject = pagesObject.getAsJsonObject(pageIdObjectName);
        var pageID = pageidObject.getAsJsonPrimitive("pageid").getAsInt();
        var pageTitle = pageidObject.getAsJsonPrimitive("title").getAsString();
        JsonArray revisionsObject = pageidObject.getAsJsonArray("revisions");

        List<Edit> pageEdits = new ArrayList<>();
        for (int i = 0; i<revisionsObject.size(); i++){
            JsonObject object = revisionsObject.get(i).getAsJsonObject();
            Gson tempGson = new Gson();
            Edit edit = tempGson.fromJson(object, Edit.class);
            pageEdits.add(edit);
        }
        return new WikipediaPage(pageTitle, pageID, pageEdits);
    }
}
