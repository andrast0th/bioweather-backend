package com.example.bioweatherbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MeteoNewsDataService {

    private final RestTemplate restTemplate = new RestTemplate();

//    public Scales getScales(String url) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(List.of(MediaType.APPLICATION_XML));
//        HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<ExampleResponse> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                entity,
//                ExampleResponse.class
//        );
//        return response.getBody();
//    }
}
