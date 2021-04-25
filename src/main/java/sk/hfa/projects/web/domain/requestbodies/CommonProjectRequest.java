package sk.hfa.projects.web.domain.requestbodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonProjectRequest extends ProjectRequest {

    private boolean common;

}
