package com.ecommerce.catalog.configurations;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;


//@Profile( ELASTICSEARCH )
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ecommerce.catalog.nosql.repository")
public class ElasticsearchConfiguration {


    @Value("${elasticsearch.cluster-nodes}")
    private List<String> hosts;

    @Bean
    public RestHighLevelClient client() {
        HttpHost[] httpHosts = new HttpHost[hosts.size()];

        for (int i = 0; i < hosts.size(); ++i) {
            String[] parts = hosts.get(i).split(":");
            if (parts.length == 1) {
                httpHosts[i] = new HttpHost(parts[0]);
            } else if (parts.length == 2) {
                httpHosts[i] = new HttpHost(parts[0], Integer.parseInt(parts[1]));
            } else {
                throw new IllegalStateException();
            }
        }
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

}
