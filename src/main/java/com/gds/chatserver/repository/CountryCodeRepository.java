package com.gds.chatserver.repository;

import com.gds.chatserver.model.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCode,Long> {
    public List<CountryCode> findAllByOrderByTitleAsc();
}
