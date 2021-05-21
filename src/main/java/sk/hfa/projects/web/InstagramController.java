package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sk.hfa.projects.web.domain.responsebodies.InstagramBodyResponse;
import sk.hfa.projects.web.domain.responsebodies.InstagramBodyValueResponse;
import sk.hfa.projects.web.domain.responsebodies.InstagramTokenMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/instagram")
public class InstagramController {

    @Value("${hfa.instagram.token}")
    private String token;

    @Value("${hfa.instagram.url}")
    private String url;

    private final RestTemplate restTemplate;

    InstagramController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping()
    public ResponseEntity<MessageResource> getInstagramToken(@RequestParam("limit") long limit) {
        String instagramUrl = url + "?fields=id,caption,media_url&access_token=" + token;
        ResponseEntity<InstagramBodyResponse> responseBodies = restTemplate.exchange(instagramUrl, HttpMethod.GET, null, InstagramBodyResponse.class);
        List<InstagramBodyValueResponse> returnEntity = Objects.requireNonNull(responseBodies.getBody()).getData().stream()
                .filter(instagramBodyValueResponse ->
                        !instagramBodyValueResponse.getMedia_url().contains("video"))
                .limit(limit)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new InstagramTokenMessageResource(returnEntity));
    }

}
