package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.*;
import com.project.hippohippogo.ids.QueryId;
import com.project.hippohippogo.ids.TrendsId;
import com.project.hippohippogo.repositories.PagesRepository;
import com.project.hippohippogo.repositories.QueriesRepository;
import com.project.hippohippogo.repositories.TrendsRepository;
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
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class SearchController {

    private QueriesRepository queriesRepository;
    private PagesRepository pagesRepository;
    private QueryProcessorService queryProcessorService;
    private TrendsRepository trendsRepository;
    private ImageRepository imageRepository;

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
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
    public List<Page> getWebResultsAsJSON(@RequestParam(value = "q", required = false, defaultValue = "") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit, @RequestParam(value = "region", required = false, defaultValue = "") String region, HttpServletRequest request) {
        region = region.length() == 0 ? null : region;
        String userIp = request.getRemoteAddr();
        List<Integer> resultsIds = queryProcessorService.getPageResults(queryString,region,userIp);
        //List<Integer> resultsIds = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Page> results = pagesRepository.findAllByIdIn(resultsIds, pageable);
        return results;
    }

    @RequestMapping(value = "/search", produces = "text/html", method = RequestMethod.GET)
    public String getWebResultsAsHTML(Model model, @RequestParam(value = "q", required = false, defaultValue = "") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit, @RequestParam(value = "region", required = false, defaultValue = "") String region, HttpServletRequest request) throws IOException {
        // Return to landing page if query is empty
        if (queryString.equals("")) {
            return "index";
        }

        // Add query for suggestions if new or increment its hits if it already exists
        region = region.length() == 0 ? null : region;
        String userIp = request.getRemoteAddr();
        QueryId queryId = new QueryId(userIp, queryString.toLowerCase());
        Optional<Query> query = queriesRepository.findById(queryId);
        if (query.isPresent()) {
            query.get().incrementHits();
            queriesRepository.save(query.get());
        } else {
            Query newQuery = new Query(userIp, queryString.toLowerCase());
            queriesRepository.save(newQuery);
        }

        // Fetch Results
        List<Integer> resultsIds = queryProcessorService.getPageResults(queryString,region,userIp);
        //List<Integer> resultsIds = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Page> results = pagesRepository.findAllByIdIn(resultsIds, pageable);
        model.addAttribute("query", queryString);
        model.addAttribute("results", results);
        model.addAttribute("region", region);
        checkIfPersonUsingOpenNLP(queryString,region);
        return "results";
    }

    @RequestMapping(value = "/img", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Image> getImgResultsAsJSON(@RequestParam(value = "q", required = false, defaultValue = "") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit, @RequestParam(value = "region", required = false, defaultValue = "") String region, HttpServletRequest request) {
        region = region.length() == 0 ? null : region;
        String userIp = request.getRemoteAddr();
        // List<Integer> resultsIds = queryProcessorService.getPageResults(queryString,region,userIp);
        List<Integer> resultsIds = Arrays.asList(225, 245, 312, 314, 214, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 24, 21, 22, 23, 28, 20, 26, 111, 123,122, 345, 345, 212, 214, 213, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 226);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Image> results = imageRepository.findAllByIdIn(resultsIds, pageable);
        return results;
    }

    @RequestMapping(value = "/img", produces = "text/html", method = RequestMethod.GET)
    public String getImgResultsAsHTML(Model model, @RequestParam(value = "q", required = false, defaultValue = "") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit, @RequestParam(value = "region", required = false, defaultValue = "") String region, HttpServletRequest request) throws IOException {
        // Return to landing page if query is empty
        if (queryString.equals("")) {
            return "index";
        }

        // Add query for suggestions if new or increment its hits if it already exists
        region = region.length() == 0 ? null : region;
        String userIp = request.getRemoteAddr();
        QueryId queryId = new QueryId(userIp, queryString.toLowerCase());
        Optional<Query> query = queriesRepository.findById(queryId);
        if (query.isPresent()) {
            query.get().incrementHits();
            queriesRepository.save(query.get());
        } else {
            Query newQuery = new Query(userIp, queryString.toLowerCase());
            queriesRepository.save(newQuery);
        }

        // Fetch Results
        // List<Integer> resultsIds = queryProcessorService.getPageResults(queryString,region,userIp);
        List<Integer> resultsIds = Arrays.asList(225, 245, 312, 314, 214, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 24, 21, 22, 23, 28, 20, 26, 111, 123,122, 345, 345, 212, 214, 213, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 226);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Image> results = imageRepository.findAllByIdIn(resultsIds, pageable);
        model.addAttribute("query", queryString);
        model.addAttribute("results", results);
        model.addAttribute("region", region);
        checkIfPersonUsingOpenNLP(queryString,region);
        return "imageResults";
    }

    @RequestMapping(value = "/trends", produces = "text/html", method = RequestMethod.GET)
    public String getTrendsAsHTML(Model model, @RequestParam(value = "region", required = false, defaultValue = "") String region, HttpServletRequest request) {
        Locale locale  = new Locale("", region.toUpperCase());
        model.addAttribute("region", region);
        model.addAttribute("regionName", locale.getDisplayCountry());
        return "trends";
    }

    @RequestMapping(value = "/trends", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Trends> getTrendsAsJSON(Model model, @RequestParam(value = "region", required = false, defaultValue = "") String region, HttpServletRequest request) {
        Locale locale  = new Locale("", region.toUpperCase());
        List<Trends> results;
        if (region.length() != 0) {
            results = trendsRepository.findTopTenByRegion(region);
        }
        else {
            results = trendsRepository.findTopTenOverall();
        }
        return results;
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


    public void checkIfPersonUsingOpenNLP(String query,String Region) throws IOException {
        if (Region==null)
        {
            Region="";
        }
        query = WordUtils.capitalizeFully(query);
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
            String name = names.get(i).toLowerCase();
            name = name.substring(0, name.length() - 1);
            TrendsId a = new TrendsId(name, Region);
            Optional<Trends> searchPerson = trendsRepository.findById(a);
            if (!searchPerson.isPresent()) {
                Trends newSearchPerson = new Trends(name, Region);
                trendsRepository.save(newSearchPerson);
            } else {
                searchPerson.get().incrementHits();
                trendsRepository.save(searchPerson.get());
            }

        }

    }
}