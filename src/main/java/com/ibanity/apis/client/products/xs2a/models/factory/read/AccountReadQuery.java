package com.ibanity.apis.client.products.xs2a.models.factory.read;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
public final class AccountReadQuery {

    private String customerAccessToken;
    private UUID financialInstitutionId;
    private UUID accountId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
