package com.aymen.security.statistics;


import com.aymen.security.book.Book;
import com.aymen.security.purchase.order.Order;
import com.aymen.security.purchase.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    OrderRepository orderRepository;








    public List<Double> getTotalSalesByYearAndMonth(int year) {
        List<Order> orders = orderRepository.findAll();
        Map<Integer, Double> currentYearSalesMap = new HashMap<>();
        Map<Integer, Double> previousYearSalesMap = new HashMap<>();

        // Initialize the maps with 0 sales for each month
        for (int month = 1; month <= 12; month++) {
            currentYearSalesMap.put(month, 0.0);
            previousYearSalesMap.put(month, 0.0);
        }

        // Calculate sales for each order and add them to the corresponding month
        for (Order order : orders) {
            LocalDateTime orderDateTime = order.getOrderDate();
            int orderYear = orderDateTime.getYear();
            int orderMonth = orderDateTime.getMonthValue();

            if (orderYear == year) {
                double orderSales = 0.0;
                for (Book book : order.getItems().keySet()) {
                    orderSales += book.getPrice() * order.getItems().get(book);
                }
                currentYearSalesMap.put(orderMonth, currentYearSalesMap.get(orderMonth) + orderSales);
            } else if (orderYear == (year - 1)) {
                double orderSales = 0.0;
                for (Book book : order.getItems().keySet()) {
                    orderSales += book.getPrice() * order.getItems().get(book);
                }
                previousYearSalesMap.put(orderMonth, previousYearSalesMap.get(orderMonth) + orderSales);
            }
        }

        // Convert the map values to a list
        List<Double> salesByMonth = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            salesByMonth.add(currentYearSalesMap.get(month));
        }
        for (int month = 1; month <= 12; month++) {
            salesByMonth.add(previousYearSalesMap.get(month));
        }
        return salesByMonth;
    }



///////////////////////////////////////////////////////////////////////////////////////////////:

    public List<SellingBook> getMostSoldBooks(int timePeriod) {
        List<Order> orders = orderRepository.findAll();
        Map<Book, Integer> bookQuantityMap = new HashMap<>();

        for (Order order : orders) {
            if (isOrderInTimePeriod(order, timePeriod)) {
                for (Book book : order.getItems().keySet()) {
                    bookQuantityMap.put(book, bookQuantityMap.getOrDefault(book, 0) + order.getItems().get(book));
                }
            }
        }

        List<SellingBook> mostSoldBooks = bookQuantityMap.entrySet().stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue().reversed())
                .limit(3)
                .map(entry -> new SellingBook(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return mostSoldBooks;
    }
    public List<Integer> getMostSoldBooksMini(int timePeriod) {
        List<Order> orders = orderRepository.findAll();
        Map<Book, Integer> bookQuantityMap = new HashMap<>();

        for (Order order : orders) {
            if (isOrderInTimePeriod(order, timePeriod)) {
                for (Book book : order.getItems().keySet()) {
                    bookQuantityMap.put(book, bookQuantityMap.getOrDefault(book, 0) + order.getItems().get(book));
                }
            }
        }

        List<Integer> topBookIds = bookQuantityMap.entrySet().stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue().reversed())
                .limit(3)
                .map(entry -> entry.getKey().getId()) // Extract the ID of the Book
                .collect(Collectors.toList());

        return topBookIds;
    }


    public List<SellingBook> getLeastSoldBooks( int timePeriod) {
        List<Order> orders =orderRepository.findAll();
        Map<Book, Integer> bookQuantityMap = new HashMap<>();

        for (Order order : orders) {
            if (isOrderInTimePeriod(order, timePeriod)) {
                for (Book book : order.getItems().keySet()) {
                    bookQuantityMap.put(book, bookQuantityMap.getOrDefault(book, 0) + order.getItems().get(book));
                }
            }
        }

        List<SellingBook> leastSoldBooks = bookQuantityMap.entrySet().stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue())
                .limit(3)
                .map(entry -> new SellingBook(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return leastSoldBooks;
    }

    private boolean isOrderInTimePeriod(Order order, int timePeriod) {
        LocalDate orderDate = order.getOrderDate().toLocalDate();
        int orderYear = orderDate.getYear();

        int orderMonth = orderDate.getMonthValue();
        //System.out.println(orderYear+"  "+orderMonth);

        if (timePeriod >= 1 && timePeriod <= 12) { // Assuming the input is a two-digit year (e.g., 21 for 2021)
            int currentYear = LocalDate.now().getYear();
            System.out.println("this is the current year "+currentYear);
            return orderYear == currentYear && orderMonth == timePeriod;
        } else if (timePeriod >= 100 && timePeriod <= 9999) { // Assuming the input is a four-digit year (e.g., 2021)
            return orderYear == timePeriod;
        } else if (timePeriod < 100) { // Assuming the input is a month (1 to 12)
            return orderYear == 2000 + timePeriod;
        }

        throw new IllegalArgumentException("Invalid time period. Please enter a valid year (e.g., 2021) or month (1 to 12).");
    }
/////////////////////////////////////////////////////////////////////////////
public Map<String, Integer> getBooksSoldByCategory(int timePeriod) {
    List<Order> orders = orderRepository.findAll();
    Map<String, Integer> categoryCountMap = new HashMap<>();

    for (Order order : orders) {
        if (isOrderInTimePeriod(order, timePeriod)) {
            for (Book book : order.getItems().keySet()) {
                String category = book.getCategory().toString();
                categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + order.getItems().get(book));
            }
        }
    }

    return categoryCountMap;
}



    public static class SellingBook {
    private Book book;
    private int quantitySold;

    public SellingBook(Book book, int quantitySold) {
        this.book = book;
        this.quantitySold = quantitySold;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantitySold() {
        return quantitySold;
    }
}

}
