package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class FinancialInstitutionUsersServiceImpl extends AbstractServiceImpl implements FinancialInstitutionUsersService {

    public FinancialInstitutionUsersServiceImpl() {
        super();
    }

    @Override
    public ResourceList<FinancialInstitutionUser> list() {
        return list(new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitutionUser> list(final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionUser.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, getFinancialInstitutionUsersRepo(null));
    }

    @Override
    public FinancialInstitutionUser find(final UUID financialInstitutionUserId)  throws ApiErrorsException {
        return getFinancialInstitutionUsersRepo(null).findOne(financialInstitutionUserId, new QuerySpec(FinancialInstitutionUser.class));
    }

    @Override
    public FinancialInstitutionUser create(final String login, final String password, final String lastName, final String firstName) {
        return create(login, password, lastName, firstName, null);
    }

    @Override
    public FinancialInstitutionUser create(final String login, final String password, final String lastName, final String firstName, final UUID idempotencyKey) {
        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setLogin(login);
        financialInstitutionUser.setPassword(password);
        financialInstitutionUser.setLastName(lastName);
        financialInstitutionUser.setFirstName(firstName);
        return getFinancialInstitutionUsersRepo(idempotencyKey).create(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser update(final FinancialInstitutionUser financialInstitutionUser) {
        return getFinancialInstitutionUsersRepo(null).save(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser update(final FinancialInstitutionUser financialInstitutionUser, final UUID idempotencyKey) {
        return getFinancialInstitutionUsersRepo(idempotencyKey).save(financialInstitutionUser);
    }

    @Override
    public void delete(final UUID financialInstitutionUserId) throws ApiErrorsException {
        getFinancialInstitutionUsersRepo(null).delete(financialInstitutionUserId);
    }

    protected ResourceRepositoryV2<FinancialInstitutionUser, UUID> getFinancialInstitutionUsersRepo(final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiIUrls().getSandbox().getFinancialInstitutionUsers()
                        .replace(FinancialInstitutionUser.RESOURCE_PATH, "")
                        .replace(FinancialInstitutionUser.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(FinancialInstitutionUser.class);
    }
}
