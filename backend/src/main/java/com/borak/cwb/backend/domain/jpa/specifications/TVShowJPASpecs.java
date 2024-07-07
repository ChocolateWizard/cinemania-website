/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa.specifications;

import com.borak.cwb.backend.domain.jpa.TVShowJPA;
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
public interface TVShowJPASpecs {

    static Specification<TVShowJPA> byTitleLike(String titlePattern) {
        return (root, query, builder)
                -> builder.like(root.get("title"), titlePattern);
    }

    static Specification<TVShowJPA> byReleaseDateYear(Integer year) {
        return (Root<TVShowJPA> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            LocalDate startDate = LocalDate.of(year, 1, 1);
            LocalDate endDate = LocalDate.of(year, 12, 31);
            return cb.between(root.get("releaseDate"), startDate, endDate);
        };
    }

    /**
     * Constructs a TVShowJPA Specification that checks whether TVShowJPA entity
     * contains at least all of the provided genres
     *
     * @param genreIds id of associated genres
     * @return TVShowJPA Specification
     */
    static Specification<TVShowJPA> hasGenres(List<Long> genreIds) {
        return (Root<TVShowJPA> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Create a subquery for the TVShowJPA entity
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<TVShowJPA> subqueryRoot = subquery.from(TVShowJPA.class);
            // Join the genres collection in the subquery
            Join<Object, Object> genresJoin = subqueryRoot.join("genres", JoinType.INNER);

            // Select the ID of the TVShowJPA entities that have the specified genre IDs
            subquery.select(subqueryRoot.get("id"))
                    .where(genresJoin.get("id").in(genreIds))// Filter by genre IDs
                    .groupBy(subqueryRoot.get("id"))// Group by TVShowJPA ID
                    .having(criteriaBuilder.equal(criteriaBuilder.count(genresJoin.get("id")), genreIds.size()));

            // The main query will filter TVShowJPA entities by the IDs from the subquery
            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }

    /**
     * Constructs a TVShowJPA Specification that checks whether TVShowJPA entity
     * contains only the provided genres
     *
     * @param genreIds id of associated genres
     * @return TVShowJPA Specification
     */
    static Specification<TVShowJPA> isOfGenres(List<Long> genreIds) {
        return (Root<TVShowJPA> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Create a subquery for the TVShowJPA entity
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<TVShowJPA> subqueryRoot = subquery.from(TVShowJPA.class);
            // Join the genres collection in the subquery
            Join<Object, Object> genresJoin = subqueryRoot.join("genres", JoinType.INNER);

            // Select the ID of the TVShowJPA entities that have the specified genre IDs
            subquery.select(subqueryRoot.get("id"))
                    .where(genresJoin.get("id").in(genreIds)) // Filter by genre IDs
                    .groupBy(subqueryRoot.get("id")) // Group by TVShowJPA ID
                    .having(criteriaBuilder.equal(criteriaBuilder.count(genresJoin.get("id")), genreIds.size()));

            // The main query will filter TVShowJPA entities by the IDs from the subquery
            return criteriaBuilder.and(
                    criteriaBuilder.in(root.get("id")).value(subquery),
                    criteriaBuilder.equal(criteriaBuilder.size(root.get("genres")), genreIds.size())
            );
        };
    }

}
