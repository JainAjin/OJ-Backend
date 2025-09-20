package com.OJ.OnlineJudge.TestCases.Service;

import com.OJ.OnlineJudge.TestCases.Entity.Tests;
import com.OJ.OnlineJudge.TestCases.Repo.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestRepo repo;
    
    public void addTest(Tests testCase) {
        repo.save(testCase);
    }
}
