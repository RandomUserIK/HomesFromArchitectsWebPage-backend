package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.instagram.domain.InstagramToken;
import sk.hfa.instagram.domain.service.interfaces.IInstagramTokenService;
import sk.hfa.projects.web.domain.responsebodies.InstagramBodyValueResource;
import sk.hfa.projects.web.domain.responsebodies.InstagramTokenMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/instagram")
public class InstagramController {

    private final IInstagramTokenService instagramTokenService;
    private final InstagramTokenClient instagramTokenClient;

    InstagramController(InstagramTokenClient instagramTokenClient, IInstagramTokenService instagramTokenService) {
        this.instagramTokenClient = instagramTokenClient;
        this.instagramTokenService = instagramTokenService;
    }

    @GetMapping()
    public ResponseEntity<MessageResource> getInstagramPosts(@RequestParam("limit") long limit) {
        InstagramToken instagramToken = instagramTokenService.findToken();
        List<InstagramBodyValueResource> returnEntity = instagramTokenClient.getInstagramPosts(instagramToken.getToken(), limit);
        return ResponseEntity.ok(new InstagramTokenMessageResource(returnEntity));
    }

}
