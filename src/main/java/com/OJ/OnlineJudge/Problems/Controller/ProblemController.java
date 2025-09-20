package com.OJ.OnlineJudge.Problems.Controller;

import com.OJ.OnlineJudge.Problems.Entity.Problem;
import com.OJ.OnlineJudge.Problems.Service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService service;

    @GetMapping
    public List<Problem> getAllProblems(){
        return service.allProblems();
    }

    @GetMapping("/{id}")
    public Problem getProblemById(@PathVariable Long id){
        return service.problemById(id);
    }

    @PostMapping("/addProblem")
    public void addProblem(@RequestBody Problem problem){
        service.addproblem(problem);
    }

}
