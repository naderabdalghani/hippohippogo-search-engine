<br />
<p align="center">
  <a href="https://github.com/naderabdalghani/hippohippogo-search-engine">
    <img src="src/main/resources/static/assets/hippohippogo_logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">HippoHippoGo</h3>

  <p align="center">
    A simple crawler-based search engine that demonstrates the main features of a search engine (web crawling, indexing and ranking)
  </p>
</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Landing Page](#landing-page)
  * [Autocomplete Suggestions](#autocomplete-suggestions)
  * [Voice Recognition Search](#voice-recognition-search)
  * [Web Search](#web-search)
  * [Image Search](#image-search)
  * [Trends](#trends)
  * [Crawler](#crawler)
  * [Indexer](#indexer)
  * [Ranker](#ranker)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Running](#running)
* [Acknowledgements](#acknowledgements)

## About The Project

### Landing Page

![Landing Page][landing-page]

### Autocomplete Suggestions

![Autocomplete][autocomplete]

### Voice Recognition Search

![Voice Search][voice-search]

### Web Search

![Web Search][web-search]

### Image Search

![Image Search][image-search]

### Trends

![Trends][trends]

### Crawler

The [web crawler](src/main/java/com/project/hippohippogo/services/CrawlerService.java) is a Spring Boot bean service that collects documents from all over the web. The crawler starts with a list of URL addresses (seed set). It downloads the documents identified by these URLs and extracts hyper-links from them. The extracted URLs are added to the list of URLs to be downloaded. Thus, the crawler is a recursive service.
The crawler has the following features:

* The crawler maintains its state. That is, if interrupted then rerun again, it starts to crawl the documents on the list without revisiting documents that have been previously downloaded.
* It respects the robots exclusion protocol (REP).
* It's a multi-threaded crawler implementation where the user can control the number of threads before starting the crawler.

### Indexer

The output of the crawling process is a set of downloaded HTML documents. To respond to user queries fast enough, the contents of these documents are indexed using a multi-threaded [indexer service](src/main/java/com/project/hippohippogo/services/IndexerService.java) in a database table that stores the words contained in each document and their importance (e.g. whether they are presented in a `<title></title>` tag, in a `<header></header>` tag or as plain text). Words are stored with their respective documents in which they are included and the indices at which they occurred in each document.

### Ranker

The [ranker module](src/main/java/com/project/hippohippogo/services/RankerService.java) sorts documents based on their popularity and relevance to the search query.

1. Word Relevance:
Relevance is a relation between the query words and the result page. It is calculated in several ways such as tf-idf of the query word in the result page and whether the query word appeared in the title, heading or body and then the score is aggregated from all query words to produce the final page relevance score.
2. Popularity:
Popularity is a measure for the importance of any web page regardless of the requested query. PageRank algorithm is used to calculate page popularity.
3. Users' Frequent Domains: Web sites are biasedly ranked towards each user frequently visited domains which are recorded each time a user clicks on a query result.
4. Geographic Location of the User: Pages score increase if they are related to the user’s location.
5. Page Recency: A web page’s score increases if it was published recently.

### Built With

* [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Thymeleaf](https://www.thymeleaf.org/)
* [Maven](https://maven.apache.org/)
* [MySQL](https://www.mysql.com/)

## Getting Started

### Prerequisites

* Download and install Maven using this [link](https://maven.apache.org/download.cgi)
* Download and install MySQL using this [link](https://dev.mysql.com/downloads/file/?id=495321)

### Running

0. **[Optional]** Setup the database by:  
0.1 Executing [database_schema.sql](database_schema.sql)  
0.2 Importing database data from [database_dump_csv.rar](database_dump_csv.rar)

1. Run using your favorite Java IDE. In our case, we used [IntelliJ IDEA](https://www.jetbrains.com/idea/).  
1.1 To run the _Crawler Service_, uncomment the following lines in [HippoHippoGoApplication.java](src/main/java/com/project/hippohippogo/HippoHippoGoApplication.java)

	```java
	// CrawlerService crawlerService = applicationContext.getBean(CrawlerService.class);
	// crawlerService.Crawl();
	```

	1.2 To run the _Indexer Service_, uncomment the following lines in [HippoHippoGoApplication.java](src/main/java/com/project/hippohippogo/HippoHippoGoApplication.java)

	```java
	// IndexerService indexer = applicationContext.getBean(IndexerService.class);
	// indexer.main();
	```

	1.3 To run the _Ranker Service_, uncomment the following lines in [HippoHippoGoApplication.java](src/main/java/com/project/hippohippogo/HippoHippoGoApplication.java)

	```java
	// RankerService rankerService = applicationContext.getBean(RankerService.class);
	// rankerService.rankPages();
	```

## Acknowledgements

* [DuckDuckGo](https://duckduckgo.com/)
* [Flaticon](https://www.flaticon.com/)
* [Icomoon](https://icomoon.io/)
* [Linearicons](https://linearicons.com/free)

[landing-page]: src/main/resources/static/assets/landing_page.png
[autocomplete]: src/main/resources/static/assets/autocomplete.gif
[voice-search]: src/main/resources/static/assets/voice_search.gif
[web-search]: src/main/resources/static/assets/web_search.gif
[image-search]: src/main/resources/static/assets/image_search.png
[trends]: src/main/resources/static/assets/trends.gif
