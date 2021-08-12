package com.jeeneee.realworld.article.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySlug_Value(String slug);

    boolean existsBySlug(Slug slug);
}
