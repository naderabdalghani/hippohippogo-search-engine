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


    public final class Pair<String,Integer> {
        private final String l;
        private final Integer r;
        private Pair(String l, Integer r){
            this.l = l;
            this.r = r;
        }
        //public L getL(){ return l; }
        //public R getR(){ return r; }
        //public void setL(L l){ this.l = l; }
        //public void setR(R r){ this.r = r; }
        public String print(){String x= (String) ("("+this.l+","+this.r+")");
            return x;}
        // Overriding the hashcode() function
        @Override
        public int hashCode()
        {

            // uses roll no to verify the uniqueness
            // of the object of Student class

            final int temp = 7;
            int stringHC = this.l.hashCode();
            int ans = temp * stringHC + (int)this.r;
            return ans;
        }

        // Equal objects must produce the same
        // hash code as long as they are equal
        @Override
        public boolean equals(Object o)
        {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            Pair other = (Pair)o;
            if (this.r != other.r) {
                return false;
            }
            if (this.l.hashCode() != other.l.hashCode()) {
                return false;
            }
            return true;
        }
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
    public  void indexing(ArrayList<String> stemmedWords, Map<String, ArrayList<Integer>> wordsToDocs, Map<Pair, Vector<Integer>> wordAndDocToIndicies,Integer pageID) {
        /*---------------------------------Indexing---------------------------------*/

        //don't forget to clear finished words after a document is finished

        // if word msh mwgoda fel hashmapp hn3ml arraylist<vector>   if (!words.containsKey(word))
        //ArrayList<Vector> word=new ArrayList<Vector>();
        //words.put("game", word);
        //lw mwgoda 5alas
        ArrayList<String> finishedwords= new ArrayList<String>();
        //Vector v = new Vector();
        Vector<Integer> indices=new Vector<Integer>();
        for (int k = 0; k <stemmedWords.size(); k++)
        {
            indices.clear();
            String wordToSearch = stemmedWords.get(k);
            // if word is repeated in the file so we already finished
            if (!finishedwords.contains(wordToSearch))
            {
                // if word is not in wordsToDocs dictionary make new arraylist and assign it to map
                if (!wordsToDocs.containsKey(wordToSearch)) {
                    ArrayList<Integer> docs = new ArrayList<Integer>();
                    wordsToDocs.put(wordToSearch, docs);
                }
                // add current document to the list
                wordsToDocs.get(wordToSearch).add(pageID);
                // get indices
                Pair x = new Pair<String,Integer>(wordToSearch,pageID);
                wordAndDocToIndicies.put(x,new Vector<Integer>());
                for (int i = k; i < stemmedWords.size(); i++)
                {
                    if (wordToSearch.equals(stemmedWords.get(i))) {
                        wordAndDocToIndicies.get(x).add(i);
                    }
                }
                //Make a pair of word and docid and assign to it indices
                finishedwords.add(wordToSearch);
            }
        }

    }




}

