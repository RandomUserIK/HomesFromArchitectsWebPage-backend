package sk.hfa.projects.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.hfa.projects.web.domain.responsebodies.InstagramBodyResource;
import sk.hfa.projects.web.domain.responsebodies.InstagramBodyValueResource;
import sk.hfa.projects.web.domain.responsebodies.InstagramRefreshTokenResource;

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

    List<InstagramBodyValueResource> getInstagramPosts(String token, long limit) {
        String instagramUrl = url + "me/media?fields=caption,media_url&access_token=" + token;
        ResponseEntity<InstagramBodyResource> responseBodies = restTemplate.exchange(instagramUrl, HttpMethod.GET, null, InstagramBodyResource.class);
        return Objects.requireNonNull(responseBodies.getBody()).getData().stream()
                .filter(instagramBodyValueResponse ->
                        !instagramBodyValueResponse.getMediaUrl().contains("video"))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public InstagramRefreshTokenResource refreshToken(String token) {
        String instagramUrl = url + "refresh_access_token?grant_type=ig_refresh_token&access_token=" + token;
        ResponseEntity<InstagramRefreshTokenResource> responseBodies = restTemplate.exchange(instagramUrl, HttpMethod.GET, null, InstagramRefreshTokenResource.class);
        return responseBodies.getBody();
    }

}
