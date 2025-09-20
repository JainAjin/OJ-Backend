package com.OJ.OnlineJudge.TestCases.Controller;

import com.OJ.OnlineJudge.TestCases.Entity.Tests;
import com.OJ.OnlineJudge.TestCases.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/addtest")
    public void addtest(@RequestBody Tests testCase){
        testService.addTest(testCase);
    }

}
