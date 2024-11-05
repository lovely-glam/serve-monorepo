package com.lovelyglam.nailserver.model;

import com.lovelyglam.database.model.constant.SubscriptionRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPayload {
    private String action; // Create, Change, Extend
    private SubscriptionRole newSubscriptionRole;
    private SubscriptionRole currentSubscriptionRole;
}
