package com.refundplatform.policy.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Component
public class PolicyIngestionClient {

	private final RestTemplate restTemplate;
	private final String ingestionBaseUrl;
	private final String internalApiKey;

	public PolicyIngestionClient(RestTemplate restTemplate,

			@Value("${policy.ingestion.base-url}") String ingestionBaseUrl,

			@Value("${policy.ingestion.internal-api-key}") String internalApiKey) {

		this.restTemplate = restTemplate;

		this.ingestionBaseUrl = removeTrailingSlash(ingestionBaseUrl);

		this.internalApiKey = internalApiKey;
	}

	public PolicyIngestionResult ingest(UUID policyDocumentId, Path policyPath) {

		HttpHeaders requestHeaders = new HttpHeaders();

		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

		// ---------- policyDocumentId multipart field ----------

		HttpHeaders textPartHeaders = new HttpHeaders();

		textPartHeaders.setContentType(MediaType.TEXT_PLAIN);

		HttpEntity<String> policyDocumentIdPart = new HttpEntity<>(policyDocumentId.toString(), textPartHeaders);

		// ---------- file multipart field ----------

		HttpHeaders filePartHeaders = new HttpHeaders();

		filePartHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		HttpEntity<FileSystemResource> filePart = new HttpEntity<>(new FileSystemResource(policyPath.toFile()),
				filePartHeaders);

		// ---------- multipart request ----------

		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

		requestBody.add("policyDocumentId", policyDocumentIdPart);

		requestBody.add("file", filePart);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

		ResponseEntity<Map> responseEntity = restTemplate.exchange(ingestionBaseUrl + "/api/v1/admin/policies/ingest",
				HttpMethod.POST, requestEntity, Map.class);

		Map<?, ?> responseBody = responseEntity.getBody();

		return new PolicyIngestionResult(number(responseBody, "chunkCount"), number(responseBody, "embeddingCount"));
	}

	private int number(Map<?, ?> responseBody, String fieldName) {

		if (responseBody == null) {
			return 0;
		}

		Object fieldValue = responseBody.get(fieldName);

		return fieldValue instanceof Number number ? number.intValue() : 0;
	}

	private String removeTrailingSlash(String value) {

		if (value != null && value.endsWith("/")) {

			return value.substring(0, value.length() - 1);
		}

		return value;
	}

	public record PolicyIngestionResult(int chunkCount, int embeddingCount) {
	}
}
