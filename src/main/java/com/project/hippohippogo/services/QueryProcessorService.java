package com.project.hippohippogo.services;

import org.tartarus.snowball.ext.englishStemmer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryProcessorService {
    public static ArrayList<String> preprocessing(String document) throws IOException
    {
        //Removing nonalphanumeric characters
        document = document.replaceAll("[^a-zA-Z0-9]", " ");
        //Removing stop words
        List<String> stopwords = Files.readAllLines(Paths.get("E:\\Muhanad\\CUFE\\Third year\\2nd Semester\\Advanced programming\\Project\\hippohippogo-search-engine\\English_stopwords.txt"));
        ArrayList<String> allWords =
                Stream.of(document.toLowerCase().split(" +"))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);
        String result = allWords.stream().collect(Collectors.joining(" "));
        System.out.println(result);
        //stemming
        englishStemmer stemmer = new englishStemmer();
        ArrayList<String> stemmedWords = new ArrayList<String>();
        for (int i = 0;i<allWords.size();i++) {
            stemmer.setCurrent(allWords.get(i));
            stemmer.stem();
            stemmedWords.add(stemmer.getCurrent());
        }
        System.out.println(stemmedWords);
        String resultafterstemming = stemmedWords.stream().collect(Collectors.joining(" "));
        System.out.println(resultafterstemming);
        return stemmedWords;
    }

    
}
