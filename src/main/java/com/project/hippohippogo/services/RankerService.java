package com.project.hippohippogo.services;

import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.entities.PageRank;
import com.project.hippohippogo.entities.PagesConnection;
import com.project.hippohippogo.repositories.PageRankRepository;
import com.project.hippohippogo.repositories.PagesRepository;
import com.project.hippohippogo.repositories.PagesConnectionRepository;
import com.project.hippohippogo.repositories.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RankerService {
    private final int pageRankIterations = 20; // Number of iterations on page ranks
    private final double d = 0.85; // Damping factor
    private PagesConnectionRepository pagesConnection;
    private PageRankRepository pageRankRepository;
    private WordsRepository wordsRepository;
    private PagesRepository pagesRepository;


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

    // This function is used to set pages rank in its table
    public void rankPages() {
        // Empty table before beginning
        pageRankRepository.deleteAll();
        List<PagesConnection> pageConnectionsArray = (List<PagesConnection>) pagesConnection.findAll();
        // Holding Page rank of each iteration
        HashMap<String,Double> pageRankHashTable = new HashMap<String,Double>();
        // Temp map for page_rank table as a database table for page_rank table
        HashMap<String,PageRank> tempHashMap = new HashMap<String,PageRank>();
        // Initialize pages in page_rank table with rank = 1
        for (PagesConnection p : pageConnectionsArray) {
            Optional<PageRank> pageRank1 = pageRankRepository.findById(p.getReferred());
            Optional<PageRank> pageRank2 = pageRankRepository.findById(p.getReferring());
            // If we found that the page is added and we find it again in Referring column then outLinks++
            // Else add the page to page_rank table
            if (pageRank2.isPresent()) {
                pageRank2.get().setOut_links(pageRank2.get().getOut_links()+1);
                pageRankRepository.save(pageRank2.get());
                tempHashMap.put(pageRank2.get().getPage(),pageRank2.get());
            } else {
                PageRank pr = new PageRank(p.getReferring(),1,1);
                pageRankRepository.save(pr);
                tempHashMap.put(pr.getPage(),pr);
                pageRankHashTable.put(pr.getPage(),(double)0);
            }
            // If the page was in the referred column and it wasn't added before then add it to page_rank table
            if (pageRank1.isPresent()) {
                continue;
            } else {
                PageRank pr = new PageRank(p.getReferred(),1,0);
                pageRankRepository.save(pr);
                tempHashMap.put(pr.getPage(),pr);
                pageRankHashTable.put(pr.getPage(),(double)0);
            }
        }

        List<PageRank> pageRankList = pageRankRepository.findAll(); // Holding pages in Page Rank table to iterate on it and get the rank value for each page in it
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

    static String getString(String link) {
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

        return link;
    }

    // Function to return the web pages URLs to be used as a search result
    public List<Integer> getPageURLs(String query) {
        long numberOfDocs = pagesRepository.count();
        List<String> words = Arrays.asList(query.split(" "));
        // Key is the doc id and value is the TF-IDF value
        HashMap<Integer,Double> pagesHashMap = new HashMap<Integer,Double>();
        for (String s : words) {
            List<Integer> docs = wordsRepository.getDocIdContainingWord(s);
            double IDF = Math.log((double)numberOfDocs/docs.size());
            if (docs.isEmpty())
                continue;
            else {
                double TF;
                for (int i : docs) {
                    Optional<Page> document = pagesRepository.findById(i);
                    // Getting page rank element of the page
                    Optional<PageRank> pageRank = pageRankRepository.findById(getBaseURL(document.get().getLink()));
                    int docLength = document.get().getLength();
                    int wordCount = wordsRepository.getWordCountInDoc(s,i);
                    TF = (double)wordCount/docLength;
                    // Handling spam if TF is higher then 0.5 then it's spam and make it equals to 0
                    TF = TF < 0.5 ? TF : 0;
                    // Weighted function from Page Rank and TF-IDF
                    double weightedTfIdfPageRank = 0.5*TF*IDF + 0.5*pageRank.get().getRank();
                    // If this page used before then add the TF-IDF of the other word to the page
                    pagesHashMap.put(i,pagesHashMap.getOrDefault(i,(double)0)+weightedTfIdfPageRank);
                }
            }
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
