package com.OJ.OnlineJudge.Submission.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeResponse {

    private String status;
    private String Output;
    private String message;

    public boolean isSuccess() {
        return "Success".equals(status);
    }

}
