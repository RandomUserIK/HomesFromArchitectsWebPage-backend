package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeltaOperation implements Serializable {

    private Serializable insert;

    private Long delete;

    private Long retain;

    private Map<String, Serializable> attributes;

}
