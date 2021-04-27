package sk.hfa.orderform.services.interfaces;

import sk.hfa.orderform.domain.OrderForm;

public interface IOrderFormService {

    OrderForm save(OrderForm order);

    void deleteById(Long id);

}
