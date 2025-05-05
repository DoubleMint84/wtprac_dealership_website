package ru.ispras.wtprac.dealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.ispras.wtprac.dealership.DAO.CarDAO;
import ru.ispras.wtprac.dealership.DAO.ClientDAO;
import ru.ispras.wtprac.dealership.DAO.ManagerDAO;
import ru.ispras.wtprac.dealership.DAO.OrderDAO;
import ru.ispras.wtprac.dealership.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) Boolean needsTestDrive,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) Long carId,
            Model model) {

        Collection<Order> orders = orderDAO.getAll();

        if (managerId != null) orders = orders.stream().filter((x) -> Objects.equals(x.getManager().getId(), managerId)).toList();
        if (needsTestDrive != null) orders = orders.stream().filter((x) -> x.getNeedsPreTestDrive() == needsTestDrive).toList();
        if (orderStatus != null) orders = orders.stream().filter((x) -> x.getOrderStatus().name().equals(orderStatus)).toList();
        if (carId != null) orders = orders.stream().filter((x) -> Objects.equals(x.getCar().getId(), carId)).toList();

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("updatedOrder", new Order());
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

}
