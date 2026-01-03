package com.example.GeoTech.repo;

import com.example.GeoTech.Model.Issue;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IssueRepository {

    private static final String COLLECTION = "Issues";

    public void save(Issue issue) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).add(issue);
    }

    public List<Issue> findByUserUid(String userUid) throws Exception {

        QuerySnapshot snapshot = FirestoreClient
                .getFirestore()
                .collection(COLLECTION)
                .whereEqualTo("userUid", userUid)
                .get()
                .get();

        return mapDocuments(snapshot);
    }

    public List<Issue> findAll() throws Exception {

        QuerySnapshot snapshot = FirestoreClient
                .getFirestore()
                .collection(COLLECTION)
                .get()
                .get();

        return mapDocuments(snapshot);
    }

    private List<Issue> mapDocuments(QuerySnapshot snapshot) {
        List<Issue> issues = new ArrayList<>();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Issue issue = doc.toObject(Issue.class);
            issue.setIssueId(doc.getId());
            issues.add(issue);
        }
        return issues;
    }
}
