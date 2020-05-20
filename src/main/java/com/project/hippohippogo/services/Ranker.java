package com.project.hippohippogo.services;

import com.project.hippohippogo.entities.PageRank;
import com.project.hippohippogo.entities.PagesConnection;
import com.project.hippohippogo.repositories.PageRankRepository;
import com.project.hippohippogo.repositories.PagesConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Ranker {
    private final int pageRankIterations = 12; // Number of iterations on page ranks
    float d = 0.5f; // Damping factor
    private PagesConnectionRepository pagesConnection;
    private PageRankRepository pageRankRepository;


    @Autowired
    public void setPagesConnection (PagesConnectionRepository pagesConnection) {
        this.pagesConnection = pagesConnection;
    }
    @Autowired
    public void setPageRankRepository(PageRankRepository pageRankRepository) {
        this.pageRankRepository = pageRankRepository;
    }

    // This function is used to set pages rank in its table
    public void rankPages() {
        // Empty table before beginning
        pageRankRepository.deleteAll();
        List<PagesConnection> pageConnectionsArray = (List<PagesConnection>) pagesConnection.findAll();
        // Holding Page rank of each iteration
        HashMap<String,Float> pageRankHashTable = new HashMap<String,Float>();
        // Temp map for page_rank table
        HashMap<String,PageRank> tempHashMap = new HashMap<String,PageRank>();
        // Initialize pages in page_rank table
        for (PagesConnection p : pageConnectionsArray) {
            Optional<PageRank> pageRank1 = pageRankRepository.findById(p.getReferred());
            Optional<PageRank> pageRank2 = pageRankRepository.findById(p.getReferring());
            if (pageRank2.isPresent()) {
                pageRank2.get().setOut_links(pageRank2.get().getOut_links()+1);
                pageRankRepository.save(pageRank2.get());
                tempHashMap.put(pageRank2.get().getPage(),pageRank2.get());
            } else {
                PageRank pr = new PageRank(p.getReferring(),1,1);
                pageRankRepository.save(pr);
                tempHashMap.put(pr.getPage(),pr);
                pageRankHashTable.put(pr.getPage(),0f);
            }
            if (pageRank1.isPresent()) {
                continue;
            } else {
                PageRank pr = new PageRank(p.getReferred(),1,0);
                pageRankRepository.save(pr);
                tempHashMap.put(pr.getPage(),pr);
                pageRankHashTable.put(pr.getPage(),0f);
            }
        }

        List<PageRank> pageRankList = pageRankRepository.findAll();
        // This loop is used to iterate on page ranks and update them
        for (int i=0;i<pageRankIterations;i++) {
            pageRankHashTable.replaceAll((k,v) -> (1-d));
            for (PagesConnection p : pageConnectionsArray) {
                PageRank pr = tempHashMap.get(p.getReferring());
                pageRankHashTable.put(p.getReferred(),pageRankHashTable.get(p.getReferred())+d*(pr.getRank()/pr.getOut_links()));
            }

            // Updating temp hash map
            for (PageRank p : pageRankList) {
                p.setRank(pageRankHashTable.get(p.getPage()));
                tempHashMap.put(p.getPage(),p);
            }
        }

        // Updating database
        for (PageRank p : pageRankList) {
            p.setRank(tempHashMap.get(p.getPage()).getRank());
            pageRankRepository.save(p);
        }

    }
}
