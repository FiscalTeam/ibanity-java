package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.jsonapi.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.ResourceApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.network.http.client.IbanityHttpUtils;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class IbanityModelMapper {

    public static <T extends IbanityModel> T mapResource(String jsonPayload, Class<T> classType) {
        return mapResource(jsonPayload, dataApiModel -> toIbanityModel(dataApiModel, classType));
    }

    public static <T extends IbanityModel> IbanityCollection<T> mapCollection(String jsonPayload, Class<T> classType) {
        return mapCollection(jsonPayload, dataApiModel -> toIbanityModel(dataApiModel, classType));
    }

    public static <T extends IbanityModel> T mapResource(String jsonPayload, Function<DataApiModel, T> customMapping) {
        try {
            DataApiModel dataApiModel = IbanityHttpUtils.objectMapper().readValue(jsonPayload, ResourceApiModel.class).getData();
            return customMapping.apply(dataApiModel);
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }

    public static <T extends IbanityModel> IbanityCollection<T> mapCollection(String jsonPayload, Function<DataApiModel, T> customMapping) {
        try {
            CollectionApiModel collectionApiModel = IbanityHttpUtils.objectMapper().readValue(jsonPayload, CollectionApiModel.class);
            return IbanityCollection.<T>builder()
                    .pageLimit(collectionApiModel.getMeta().getPaging().getLimit())
                    .items(
                            collectionApiModel.getData().stream()
                                    .map(customMapping)
                                    .collect(Collectors.toList())
                    )
                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }

    public static <T extends IbanityModel> T toIbanityModel(DataApiModel data, Class<T> classType) {
        try {
            T clientObject = IbanityHttpUtils.objectMapper().convertValue(data.getAttributes(), classType);
            if (clientObject == null) {
                clientObject = classType.newInstance();
            }
            clientObject.setId(data.getId());
            if (data.getLinks() != null) {
                clientObject.setSelfLink(data.getLinks().getSelf());
            }

            return clientObject;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(format("Instantiation of class %s is impossible for default constructor", classType), e);
        }
    }
}
