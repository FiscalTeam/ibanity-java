package com.ibanity.apis.client.products.ponto_connect.models.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PendingTransactionsReadQuery {

    private String accessToken;
    private UUID accountId;
    private UUID synchronizationId;
    private IbanityPagingSpec pagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
