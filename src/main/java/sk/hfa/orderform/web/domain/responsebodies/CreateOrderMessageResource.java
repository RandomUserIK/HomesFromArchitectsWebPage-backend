package sk.hfa.orderform.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Data
@AllArgsConstructor
public class CreateOrderMessageResource implements MessageResource {
    private String message;
}
