package com.OJ.OnlineJudge.TestCases.Repo;

import com.OJ.OnlineJudge.TestCases.Entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends JpaRepository<Tests, Long> {
}
