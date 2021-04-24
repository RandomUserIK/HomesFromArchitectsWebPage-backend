package sk.hfa.orderform.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.orderform.domain.OrderForm;
import sk.hfa.orderform.services.interfaces.IOrderFormService;
import sk.hfa.orderform.web.domain.requestbodies.OrderFormRequest;
import sk.hfa.orderform.web.domain.responsebodies.CreateOrderMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/api/order")
public class OrderFormController {

    private final IOrderFormService orderService;

    public OrderFormController(IOrderFormService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<MessageResource> createProject(@Valid @RequestBody OrderFormRequest request) {
        OrderForm order = OrderForm.build(request);
        orderService.save(order);

        MessageResource messageResource = new CreateOrderMessageResource("success");
        return ResponseEntity.ok(messageResource);
    }

}
