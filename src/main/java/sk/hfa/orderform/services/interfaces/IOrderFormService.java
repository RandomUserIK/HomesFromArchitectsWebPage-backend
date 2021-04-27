package sk.hfa.orderform.services.interfaces;

import sk.hfa.orderform.domain.OrderForm;
import sk.hfa.orderform.web.domain.requestbodies.OrderFormRequest;

public interface IOrderFormService {

    OrderForm save(OrderFormRequest request);

    void deleteById(Long id);

}
