package com.example.library.repository;

import com.example.library.entity.Subscription;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @EntityGraph(attributePaths = {"loans.book"})
    Optional<Subscription> findByUsername(String username);

    @EntityGraph(attributePaths = {"loans.book"})
    List<Subscription> findByUserFullNameContainingIgnoreCase(String userFullName);

    @EntityGraph(attributePaths = {"loans.book"})
    void deleteByUsername(String username);
}
