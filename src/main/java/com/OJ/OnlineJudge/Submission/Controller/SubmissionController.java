package com.OJ.OnlineJudge.Submission.Controller;

import com.OJ.OnlineJudge.Submission.Entity.Submission;
import com.OJ.OnlineJudge.Submission.Model.SubmitRequest;
import com.OJ.OnlineJudge.Submission.Model.JudgeResponse;
import com.OJ.OnlineJudge.Submission.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/submit")
public class SubmissionController {

    @Autowired
    private SubmissionService service;

    @PostMapping
    public String submitCode(@RequestBody SubmitRequest submitRequest) throws IOException, InterruptedException {
        return service.SubmitCode(submitRequest);
    }

    @GetMapping("/{id}")
    public Submission getSubmissions(@PathVariable long id){
        return service.GetSubmission(id);
    }

    @GetMapping("/attempts/{id}")
    public List<Submission> getSubmissionsbyuser(@PathVariable long id){
        return service.getAllSubmissionsbyuser(id);
    }
}
