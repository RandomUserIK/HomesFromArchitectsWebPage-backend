package sk.hfa.projects.web.domain.responsebodies;

import lombok.Data;

import java.util.List;

@Data
public class InstagramBodyResponse {

    private List<InstagramBodyValueResponse> data;
}
