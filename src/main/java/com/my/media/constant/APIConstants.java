package com.my.media.constant;

/**
 * A class for the constant values used accross the API.
 * 
 * @author ben-maliktchamalam
 */
public final class APIConstants {
   
    private APIConstants() {}

    public static final String ITUNES_URL = "http://itunes.apple.com/search?media=album&entity=album&term=";
    public static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";
    public static final int GOOGLE_BOOK_API_MAXIMUM_RESULT_VALUE = 40;
    public static final int DEFAULT_START_INDEX = 0;
    public static final int MAXIMUM_NUMBER_OF_MINUTE_TO_WAIT = 1;
    public static final int OK = 200;

    public static final String DEFAULT_SEARCH_INPUT = "kramp hub"; //to be used when the given input text is invalid of null.
    public static final String BLANK_SPACE = " ";
    public static final String INPUT_TERMS_SEPERATOR = "+";

}