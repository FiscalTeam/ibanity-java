package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionUserDeleteQuery {
    private UUID financialInstitutionUserId;

    private UUID idempotencyKey;
}