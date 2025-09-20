package com.OJ.OnlineJudge.Problems.Entity;

import com.OJ.OnlineJudge.TestCases.Entity.Tests;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OJProblems")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "Title")
    private String title;
    @Column(columnDefinition = "TEXT",name = "Description")
    private String description;
    @Column(name = "Time_Limit")
    private Long timeLimit;
    @Column(name = "Memory_Limit")
    private Long memoryLimit;

    @OneToMany(mappedBy = "problem_id", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<Tests> test = new ArrayList<>();
}
