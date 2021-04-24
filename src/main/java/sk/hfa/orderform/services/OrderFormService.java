package sk.hfa.orderform.services;

import org.springframework.stereotype.Service;
import sk.hfa.orderform.domain.OrderForm;
import sk.hfa.orderform.domain.repositories.OrderFormRepository;
import sk.hfa.orderform.services.interfaces.IOrderFormService;

@Service
public class OrderFormService implements IOrderFormService {

    private final OrderFormRepository orderRepository;

    public OrderFormService(OrderFormRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void save(OrderForm order) {
        orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
