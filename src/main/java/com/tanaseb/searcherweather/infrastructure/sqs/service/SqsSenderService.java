package com.tanaseb.searcherweather.infrastructure.sqs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SqsSenderService {

	@Value("${aws.queueEndpoint}")
	private String endpoint;

	private final AmazonSQS amazonSqs;

	public SqsSenderService(AmazonSQS amazonSqs) {
		this.amazonSqs = amazonSqs;
	}

	public void send(String command) {
		SendMessageRequest request = new SendMessageRequest()
				.withQueueUrl(endpoint)
				.withMessageBody(command);

		log.info("Sending to SQS command: {}", command);
		amazonSqs.sendMessage(request);
	}
}
