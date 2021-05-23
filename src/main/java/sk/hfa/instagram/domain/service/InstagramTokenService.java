package sk.hfa.instagram.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.hfa.instagram.domain.InstagramToken;
import sk.hfa.instagram.domain.repository.InstagramTokenRepository;
import sk.hfa.instagram.domain.service.interfaces.IInstagramTokenService;

import java.util.List;

@Service
public class InstagramTokenService implements IInstagramTokenService {

    @Value("${hfa.instagram.token}")
    private String token;

    private final InstagramTokenRepository instagramTokenRepository;

    public InstagramTokenService(InstagramTokenRepository instagramTokenRepository) {
        this.instagramTokenRepository = instagramTokenRepository;
    }

    @Override
    public InstagramToken findToken() {
        List<InstagramToken> instagramToken = instagramTokenRepository.findAll();
        if (instagramToken.isEmpty()) {
            instagramTokenRepository.save(new InstagramToken(token, true));
            instagramToken = instagramTokenRepository.findAll();
        }
        return instagramToken.get(0);
    }

    @Override
    public void save(InstagramToken token) {
        instagramTokenRepository.deleteAll();
        instagramTokenRepository.save(token);
    }

}
