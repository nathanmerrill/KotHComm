package com.ppcg.kothcomm.loader;

import com.ppcg.kothcomm.messaging.PipeCommunicator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;


public class Downloader {

    public final static String QUERY_URL = "http://api.stackexchange.com/2.2/questions/";
    public final static String QUERY_PARAMS = "/answers?pagesize=100&order=desc&sort=activity&site=codegolf&filter=!FcbKgRqyv4bqdqoj9fAB6fZ05P";
    private final URL url;
    private final SubmissionFileManager fileManager;

    public Downloader(SubmissionFileManager fileManager, int questionID){
        try {
            url = new URL(QUERY_URL + questionID + QUERY_PARAMS);
        } catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
        this.fileManager = fileManager;
    }
    public void downloadQuestions(){
        try {
            readJSON(new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream()))));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void readJSON(BufferedReader reader) throws IOException{
        JsonObject parent = new JsonParser().parse(reader).getAsJsonObject();
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
        String language = parts.length < 2 ? "" : parts[1];
        List<Element> codeBlocks = document.select("pre>code");
        if (codeBlocks.size() == 0){
            return;
        }
        if (language.contains("Java")){
            saveJava(name, codeBlocks.get(0).text());
            return;
        }
        if (codeBlocks.size() == 1){
            return;
        }
        saveOther(name, codeBlocks.remove(0).text(), codeBlocks.stream().map(Element::text).collect(Collectors.toList()));

    }

    private void saveJava(String name, String code){
        writeFile(new File(fileManager.getJavaDirectory(), name + ".java"), code);
    }

    private void writeFile(File file, String contents){
        try {
            Writer writer = new FileWriter(file);
            writer.write(contents);
            writer.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void saveOther(String name, String command, List<String> codeBlocks){
        File submissionDirectory = new File(fileManager.getAlternateDirectory(), name);
        submissionDirectory.mkdir();
        File commandFile = new File(submissionDirectory, PipeCommunicator.COMMAND_FILE);
        writeFile(commandFile, command);
        for (String codeBlock: codeBlocks){
            int lineIndex = codeBlock.indexOf('\n');
            String fileName = codeBlock.substring(0, lineIndex).trim();
            if (!fileName.contains(".") || fileName.contains(" ")){
                continue;
            }
            File dest = new File(submissionDirectory, fileName);
            try{
                dest.getCanonicalPath();
            } catch (IOException e){
                continue;
            }
            String contents = codeBlock.substring(lineIndex);
            writeFile(new File(submissionDirectory, fileName), contents);
        }
    }

}
