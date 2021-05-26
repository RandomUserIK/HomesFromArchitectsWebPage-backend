package sk.hfa.instagram.domain.service.interfaces;

import sk.hfa.instagram.domain.InstagramToken;

public interface IInstagramTokenService {

    InstagramToken findToken();

    InstagramToken save(InstagramToken token);
}
