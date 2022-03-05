package com.my.media.constant;

/**
 * A class for the constant values used accross the API.
 * 
 * @author ben-maliktchamalam
 */
public final class APIConstants {
   
    private APIConstants() {}

    /** The url value for searching albums using the itunes API. */
    public static final String ITUNES_URL = "http://itunes.apple.com/search?media=album&entity=album&term=";

    /**  The url value for searching for books using the Google books API. */
    public static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";

    /** The maximum value of books you can retrieve from the Google Book API in one single request. */
    public static final int GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE = 40;

    /** The default value of the starting index for fetching data on the APIs.  */
    public static final int DEFAULT_START_INDEX = 0;

    /**  The maximum amount of minutes the {@linkplain com.my.media.service.MediaManager} api would wait before rendering the value.  */
    public static final int MAXIMUM_NUMBER_OF_MINUTE_TO_WAIT = 1;

    /** The code for OK status. */
    public static final int OK = 200;

    /** The value to be used as default search input in case a wrongly formatted input is given as a parameter. */
    public static final String DEFAULT_SEARCH_INPUT = "kramp hub";

    public static final String BLANK_SPACE = " ";
    public static final String INPUT_TERMS_SEPARATOR = "+";

}