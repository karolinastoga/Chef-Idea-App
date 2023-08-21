package com.programmazione_avanzata.final_project.chef_ideas_app.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeInputDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Error;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.programmazione_avanzata.final_project.chef_ideas_app.request.RequestInfo.*;

public class HttpRequest {

    public static HttpResponse initializeDatabase(String recipesNumber) {
        var httpclient = HttpClients.createDefault();
        var request = String.format(INITIALIZE_DATABASE, recipesNumber);
        var httpPost = new HttpPost(request);
        return getHttpResponse(httpclient, httpPost);
    }

    public static HttpResponse getAllRecipes() {
        var httpclient = HttpClients.createDefault();
        var httpGet = new HttpGet(GET_ALL_RECIPES);
        return getHttpResponse(httpclient, httpGet);
    }

    public static HttpResponse getRecipeDetailedInfo(Long recipeId) {
        var httpclient = HttpClients.createDefault();
        var httpAddress = String.format(GET_RECIPE_INFO, recipeId);
        var httpGet = new HttpGet(httpAddress);
        return getHttpResponse(httpclient, httpGet);
    }

    public static HttpResponse addNewRecipe(RecipeInputDto newRecipe) {
        var httpClient = HttpClientBuilder.create().build();
        var httpPost = new HttpPost(ADD_NEW_RECIPE);
        var entity = getStringEntity(newRecipe);
        httpPost.setEntity(entity);
        return getHttpResponse(httpClient, httpPost);
    }

    public static HttpResponse deleteRecipe(Long recipeId) {
        var httpclient = HttpClients.createDefault();
        var request = String.format(DELETE_RECIPE, recipeId);
        var httpDelete = new HttpDelete(request);
        return getHttpResponse(httpclient, httpDelete);
    }

    public static HttpResponse editRecipe(Long recipeId, RecipeInputDto updatedRecipe) {
        var httpclient = HttpClients.createDefault();
        var httpPut = new HttpPut(EDIT_RECIPE);
        var entity = getStringEntity(updatedRecipe);
        var uri = getUri(httpPut, RECIPE_ID, String.valueOf(recipeId));
        httpPut.setEntity(entity);
        httpPut.setURI(uri);
        return getHttpResponse(httpclient, httpPut);
    }

    public static HttpResponse findRecipesByName(String searchInput) {
        var httpClient = HttpClientBuilder.create().build();
        var httpGet = new HttpGet(FIND_RECIPES_BY_NAME);
        var uri = getUri(httpGet, RECIPE_NAME, searchInput);
        httpGet.setURI(uri);
        return getHttpResponse(httpClient, httpGet);
    }

    public static HttpResponse findRecipesByProducts(String[] foodEntitiesList) {
        var httpClient = HttpClientBuilder.create().build();
        var entity = getStringEntity(foodEntitiesList);
        var httpPost = new HttpPost(FIND_RECIPES_BY_FOOD_ENTITY);
        httpPost.setEntity(entity);
        return getHttpResponse(httpClient, httpPost);
    }

    private static HttpResponse getHttpResponse(CloseableHttpClient httpclient, HttpRequestBase httpRequestBase) {
        try {
            return httpclient.execute(httpRequestBase);
        } catch (IOException ioe) {
            Error.displayErrorPopup(ioe);
        }
        return null;
    }

    private static StringEntity getStringEntity(RecipeInputDto newRecipe) {
        var mapper = new ObjectMapper();
        try {
            var newRecipeJson = mapper.writeValueAsString(newRecipe);
            return new StringEntity(newRecipeJson, ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            var title = "";
            Error.displayErrorPopup(title, e);
        }
        return null;
    }


    private static StringEntity getStringEntity(String[] foodEntitiesList) {
        var mapper = new ObjectMapper();
        var foodEntities = Arrays.stream(foodEntitiesList).collect(Collectors.toList());
        try {
            var foodEntitiesJson = mapper.writeValueAsString(foodEntities);
            return new StringEntity(foodEntitiesJson, ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            var title = "";
            Error.displayErrorPopup(title, e);
        }
        return null;
    }

    private static URI getUri(HttpRequestBase httpRequest, String parameter, String value) {
        try {
            return new URIBuilder(httpRequest.getURI())
                    .addParameter(parameter, value)
                    .build();
        } catch (URISyntaxException e) {
            var title = "";
            Error.displayErrorPopup(title, e);
        }
        return null;
    }

    public static boolean isResponseSuccess(HttpResponse response) {
        var code = response.getStatusLine().getStatusCode();
        return code >= 200 && code < 300;
    }
}
