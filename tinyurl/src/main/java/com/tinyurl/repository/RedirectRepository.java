package com.tinyurl.repository;

import com.tinyurl.entity.Redirect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedirectRepository extends JpaRepository<Redirect, Long> {

    @Query(value = "SELECT url FROM redirect WHERE alias = ?1", nativeQuery = true)
    Optional<String> findUrlByAlias(String alias);
}
