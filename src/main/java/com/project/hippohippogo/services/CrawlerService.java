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

import java.io.*;
import java.util.Scanner;
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
        Integer till = 100;
        private String MainSeed;
        int status;

        public CrawlerThreaded(String seed, int status) {
            count = 0;
            MainSeed = seed;
            this.status = status;
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
            if(description.length() > 400) {
                description = description.substring(0,400);
            }
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
                if (count < till){
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
                write = new FileWriter("state/queue" + Thread.currentThread().getName()  + ".txt");
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
            try (FileWriter f = new FileWriter("state/visited" + Thread.currentThread().getName() + ".txt", true);
                 BufferedWriter b = new BufferedWriter(f);
                 PrintWriter p = new PrintWriter(b);) {
                p.println(link);
            } catch (IOException i) {
                //if open the file failed then create new one then append to it afterwords
                FileWriter write = null;
                try {
                    System.out.println(Thread.currentThread().getName() + "saved visited");
                    write = new FileWriter("state/visited" + Thread.currentThread().getName() + ".txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PrintWriter print_line = new PrintWriter(write);
                print_line.println(link);
                print_line.close();
            }
        }

        private void ReadState() {
            ReadVisited();
            ReadQueue();
            try {
                File myObj = new File("state/count" + Thread.currentThread().getName() + ".txt");
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
                File myObj = new File("state/queue" + Thread.currentThread().getName()  + ".txt");
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
                File myObj = new File("state/visited" + Thread.currentThread().getName()  + ".txt");
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

        public void Crawl(String seed, int status) {
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
                    AppendVisited(link);
                    System.out.println(i);
                } catch (Exception e) {
                    count--;
                    System.out.println(i.toString()+link);
                    System.out.println(e);
                    System.out.println("i am here");
                }
            }
        }

        @Override
        public void run() {
            Crawl(MainSeed,status);
        }
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
        if (status == 0) {
            PageRepo.deleteAll();
            pagesConnectionRepository.deleteAll();
        }

        Thread t0 = new Thread(new CrawlerThreaded("https://www.geeksforgeeks.org/", status));
        Thread t1 = new Thread(new CrawlerThreaded("https://www.who.int/",status));
        Thread t2 = new Thread(new CrawlerThreaded("https://www.bbc.com/",status));
        Thread t3 = new Thread(new CrawlerThreaded("https://www.quora.com",status));
        Thread t4 = new Thread(new CrawlerThreaded("https://www.stackoverflow.com",status));

        t0.setName("thread_0");
        t1.setName("thread_1");
        t2.setName("thread_2");
        t3.setName("thread_3");
        t4.setName("thread_4");

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t0.join();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SaveStatusZero();
    }


}
