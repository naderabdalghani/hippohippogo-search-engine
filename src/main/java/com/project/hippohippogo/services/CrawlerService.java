package com.project.hippohippogo.services;


import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.entities.PagesConnection;
import com.project.hippohippogo.repositories.PagesRepository;
import com.project.hippohippogo.repositories.PagesConnectionRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import static com.project.hippohippogo.services.RankerService.getString;

@Service
public class CrawlerService {
    private PagesRepository PageRepo;
    private PagesConnectionRepository pagesConnectionRepository;


    @Autowired
    public void setPageRepository(PagesRepository PageRepo) {
        this.PageRepo = PageRepo;
    }

    @Autowired
    public void setInnerRepository(PagesConnectionRepository pagesConnectionRepository) {
        this.pagesConnectionRepository = pagesConnectionRepository;
    }

    public class CrawlerThreaded extends Thread {
        Queue<String> LinksQueue = new LinkedList<>();
        Queue<String> VisitedQueue = new LinkedList<>();
        Integer count;
        private String MainSeed;

        public CrawlerThreaded(String seed) {
            count = 0;
            MainSeed = seed;
        }

        private void insertPageAndContent(String link, String title, String content, String description) {
            Page P = new Page(link, title, content, description);
            synchronized (PageRepo){
                PageRepo.save(P);
            }
        }

        private void insertInnerlink(String Base, String Inner) {
            PagesConnection I = new PagesConnection(Base, Inner);
            synchronized (pagesConnectionRepository) {
                pagesConnectionRepository.save(I);
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
            return getString(link);
        }

        private void CleanAndSetPage(String html, Document doc, String seed) {
            String content = doc.text();
            String title = doc.title();
            String description = doc.body().text(); // Needs revision
            insertPageAndContent(seed, title, content, description);
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
                    //fixing and completing the links if there is anything missing in them (like 'https://' in the begining)
                    String linkString = fixLink(link, seed);

                    //checking if we have visited the link before
                    if(!VisitedQueue.contains(linkString)) {
                        count++;
                        LinksQueue.add(linkString);

                        String baseSeed = getBaseURL(seed);
                        String baseLink = getBaseURL(linkString);
                        if (!baseSeed.equals(baseLink) && !Pattern.compile("^#").matcher(baseSeed).find() && !Pattern.compile("^#").matcher(baseLink).find()) {
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

        private void SaveState() {
            SaveVisited();
            SaveQueue();
            FileWriter write = null;
            try {
                write = new FileWriter("count" + getName() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter print_line = new PrintWriter(write);
            print_line.print(count);
            print_line.close();
        }

        private void SaveQueue() {
            FileWriter write = null;
            try {
                write = new FileWriter("queue" + getName()  + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter print_line = new PrintWriter(write);
            LinksQueue.forEach(link -> {
                print_line.println(link);
            });
            print_line.close();
        }

        private void SaveVisited() {
            FileWriter write = null;
            try {
                write = new FileWriter("visited" + getName() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter print_line = new PrintWriter(write);
            VisitedQueue.forEach(link -> {
                print_line.println(link);
            });
            print_line.close();
        }

        private void ReadState() {
            ReadVisited();
            ReadQueue();
            try {
                File myObj = new File("count" + getName() + ".txt");
                Scanner myReader = new Scanner(myObj);
                String data = myReader.nextLine();
                count = Integer.parseInt(data);
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        private void ReadQueue() {
            try {
                File myObj = new File("queue" + getName()  + ".txt");
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
            try {
                File myObj = new File("visited" + getName()  + ".txt");
                Scanner myReader = new Scanner(myObj);
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

        public void Crawl(String seed) {
            int status = ReadStatus();
            System.out.println("status is " + status);
            if(status == 0){
                try {
                    getLinks(seed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ReadState();
            }
            Integer i=0;
            while(!LinksQueue.isEmpty()) {
                SaveState();
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

    private Integer ReadStatus() {
        File myObj = new File("status.txt");
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
            write = new FileWriter("status.txt");
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
            write = new FileWriter("status.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter print_line = new PrintWriter(write);
        print_line.print(1);
        print_line.close();
    }

    public void Crawl() {
        Integer status = ReadStatus();
        if (status == 0) {
            SaveStatusOne();
            PageRepo.deleteAll();
            pagesConnectionRepository.deleteAll();
        }
        //(new Thread(new CrawlerThreaded("https://en.wikipedia.org/wiki/Main_Page"))).start();
        //(new Thread(new CrawlerThreaded("https://www.geeksforgeeks.org/"))).start();
        new CrawlerThreaded("https://www.geeksforgeeks.org/").Crawl("https://www.geeksforgeeks.org/");
//        (new Thread(new CrawlerThreaded("https://www.bbc.com/"))).start();
//        (new Thread(new CrawlerThreaded("https://www.nationalgeographic.com/"))).start();
//        (new Thread(new CrawlerThreaded("https://www.youtube.com/"))).start();
        SaveStatusZero();
    }


}
