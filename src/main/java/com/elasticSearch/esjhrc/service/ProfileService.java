package com.elasticSearch.esjhrc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elasticSearch.esjhrc.entity.ProfileDocument;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfileService {
	
	private RestHighLevelClient restClient;
	
	private ObjectMapper objectMapper;
	
	private static String INDEX = "lead";
	private static String TYPE = "lead";
	
	@Autowired
	public ProfileService(RestHighLevelClient restClient, ObjectMapper objectMapper){
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	
	private Map<String, Object> convertProfileDocumentToMap(ProfileDocument profileDocument) {
        return objectMapper.convertValue(profileDocument, Map.class);
    }

    private ProfileDocument convertMapToProfileDocument(Map<String, Object> map){
        return objectMapper.convertValue(map,ProfileDocument.class);
    }

	
	public String cerateProfileDocument(ProfileDocument document) throws Exception{
		UUID uuid = UUID.randomUUID();
		document.setId(uuid.toString());
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, document.getId())
	                .source(convertProfileDocumentToMap(document));
		IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);
		
		return indexResponse.getResult().name();
	}
	
	public ProfileDocument findById(String id) throws Exception{
		GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
		GetResponse getResponse = restClient.get(getRequest, RequestOptions.DEFAULT);
		Map<String, Object> resultMap = getResponse.getSource();
		return convertMapToProfileDocument(resultMap);
	}
	
	public List<ProfileDocument> findAll() throws Exception{
		SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        searchRequest.types(TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse =
        		restClient.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(searchResponse);
	}
	
	private List<ProfileDocument> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<ProfileDocument> profileDocuments = new ArrayList<>();
        Arrays.stream(searchHit).forEach(
        		hit -> profileDocuments.add(objectMapper.convertValue(hit.getSourceAsMap(), ProfileDocument.class))
        		);        return profileDocuments;
    }

}
