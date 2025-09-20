package com.OJ.OnlineJudge.TestCases.Entity;

import com.OJ.OnlineJudge.Problems.Entity.Problem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TestCases")
public class Tests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Input",nullable = false)
    private String input;
    @Column(name = "Output",nullable = false)
    private String expected_output;
    @Column(name = "IsSample",nullable = false)
    private Boolean isSample;
    @ManyToOne
    @JoinColumn(name = "test") // Foreign key
    @JsonBackReference
    @ToString.Exclude
    private Problem problem_id;

}

