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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * An implementation to the {@linkplain MediaManager} interface.
 * 
 * @author ben-maliktchamalam
 */
@Service
public class MediaManagerImpl implements MediaManager {

    private static final String ITUNES_URL = "http://itunes.apple.com/search?media=album&entity=album&term=";
    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE = 40;
    private static final int DEFAULT_START_INDEX = 0;

    @Value("${upstreamLimit}")
    private int upstreamLimit;

    @Override
    public List<Media> findAllBy(String input) {

        List<Media> allMedia = new ArrayList<>();

        CompletableFuture<Void> books = CompletableFuture.runAsync(() -> {
            allMedia.addAll(findBooksBy(input, false));
        });

        CompletableFuture<Void> albums = CompletableFuture.runAsync(() -> {
            allMedia.addAll(findAlbumsBy(input, false));
        });

        CompletableFuture<Void> apiFuture = CompletableFuture.allOf(books, albums);
        try {
            apiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        allMedia.sort(Comparator.comparing((Media::getTitle)));
        System.out.println("All media size: " + allMedia.size());
        return allMedia;
    }

    @Override
    public List<Media> findAlbumsBy(String input, boolean sortOutput) {
        List<Media> searchResult = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        String query = prepareQuery(input, MediaType.ALBUM, DEFAULT_START_INDEX, upstreamLimit);
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

        if (sortOutput) {
            searchResult.sort(Comparator.comparing((Media::getTitle)));
        }
        return searchResult;
    }

    @Override
    public List<Media> findBooksBy(String input, boolean sortOutput) {

        List<Media> searchResult = new ArrayList<>();

        for (Boundary boundary: getRequestBoundaries(upstreamLimit)) {
            
            System.out.println(boundary.toString());
            
            String query = prepareQuery(input, MediaType.BOOK, boundary.getStart(), boundary.getLength());
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
    
                // In case the first output happens to have no items available.
                if (Integer.parseInt(responseAsJSONObject.get("totalItems").toString()) == 0) {
                    break;
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
                if (sortOutput) {
                    searchResult.sort(Comparator.comparing((Media::getTitle)));
                }
            } catch (MalformedURLException e22) {
                e22.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
       
        System.out.println("Total Books: " + searchResult.size());
        searchResult.forEach(e -> System.out.println(e.toString()));
        return searchResult;
    }

    private List<Media> executeQuery(String query) {
        List<Media> queryResult = new ArrayList<>();

        return queryResult;
    }
    /**
     * Prepares the query for the media given a text input and a media type.
     * 
     * @param input     The input for the query.
     * @param mediaType The type of the media for which to query for.
     * @return a ready to use query as a String value.
     */
    private String prepareQuery(String input, MediaType mediaType, int startIndex, int limit) {
        StringBuilder sb = new StringBuilder();
        input = input.replaceAll(" ", "+");
        System.out.println("limit: " + limit);
        if (mediaType == MediaType.ALBUM) {
            sb.append(ITUNES_URL + input + "&limit=" + limit);
        } else {
            sb.append(GOOGLE_BOOKS_API + input + "&maxResults="
                    + (limit > GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE ? GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE : limit) 
                    + "&startIndex=" + startIndex);
        }

        return sb.toString();
    }

    /***
     * TODO JAVADOC
     */
    @Data
    @ToString
    @AllArgsConstructor
    class Boundary {
        private int start;
        private int length;
    }

    /***
     * 
     * @param limit
     * @return
     */
    private List<Boundary> getRequestBoundaries(int limit) {
        List<Boundary> output = new ArrayList<>();
        double numberOfRange = limit / GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE;
        int currentStart = DEFAULT_START_INDEX;
        int subrangeLength = GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE;

        for (int i = 0;  i < numberOfRange; i++) {
            output.add(new Boundary(currentStart, subrangeLength));
            currentStart += subrangeLength;
        }
        if (limit % GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE != 0) {
            output.add(new Boundary(currentStart, limit - currentStart));
        }
        if (output.isEmpty() && limit <= GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE) {
            output.add(new Boundary(DEFAULT_START_INDEX, limit));
        }
        return output;
    }

}
