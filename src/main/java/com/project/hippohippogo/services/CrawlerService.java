package com.project.hippohippogo.services;

import com.project.hippohippogo.entities.Innerlink;
import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.repositories.InnerlinkRepository;
import com.project.hippohippogo.repositories.PageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CrawlerService {
    private PageRepository PageRepo;
    private InnerlinkRepository InnerRepo;
    private String MainSeed;

    @Autowired
    public void setPageRepository(PageRepository PageRepo) {
        this.PageRepo = PageRepo;
    }

    @Autowired
    public void setInnerRepository(InnerlinkRepository InnerRepo) {
        this.InnerRepo = InnerRepo;
    }

    public class CrawlerThreaded implements Runnable {
        Queue<String> LinksQueue = new LinkedList<>();
        Queue<String> VisitedQueue = new LinkedList<>();
        Integer count;

        public CrawlerThreaded(String seed) {
            count = 0;
            MainSeed = seed;
        }

        private void insertPageAndContent(String link, String title, String content) {
            Page P = new Page(link, title, content);
            synchronized (PageRepo){
                PageRepo.save(P);
            }
        }

        private void insertInnerlink(String Base, String Inner) {
            Innerlink I = new Innerlink(Base, Inner);
            synchronized (InnerRepo) {
                InnerRepo.save(I);
            }
        }

        private String getHtmlPage(String seed) throws IOException {
            URL url = new URL(seed);
            URLConnection urlcon = url.openConnection();
            InputStream stream = urlcon.getInputStream();
            int i;
            StringBuilder textBuilder = new StringBuilder();
            while ((i = stream.read()) != -1) {
                textBuilder.append((char) i);
            }
            return textBuilder.toString();
        }

        private String fixLink(Element Emlink, String baseURL) {
            String link = Emlink.attr("href");
            if (Pattern.compile("^//").matcher(link).find()) {
                return "https:" + link;
            } else if (Pattern.compile("^/").matcher(link).find()) {
                return baseURL + link;
            } else if (Pattern.compile("^http:").matcher(link).find()) {
                return "https:" + link.substring(5, link.length());
            }
            else {
                return link;
            }
        }

        private String getBaseURL(String link) {
            //extracting the base link from the link we have
            Matcher httpsMatcher = Pattern.compile("^https://(.*?)/").matcher(link);
            Matcher httpsMatcher2 = Pattern.compile("^https://(.*?)").matcher(link);
            Matcher httpMatcher = Pattern.compile("^http://(.*?)/").matcher(link);
            Matcher httpMatcher2 = Pattern.compile("^http://(.*?)").matcher(link);

            if (httpsMatcher.find()) {
                return httpsMatcher.group(1);
            }
            if (httpsMatcher2.find()) {
                return link.substring(8, link.length());
            }
            if (httpMatcher.find()) {
                return httpMatcher.group(1);
            }
            if (httpMatcher2.find()) {
                return link.substring(7, link.length());
            }
            return link;
        }

        private void CleanAndSetPage(String html, Document doc, String seed) {
            String content = Jsoup.clean(html, Whitelist.none());
            String title = doc.title();
            insertPageAndContent(seed, title, content);
        }

        private void getLinks(String seed) throws IOException {

            //delete / at the end of the link if there is one
            if (Pattern.compile("/$").matcher(seed).find()) {
                seed = seed.substring(0, seed.length() - 1);
            }

            //connecting to the website and getting the html page
            String html = getHtmlPage(seed);

            //parsing the html page and process it
            Document doc = Jsoup.parse(html);
            Elements links = doc.select("a[href]");     //selecting the links in the page to get them and recrawl them
            //start looping on the links and add them to  the queue
            for (Element link : links) {
                if (count<5000){
                    count++;

                    //fixing and completing the links if there is anything missing in them (like 'https://' in the begining)
                    String linkString = fixLink(link, seed);

                    //checking if we have visited the link before
                    if(!VisitedQueue.contains(linkString)) {
                        LinksQueue.add(linkString);

                        String baseSeed = getBaseURL(seed);
                        String baseLink = getBaseURL(linkString);
                        if (!baseSeed.equals(baseLink)) {
                            insertInnerlink(baseSeed, baseLink);
                        }
                    }
                }
            }
            CleanAndSetPage(html, doc, seed);
        }

        private void print() {
            System.out.println(LinksQueue);
        }

        public void Crawl(String seed) {
            try {
                getLinks(seed);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Integer i=0;
            while(!LinksQueue.isEmpty()) {
                String link = LinksQueue.remove();
                i++;
                try {
                    getLinks(link);
                    VisitedQueue.add(link);
                    System.out.println(i);
                } catch (Exception e) {
                    System.out.println(i.toString()+link);
                    System.out.println(e);
                    System.out.println("i am here");
                }
            }
        }

        @Override
        public void run() {
            Crawl(MainSeed);
        }
    }

    public void Crawl() {
        (new Thread(new CrawlerThreaded("https://www.wikipedia.org/"))).start();
      //  (new Thread(new CrawlerThreaded("https://www.geeksforgeeks.org/"))).start();
//        (new Thread(new CrawlerThreaded("https://www.bbc.com/"))).start();
//        (new Thread(new CrawlerThreaded("https://www.nationalgeographic.com/"))).start();
//        (new Thread(new CrawlerThreaded("https://www.youtube.com/"))).start();
    }
}
