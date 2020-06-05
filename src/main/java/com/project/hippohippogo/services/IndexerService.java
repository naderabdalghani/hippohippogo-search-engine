package com.project.hippohippogo.services;

import com.project.hippohippogo.entities.ImageWord;
import com.project.hippohippogo.entities.Words;
import com.project.hippohippogo.entities.WordsOccurrences;
import com.project.hippohippogo.repositories.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IndexerService {

    private PagesRepository pagesRepository;

    @Autowired
    public void setPagesRepository(PagesRepository pagesRepository) {
        this.pagesRepository = pagesRepository;
    }

    private WordsRepository wordsRepository;

    @Autowired
    public void setWordsRepository(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
    }

    private ImageRepository imageRepository;

    @Autowired
    public void setImagesRepository(ImageRepository imagesRepository) {
        this.imageRepository = imagesRepository;
    }

    private ImagesWordsRepository imageswordsRepository;

    @Autowired
    public void setImageswordsRepository(ImagesWordsRepository imageswordsRepository) {
        this.imageswordsRepository = imageswordsRepository;
    }

    private WordsOccurrencesRepository wordsOccurrencesRepository;

    @Autowired
    public void setWordsOccurrencesRepository(WordsOccurrencesRepository wordsOccurrencesRepository) {
        this.wordsOccurrencesRepository = wordsOccurrencesRepository;
    }


    public final class Pair{
        private final String l;
        private final Integer r;
        private Pair(String l, Integer r){
            this.l = l;
            this.r = r;
        }

        public String getWord() {
            return l;
        }

        public Integer getDocId() {
            return r;
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
        int type;

        public IndexerThreaded(int x,int y,ArrayList<String> webpages,ArrayList<Integer> webpagesIds,int type) {
            this.startingWebPage=x;
            this.endingWebPage=y;
            this.webpages=webpages;
            this.webpagesIds=webpagesIds;
            this.type=type;
        }

        public  ArrayList<String> preprocessing(String document) throws IOException {

            /*--------------------Removing nonalphanumeric characters--------------------*/
            document = document.replaceAll("[^a-zA-Z0-9]", " ");
            /*----------------------------Removing stop words----------------------------*/
            URL url = getClass().getResource("/English_stopwords.txt");
            //System.out.println(url.getPath());
            List<String> stopwords = Files.readAllLines(Paths.get("D:\\third year\\APT\\final assessment\\hippohippogo-search-engine\\src\\main\\resources\\English_stopwords.txt"));
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
            stemmedWords.removeAll(Collections.singleton(""));
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
                    Pair x = new Pair(wordToSearch, pageID);
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

        public void getNumberOfOccurrences(Map<Pair, ArrayList<Integer>> wordAndDocToOccurences) {
            for (int i=this.startingWebPage;i<this.endingWebPage;i++) {
                ArrayList<String> finishedWordsTitle = new ArrayList<String>();
                ArrayList<String> finishedWordsHeader = new ArrayList<String>();
                String url = pagesRepository.getPageLink(this.webpagesIds.get(i));
                Document doc = null;
                try {
                    if(url != null) {
                        doc = Jsoup.connect(url).get();
                    } else {
                        continue;
                    }
                } catch (IOException e) {

                    continue;
                }
                String title = doc.title();
                String header = doc.body().getElementsByTag("h1").text();
                ArrayList<String> stemmedWordsTitle = null;
                try {
                    stemmedWordsTitle = preprocessing(title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<String> stemmedWordsHeader = null;
                try {
                    stemmedWordsHeader = preprocessing(header);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(stemmedWordsTitle!= null) {
                    for (int j = 0; j < stemmedWordsTitle.size(); j++) {
                        if (!finishedWordsTitle.contains(stemmedWordsTitle.get(j))) {

                            Pair x = new Pair(stemmedWordsTitle.get(j), this.webpagesIds.get(i));
                            wordAndDocToOccurences.put(x, new ArrayList<Integer>());
                            int titleOccurences = Collections.frequency(stemmedWordsTitle, stemmedWordsTitle.get(j));
                            wordAndDocToOccurences.get(x).add(titleOccurences);
                            wordAndDocToOccurences.get(x).add(0);
                            finishedWordsTitle.add(stemmedWordsTitle.get(j));
                        }
                    }
                }
                if (stemmedWordsHeader!=null) {
                    for (int j = 0; j < stemmedWordsHeader.size(); j++) {
                        if (!finishedWordsHeader.contains(stemmedWordsHeader.get(j))) {
                            Pair x = new Pair(stemmedWordsHeader.get(j), this.webpagesIds.get(i));
                            int headerOccurences = Collections.frequency(stemmedWordsHeader, stemmedWordsHeader.get(j));
                            if (wordAndDocToOccurences.containsKey(x)) {
                                wordAndDocToOccurences.get(x).set(1, headerOccurences);
                            } else {
                                wordAndDocToOccurences.put(x, new ArrayList<Integer>());
                                wordAndDocToOccurences.get(x).add(0);
                                wordAndDocToOccurences.get(x).add(headerOccurences);
                            }
                            finishedWordsHeader.add(stemmedWordsHeader.get(j));
                        }
                    }
                }
            }
            //Assign to DB
            ArrayList<WordsOccurrences> occurrences=new ArrayList<WordsOccurrences>();
            ArrayList<Pair> wordsAndDocId = new ArrayList<Pair>(wordAndDocToOccurences.keySet());
            for (int i=0;i<wordsAndDocId.size();i++){
                Pair x = wordsAndDocId.get(i);
                int titlecount= wordAndDocToOccurences.get(x).get(0);
                int headercount= wordAndDocToOccurences.get(x).get(1);
                WordsOccurrences word = new WordsOccurrences(x.getWord(),x.getDocId(),titlecount,headercount);
                occurrences.add(word);
            }
            wordsOccurrencesRepository.saveAll(occurrences);
        }

        public void assignDb(Map<String, ArrayList<Integer>> wordsToDocs, Map<Pair, Vector<Integer>> wordAndDocToIndicies) {
            /*---------------------------------Assigning to DB---------------------------------*/
            // Get list of words
            ArrayList<String> words = new ArrayList<String>(wordsToDocs.keySet());
            System.out.println(words);
            ArrayList<Words> wordsToSave=new ArrayList<>();
            ArrayList<ImageWord> imagesToSave=new ArrayList<>();
            for (int i = 0; i < words.size(); i++) {
                // Get list of docIds the word appeared in them
                ArrayList<Integer> docs = new ArrayList<Integer>(wordsToDocs.get(words.get(i)));
                System.out.println(words.get(i));
                System.out.println(docs);
                for (int j = 0; j < docs.size(); j++) {
                    // Pair of the word in that docID Return with list of indicies where the word occur
                    Pair x = new Pair(words.get(i), docs.get(j));
                    System.out.println(x.print());
                    System.out.println(wordAndDocToIndicies.get(x));
                    Vector<Integer> indicies;
                    indicies = new Vector<Integer>(wordAndDocToIndicies.get(x));
                    System.out.println(indicies);
                    for (int k = 0; k < indicies.size(); k++) {
                        if (this.type==0) {
                            // Assign to the Database Table "words"
                            Words word = new Words(words.get(i), docs.get(j), indicies.get(k));
                            wordsToSave.add(word);
                            //synchronized (wordsRepository) {
                              //  wordsRepository.save(word);
                            //}
                        }
                        else {
                            // Assign to the Database Table "imageswords"
                            ImageWord word = new ImageWord(words.get(i), docs.get(j), indicies.get(k));
                            imagesToSave.add(word);
                            //synchronized (imageswordsRepository) {
                                //imageswordsRepository.save(word);
                            //}
                        }
                    }

                }

            }
            if (this.type==0)
            {
                wordsRepository.saveAll(wordsToSave);
            }
            else
            {
                imageswordsRepository.saveAll(imagesToSave);
            }
        }

        public void index (){
            // Map each word to list of documents appeared in it
            Map<String, ArrayList<Integer>> wordsToDocs = new HashMap<String, ArrayList<Integer>>();
            // Map each pair of  (word,docId) to list of indicies "the place of occurence of the word"
            Map<Pair, Vector<Integer>> wordAndDocToIndicies = new HashMap<Pair, Vector<Integer>>();
            // Map each pair of (word,docId) to two numbers countInTitle and countInHeader
            Map<Pair, ArrayList<Integer>> wordAndDocToOccurences = new HashMap<Pair, ArrayList<Integer>>();

            ArrayList<String> stemmedWords = new ArrayList<String>();
            // Document doc = Jsoup.parse(html);
            //System.out.println(doc.text());
            //String original =doc.text() ;

            // if there is no documents to index
            if (this.endingWebPage==0)
                return;
            // Get Number of Occurence of each word in title and header
            getNumberOfOccurrences(wordAndDocToOccurences);
            // For loop on all the webpages
            for (int i = this.startingWebPage; i <this.endingWebPage ; i++)
            {
                // Check if document was already indexed and needs to be updated
                // so delete it from table and then index it
                if (this.type ==0) {
                    synchronized (wordsRepository) {
                        if (wordsRepository.checkIfDocExists(webpagesIds.get(i)) == 1) {
                            wordsRepository.deleteEntriesOfDocId(webpagesIds.get(i));
                            //continue;
                        }
                    }
                }
                else {
                    synchronized (imageswordsRepository) {
                        if (imageswordsRepository. checkIfimageExists(webpagesIds.get(i))== 1) {
                            imageswordsRepository.deleteEntriesOfimageId(webpagesIds.get(i));
                            //continue;
                        }
                    }

                }
                // Preprocess this webpage
                try {
                    stemmedWords = preprocessing(webpages.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Index this webpage
                indexing(stemmedWords, wordsToDocs, wordAndDocToIndicies, webpagesIds.get(i));
                if(this.type==0) {
                    pagesRepository.setWebPagesIndexed(webpagesIds.get(i));
                }
                else
                {
                    imageRepository.setImagesIndexed(webpagesIds.get(i));
                }
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

        //wordsRepository.deleteAll();

        // Get Webpages content
        ArrayList<String> webpages= pagesRepository.getWebPages();
        // Get Webpages Ids
        ArrayList<Integer> webpagesIds= pagesRepository.getWebPagesIds();
        // Get images content
        ArrayList<String> imagepages= imageRepository.getImageContent();
        // Get images Ids
        ArrayList<Integer> imagepagesIds= imageRepository.getImagesIds();

        
        Thread t1 =new Thread(new IndexerService.IndexerThreaded(0,webpages.size()/5,webpages,webpagesIds,0));
        Thread t2 =new Thread(new IndexerService.IndexerThreaded(webpages.size()/5,2*(webpages.size()/5),webpages,webpagesIds,0));
        Thread t3 =new Thread(new IndexerService.IndexerThreaded(2*(webpages.size()/5),3*(webpages.size()/5),webpages,webpagesIds,0));
        Thread t4 =new Thread(new IndexerService.IndexerThreaded(3*(webpages.size()/5),4*(webpages.size()/5),webpages,webpagesIds,0));
        Thread t5 =new Thread(new IndexerService.IndexerThreaded(4*(webpages.size()/5),webpages.size(),webpages,webpagesIds,0));

        Thread t6 =new Thread(new IndexerService.IndexerThreaded(0,imagepages.size()/5,imagepages,imagepagesIds,1));
        Thread t7 =new Thread(new IndexerService.IndexerThreaded(imagepages.size()/5,2*(imagepages.size()/5),imagepages,imagepagesIds,1));
        Thread t8 =new Thread(new IndexerService.IndexerThreaded(2*(imagepages.size()/5),3*(imagepages.size()/5),imagepages,imagepagesIds,1));
        Thread t9 =new Thread(new IndexerService.IndexerThreaded(3*(imagepages.size()/5),4*(imagepages.size()/5),imagepages,imagepagesIds,1));
        Thread t10 =new Thread(new IndexerService.IndexerThreaded(4*(imagepages.size()/5),imagepages.size(),imagepages,imagepagesIds,1));
        
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
            t9.join();
            t10.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Indexer has finished");
    }





}

