package sk.hfa.recaptcha.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import sk.hfa.recaptcha.RecaptchaSettings;
import sk.hfa.recaptcha.domain.throwable.RecaptchaException;
import sk.hfa.recaptcha.service.interfaces.IRecaptchaService;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

public abstract class AbstractRecaptchaService implements IRecaptchaService {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected RecaptchaSettings captchaSettings;

    @Autowired
    protected RestOperations restTemplate;

    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";

    @Override
    public String getReCaptchaSecret() {
        return captchaSettings.getSecret();
    }


    protected void securityCheck(final String response) {
        if (!responseSanityCheck(response)) {
            throw new RecaptchaException("Response contains invalid characters");
        }
    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    protected String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
