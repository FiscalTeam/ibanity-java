package com.ibanity.apis.client.utils;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.NonNull;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class URIHelper {

    public static URI buildUri(@NonNull String url, @NonNull IbanityPagingSpec pagingSpec) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            addIfNotNull(uriBuilder, "before", pagingSpec.getBefore());
            addIfNotNull(uriBuilder, "after", pagingSpec.getAfter());
            addIfNotNull(uriBuilder, "limit", pagingSpec.getLimit());
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL cannot be build", e);
        }
    }

    public static URI buildUri(@NonNull String url) {
        try {
            return new URIBuilder(url).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL cannot be build", e);
        }
    }

    private static void addIfNotNull(URIBuilder uriBuilder, String paramName, Object paramValue) {
        if (paramValue != null) {
            uriBuilder.addParameter(paramName, "" + paramValue.toString());
        }
    }
}
