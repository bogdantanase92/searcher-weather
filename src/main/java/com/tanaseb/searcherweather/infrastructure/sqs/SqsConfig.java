package com.tanaseb.searcherweather.infrastructure.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class SqsConfig {

	@Value("${aws.accessKey}")
	private String accessKey;
	@Value("${aws.secretKey}")
	private String secretKey;
	@Value("${aws.queueEndpoint}")
	private String queueEndpoint;

	@Bean
	public AmazonSQS createSQSClient() {
		return AmazonSQSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(queueEndpoint, Regions.US_EAST_2.getName()))
				.build();
	}
}
