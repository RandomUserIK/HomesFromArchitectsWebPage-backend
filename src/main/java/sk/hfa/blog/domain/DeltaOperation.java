package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeltaOperation {

    private Object insert;

    private Long delete;

    private Long retain;

    private Map<String, Object> attributes;

}
