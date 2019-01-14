package com.elasticSearch.esjhrc.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
	
	@Value("${elasticsearch.host}")
	private String elasticSearchHost;
	
	@Value("${elasticsearch.port}")
    private int elasticsearchPort;

    @Value("${elasticsearch.username}")
    private String userName;

    @Value("${elasticsearch.password}")
    private String password;
    

	
	@Bean()
	public RestHighLevelClient restClient(){
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
		
		RestClientBuilder builder = RestClient.builder(new HttpHost(elasticSearchHost, elasticsearchPort, "https"));
				//.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		
		//RestClientBuilder builder = RestClient.builder(new HttpHost(elasticSearchHost, elasticsearchPort, "https"));
		
		
		return new RestHighLevelClient(builder);
	}
	

}
