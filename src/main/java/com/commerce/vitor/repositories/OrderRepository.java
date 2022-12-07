package com.commerce.vitor.repositories;

import com.commerce.vitor.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


}
