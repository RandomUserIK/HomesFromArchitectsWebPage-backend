package sk.hfa.projects.web.domain.responsebodies;

import lombok.Data;

import java.util.List;

@Data
public class InstagramBodyResource {

    private List<InstagramBodyValueResource> data;
}
