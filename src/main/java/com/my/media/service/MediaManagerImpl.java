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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.my.media.constant.APIConstants;
import com.my.media.enums.MediaType;
import com.my.media.model.Media;
import com.my.media.advise.*;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
@Service("MediaManagerImpl")
public class MediaManagerImpl implements MediaManager {

    @Value("${upstreamLimit}")
    private int upstreamLimit;

    @Override
    @ExecutionTimeAdvice.TrackExecutionTime
    public List<Media> findAllBy(String input) {

        List<Media> allMedia = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Set<Callable<List<Media>>> callables = new HashSet<Callable<List<Media>>>();

        callables.add(new Callable<List<Media>>() {
            public List<Media> call() throws Exception {
                return findAlbumsBy(input, false);
            }
        });
        callables.add(new Callable<List<Media>>() {
            public List<Media> call() throws Exception {
                return findBooksBy(input, false);
            }
        });

        try {
            List<Future<List<Media>>> futures = executorService.invokeAll(callables);
            for (Future<List<Media>> future : futures) {
                allMedia.addAll(future.get());
            }
            executorService.shutdown();

            if (!executorService.awaitTermination(APIConstants.MAXIMUM_NUMBER_OF_MINUTE_TO_WAIT, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }

        allMedia.sort(Comparator.comparing((Media::getTitle)));
        return allMedia;
    }

    @Override
    @ExecutionTimeAdvice.TrackExecutionTime
    public List<Media> findAlbumsBy(String input, boolean sortOutput) {
       
        List<Media> searchResult = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        String query = prepareQuery(input, MediaType.ALBUM, APIConstants.DEFAULT_START_INDEX, upstreamLimit);
        ResponseEntity<String> response = restTemplate.getForEntity(query, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return searchResult;
        }

        JSONObject jsonpObject = new JSONObject(response.getBody());
        if (jsonpObject.isNull("results")) {
            return searchResult;
        }

        JSONArray responseArray = jsonpObject.getJSONArray("results");
        for (Object o : responseArray) {
            JSONObject current = (JSONObject) o;
            String artistName = current.getString("artistName");
            String title = current.getString("collectionName");

            searchResult.add(new Media(title, Arrays.asList(artistName), MediaType.ALBUM));
        }

        if (sortOutput) {
            searchResult.sort(Comparator.comparing((Media::getTitle)));
        }
        return searchResult;
    }

    @Override
    @ExecutionTimeAdvice.TrackExecutionTime
    public List<Media> findBooksBy(String input, boolean sortOutput) {

        List<Media> searchResult = new ArrayList<>();

        List<Boundary> requestBoundaries = getRequestBoundaries(upstreamLimit);

        ExecutorService executorService = Executors.newFixedThreadPool(requestBoundaries.size());

        Set<Callable<List<Media>>> callables = new HashSet<Callable<List<Media>>>();
        for (Boundary boundary : requestBoundaries) {
            callables.add(new Callable<List<Media>>() {
                public List<Media> call() throws Exception {
                    String query = prepareQuery(input, MediaType.BOOK, boundary.getStart(), boundary.getLength());
                    return executeBookQuery(query);
                }
            });
        }
        try {
            List<Future<List<Media>>> futures = executorService.invokeAll(callables);
            for (Future<List<Media>> future : futures) {
                searchResult.addAll(future.get());
            }
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (sortOutput) {
            searchResult.sort(Comparator.comparing((Media::getTitle)));
        }

        return searchResult;
    }

    /**
     * A helper method to execture a given Query for fetching the books.
     * 
     * @param query The query to be exectured.
     * @return a list of {@linkplain Media}s.
     */
    private List<Media> executeBookQuery(String query) {
        List<Media> queryResult = new ArrayList<>();

        try {
            URL url = new URL(query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != APIConstants.OK) {
                return queryResult;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String responseBooks = response.toString();
            JSONObject responseAsJSONObject = new JSONObject(responseBooks);

            // In case the first output happens to have no items available.
            if (Integer.parseInt(responseAsJSONObject.get("totalItems").toString()) == 0
                    || responseAsJSONObject.isNull("items")) {
                return queryResult;
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
                queryResult.add(new Media(title, authors, MediaType.BOOK));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (StringUtils.isBlank(input)) {
            input = APIConstants.DEFAULT_SEARCH_INPUT;
        }
        StringBuilder sb = new StringBuilder();
        input = input.replaceAll(APIConstants.BLANK_SPACE, APIConstants.INPUT_TERMS_SEPARATOR);
        if (mediaType == MediaType.ALBUM) {
            sb.append(APIConstants.ITUNES_URL + input + "&limit=" + limit);
        } else {
            sb.append(APIConstants.GOOGLE_BOOKS_API + input + "&maxResults="
                    + (limit > APIConstants.GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE ? APIConstants.GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE : limit)
                    + "&startIndex=" + startIndex);
        }

        return sb.toString();
    }

    /***
     * <div> A private class to encapsulate the data or boundaries used while
     * dividing the queries.
     * That is, when the upstream limit is more than the
     * GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE,
     * we divide things into boundaries, and then flush the queries using
     * concurrency.
     * </div>
     */
    @Data
    @ToString
    @AllArgsConstructor
    private class Boundary {
        private int start;
        private int length;
    }

    /***
     * Given an integer value as limit, chunks it into a lit of
     * {@linkplain Boundary} having the same lenght.
     * 
     * @param limit The limit to be used.
     * @return a list of {@linkplain Boundary}
     */
    private List<Boundary> getRequestBoundaries(int limit) {
        List<Boundary> output = new ArrayList<>();
        double numberOfRange = limit / APIConstants.GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE;
        int currentStart = APIConstants.DEFAULT_START_INDEX;
        int subrangeLength = APIConstants.GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE;

        for (int i = 0; i < numberOfRange; i++) {
            output.add(new Boundary(currentStart, subrangeLength));
            currentStart += subrangeLength;
        }
        if (limit % APIConstants.GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE != 0) {
            output.add(new Boundary(currentStart, limit - currentStart));
        }
        if (output.isEmpty() && limit <= APIConstants.GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE) {
            output.add(new Boundary(APIConstants.DEFAULT_START_INDEX, limit));
        }
        return output;
    }

}