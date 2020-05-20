package com.project.hippohippogo.services;

import com.project.hippohippogo.repositories.PageRankRepository;
import com.project.hippohippogo.repositories.PagesConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Ranker {
    private PagesConnectionRepository pagesConnection;
    private PageRankRepository pageRank;

    @Autowired
    public void setPagesConnection (PagesConnectionRepository pagesConnection) {
        this.pagesConnection = pagesConnection;
    }
    @Autowired
    public void setPageRank (PageRankRepository pageRank) {
        this.pageRank = pageRank;
    }
}
