package com.commerce.vitor.services;

import com.commerce.vitor.dto.OrderDTO;
import com.commerce.vitor.dto.OrderItemsDTO;
import com.commerce.vitor.entities.*;
import com.commerce.vitor.repositories.OrderItemRepository;
import com.commerce.vitor.repositories.OrderRepository;
import com.commerce.vitor.repositories.ProductRepository;
import com.commerce.vitor.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {

        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticated(); //Pega o usuário logado
        order.setClient(user);

        for (OrderItemsDTO itemDto : dto.getItems()) {
            //Instanciando um Product apartir da referencia do id passado no itemDto
            Product product = productRepository.getReferenceById(itemDto.getProductId());

            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());

            order.getItems().add(item);
        }

        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }
}
