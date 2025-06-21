package com.example.bioweatherbackend.config.restclient;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RestClientLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RestClientLoggingInterceptor.class);


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logger.info("request method: {}, request URI: {}, request headers: {}, request body: {}",
                request.getMethod(), request.getURI(), request.getHeaders(), new String(body, StandardCharsets.UTF_8));

        ClientHttpResponse response = execution.execute(request, body);

        logger.info("response status code: {}, response headers: {}, response body: {}",
                response.getStatusCode(), response.getHeaders(), new String(StreamUtils.copyToByteArray(response.getBody()), StandardCharsets.UTF_8));

        return response;
    }
}