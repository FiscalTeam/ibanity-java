package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.isabel_connect.models.create.BulkPaymentInitiationRequestCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.BulkPaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.products.isabel_connect.services.BulkPaymentInitiationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class BulkPaymentInitiationRequestServiceImpl implements BulkPaymentInitiationRequestService {
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityResponseHandler ibanityResponseHandler;
    private final IbanityHttpClient ibanityHttpClient;
    private final HttpClient httpClient;

    public BulkPaymentInitiationRequestServiceImpl(
            ApiUrlProvider apiUrlProvider,
            IbanityResponseHandler ibanityResponseHandler,
            IbanityHttpClient ibanityHttpClient,
            HttpClient httpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityResponseHandler = ibanityResponseHandler;
        this.ibanityHttpClient = ibanityHttpClient;
        this.httpClient = httpClient;
    }

    @Override
    public BulkPaymentInitiationRequest create(BulkPaymentInitiationRequestCreateQuery query) {
        URI url = buildUri(getUrl());
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/vnd.api+json");
        httpPost.setHeader("Content-Type", "application/xml");
        httpPost.setHeader("Content-Disposition", "inline; filename=" + query.getFilename());
        InputStreamEntity entity = new InputStreamEntity(query.getStream());
        entity.setChunked(true);
        httpPost.setEntity(entity);

        HttpResponse res = execute(query.getAdditionalHeaders(), query.getAccessToken(), httpPost);

        return IsabelModelMapper.mapResource(res, BulkPaymentInitiationRequest.class);
    }

    @Override
    public BulkPaymentInitiationRequest find(BulkPaymentInitiationRequestReadQuery query) {
        URI uri = buildUri(getUrl(query.getBulkPaymentInitiationRequestId()));
        HttpResponse response = ibanityHttpClient.get(uri, query.getAdditionalHeaders(), query.getAccessToken());

        return IsabelModelMapper.mapResource(response, BulkPaymentInitiationRequest.class);
    }

    private String getUrl() {
        return getUrl("");
    }

    private String getUrl(String bulkPaymentInitiationRequestId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "bulk-payment-initiation-requests")
                .replace("{bulkPaymentInitiationRequestId}", bulkPaymentInitiationRequestId);

        return StringUtils.removeEnd(url, "/");
    }

    private HttpResponse execute(@NonNull Map<String, String> additionalHeaders,
                                 String customerAccessToken,
                                 HttpRequestBase httpRequestBase) {
        try {
            addHeaders(httpRequestBase, customerAccessToken, additionalHeaders);
            return ibanityResponseHandler.handleResponse(this.httpClient.execute(httpRequestBase));
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", exception);
        }
    }

    private void addHeaders(HttpRequestBase httpRequestBase,
                            String customerAccessToken,
                            Map<String, String> additionalHeaders) {
        addAuthorizationHeader(httpRequestBase, customerAccessToken);
        additionalHeaders.forEach(httpRequestBase::addHeader);
    }

    private void addAuthorizationHeader(HttpRequestBase requestBase, String customerAccessToken) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(customerAccessToken)) {
            requestBase.addHeader(new BasicHeader(AUTHORIZATION, "Bearer " + customerAccessToken));
        }
    }
}