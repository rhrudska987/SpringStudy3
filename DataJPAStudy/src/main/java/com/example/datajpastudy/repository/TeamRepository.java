package com.example.datajpastudy.repository;

import com.example.datajpastudy.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
