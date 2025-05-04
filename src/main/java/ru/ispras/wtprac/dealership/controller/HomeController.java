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
import ru.ispras.wtprac.dealership.DAO.OrderDAO;
import ru.ispras.wtprac.dealership.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {


    private final CarDAO carDAO;

    private final OrderDAO orderDAO;

    private final ClientDAO clientDAO;

    @RequestMapping(value = { "/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/catalog")
    public String carsListPage(Model model) {
        List<Car> cars = (List<Car>) carDAO.getAll();
        model.addAttribute("cars", cars);
        model.addAttribute("carService", carDAO);
        return "catalog";
    }

    @GetMapping("/car/{id}")
    public String carDetailPage(@PathVariable("id") final Long id, Model model) {
        final Car car = carDAO.getById(id);
        model.addAttribute("car", car);
        return "carpage";
    }

    @GetMapping("/order/car/{id}")
    public String carOrderPage(@PathVariable("id") final Long id, Model model) {
        final Car car = carDAO.getById(id);
        model.addAttribute("car", car);
        return "order_form";
    }

    @PostMapping("/order/car/{id}")
    public String processOrder(
            @PathVariable Long id,
            @RequestParam(required = false) boolean testDrive,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        var client = clientDAO.findByEmail(userDetails.getUsername());
        // Находим автомобиль
        Car car = carDAO.getById(id);
        if (car == null) {
            throw new IllegalArgumentException("Car not found");
        }
        try {
            // Создаем заказ
            Order order = new Order();
            order.setCar(car);
            order.setClient(client);
            order.setOrderStatus(OrderStatus.InProcessing);
            order.setOrderDatetime(LocalDateTime.now());
            order.setNeedsPreTestDrive(testDrive);
            orderDAO.saveOne(order);

            car.setCarStatus(CarStatus.Sold);
            carDAO.updateOne(car);

            redirectAttributes.addFlashAttribute("success",
                    "Заказ успешно оформлен!" +
                            (testDrive ? " Тест-драйв в обработке." : ""));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при оформлении заказа: " + e.getMessage());
        }

        return "redirect:/account";
    }

    @GetMapping("/login_registration")
    public String loginRegistrationPage(Model model) {
        model.addAttribute("userForm", new Client());
        return "login_registration";
    }

}
