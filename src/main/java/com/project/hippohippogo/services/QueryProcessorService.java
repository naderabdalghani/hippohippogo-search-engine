package com.project.hippohippogo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QueryProcessorService {
    private RankerService rankerService;

    @Autowired
    public void setRankerService(RankerService rankerService) {
        this.rankerService = rankerService;
    }
    private String preprocessing(String document) throws IOException
    {
        //Removing nonalphanumeric characters
        document = document.replaceAll("[^a-zA-Z0-9]", " ");
        //Removing stop words
        URL url = getClass().getResource("E:\\Muhanad\\CUFE\\Third year\\2nd Semester\\Advanced programming\\Project\\hippohippogo-search-engine\\src\\main\\resources\\English_stopwords.txt");
        List<String> stopwords = Files.readAllLines(Paths.get("E:\\Muhanad\\CUFE\\Third year\\2nd Semester\\Advanced programming\\Project\\hippohippogo-search-engine\\src\\main\\resources\\English_stopwords.txt"));
        ArrayList<String> allWords =
                Stream.of(document.toLowerCase().split(" +"))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);
        String result = allWords.stream().collect(Collectors.joining(" "));
        //stemming
        englishStemmer stemmer = new englishStemmer();
        ArrayList<String> stemmedWords = new ArrayList<String>();
        for (int i = 0;i<allWords.size();i++) {
            stemmer.setCurrent(allWords.get(i));
            stemmer.stem();
            stemmedWords.add(stemmer.getCurrent());
        }
        String resultafterstemming = stemmedWords.stream().collect(Collectors.joining(" "));
        return resultafterstemming;
    }

    // Function to get search page results
    public List<Integer> getPageResults(String query, String location, String userIP) {
        try {
            String processedQuery = preprocessing(query);
            return rankerService.getPageIDs(processedQuery,location,userIP);
        } catch (IOException e) {
            return new ArrayList<Integer>();
        }
    }

    // Function to get search image results
    public List<Integer> getImageResults(String query, String location, String userIP) {
        try {
            String processedQuery = preprocessing(query);
            return rankerService.getImageIDS(processedQuery,location,userIP);
        } catch (IOException e) {
            return new ArrayList<Integer>();
        }
    }

}
