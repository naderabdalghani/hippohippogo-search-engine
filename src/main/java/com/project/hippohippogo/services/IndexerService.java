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

    public class IndexerThreaded implements Runnable {
        int startingWebPage;
        int endingWebPage;
        ArrayList<String> webpages;
        ArrayList<Integer> webpagesIds;

        public IndexerThreaded(int x,int y,ArrayList<String> webpages,ArrayList<Integer> webpagesIds) {
            this.startingWebPage=x;
            this.endingWebPage=y;
            this.webpages=webpages;
            this.webpagesIds=webpagesIds;
        }

        public  ArrayList<String> preprocessing(String document) throws IOException {

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
            for (int i = 0; i < allWords.size(); i++) {
                stemmer.setCurrent(allWords.get(i));
                stemmer.stem();
                stemmedWords.add(stemmer.getCurrent());
            }
            //System.out.println(stemmedWords);
            //String resultafterstemming = stemmedWords.stream().collect(Collectors.joining(" "));
            //System.out.println(resultafterstemming);
            return stemmedWords;
        }

        public void indexing(ArrayList<String> stemmedWords, Map<String, ArrayList<Integer>> wordsToDocs, Map<Pair, Vector<Integer>> wordAndDocToIndicies, Integer pageID) {
            /*---------------------------------Indexing---------------------------------*/

            //don't forget to clear finished words after a document is finished

            // if word msh mwgoda fel hashmapp hn3ml arraylist<vector>   if (!words.containsKey(word))
            //ArrayList<Vector> word=new ArrayList<Vector>();
            //words.put("game", word);
            //lw mwgoda 5alas
            ArrayList<String> finishedwords = new ArrayList<String>();
            //Vector v = new Vector();
            Vector<Integer> indices = new Vector<Integer>();
            for (int k = 0; k < stemmedWords.size(); k++) {
                indices.clear();
                String wordToSearch = stemmedWords.get(k);
                // if word is repeated in the file so we already finished
                if (!finishedwords.contains(wordToSearch)) {
                    // if word is not in wordsToDocs dictionary make new arraylist and assign it to map
                    if (!wordsToDocs.containsKey(wordToSearch)) {
                        ArrayList<Integer> docs = new ArrayList<Integer>();
                        wordsToDocs.put(wordToSearch, docs);
                    }
                    // add current document to the list
                    wordsToDocs.get(wordToSearch).add(pageID);
                    // get indices
                    Pair x = new Pair<String, Integer>(wordToSearch, pageID);
                    wordAndDocToIndicies.put(x, new Vector<Integer>());
                    for (int i = k; i < stemmedWords.size(); i++) {
                        if (wordToSearch.equals(stemmedWords.get(i))) {
                            wordAndDocToIndicies.get(x).add(i);
                        }
                    }
                    //Make a pair of word and docid and assign to it indices
                    finishedwords.add(wordToSearch);
                }
            }

        }


        public void assignDb(Map<String, ArrayList<Integer>> wordsToDocs, Map<Pair, Vector<Integer>> wordAndDocToIndicies) {
            /*---------------------------------Assigning to DB---------------------------------*/
            // Get list of words
            ArrayList<String> words = new ArrayList<String>(wordsToDocs.keySet());
            System.out.println(words);
            for (int i = 0; i < words.size(); i++) {
                // Get list of docIds the word appeared in them
                ArrayList<Integer> docs = new ArrayList<Integer>(wordsToDocs.get(words.get(i)));
                System.out.println(words.get(i));
                System.out.println(docs);
                for (int j = 0; j < docs.size(); j++) {
                    // Pair of the word in that docID Return with list of indicies where the word occur
                    Pair<String, Integer> x = new Pair<>(words.get(i), docs.get(j));
                    System.out.println(x.print());
                    System.out.println(wordAndDocToIndicies.get(x));
                    Vector<Integer> indicies;
                    indicies = new Vector<Integer>(wordAndDocToIndicies.get(x));
                    System.out.println(indicies);
                    for (int k = 0; k < indicies.size(); k++) {
                        // Assign to the Database Table "words"
                        Words word = new Words(words.get(i), docs.get(j), indicies.get(k));
                        wordsRepository.save(word);
                    }
                }
            }

        }

        public void index (){
            // Map each word to list of documents appeared in it
            Map<String, ArrayList<Integer>> wordsToDocs = new HashMap<String, ArrayList<Integer>>();
            // Map each pair of  (word,docId) to list of indicies "the place of occurence of the word"
            Map<Pair, Vector<Integer>> wordAndDocToIndicies = new HashMap<Pair, Vector<Integer>>();

            ArrayList<String> stemmedWords = new ArrayList<String>();
            // Document doc = Jsoup.parse(html);
            //System.out.println(doc.text());
            //String original =doc.text() ;

            // For loop on all the webpages
            for (int i = this.startingWebPage; i <this.endingWebPage ; i++)
            {
                // Preprocess this webpage
                try {
                    stemmedWords = preprocessing(webpages.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Index this webpage
                indexing(stemmedWords, wordsToDocs, wordAndDocToIndicies, webpagesIds.get(i));
            }

        /*System.out.println(wordsToDocs);
        Iterator iterator = wordAndDocToIndicies.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Pair y= (Pair) me2.getKey();
            Vector z= (Vector) me2.getValue();
            System.out.println("Key: " + y.print()+ " & Value: " + z);
        }*/

            // Assign to the DB
            assignDb(wordsToDocs,wordAndDocToIndicies);


        }

        @Override
        public void run() {
            index();
        }
    }
    public void main() {
        wordsRepository.deleteAll();
        //ArrayList<String> webpages= new ArrayList<String>();

        // Get Webpages content
        ArrayList<String> webpages= pagesRepository.getWebPages();
        // Get Webpages Ids
        ArrayList<Integer> webpagesIds= pagesRepository.getWebPagesIds();


        //String html = "<html><head><title>First parse</title></head>"
        //      + "<body><p>Parsed? ,,HTML.!? into a doc.   ?  ? /</p></body></html>";

        //String html2 = "<html><head><title>First parse</title></head>"
        //        + "<body><p>Parsed? ,,HTML.!? into a doc.   ?  ? /</p></body></html>";
        //webpages.add(html);
        //webpages.add(html2);
        (new Thread(new IndexerService.IndexerThreaded(0,webpages.size()/5,webpages,webpagesIds))).start();
        (new Thread(new IndexerService.IndexerThreaded(webpages.size()/5,2*(webpages.size()/5),webpages,webpagesIds))).start();
        (new Thread(new IndexerService.IndexerThreaded(2*(webpages.size()/5),3*(webpages.size()/5),webpages,webpagesIds))).start();
        (new Thread(new IndexerService.IndexerThreaded(3*(webpages.size()/5),4*(webpages.size()/5),webpages,webpagesIds))).start();
        (new Thread(new IndexerService.IndexerThreaded(4*(webpages.size()/5),webpages.size(),webpages,webpagesIds))).start();

    }





}

