package com.OJ.OnlineJudge.Problems.Service;

import com.OJ.OnlineJudge.Problems.Entity.Problem;
import com.OJ.OnlineJudge.Problems.Repo.ProblemRepo;
import com.OJ.OnlineJudge.TestCases.Entity.Tests;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class ProblemService {

    @Autowired
    private ProblemRepo repo;

    public List<Problem> allProblems() {
        return repo.findAll();
    }

    public Problem problemById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void addproblem(Problem problem) {
        repo.save(problem);
    }

}
