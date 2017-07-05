package com.example.android.news;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    /***
     * Obtém a lista de artigos da URL informada por meio de uma requisição HTTP à API do Guardian.
     */
    public static ArrayList<Article> fetchArticleData(String stringUrl) {

        // Cria o objeto URL
        URL url = createUrl(stringUrl);

        // Realiza a requisição HTTP para a URL informada e recebe a resposta no formato JSON
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extrai os campos relevantes da resposta JSON e retorna a lista de artigos
        return extractArticlesFromJson(jsonResponse);
    }

    /**
     * Retorna novo objeto URL de uma string informada.
     */
    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            url = null;
        }
        return url;
    }

    /**
     * Realiza requisição HTTP para a URL informada e retorna o resultado em uma string.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Converte o InputStream em uma string que contém a resposta completa do servidor.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Article> extractArticlesFromJson(String articleJson) {

        ArrayList<Article> articles = new ArrayList<>();

        // Tenta obter os atributos do JSON informado. Caso o JSON esteja em um formato inválido,
        // imprime a mensagem de erro no log da aplicação.
        try {

            // Converte a string em um JSONObject e extrai o objeto 'response'
            JSONObject root = new JSONObject(articleJson);
            JSONObject response = root.getJSONObject("response");

            if (response.has("status") && response.getString("status").equals("ok")) {

                if (response.has("results")) {

                    JSONArray results = response.getJSONArray("results");

                    for (int i=0; i<results.length(); i++) {

                        JSONObject article = results.getJSONObject(i);
                        String title = article.getString("webTitle");
                        String sectionName = article.getString("sectionName");
                        String authorName = "";
                        if (article.has("tags")) {
                            JSONArray tags = article.getJSONArray("tags");
                            if (tags.length()>0) {
                                JSONObject author = tags.getJSONObject(0);
                                if (author.has("webTitle")) {
                                    authorName = author.getString("webTitle");
                                }
                            }
                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        Date publicationDate = null;
                        try {
                            publicationDate = simpleDateFormat.parse(article.getString("webPublicationDate"));
                        } catch (ParseException e) {
                            Log.e(LOG_TAG, "Problem parsing the article publication date", e);
                        }
                        String url = article.getString("webUrl");

                        articles.add(new Article(title, sectionName, authorName, publicationDate, url));
                    }
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the article JSON results", e);
        }

        // Retorna a lista de artigos
        return articles;
    }
}