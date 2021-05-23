package sk.hfa.projects.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.hfa.projects.web.domain.responsebodies.InstagramBodyResponse;
import sk.hfa.projects.web.domain.responsebodies.InstagramBodyValueResponse;
import sk.hfa.projects.web.domain.responsebodies.InstagramRefreshTokenResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InstagramTokenClient {


    @Value("${hfa.instagram.url}")
    private String url;

    private final RestTemplate restTemplate;

    public InstagramTokenClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    List<InstagramBodyValueResponse> getInstagramPosts(String token, Long limit) {
        String instagramUrl = url + "me/media?fields=caption,media_url&access_token=" + token;
        ResponseEntity<InstagramBodyResponse> responseBodies = restTemplate.exchange(instagramUrl, HttpMethod.GET, null, InstagramBodyResponse.class);
        return Objects.requireNonNull(responseBodies.getBody()).getData().stream()
                .filter(instagramBodyValueResponse ->
                        !instagramBodyValueResponse.getMedia_url().contains("video"))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public InstagramRefreshTokenResponse refreshToken(String token){
        String instagramUrl = url + "refresh_access_token?grant_type=ig_refresh_token&access_token="+token;
        ResponseEntity<InstagramRefreshTokenResponse> responseBodies = restTemplate.exchange(instagramUrl, HttpMethod.GET, null, InstagramRefreshTokenResponse.class);
        return responseBodies.getBody();
    }

}
