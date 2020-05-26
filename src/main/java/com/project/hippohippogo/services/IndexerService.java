package com.project.hippohippogo.services;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.project.hippohippogo.entities.Words;
import com.project.hippohippogo.repositories.PagesRepository;
import com.project.hippohippogo.repositories.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.tartarus.snowball.ext.englishStemmer;

@Service
public class IndexerService {

    private PagesRepository pagesRepository;

    @Autowired
    public void setPagesRepository(PagesRepository pagesRepository) {
        this.pagesRepository = pagesRepository;
    }

    private WordsRepository wordsRepository;

    @Autowired
    public void setPagesRepository(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
    }


    public static ArrayList<String> preprocessing(String document) throws IOException
    {

        /*--------------------Removing nonalphanumeric characters--------------------*/
        document = document.replaceAll("[^a-zA-Z0-9]", " ");
        /*----------------------------Removing stop words----------------------------*/
        List<String> stopwords = Files.readAllLines(Paths.get("C:\\Users\\Mahmood\\Music\\English_stopwords.txt"));
        ArrayList<String> allWords =
                Stream.of(document.toLowerCase().split(" +"))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);
        String result = allWords.stream().collect(Collectors.joining(" "));
        //System.out.println(result);
        /*---------------------------------Stemming---------------------------------*/
        englishStemmer stemmer = new englishStemmer();
        ArrayList<String> stemmedWords = new ArrayList<String>();
        for (int i = 0;i<allWords.size();i++) {
            stemmer.setCurrent(allWords.get(i));
            stemmer.stem();
            stemmedWords.add(stemmer.getCurrent());
        }
        //System.out.println(stemmedWords);
        //String resultafterstemming = stemmedWords.stream().collect(Collectors.joining(" "));
        //System.out.println(resultafterstemming);
        return stemmedWords;
    }



}

