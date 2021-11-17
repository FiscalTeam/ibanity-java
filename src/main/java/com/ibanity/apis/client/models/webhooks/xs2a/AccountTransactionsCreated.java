package com.ibanity.apis.client.models.webhooks.xs2a;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.models.webhooks.IbanityWebhooks;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityWebhooks;
import static java.util.UUID.fromString;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountTransactionsCreated implements IbanityWebhooks {

    private UUID id;
    private UUID accountId;
    private int count;
    private UUID synchronizationId;
    private Instant createdAt;

    public static Function<DataApiModel, AccountTransactionsCreated> mappingFunction() {
        return dataApiModel -> {
            AccountTransactionsCreated accountTransactionsCreated = toIbanityWebhooks(dataApiModel, AccountTransactionsCreated.class);

            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                accountTransactionsCreated.setAccountId(fromString(accountRelationship.getData().getId()));
            }

            RelationshipsApiModel synchronizationRelationship = dataApiModel.getRelationships().get("synchronization");
            if (synchronizationRelationship != null) {
                accountTransactionsCreated.setSynchronizationId(fromString(synchronizationRelationship.getData().getId()));
            }

            return accountTransactionsCreated;
        };
    }
}