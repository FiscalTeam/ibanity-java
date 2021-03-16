package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.AccountReport;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReportsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigInteger;
import java.net.URI;
import java.time.Instant;
import java.util.Arrays;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountReportServiceImplTest {
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String ACCOUNT_REPORT_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/account-reports/{accountReportId}";
    private static final String LIST_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/account-reports?size=10";

    @InjectMocks
    private AccountReportServiceImpl accountReportService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setup() {
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "account-reports"))
                .thenReturn(ACCOUNT_REPORT_ENDPOINT);
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_ACCOUNT_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/account_reports.json"));

        IsabelCollection<AccountReport> actual = accountReportService.list(AccountReportsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .build());

        Assertions.assertThat(actual.getItems()).containsExactly(createExpected());
        Assertions.assertThat(actual.getPagingOffset()).isEqualTo(0);
        Assertions.assertThat(actual.getPagingTotal()).isEqualTo(2);
    }

    private AccountReport createExpected() {
        String refs[] = {"BE96153112434405"};

        AccountReport report = AccountReport.builder()
                .accountReferences(Arrays.asList(refs))
                .fileFormat("CODA")
                .fileName("CODA_20181009_BE96153112434405")
                .fileSize(BigInteger.valueOf(29680L))
                .financialInstitutionName("GringotBank")
                .receivedAt(Instant.parse("2018-10-09T03:55:00.710Z"))
                .build();

        return report;
    }
}
