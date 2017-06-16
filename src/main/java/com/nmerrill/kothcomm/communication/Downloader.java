package com.nmerrill.kothcomm.communication;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nmerrill.kothcomm.communication.languages.Language;
import com.nmerrill.kothcomm.exceptions.DownloadingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;


public final class Downloader {

    public final static String QUERY_URL = "http://api.stackexchange.com/2.2/questions/";
    public final static String QUERY_PARAMS = "/answers?pagesize=100&order=desc&sort=activity&site=codegolf&filter=!FcbKgRqyv4bqdqoj9fAB6fZ05P";
    private final URL url;
    private final LanguageLoader languageLoader;

    public Downloader(LanguageLoader languageLoader, int questionID){
        if (questionID <= 0){
            throw new DownloadingException("Invalid question id");
        }
        try {
            url = new URL(QUERY_URL + questionID + QUERY_PARAMS);
        } catch (MalformedURLException e){
            throw new DownloadingException("Invalid url");
        }
        this.languageLoader = languageLoader;
    }
    public void downloadQuestions(){
        try {
            readJSON(new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream()))));
        } catch (IOException e){
            throw new DownloadingException("Unable to read response", e);
        }
    }

    private void readJSON(BufferedReader reader) throws IOException{
        JsonObject parent = new JsonParser().parse(reader).getAsJsonObject();
        JsonArray items = parent.get("items").getAsJsonArray();
        if (items.size() == 0){
            System.out.println("No submissions");
        }
        for (JsonElement item: parent.get("items").getAsJsonArray()){
            String body = item.getAsJsonObject().get("body").getAsString();
            saveSubmission(body);
        }
    }

    private void saveSubmission(String html){
        Document document = Jsoup.parse(html);
        Elements elements = document.select("h1,h2,h3,h4,h5,h6");
        if (elements.isEmpty()){
            System.out.println("No header found");
            return;
        }
        String header = elements.get(0).text();
        if (header.contains("Invalid")){
            return;
        }
        String[] parts =  header.split(",");
        String language = parts.length < 2 ? "" : parts[1];
        List<Element> codeBlocks = document.select("pre>code");
        if (codeBlocks.size() == 0){
            System.out.println("No code blocks in submission:"+header);
            return;
        }

        Language loader = languageLoader.byName(language);
        if (loader == null){
            System.out.println("Cannot find a language with the name "+language);
        }
        File directory = languageLoader.getDirectory(loader);

        for (Element element: codeBlocks){
            saveOther(element.text(), directory);
        }

    }

    private void writeFile(File file, String contents){
        try {
            Writer writer = new FileWriter(file);
            writer.write(contents);
            writer.close();
        } catch (IOException e){
            throw new DownloadingException("Unable to save response", e);
        }
    }

    private void saveOther(String codeBlock, File directory){

        int lineIndex = codeBlock.indexOf('\n');
        String fileName = codeBlock.substring(0, lineIndex).trim();
        if (!fileName.contains(".") || fileName.contains(" ")){
            System.out.println("Skipping code block, doesn't contain valid filename: "+fileName);
            return;
        }
        File dest = new File(directory, fileName);
        String contents = codeBlock.substring(lineIndex);
        writeFile(dest, contents);
    }

}
