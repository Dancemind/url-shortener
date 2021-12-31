package com.dancemind.urlshortener.repository;

import com.dancemind.urlshortener.entity.UrlData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlDataRepository extends JpaRepository<UrlData, Long> {

    List<UrlData> findAllByDeletedFalseOrderByIdDesc();

    UrlData findByShortUrlAndDeletedFalse(String shortUrl);

    UrlData findFirstByDeletedFalseAndIsCustomFalseOrderByIdDesc();
}