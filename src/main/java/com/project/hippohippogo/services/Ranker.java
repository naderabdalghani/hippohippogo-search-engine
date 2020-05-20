package com.project.hippohippogo.services;

import com.project.hippohippogo.entities.PageRank;
import com.project.hippohippogo.entities.PagesConnection;
import com.project.hippohippogo.repositories.PageRankRepository;
import com.project.hippohippogo.repositories.PagesConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ranker {
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
    public void setPageRankTable() {
        // Empty table before beginning
        pageRankRepository.deleteAll();
        List<PagesConnection> pageConnectionsArray = (List<PagesConnection>) pagesConnection.findAll();
        // Storing pages in page_rank table
        for (PagesConnection p : pageConnectionsArray) {
            PageRank pageRank1 = new PageRank(p.getReferred(), 1);
            PageRank pageRank2 = new PageRank(p.getReferring(), 1);
            pageRankRepository.save(pageRank1);
            pageRankRepository.save(pageRank2);
        }
        
    }
}
