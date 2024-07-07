/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa.specifications;

import com.borak.cwb.backend.domain.jpa.MovieJPA;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author User
 */
public interface MovieJPASpecs {

    static Specification<MovieJPA> byTitleLike(String titlePattern) {
        return (root, query, builder)
                -> builder.like(root.get("title"), titlePattern);
    }

    static Specification<MovieJPA> byReleaseDateYear(Integer year) {
        return (Root<MovieJPA> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            LocalDate startDate = LocalDate.of(year, 1, 1);
            LocalDate endDate = LocalDate.of(year, 12, 31);
            return cb.between(root.get("releaseDate"), startDate, endDate);
        };
    }

    /**
     * Constructs a MovieJPA Specification that checks whether MovieJPA entity
     * contains at least all of the provided genres
     *
     * @param genreIds id of associated genres
     * @return MovieJPA Specification
     */
    static Specification<MovieJPA> hasGenres(List<Long> genreIds) {
        return (Root<MovieJPA> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Create a subquery for the MovieJPA entity
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<MovieJPA> subqueryRoot = subquery.from(MovieJPA.class);
            // Join the genres collection in the subquery
            Join<Object, Object> genresJoin = subqueryRoot.join("genres", JoinType.INNER);

            // Select the ID of the MovieJPA entities that have the specified genre IDs
            subquery.select(subqueryRoot.get("id"))
                    .where(genresJoin.get("id").in(genreIds))// Filter by genre IDs
                    .groupBy(subqueryRoot.get("id"))// Group by MovieJPA ID
                    .having(criteriaBuilder.equal(criteriaBuilder.count(genresJoin.get("id")), genreIds.size()));

            // The main query will filter MovieJPA entities by the IDs from the subquery
            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }

    /**
     * Constructs a MovieJPA Specification that checks whether MovieJPA entity
     * contains only the provided genres
     *
     * @param genreIds id of associated genres
     * @return MovieJPA Specification
     */
    static Specification<MovieJPA> isOfGenres(List<Long> genreIds) {
        return (Root<MovieJPA> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Create a subquery for the MovieJPA entity
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<MovieJPA> subqueryRoot = subquery.from(MovieJPA.class);
            // Join the genres collection in the subquery
            Join<Object, Object> genresJoin = subqueryRoot.join("genres", JoinType.INNER);

            // Select the ID of the MovieJPA entities that have the specified genre IDs
            subquery.select(subqueryRoot.get("id"))
                    .where(genresJoin.get("id").in(genreIds)) // Filter by genre IDs
                    .groupBy(subqueryRoot.get("id")) // Group by MovieJPA ID
                    .having(criteriaBuilder.equal(criteriaBuilder.count(genresJoin.get("id")), genreIds.size()));

            // The main query will filter MovieJPA entities by the IDs from the subquery
            return criteriaBuilder.and(
                    criteriaBuilder.in(root.get("id")).value(subquery),
                    criteriaBuilder.equal(criteriaBuilder.size(root.get("genres")), genreIds.size())
            );
        };
    }

}
