package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.DummyItem;
import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.entities.SearchQuery;
import com.project.hippohippogo.entities.Trends;
import com.project.hippohippogo.repositories.*;
import com.project.hippohippogo.services.QueryProcessorService;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class SearchController {

    private DummyRepository dummyRepository;
    private QueriesRepository queriesRepository;
    private PagesRepository pagesRepository;
    private QueryProcessorService queryProcessorService;
    private TrendsRepository trendsRepository;
    @Autowired
    public void setDummyRepository(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    @Autowired
    public void setQueriesRepository(QueriesRepository queriesRepository) {
        this.queriesRepository = queriesRepository;
    }

    @Autowired
    public void setTrendsRepository(TrendsRepository trendsRepository) {
        this.trendsRepository = trendsRepository;
    }


    @Autowired
    public void setPagesRepository(PagesRepository pagesRepository) {
        this.pagesRepository = pagesRepository;
    }

    @Autowired
    public void setQueryProcessorService(QueryProcessorService queryProcessorService) {
        this.queryProcessorService = queryProcessorService;
    }

    @RequestMapping(value = "/search", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Page> getWebResultsAsJSON(@RequestParam("q") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        // List<Integer> resultsIds = queryProcessorService.getPageResults(queryString);
        List<Integer> resultsIds = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Page> results = pagesRepository.findAllByIdIn(resultsIds, pageable);
        System.out.print(results);
        return results;
    }

    @RequestMapping(value = "/search", produces = "text/html", method = RequestMethod.GET)
    public String getWebResultsAsHTML(Model model, @RequestParam("q") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        // Return to landing page if query is empty
        if (queryString.equals("")) {
            return "index";
        }

        // Register query for suggestions
        Optional<SearchQuery> searchQuery = queriesRepository.findById(queryString);
        if (!searchQuery.isPresent()) {
            SearchQuery newSearchQuery = new SearchQuery(queryString.toLowerCase());
            queriesRepository.save(newSearchQuery);
        } else {
            searchQuery.get().setQuery(searchQuery.get().getQuery().toLowerCase());
            searchQuery.get().incrementHits();
            queriesRepository.save(searchQuery.get());
        }

        // Get Results
        // List<Integer> resultsIds = queryProcessorService.getPageResults(queryString);
        List<Integer> resultsIds = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Page> results = pagesRepository.findAllByIdIn(resultsIds, pageable);
        model.addAttribute("query", queryString);
        model.addAttribute("results", results);
        return "results";
    }

    @GetMapping("/img")
    public String getImgResults(Model model) {
        List<DummyItem> items = (List<DummyItem>) dummyRepository.findAll();
        model.addAttribute("items", items);
        return "showDummyData";
    }
    public void checkIfPerson(String query)
    {
        boolean prevPerson=false;
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String str = " ";
        ArrayList<String> names=new ArrayList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(query);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if (ne.equalsIgnoreCase("Person"))
                {
                    if (prevPerson==false)
                    {
                        str=word;
                    }
                    else {

                        str=(new StringBuilder()).append(str).append(" ").append(word).toString();
                    }
                    prevPerson=true;
                }
                else{
                    if(prevPerson==true)
                    {
                        names.add(str);
                    }
                    prevPerson=false;
                }
            }
            if(prevPerson==true)
            {
                names.add(str);
            }
        }
        System.out.println(names);

    }


    public void checkIfPersonUsingOpenNLP(String query) throws IOException {
            //InputStream inputStream = new FileInputStream("C:/OpenNLP_models/en-ner-person.bin");
        ArrayList<String> names=new ArrayList<String>();
        InputStream inputStream = getClass().getResourceAsStream("/en-ner-person.zip");
        TokenNameFinderModel model = new TokenNameFinderModel(inputStream);

        //Instantiating the NameFinder class
        NameFinderME nameFinder = new NameFinderME(model);
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String tokens[] = tokenizer.tokenize(query);
        //Getting the sentence in the form of String array

        //Finding the names in the sentence
        Span nameSpans[] = nameFinder.find(tokens);

        //Printing the spans of the names in the sentence
        for(Span s: nameSpans) {
            StringBuilder builder = new StringBuilder();
            System.out.println(s.toString());
            for (int i = s.getStart(); i < s.getEnd(); i++) {
                builder.append(tokens[i]).append(" ");
            }
            names.add(builder.toString());
        }
        System.out.println(names);
        for (int i=0;i<names.size();i++)
        {
            Optional<Trends> searchPerson = trendsRepository.findById(names.get(i));
            if (!searchPerson.isPresent()) {
                Trends newSearchPerson = new Trends(names.get(i).toLowerCase());
                trendsRepository.save(newSearchPerson);
            } else {
                searchPerson.get().setPerson(searchPerson.get().getPerson().toLowerCase());
                searchPerson.get().incrementHits();
                trendsRepository.save(searchPerson.get());
            }

        }

    }
}