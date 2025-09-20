package com.OJ.OnlineJudge.Problems.Repo;

import com.OJ.OnlineJudge.Problems.Entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepo extends JpaRepository<Problem,Long> {
}
