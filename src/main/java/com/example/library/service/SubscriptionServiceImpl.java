package com.example.library.service;

import com.example.library.dto.SubscriptionDto;
import com.example.library.exception.NotFoundException;
import com.example.library.mapper.LibraryMapper;
import com.example.library.repository.SubscriptionRepository;
import com.example.library.service.impl.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final LibraryMapper mapper;

    @Cacheable(value = "subscriptions", key = "#userFullName")
    @Transactional(readOnly = true)
    public List<SubscriptionDto> findByUserFullName(String userFullName) {
        return subscriptionRepository.findByUserFullNameContainingIgnoreCase(userFullName)
                .stream()
                .map(mapper::toSubscriptionDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "subscriptions", key = "#username")
    @Transactional(readOnly = true)
    public SubscriptionDto findByUsername(String username) {
        return subscriptionRepository.findByUsername(username)
                .map(mapper::toSubscriptionDto)
                .orElseThrow(() -> new NotFoundException("Subscription not found"));
    }

    @CacheEvict(value = "subscriptions", key = "#username")
    @Transactional
    public void deleteByUsername(String username) {
        subscriptionRepository.deleteByUsername(username);
    }
}
