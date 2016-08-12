package KoTHComm.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class Downloader {

    public final static String QUERY_URL = "https://api.stackexchange.com/2.2/questions/";
    public final static String QUERY_PARAMS = "/answers?pagesize=100&order=desc&sort=activity&site=codegolf&filter=!FcbKgRqyv4bqdqoj9fAB6fZ05P";
    private final URL url;
    public Downloader(int questionID){
        try {
            url = new URL(QUERY_URL + questionID + QUERY_PARAMS);
        } catch (MalformedURLException e){
            throw new RuntimeException(e);
        }

    }
    public String downloadQuestions(){
        try {
            // TODO: look for the "has more", and do additional downloads
            StringBuilder result = new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void toJSON(String string){
        JsonObject parent = new JsonParser().parse(string).getAsJsonObject();
        for (JsonElement item: parent.get("items").getAsJsonArray()){
            String body = item.getAsJsonObject().get("body").getAsString();
            saveSubmission(body);
        }
    }

    private void saveSubmission(String html){
        Document document = Jsoup.parse(html);
        String header = document.select("h1,h2,h3,h4,h5,h6").get(0).text();
        String[] parts =  header.split(",");
        String name = parts[0];
        String language = parts[1];
        List<Element> codeBlocks = document.select("pre>code");
    }


}
