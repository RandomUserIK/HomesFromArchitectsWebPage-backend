package sk.hfa.orderform.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.hfa.orderform.domain.OrderForm;


@Repository
public interface OrderFormRepository extends JpaRepository<OrderForm, Long> {
}
