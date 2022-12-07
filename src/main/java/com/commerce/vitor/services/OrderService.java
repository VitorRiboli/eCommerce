package com.commerce.vitor.services;

import com.commerce.vitor.dto.OrderDTO;
import com.commerce.vitor.entities.Order;
import com.commerce.vitor.repositories.OrderRepository;
import com.commerce.vitor.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {

        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return new OrderDTO(order);
    }
}
