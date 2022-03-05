# Media (Book & Album) API

## [Definition](DEFINITION.md)

### Tech Stack
1. **Backend**: Java 11, Spring Boot, Maven, Hibernate, Postgresql.
2. **Frontend**: HTML5, Thymeleaf, JavaScript, Bootstrap, CSS.
3. **Hosting & DevOps**: Heroku

### A live demo of the service app is available <a href="https://media-api-spring-boot.herokuapp.com" target="new">here</a>!

### To run the app

``` 
 git clone https://github.com/kramphub-recruitment/BenMalik
 cd BenMalik
```

### To create necessary maven updates
```
mvn -N io.takari:maven:wrapper
```

### To clean and install
```
 ./mvnw clean install
```

### To run the app
```
 ./mvnw spring-boot:run
```

### The app is running on Port ``` 8090 ```
Go to <a href="http://localhost:8090" target="new">localhost:8090</a>.

### Import Decisions Made
1. findAllBy(String input) service runs both findBooksBy(String input, boolean sortOutput) and findAlbumsBy(String input, boolean sortOutput) concurrently.
2. Due to the fact that Google Book API has a restriction of maxResult=40, in case the upstreamLimit is larger than the maxResult, the upstreamLimit is divided into chuncks of intervals and a thread pull having the outcome number is created to ensure the spead of the response is as high as possible.
3. Although both findAlbumsBy(String input, boolean sortOutput) and findBooksBy(String input, boolean sortOutput) return a list of Medias, to avoid redundant sorting, only the findAllBy(Strint input) does the actual storing after completion of all requests.


### Test the api without running it on your local device using Postman.

### Endpoints.

*Change `{input}` param with a string value say `Kramp Hub`*.


GET: *v1/allMedia/{input}* ~
Returns 5 books and 5 albums corresponding the input given.
```
https://media-api-spring-boot.herokuapp.com/v1/allMedia/{input} 
```
```
Sample output with input: Adele
[
    {
        "title": "25",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    },
    {
        "title": "30",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    },
    {
        "title": "Adele",
        "authors": [
            "Adele Bloemendaal"
        ],
        "type": "ALBUM"
    },
    {
        "title": "Adele",
        "authors": [
            "Chas Newkey-Burden"
        ],
        "type": "BOOK"
    },
    {
        "title": "Adele",
        "authors": [
            "Sean Smith"
        ],
        "type": "BOOK"
    },
    {
        "title": "Adele",
        "authors": [
            "Katherine E. Krohn"
        ],
        "type": "BOOK"
    },
    {
        "title": "Adèle",
        "authors": [
            "Leila Slimani"
        ],
        "type": "BOOK"
    },
    {
        "title": "Skyfall - Single",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    },
    {
        "title": "Untamed",
        "authors": [
            "Glennon Doyle"
        ],
        "type": "BOOK"
    },
    {
        "title": "iTunes Festival: London 2011 - EP",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    }
]
    
```
GET: *v1/book/{input}* ~
Returns 5 books corresponding the input given.
```
https://media-api-spring-boot.herokuapp.com/v1/book/{input}
```

```
Sample output with input: Kramp hub

[
    {
        "title": "Entscheidungstage",
        "authors": [
            "Stephan Lamby"
        ],
        "type": "BOOK"
    },
    {
        "title": "Index of Patents Issued from the United States Patent Office",
        "authors": [
            "United States. Patent Office"
        ],
        "type": "BOOK"
    },
    {
        "title": "Life in the Open Ocean",
        "authors": [
            "Joseph J. Torres",
            "Thomas G. Bailey"
        ],
        "type": "BOOK"
    },
    {
        "title": "The Cinema in the Arab Countries",
        "authors": [
            "Georges Sadoul"
        ],
        "type": "BOOK"
    },
    {
        "title": "Urban Kiz",
        "authors": [
            "Kelvin Kramp"
        ],
        "type": "BOOK"
    }
]
```

GET: *v1/album/{input}* ~
Returns 5 albums corresponding the input given.
```
https://media-api-spring-boot.herokuapp.com/v1/album/{input}
```
```
Sample output with input: Adele

[
    {
        "title": "25",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    },
    {
        "title": "30",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    },
    {
        "title": "Adele",
        "authors": [
            "Adele Bloemendaal"
        ],
        "type": "ALBUM"
    },
    {
        "title": "Skyfall - Single",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    },
    {
        "title": "iTunes Festival: London 2011 - EP",
        "authors": [
            "Adele"
        ],
        "type": "ALBUM"
    }
]
```

### To check the health of the api.

Run ```./mvn clean install spring-boot:run``` Go to http://localhost:8090/actuator/health


### The Documentation of the app is here.

https://www.tchamalam.com/media-api-doc