package sk.hfa.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.order.web.domain.requestbodies.OrderRequest;
import sk.hfa.order.web.domain.responsebodies.CreateOrderMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

    @PostMapping
    public ResponseEntity<MessageResource> createProject(@RequestBody OrderRequest request) {

        MessageResource messageResource = new CreateOrderMessageResource("success");
        return ResponseEntity.ok(messageResource);
    }

}
