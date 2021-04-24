package sk.hfa.orderform.services.interfaces;

import sk.hfa.orderform.domain.OrderForm;

public interface IOrderFormService {

    void save(OrderForm order);

    void deleteById(Long id);

}
