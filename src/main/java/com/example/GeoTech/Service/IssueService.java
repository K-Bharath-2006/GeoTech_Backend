package com.example.GeoTech.Service;

import com.example.GeoTech.Model.Issue;
import com.example.GeoTech.repo.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public void createIssue(Issue issue, String userUid) {


        issue.setUserUid(userUid);
        issue.setStatus(Issue.Status.OPEN);
        issue.setVerified(false);

        issueRepository.save(issue);
    }
    public List<Issue> getIssuesByUser(String userUid) throws Exception {
        return issueRepository.findByUserUid(userUid);
    }
    public List<Issue> getAllIssues() throws Exception {
        return issueRepository.findAll();
    }


}
