package ru.ispras.wtprac.dealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ispras.wtprac.dealership.DAO.CarDAO;
import ru.ispras.wtprac.dealership.DAO.ClientDAO;
import ru.ispras.wtprac.dealership.DAO.ManagerDAO;
import ru.ispras.wtprac.dealership.DAO.OrderDAO;
import ru.ispras.wtprac.dealership.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ManagerController {
    private final CarDAO carDAO;

    private final OrderDAO orderDAO;

    private final ClientDAO clientDAO;

    private final ManagerDAO managerDAO;

    @GetMapping("/manager/order_list")
    public String getOrders(
            @RequestParam(required = false) String managerId,
            @RequestParam(required = false) String needsTestDrive,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String carId,
            Model model) {

        /*model.addAttribute("param", Map.of(
            "managerId", managerId,
            "needsTestDrive", needsTestDrive,
            "orderStatus", orderStatus,
            "carId", carId
        ));*/

        Collection<Order> orders = orderDAO.getAll();

        // Обработка managerId
        Long managerIdLong = null;
        if (managerId != null && !managerId.isEmpty()) {
            try {
                managerIdLong = Long.parseLong(managerId);
            } catch (NumberFormatException e) {
                // Обработка неверного формата
            }
        }

        // Обработка needsTestDrive
        Boolean needsTestDriveBool;
        if (needsTestDrive != null && !needsTestDrive.isEmpty()) {
            needsTestDriveBool = Boolean.parseBoolean(needsTestDrive);
        } else {
            needsTestDriveBool = null;
        }

        // Обработка carId
        Long carIdLong = null;
        if (carId != null && !carId.isEmpty()) {
            try {
                carIdLong = Long.parseLong(carId);
            } catch (NumberFormatException e) {
                // Обработка неверного формата
            }
        }

        // Фильтрация
        if (managerIdLong != null) {
            Long finalManagerIdLong = managerIdLong;
            orders = orders.stream()
                    .filter(x -> x.getManager() != null && Objects.equals(x.getManager().getId(), finalManagerIdLong))
                    .collect(Collectors.toList());
        }
        if (needsTestDriveBool != null) {
            orders = orders.stream()
                    .filter(x -> x.getNeedsPreTestDrive() == needsTestDriveBool)
                    .collect(Collectors.toList());
            model.addAttribute("needsTestDrive", needsTestDriveBool);
        }
        if (orderStatus != null && !orderStatus.isEmpty()) {
            orders = orders.stream()
                    .filter(x -> x.getOrderStatus().name().equals(orderStatus))
                    .collect(Collectors.toList());
            model.addAttribute("orderStatus", orderStatus);
        }
        if (carIdLong != null) {
            Long finalCarIdLong = carIdLong;
            orders = orders.stream()
                    .filter(x -> Objects.equals(x.getCar().getId(), finalCarIdLong))
                    .collect(Collectors.toList());
        }

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "order_list";
    }

    @PostMapping("/manager/orders/{id}/edit")
    public String updateOrder(
            @PathVariable Long id,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false, defaultValue = "false") Boolean needsPreTestDrive,
            @RequestParam OrderStatus orderStatus,
            @RequestParam Long carId) {

        Order order = orderDAO.getById(id);
        if (order == null) {
            throw new IllegalArgumentException("Invalid order Id:" + id);
        }

        // Обновление менеджера
        if (managerId != null) {
            Manager manager = managerDAO.getById(managerId);
            order.setManager(manager);
        } else {
            order.setManager(null);
        }

        order.setNeedsPreTestDrive(needsPreTestDrive);
        order.setOrderStatus(orderStatus);

        Car car = carDAO.getById(carId);
        if (car == null) {
            throw new IllegalArgumentException("Invalid car Id:" + carId);
        }
        order.setCar(car);

        orderDAO.updateOne(order);
        return "redirect:/manager/order_list";
    }

    @PostMapping("/manager/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderDAO.deleteById(id);
        return "redirect:/manager/order_list";
    }

    @GetMapping("/manager/car_list")
    public String getCars(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String vin,
        @RequestParam(required = false) String color,
        @RequestParam(required = false) String status,
        Model model) {

        Collection<Car> cars = carDAO.getAll();

        // Фильтрация
        if (name != null && !name.isEmpty()) {
            cars = cars.stream()
                    .filter(car -> car.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("name", name);
        }
        if (vin != null && !vin.isEmpty()) {
            cars = cars.stream()
                    .filter(car -> car.getVin().equalsIgnoreCase(vin))
                    .collect(Collectors.toList());
            model.addAttribute("vin", vin);
        }
        if (color != null && !color.isEmpty()) {
            cars = cars.stream()
                    .filter(car -> car.getColor() != null && car.getColor().equalsIgnoreCase(color))
                    .collect(Collectors.toList());
            model.addAttribute("color", color);
        }
        if (status != null && !status.isEmpty()) {
            cars = cars.stream()
                    .filter(car -> car.getCarStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
            model.addAttribute("status", status);
        }

        model.addAttribute("cars", cars);
        model.addAttribute("statuses", CarStatus.values());
        return "car_list";
    }

    @PostMapping("/manager/cars/{id}/edit")
    public String updateCar(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String vin,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String seat,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam CarStatus status) {

        Car car = carDAO.getById(id);
        if (car == null) {
            throw new IllegalArgumentException("Invalid order Id:" + id);
        }

        car.setName(name);
        car.setVin(vin);
        car.setColor(color);
        car.setSeat(seat);
        car.setPrice(price);
        car.setCarStatus(status);

        carDAO.updateOne(car);
        return "redirect:/manager/car_list";
    }

    @PostMapping("/manager/cars/{id}/delete")
    public String deleteCar(@PathVariable Long id) {
        carDAO.deleteById(id);
        return "redirect:/manager/car_list";
    }

}
