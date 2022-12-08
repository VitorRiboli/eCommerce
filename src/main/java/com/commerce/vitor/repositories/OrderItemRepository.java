package com.commerce.vitor.repositories;

import com.commerce.vitor.entities.OrderItem;
import com.commerce.vitor.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
