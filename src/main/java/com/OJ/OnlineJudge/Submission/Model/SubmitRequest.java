package com.OJ.OnlineJudge.Submission.Model;

import com.OJ.OnlineJudge.Problems.Entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitRequest {

    private Problem problem_id;
    private String sourceCode;
    private String language;
    private Long UserID;

}
