package com.aymen.security.statistics;


import com.aymen.security.book.Book;
import com.aymen.security.purchase.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Staistics")
public class StatisticsController {
    @Autowired
    StatisticsService service;

    @GetMapping("/getTotalSalesByYearAndMonth")
    public ResponseEntity<List<Double> > orders( @RequestParam int year){

        List<Double>  result = service.getTotalSalesByYearAndMonth(year);

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    @GetMapping("/MostSoldBooks")
    public ResponseEntity<List<StatisticsService.SellingBook>> mostSold(@RequestParam int period){

        List<StatisticsService.SellingBook>  result = service.getMostSoldBooks(period);

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/MostSoldBooksMini")
    public ResponseEntity<List<Integer>> mostSoldMini(@RequestParam int period){

        List<Integer>  result = service.getMostSoldBooksMini(period);

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/LeastSoldBooks")
    public ResponseEntity<List<StatisticsService.SellingBook>> leastSold(@RequestParam int period){

        List<StatisticsService.SellingBook>  result = service.getLeastSoldBooks(period);

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/BooksSoldByCategory")
    public ResponseEntity<Map<String, Integer>> getBooksSoldByCategory(@RequestParam int period){

        Map<String, Integer>  result = service.getBooksSoldByCategory(period);

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
