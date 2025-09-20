package com.OJ.OnlineJudge.Submission.Entity;

import com.OJ.OnlineJudge.Problems.Entity.Problem;
import com.OJ.OnlineJudge.User.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT",name = "Code")
    private String sourceCode;
    @Enumerated(EnumType.STRING)
    @Column(name = "Language")
    private Language language;
    @Column(name = "Status")
    private Status status= Status.Pending;
    @Column(name = "Submitted_At")
    private LocalDateTime submitted_at=LocalDateTime.now();
    @Column(name = "UserID")
    private Long UserID;

    @ManyToOne
    @JoinColumn(name = "problem_Id") // Foreign key
    private Problem problem_id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Foreign key
    private User user_id;


    public enum Language {
        JAVA,
        CPP
    }
    public enum Status {
        Pending,
        Processing,
        Success,
        Failed
    }
}
