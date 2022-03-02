package com.my.media.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.my.media.enums.MediaType;
import com.my.media.model.Media;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * An implementation to the {@linkplain MediaManager} interface.
 * 
 * @author ben-maliktchamalam
 */
@Service
public class MediaManagerImpl implements MediaManager {

    private static final String ITUNES_URL = "http://itunes.apple.com/search?media=album&entity=album&term=";
    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";

    @Value("${upstreamLimit}")
    private int upstreamLimit;

    @Override
    public List<Media> findAllBy(String input) {

        List<Media> allMedia = new ArrayList<>();

        CompletableFuture<Void> books = CompletableFuture.runAsync(() -> {
            allMedia.addAll(findBooksBy(input));
        });

        CompletableFuture<Void> albums = CompletableFuture.runAsync(() -> {
            allMedia.addAll(findAlbumsBy(input));
        });
        
        CompletableFuture<Void> apiFuture = CompletableFuture.allOf(books, albums);
        try {
            apiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        allMedia.sort(Comparator.comparing((Media::getTitle)));
        return allMedia;
    }

    @Override
    public List<Media> findAlbumsBy(String input) {
        List<Media> searchResult = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        String query = prepareQuery(input, MediaType.ALBUM);
        ResponseEntity<String> response = restTemplate.getForEntity(query, String.class);
        JSONObject jsonpObject = new JSONObject(response.getBody());
        JSONArray responseArray = jsonpObject.getJSONArray("results");
        for (Object o : responseArray) {
            JSONObject current = (JSONObject) o;
            String artistName = current.getString("artistName");
            String title = current.getString("collectionName");

            Media media = new Media();
            media.setTitle(title);
            media.setAuthors(Arrays.asList(artistName));
            media.setType(MediaType.ALBUM);
            searchResult.add(media);
        }

        searchResult.sort(Comparator.comparing((Media::getTitle)));
        
        return searchResult;
    }

    @Override
    public List<Media> findBooksBy(String input) {

        // TODO Fix the problem of maxResult being larger than 40;

        List<Media> searchResult = new ArrayList<>();

        String query = prepareQuery(input, MediaType.BOOK);
        System.out.println("Query: " + query);
        try {
            URL url = new URL(query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String responseBooks = response.toString();
            JSONObject responseAsJSONObject = new JSONObject(responseBooks);

            if (Integer.parseInt(responseAsJSONObject.get("totalItems").toString()) == 0) {
                return searchResult;
            }
            JSONArray responseAJsonArray = responseAsJSONObject.getJSONArray("items");

            for (Object o : responseAJsonArray) {
                List<String> authors = new ArrayList<>();

                JSONObject currentObject = (JSONObject) o;
                JSONObject volumeInfoObject = currentObject.getJSONObject("volumeInfo");
                String title = volumeInfoObject.getString("title");
                if (!volumeInfoObject.isNull("authors")) {
                    JSONArray authorsArray = volumeInfoObject.getJSONArray("authors");

                    if (authorsArray != null && !authorsArray.isEmpty()) {
                        for (Object author : authorsArray) {
                            authors.add(author.toString());
                        }
                    }
                }

                Media currentMedia = new Media(title, authors, MediaType.BOOK);
                searchResult.add(currentMedia);
            }
            searchResult.sort(Comparator.comparing((Media::getTitle)));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    /**
     * Prepares the query for the media given a text input and a media type.
     * 
     * @param input     The input for the query.
     * @param mediaType The type of the media for which to query for.
     * @return a ready to use query as a String value.
     */
    private String prepareQuery(String input, MediaType mediaType) {
        StringBuilder sb = new StringBuilder();
        input = input.replaceAll(" ", "+");

        if (mediaType == MediaType.ALBUM) {
            sb.append(ITUNES_URL + input + "&limit=" + upstreamLimit);
        } else {
            sb.append(GOOGLE_BOOKS_API + input + "&maxResults=" + upstreamLimit);
        }

        return sb.toString();
    }

}
