package com.example.library.service.impl;

import com.example.library.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionDto> findByUserFullName(String userFullName);
    SubscriptionDto findByUsername(String username);
    void deleteByUsername(String username);
}
