package com.project.hippohippogo.services;

import com.project.hippohippogo.entities.PageRank;
import com.project.hippohippogo.entities.PagesConnection;
import com.project.hippohippogo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RankerService {
    private final int pageRankIterations = 20; // Number of iterations on page ranks
    private final double d = 0.85; // Damping factor
    private final Date defaultPubDate = new Date(946688684000L); // Default publication date of the page
    private PagesConnectionRepository pagesConnection;
    private PageRankRepository pageRankRepository;
    private WordsRepository wordsRepository;
    private PagesRepository pagesRepository;
    private ImageRepository imageRepository;
    private ImagesWordsRepository imagesWordsRepository;
    private UsersFrequentDomainsRepository usersFrequentDomainsRepository;
    private WordsOccurrencesRepository wordsOccurrencesRepository;


    @Autowired
    public void setPagesConnection (PagesConnectionRepository pagesConnection) {
        this.pagesConnection = pagesConnection;
    }
    @Autowired
    public void setPageRankRepository(PageRankRepository pageRankRepository) {
        this.pageRankRepository = pageRankRepository;
    }

    @Autowired
    public void setWordsRepository(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
    }

    @Autowired
    public void setPagesRepository(PagesRepository pagesRepository) {
        this.pagesRepository = pagesRepository;
    }

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Autowired
    public void setImagesWordsRepository(ImagesWordsRepository imagesWordsRepository) {
        this.imagesWordsRepository = imagesWordsRepository;
    }

    @Autowired
    public void setUsersFrequentDomainsRepository (UsersFrequentDomainsRepository usersFrequentDomainsRepository) {
        this.usersFrequentDomainsRepository = usersFrequentDomainsRepository;
    }

    @Autowired
    public void setWordsOccurrencesRepository (WordsOccurrencesRepository wordsOccurrencesRepository) {
        this.wordsOccurrencesRepository = wordsOccurrencesRepository;
    }

    // This function is used to set pages rank in its table
    public void rankPages() {
        System.out.println("////////////////////////////////////////////////////////////");
        System.out.println("//////////////////// Page Rank Started /////////////////////");
        System.out.println("////////////////////////////////////////////////////////////");
        // Empty table before beginning
        pageRankRepository.deleteAll();
        List<PagesConnection> pageConnectionsArray = (List<PagesConnection>) pagesConnection.findAll();
        // Holding Page rank of each iteration
        HashMap<String,Double> pageRankHashTable = new HashMap<String,Double>();
        // Temp map for page_rank table as a database table for page_rank table
        HashMap<String,PageRank> tempHashMap = new HashMap<String,PageRank>();
        // Initialize pages in page_rank table with rank = 1
        for (PagesConnection p : pageConnectionsArray) {
            String page1 = p.getReferred();
            String page2 = p.getReferring();
            // If we found that the page is added and we find it again in Referring column then outLinks++
            // Else add the page to page_rank table
            PageRank pageRankDefault2 = tempHashMap.getOrDefault(page2,new PageRank(page2,1,0));
            pageRankDefault2.setOut_links(pageRankDefault2.getOut_links()+1);
            tempHashMap.put(page2,pageRankDefault2);
            pageRankHashTable.put(page2,(double)0);

            // If the page was in the referred column and it wasn't added before then add it to page_rank table
            PageRank pageRankDefault1 = tempHashMap.getOrDefault(page1,new PageRank(page1,1,0));
            tempHashMap.put(page1,pageRankDefault1);
            pageRankHashTable.put(page1,(double)0);
        }

        List<PageRank> pageRankList = new ArrayList<PageRank>(); // Holding pages in Page Rank table to iterate on it and get the rank value for each page in it

        // Getting an iterator
        Iterator hmIterator = tempHashMap.entrySet().iterator();

        // Iterate through the hashmap
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            pageRankList.add((PageRank) mapElement.getValue());
        }

        // This loop is used to iterate on page ranks and update them
        // For each loop we calculate PR(A) = (d-1)+d*(PR(B)/C(B)+...+PR(N)/C(N)
        for (int i=0;i<pageRankIterations;i++) {
            // Initialize each page rank with (1-d) at the beginning
            pageRankHashTable.replaceAll((k,v) -> (1-d));
            // Then iterate and when finding a page refer to another page update referred page rank value
            for (PagesConnection p : pageConnectionsArray) {
                PageRank pr = tempHashMap.get(p.getReferring());
                pageRankHashTable.put(p.getReferred(),pageRankHashTable.get(p.getReferred())+d*(pr.getRank()/pr.getOut_links()));
            }

            // Updating temp hash map with the last calculated values
            for (PageRank p : pageRankList) {
                p.setRank(pageRankHashTable.get(p.getPage()));
                tempHashMap.put(p.getPage(),p);
            }
        }

        // Updating database with the page rank values from the last iteration
        for (PageRank p : pageRankList) {
            p.setRank((double) tempHashMap.get(p.getPage()).getRank());
            pageRankRepository.save(p);
        }

        System.out.println("////////////////////////////////////////////////////////////");
        System.out.println("///////////////// Page Rank has Finished ///////////////////");
        System.out.println("////////////////////////////////////////////////////////////");
    }

    // Sorting HashMap using values
    private List<Map.Entry<Integer, Double>> sortByValue(HashMap<Integer, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Double> > list =
                new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        return list;
    }

    private String getBaseURL(String link) {
        // Extracting the base link from the link we have
        return getString(link);
    }

    public static String getString(String link) {
        Matcher httpsMatcher = Pattern.compile("^https://www.(.*?)/").matcher(link);
        Matcher httpsMatcher1 = Pattern.compile("^https://www.(.*?)").matcher(link);
        Matcher httpsMatcher2 = Pattern.compile("^https://(.*?)/").matcher(link);
        Matcher httpsMatcher3 = Pattern.compile("^https://(.*?)").matcher(link);
        Matcher httpMatcher = Pattern.compile("^http://www.(.*?)/").matcher(link);
        Matcher httpMatcher1 = Pattern.compile("^http://www.(.*?)").matcher(link);
        Matcher httpMatcher2 = Pattern.compile("^http://(.*?)/").matcher(link);
        Matcher httpMatcher3 = Pattern.compile("^http://(.*?)").matcher(link);

        if (httpsMatcher.find()) {
            return httpsMatcher.group(1);
        }
        if (httpsMatcher1.find()) {
            return link.substring(12, link.length());
        }
        if (httpsMatcher2.find()) {
            return httpsMatcher2.group(1);
        }
        if (httpsMatcher3.find()) {
            return link.substring(8, link.length());
        }


        if (httpMatcher.find()) {
            return httpMatcher.group(1);
        }
        if (httpMatcher1.find()) {
            return link.substring(11, link.length());
        }
        if (httpMatcher2.find()) {
            return httpMatcher2.group(1);
        }
        if (httpMatcher3.find()) {
            return link.substring(7, link.length());
        }
        // Removing spaces from link
        link = link.replaceAll("\\s+","");
        // Truncate first 255 char of the link
        link = link.substring(0, Math.min(link.length(), 255));
        return link;
    }

    // Function to return the web pages IDs to be used as a search result
    public List<Integer> getPageIDs(String query, String location, String userIP) {
        long numberOfDocs = pagesRepository.count();
        List<String> words = Arrays.asList(query.split(" "));
        // Key is the doc id and value is the TF-IDF value
        HashMap<Integer,Double> pagesHashMap = new HashMap<Integer,Double>();
        for (String s : words) {
            List<Integer> docs = wordsRepository.getDocIdContainingWord(s);
            double IDF = Math.log((double)numberOfDocs/docs.size())>0?Math.log((double)numberOfDocs/docs.size()):0.01;
            if (docs.isEmpty())
                continue;
            else {
                double TF;
                for (int i : docs) {
                    // Getting page rank element of the page
                    int docLength = pagesRepository.getPageLength(i);
                    int wordCount = wordsRepository.getWordCountInDoc(s,i);
                    if (wordsOccurrencesRepository.isWordExists(i,s) == 1) {
                        wordCount += wordsOccurrencesRepository.getTitleCount(i,s)*2 + wordsOccurrencesRepository.getHeaderCount(i,s);
                    }
                    TF = (double)wordCount/docLength;
                    // Handling spam if TF is higher then 0.5 then it's spam and make it equals to 0
                    TF = TF < 0.5 ? TF : 0;
                    // Weighted function from TF-IDF
                    double TF_IDF = TF*IDF;
                    // If this page used before then add the TF-IDF of the other word to the page
                    pagesHashMap.put(i,pagesHashMap.getOrDefault(i,(double)0)+TF_IDF);
                }
            }
        }
        // Getting an iterator
        Iterator hmIterator = pagesHashMap.entrySet().iterator();
        // Iterate through the hashmap
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            Optional<PageRank> pageRank = pageRankRepository.findById(getBaseURL(pagesRepository.getPageLink((int)mapElement.getKey())));
            // Getting publication date of page
            Date date = pagesRepository.getPageDate((int)mapElement.getKey()) != null ? pagesRepository.getPageDate((int)mapElement.getKey()) : defaultPubDate;
            Date currentDate = new Date();
            // Personalize search weight for rank function
            double personalizedSearch;
            String domain = getBaseURL(pagesRepository.getPageLink((int)mapElement.getKey()));
            if (usersFrequentDomainsRepository.isUserDomainExists(userIP,domain) == 1) {
                personalizedSearch = (double)usersFrequentDomainsRepository.getDomainHits(userIP,domain) / usersFrequentDomainsRepository.getUserDomainSum(userIP);
            }
            else {
                personalizedSearch = 0;
            }
            // Getting location of the page
            double loc = (location != null && location.equalsIgnoreCase(pagesRepository.getPageRegion((int)mapElement.getKey()))) ? 0.15 : 0;
            // Weighted function from TF-IDF, Page Rank, Time, Location, and personalized search
            double weightedRankFunction = (double)mapElement.getValue()+ 0.3*pageRank.get().getRank() + 0.15*((double)date.getTime()/currentDate.getTime()) + loc + 0.3*personalizedSearch;
            pagesHashMap.put((int)mapElement.getKey(),(double)mapElement.getValue()+ weightedRankFunction);
        }

        List<Map.Entry<Integer,Double>> sortedPageMap = sortByValue(pagesHashMap);
        List<Integer> URLids = new ArrayList<>();
        // Filling pages Ids
        for (int i = sortedPageMap.size()-1; i >= 0; i--) {
            URLids.add(sortedPageMap.get(i).getKey());
        }
        return URLids;
    }

    // Function to return the Images IDs to be used as a search result
    public List<Integer> getImageIDS(String query, String location, String userIP) {
        long numberOfDocs = imageRepository.count();
        List<String> words = Arrays.asList(query.split(" "));
        // Key is the doc id and value is the TF-IDF value
        HashMap<Integer,Double> pagesHashMap = new HashMap<Integer,Double>();
        for (String s : words) {
            List<Integer> docs = imagesWordsRepository.getDocIdContainingWord(s);
            double IDF = Math.log((double)numberOfDocs/docs.size())>0?Math.log((double)numberOfDocs/docs.size()):0.01;
            if (docs.isEmpty())
                continue;
            else {
                double TF;
                for (int i : docs) {
                    // Getting page rank element of the page
                    int docLength = imageRepository.getImageDescriptionLength(i);
                    int wordCount = imagesWordsRepository.getWordCountInDoc(s,i);
                    TF = (double)wordCount/docLength;
                    // Handling spam if TF is higher then 0.5 then it's spam and make it equals to 0
                    TF = TF < 0.5 ? TF : 0;
                    // Weighted function from TF-IDF
                    double TF_IDF = TF*IDF;
                    // If this page used before then add the TF-IDF of the other word to the page
                    pagesHashMap.put(i,pagesHashMap.getOrDefault(i,(double)0)+TF_IDF);
                }
            }
        }
        // Getting an iterator
        Iterator hmIterator = pagesHashMap.entrySet().iterator();
        // Iterate through the hashmap
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            Optional<PageRank> pageRank = pageRankRepository.findById(getBaseURL(imageRepository.getImageLink((int)mapElement.getKey())));
            // Getting publication date of page
            Date date = imageRepository.getImageDate((int)mapElement.getKey()) != null ? imageRepository.getImageDate((int)mapElement.getKey()) : defaultPubDate;
            Date currentDate = new Date();
            // Personalize search weight for rank function
            double personalizedSearch;
            String domain = getBaseURL(imageRepository.getImageLink((int)mapElement.getKey()));
            if (usersFrequentDomainsRepository.isUserDomainExists(userIP,domain) == 1) {
                personalizedSearch = (double)usersFrequentDomainsRepository.getDomainHits(userIP,domain) / usersFrequentDomainsRepository.getUserDomainSum(userIP);
            }
            else {
                personalizedSearch = 0;
            }
            // Getting location of the page
            double loc = (location != null && location.equalsIgnoreCase(imageRepository.getImageRegion((int)mapElement.getKey()))) ? 0.15 : 0;
            // Weighted function from TF-IDF, Page Rank, Time, Location, and personalized search
            double weightedRankFunction = (double)mapElement.getValue()+ 0.3*pageRank.get().getRank() + 0.15*((double)date.getTime()/currentDate.getTime()) + loc + 0.3*personalizedSearch;
            pagesHashMap.put((int)mapElement.getKey(),(double)mapElement.getValue()+ weightedRankFunction);
        }

        List<Map.Entry<Integer,Double>> sortedPageMap = sortByValue(pagesHashMap);
        List<Integer> URLids = new ArrayList<>();
        // Filling pages Ids
        for (int i = sortedPageMap.size()-1; i >= 0; i--) {
            URLids.add(sortedPageMap.get(i).getKey());
        }
        return URLids;
    }


}
