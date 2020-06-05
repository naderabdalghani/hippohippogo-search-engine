package com.project.hippohippogo.services;

import java.io.File;
import java.io.IOException;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

import com.project.hippohippogo.entities.Image;
import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.entities.PagesConnection;
import com.project.hippohippogo.repositories.ImageRepository;
import com.project.hippohippogo.repositories.PagesConnectionRepository;
import com.project.hippohippogo.repositories.PagesRepository;
import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Pattern;

import static com.project.hippohippogo.services.RankerService.getString;

@Service
public class CrawlerService {
    private PagesRepository PageRepo;
    private PagesConnectionRepository pagesConnectionRepository;
    private ImageRepository imageRepository;


    @Autowired
    public void setPageRepository(PagesRepository PageRepo) {
        this.PageRepo = PageRepo;
    }

    @Autowired
    public void setPageConnectionRepository(PagesConnectionRepository pagesConnectionRepository) {
        this.pagesConnectionRepository = pagesConnectionRepository;
    }

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private Integer ReadStatus() {
        File myObj = new File("state/status.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String data = myReader.nextLine();
        myReader.close();
        return Integer.parseInt(data);
    }

    private void SaveStatusZero() {
        FileWriter write = null;
        try {
            write = new FileWriter("state/status.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter print_line = new PrintWriter(write);
        print_line.print(0);
        print_line.close();
    }

    private void SaveStatusOne() {
        FileWriter write = null;
        try {
            write = new FileWriter("state/status.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter print_line = new PrintWriter(write);
        print_line.print(1);
        print_line.close();
    }

    public void Crawl() {
        Integer status = ReadStatus();
//        if (status == 0) {
//            PageRepo.deleteAll();
//            pagesConnectionRepository.deleteAll();
//            imageRepository.deleteAll();
//        }

        Thread t0 = new Thread(new CrawlerThreaded("https://www.imdb.com/", status));
        Thread t1 = new Thread(new CrawlerThreaded("https://www.who.int/", status));
        Thread t2 = new Thread(new CrawlerThreaded("https://www.bbc.com/", status));
        Thread t3 = new Thread(new CrawlerThreaded("https://www.nationalgeographic.com/", status));
        Thread t4 = new Thread(new CrawlerThreaded("https://www.kingfut.com/", status));
        Thread t5 = new Thread(new CrawlerThreaded("https://www.geeksforgeeks.org/", status));
        Thread t6 = new Thread(new CrawlerThreaded("https://en.wikipedia.org/wiki/Main_Page", status));

        t0.setName("thread_0");
        t1.setName("thread_1");
        t2.setName("thread_2");
        t3.setName("thread_3");
        t4.setName("thread_4");
        t5.setName("thread_5");
        t6.setName("thread_6");

//        t0.start();
//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        t5.start();
//        t6.start();

        try {
            t0.join();
            t1.join();
//            t2.join();
//            t3.join();
//            t4.join();
//            t5.join();
//            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SaveStatusZero();

        System.out.println("/*****************************************************************************************************/");
        System.out.println("/*                                       the Crawler is done                                         */");
        System.out.println("/*****************************************************************************************************/");
    }

    public class CrawlerThreaded extends Thread {
        Queue<String> LinksQueue = new LinkedList<>();
        Queue<String> VisitedQueue = new LinkedList<>();
        Integer count;
        Integer till;
        private String MainSeed;
        int status;
        File database;
        DatabaseReader reader;


        public CrawlerThreaded(String seed, int status) {
            count = 0;
            till = 200;
            MainSeed = seed;
            this.status = status;
            database = new File("D:\\third year\\APT\\final assessment\\hippohippogo-search-engine\\database\\GeoLite2-Country_20200526\\GeoLite2-Country.mmdb");
            try {
                reader = new DatabaseReader.Builder(database).build();
            } catch (IOException e) {
                System.out.println("problem in building the database for regions#################################################");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Crawl(MainSeed, status);
        }

        public void Crawl(String seed, int status) {
            if (status == 0) {
                try {
                    VisitedQueue.add(seed);
                    CrawlPage(seed);
                    //getLinks(seed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ReadState();
            }

            Integer i = 0;
            while (!LinksQueue.isEmpty()) {
                SaveState();
                String link = LinksQueue.remove();
                try {
                    VisitedQueue.add(link);
                    CrawlPage(link);
                    i++;
                    AppendVisited(link);
                    System.out.println(Thread.currentThread().getName() + " " +i);
                } catch (Exception e) {
                    count--;
                    System.out.println(count.toString()+ "-" + link);
                    System.out.println(e);
                    System.out.println("i am here");
                }
            }
            DeleteMyFilesContent();
        }

        private void CrawlPage(String seed) throws IOException {
            if (isSocial(seed)) {
                throw new IOException("this link is social media link");
            }
            //delete / at the end of the link if there is one
            if (Pattern.compile("/$").matcher(seed).find()) {
                seed = seed.substring(0, seed.length() - 1);
            }

            //connecting to the website and getting the html page
            String html = getHtmlPage(seed);
//
//            //parsing the html page and process it
            Document doc = Jsoup.parse(html);
//            Document doc = Jsoup.connect(seed).get();


            getLinks(doc, seed);
            getImages(doc, seed);
            CleanAndSetPage(doc, seed);
        }

        private boolean isSocial(String seed) {
            String base = getBaseURL(seed);
            return (base == "facebook.com" || base == "instagram.com" || base == "twitter.com");
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

        private String getBaseURL(String link) {
            //extracting the base link from the link we have
            return getString(link);
        }

        private void getLinks(Document doc, String seed) {
            Elements links = doc.select("a[href]");     //selecting the links in the page to get them and recrawl them
            //start looping on the links and add them to  the queue
            for (Element link : links) {
                //fixing and completing the links if there is anything missing in them (like 'https://' in the begining)
                String linkString = fixLink(link.attr("href"), seed);

                String baseSeed = getBaseURL(seed);
                String baseLink = getBaseURL(linkString);
                if (!baseSeed.equals(baseLink) && !Pattern.compile("^#").matcher(baseSeed).find() && !Pattern.compile("^#").matcher(baseLink).find() && !baseLink.equals("")) {
                    insertInnerlink(baseSeed, baseLink);
                }

                if (count < till && !VisitedQueue.contains(linkString) && !Pattern.compile("^#").matcher(linkString).find() && checkRobots(seed, linkString)) {
                    count++;
                    LinksQueue.add(linkString);
                }
            }
        }

        private String getRegion(String link) throws IOException, GeoIp2Exception {
            InetAddress ipAddress = InetAddress.getByName(InetAddress.getByName(getBaseURL(link)).getHostAddress());
            CountryResponse response = reader.country(ipAddress);
            Country country = response.getCountry();
            return country.getIsoCode();
        }

        private void getImages(Document doc, String seed) {
            Elements imgs = doc.getElementsByTag("img");
            String title = doc.title();
            int length = countWords(title);
            String region = "eg";
            try {
                region = getRegion(seed);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeoIp2Exception e) {
                e.printStackTrace();
            }
            String date = doc.select("time").attr("date");
            // Loop through img tags
            for (Element img : imgs) {
                String link = fixLink(img.attr("src"), seed);
                // If alt is empty or null, add one to counter
                if (Pattern.compile("^https://").matcher(link).find()) {
                    insertImage(link, seed, title, length, region, date);
                }
            }
        }

        private void CleanAndSetPage(Document doc, String seed) {
            String content = doc.text();
            int length = countWords(content);
            String title = doc.title();
            String description = doc.select("meta[name=\"description\"]").attr("content");
            if(description == "" || description == null) {
                description = doc.select("p").first().text();
            }
            if(description.length() > 397) {
                description = description.substring(0,396);
            }
            description = description + "...";
            String date = doc.select("time").attr("date");
            String region = "eg";
            try {
                region = getRegion(seed);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeoIp2Exception e) {
                e.printStackTrace();
            }

            insertPageAndContent(content, seed, length, title, description, region, date);
        }

        private String fixLink(String link, String baseURL) {
            if (Pattern.compile("^//").matcher(link).find()) {
                return "https:" + link;
            } else if (Pattern.compile("^/").matcher(link).find()) {
                return baseURL + link;
            } else if (Pattern.compile("^http:").matcher(link).find()) {
                return "https:" + link.substring(5, link.length());
            } else {
                return link;
            }
        }

        private boolean checkRobots(String seed, String link) {
            SimpleRobotRulesParser parser = new SimpleRobotRulesParser();
            String url = "https://www." + getBaseURL(seed);
            URLConnection connection = null;
            byte[] content = null;
            try {
                connection = new URL(url).openConnection();
                content = IOUtils.toByteArray(connection);
            } catch (IOException e) {
                //e.printStackTrace();
                url = "https://" + getBaseURL(seed);
                try {
                    connection = new URL(url).openConnection();
                    content = IOUtils.toByteArray(connection);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    //System.out.println("true from the exception");
                    return true;
                }
            }

            BaseRobotRules rules = parser.parseContent(url, content, "text/plain", "*");
            //System.out.println(rules.isAllowed(link) + "from the robots");
            return rules.isAllowed(link);
        }

        public int countWords(String sentence) {
            if (sentence == null || sentence.isEmpty()) {
                return 0;
            }
            StringTokenizer tokens = new StringTokenizer(sentence);
            return tokens.countTokens();
        }

        private void insertPageAndContent(String content, String link, int length, String title, String description, String region, String date) {
            Page P;
            if (date == "" || date == null) {
                P = new Page(content, link, length, title, description, region, false);
            } else {
                P = new Page(content, link, length, title, description, region, date, false);
            }

//            ExampleMatcher modelMatcher = ExampleMatcher.matching().withIgnorePaths("id", "content", "length", "title", "description", "date", "indexed");
//            Example<Page> pageExample = Example.of(P, modelMatcher);

            synchronized (PageRepo) {
                List<Page> pageList = PageRepo.findByLink(link);
                if(pageList.size() == 0) {
                    PageRepo.save(P);
                } else {
                    count--;
                    while(pageList.size() > 0) {
                        Page p2 = pageList.remove(pageList.size()-1);
                        if(!p2.getContent().equals(P.getContent())) {
                            P.setId(p2.getId());
                            PageRepo.save(P);
                        }
                    }
                }
            }
        }

        private void insertInnerlink(String Base, String Inner) {
            PagesConnection I = new PagesConnection(Base, Inner);
//            ExampleMatcher modelMatcher = ExampleMatcher.matching().withIgnorePaths("id");
//            Example<PagesConnection> pageExample = Example.of(I, modelMatcher);

            synchronized (pagesConnectionRepository) {
                List<PagesConnection> pagesConnectionsList = pagesConnectionRepository.findByReferringAndReferred(Base, Inner);
                if(pagesConnectionsList.size() == 0) {
                    pagesConnectionRepository.save(I);
                }
            }
        }

        private void insertImage(String image_link, String source_link, String title, int length, String region, String date) {
            Image i;
            if(date == "" || date == null) {
                i = new Image(image_link, source_link, title, length, region, false);
            } else {
                i = new Image(image_link, source_link, title, length, region, date, false);
            }
//            ExampleMatcher modelMatcher = ExampleMatcher.matching().withIgnorePaths("id", "indexed");
//            Example<Image> imageExample = Example.of(i, modelMatcher);
            synchronized (imageRepository) {
                List<Image> imageList = imageRepository.findImageByImageLinkAndSourceLink(image_link, source_link);
                if (imageList.size() == 0) {
                    imageRepository.save(i);
                } else {
                    while (imageList.size() > 0) {
                        Image i2 = imageList.remove(imageList.size()-1);
                        if (!i2.getTitle().equals(i.getTitle())) {
                            i.setId(i2.getId());
                            imageRepository.save(i);
                        }
                    }
                }
            }
        }

        private void SaveState() {
            SaveQueue();
            FileWriter write = null;
            try {
                write = new FileWriter("state/count" + Thread.currentThread().getName() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter print_line = new PrintWriter(write);
            print_line.print(count);
            print_line.close();
            SaveStatusOne();
        }

        private void SaveQueue() {
            FileWriter write = null;
            try {
                write = new FileWriter("state/queue" + Thread.currentThread().getName() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter print_line = new PrintWriter(write);
            LinksQueue.forEach(link -> {
                print_line.println(link);
            });
            print_line.close();
        }

        private void AppendVisited(String link) {
            //try to open the file to append
            try (FileWriter write = new FileWriter("state/visited" + Thread.currentThread().getName() + ".txt", true);
                 BufferedWriter buffer = new BufferedWriter(write);
                 PrintWriter print_line = new PrintWriter(buffer);) {
                print_line.println(link);
            } catch (IOException i) {
                System.out.println("the exception occured####################################################################################");
            }
        }

        private void ReadState() {
            ReadVisited();
            ReadQueue();
            try {
                File myObj = new File("state/count" + Thread.currentThread().getName() + ".txt");
                if(myObj.length() > 0) {
                    Scanner myReader = new Scanner(myObj);
                    String data = myReader.nextLine();
                    count = Integer.parseInt(data);
                    myReader.close();
                } else {
                    count = till;
                }
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        private void ReadQueue() {
            try {
                File myObj = new File("state/queue" + Thread.currentThread().getName() + ".txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    LinksQueue.add(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        private void ReadVisited() {
            //I could not use the database because of each thread has its own queue and tha database stores the visited for all threads
            try {
                File myObj = new File("state/visited" + Thread.currentThread().getName() + ".txt");
                Scanner myReader = new  Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    VisitedQueue.add(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        private void DeleteMyFilesContent() {
            FileWriter writeCount = null;
            FileWriter writeVisited = null;
            FileWriter writeQueue = null;
            try {
                writeCount = new FileWriter("state/count" + Thread.currentThread().getName() + ".txt");
                writeVisited = new FileWriter("state/visited" + Thread.currentThread().getName() + ".txt");
                writeQueue = new FileWriter("state/queue" + Thread.currentThread().getName() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

            PrintWriter writerCount = new PrintWriter(writeCount);
            writerCount.print("");
            writerCount.close();

            PrintWriter writerVisited = new PrintWriter(writeVisited);
            writerVisited.print("");
            writerVisited.close();

            PrintWriter writerQueue = new PrintWriter(writeQueue);
            writerQueue.print("");
            writerQueue.close();
        }
    }
}
