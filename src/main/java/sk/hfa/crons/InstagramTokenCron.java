package sk.hfa.crons;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sk.hfa.instagram.domain.InstagramToken;
import sk.hfa.instagram.domain.service.interfaces.IInstagramTokenService;
import sk.hfa.projects.web.InstagramTokenClient;
import sk.hfa.projects.web.domain.responsebodies.InstagramRefreshTokenResource;

@Component
public class InstagramTokenCron {

    private final IInstagramTokenService instagramTokenService;
    private final InstagramTokenClient instagramTokenClient;


    public InstagramTokenCron(IInstagramTokenService instagramTokenService, InstagramTokenClient instagramTokenClient) {
        this.instagramTokenService = instagramTokenService;
        this.instagramTokenClient = instagramTokenClient;
    }

    @Scheduled(cron = "@monthly")
    public void refreshInstagramToken() {
        refreshToken(instagramTokenService.findToken());
    }

    @Scheduled(cron = "@midnight")
    public void refreshInstagramTokenDailyIfMonthlyRefreshFailed() {
        InstagramToken instagramToken = instagramTokenService.findToken();
        if(!instagramToken.isRefreshSuccessful()){
            refreshToken(instagramToken);
        }
    }

    private void setNewInstagramTokenData(InstagramToken instagramToken,
                                          InstagramRefreshTokenResource instagramRefreshTokenResponse) {
        instagramToken.setToken(instagramRefreshTokenResponse.getAccessToken());
        instagramToken.setRefreshSuccessful(true);
        instagramTokenService.save(instagramToken);
    }

    private void refreshToken(InstagramToken instagramToken) {
        try {
            InstagramRefreshTokenResource instagramRefreshTokenResponse = instagramTokenClient
                    .refreshToken(instagramToken.getToken());
            setNewInstagramTokenData(instagramToken, instagramRefreshTokenResponse);
        } catch (Exception exception) {
            instagramToken.setRefreshSuccessful(false);
            instagramTokenService.save(instagramToken);
        }
    }
}