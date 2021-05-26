package sk.hfa.projects.web.domain.responsebodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InstagramBodyValueResource {

    private String caption;
    @JsonProperty("media_url")
    private String mediaUrl;
}
