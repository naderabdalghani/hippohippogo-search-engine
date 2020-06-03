package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.Query;
import com.project.hippohippogo.entities.UserFrequentDomain;
import com.project.hippohippogo.ids.QueryId;
import com.project.hippohippogo.ids.UserFrequentDomainId;
import com.project.hippohippogo.repositories.UsersFrequentDomainsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.project.hippohippogo.services.RankerService.getString;

@RestController
public class HistoryController {

    private UsersFrequentDomainsRepository usersFrequentDomainsRepository;

    @Autowired
    public void setQueriesRepository(UsersFrequentDomainsRepository usersFrequentDomainsRepository) {
        this.usersFrequentDomainsRepository = usersFrequentDomainsRepository;
    }

    private String getDomain(String link) {
        return getString(link);
    }

    @PostMapping("/history")
    public void registerClick(@RequestParam("link") String link, HttpServletRequest request) throws JSONException {
        String userIp = request.getRemoteAddr();
        String domain = getDomain(link);
        UserFrequentDomainId userFrequentDomainId = new UserFrequentDomainId(userIp, domain);
        Optional<UserFrequentDomain> userFrequentDomain = usersFrequentDomainsRepository.findById(userFrequentDomainId);
        if (userFrequentDomain.isPresent()) {
            userFrequentDomain.get().incrementHits();
            usersFrequentDomainsRepository.save(userFrequentDomain.get());
        } else {
            UserFrequentDomain newUserFrequentDomain = new UserFrequentDomain(userIp, domain);
            usersFrequentDomainsRepository.save(newUserFrequentDomain);
        }
    }
}