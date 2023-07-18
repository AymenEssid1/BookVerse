package com.aymen.security.purchase.order;

import com.aymen.security.purchase.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

   List<Order> findByUserId(Integer userid);
}
