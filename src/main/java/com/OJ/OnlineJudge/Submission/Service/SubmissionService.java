package com.OJ.OnlineJudge.Submission.Service;
import com.OJ.OnlineJudge.Problems.Entity.Problem;
import com.OJ.OnlineJudge.Problems.Service.ProblemService;
import com.OJ.OnlineJudge.Submission.Entity.Submission;
import com.OJ.OnlineJudge.Submission.Model.SubmitRequest;
import com.OJ.OnlineJudge.Submission.Model.JudgeResponse;
import com.OJ.OnlineJudge.Submission.Repo.SubmissionRepo;
import com.OJ.OnlineJudge.TestCases.Entity.Tests;
import com.OJ.OnlineJudge.User.Entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepo repo;
    @Autowired
    private JudgeService judgeService;
    private static ProblemService problemService;

    public String SubmitCode(SubmitRequest submitRequest) throws IOException, InterruptedException {
        Submission submission = new Submission();

        submission.setProblem_id(submitRequest.getProblem_id());
        submission.setSourceCode(submitRequest.getSourceCode());
        submission.setLanguage(Submission.Language.valueOf(submitRequest.getLanguage().toUpperCase()));
        submission.setStatus(Submission.Status.Pending);
        submission.setSubmitted_at(LocalDateTime.now());
        submission.setUserID(submitRequest.getUserID());
        Path filePath = judgeService.prepareWorkspace(submission);
        JudgeResponse CompilationResult = judgeService.compile(filePath,submission.getLanguage().toString());
        if (!CompilationResult.isSuccess()) {
            submission.setStatus(Submission.Status.Failed);
            repo.save(submission);
            return submission.getStatus().toString();
        }

        Problem problem = submission.getProblem_id();
        List<Tests> testCases = problem.getTest();
        log.info(problem.getTest());
        for (Tests testCase : testCases) {
            log.info(testCase.toString());
        JudgeResponse OJResult =  judgeService.execute(filePath,
                submission.getLanguage().toString(),
                testCase.getInput(),
                submission.getProblem_id().getTimeLimit(),
                submission.getProblem_id().getMemoryLimit());

            if (!OJResult.isSuccess()) {
                submission.setStatus(Submission.Status.Failed);
                repo.save(submission);
                return (submission.getStatus().toString() + "failed at :" + testCase.getId() +"testcase");
            }
            String expextedOutput = testCase.getExpected_output().trim().replaceAll("\\r\\n", "\n");
            String realOutput = OJResult.getOutput().trim().replaceAll("\\r\\n", "\n");
            log.info("realOutput :"+ realOutput);
            log.info("expextedOutput :"+ expextedOutput);
            if(!realOutput.equals(expextedOutput)){
                submission.setStatus(Submission.Status.Failed);
                repo.save(submission);
                return submission.getStatus().toString();
            }
         }
        submission.setStatus(Submission.Status.Success);
        repo.save(submission);

        judgeService.cleanupWorkspace(filePath);
        return submission.getStatus().toString();
    }

    public Submission GetSubmission(long id) {
       return repo.findById(id).orElse(null);
    }

    public List<Submission> getAllSubmissionsbyuser(long id){
        List<Submission> allsubmissions = repo.findAll().stream().filter(submission -> submission.getUserID().equals(id))
                .toList();;
        return allsubmissions;
    }
}
