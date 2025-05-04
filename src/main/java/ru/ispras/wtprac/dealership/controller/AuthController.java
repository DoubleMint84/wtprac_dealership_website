package ru.ispras.wtprac.dealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.ispras.wtprac.dealership.DAO.CarDAO;
import ru.ispras.wtprac.dealership.DAO.ClientDAO;
import ru.ispras.wtprac.dealership.DAO.OrderDAO;
import ru.ispras.wtprac.dealership.DAO.TestDriveScheduleDAO;
import ru.ispras.wtprac.dealership.model.CarStatus;
import ru.ispras.wtprac.dealership.model.Client;
import ru.ispras.wtprac.dealership.model.OrderStatus;
import ru.ispras.wtprac.dealership.model.TestDriveStatus;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AuthController {


    private final ClientDAO clientDAO;

    private final OrderDAO orderDAO;

    private final TestDriveScheduleDAO testDriveScheduleDAO;

    private final PasswordEncoder passwordEncoder;
    private final CarDAO carDAO;

    @GetMapping("/register")
    public String showRegistration(Model model) {
        model.addAttribute("userForm", new Client());
        return "login_registration";
    }

    /* @GetMapping("/logout")
    public String logout(Model model) {
        return "redirect:/";
    }*/

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute("userForm") @Valid Client userForm,
            BindingResult br,
            Model model
    ) {
        if (clientDAO.findByEmail(userForm.getEmail()) != null) {
            br.rejectValue("email", "error.user", "Пользователь с таким email уже существует");
        }
        if (br.hasErrors()) {
            return "login_registration";
        }
        userForm.setPasswordHash(passwordEncoder.encode(userForm.getPasswordHash()));
        clientDAO.saveOne(userForm);
        return "redirect:/login_registration?registered";
    }

    @GetMapping("/account")
    public String accountPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        model.addAttribute("client", clientDAO.findByEmail(userDetails.getUsername()));
        model.addAttribute("user", userDetails);
        model.addAttribute("orderDAO", orderDAO);
        model.addAttribute("testDriveScheduleDAO", testDriveScheduleDAO);
        return "account";
    }

    @PostMapping("/delete/order/{id}")
    public String deleteOrderItem(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var order = orderDAO.getById(id);
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Заказ не найден (ошибочный номер)");
                return "redirect:/account";
            }
            order.setOrderStatus(OrderStatus.Canceled);
            orderDAO.updateOne(order);
            order.getCar().setCarStatus(CarStatus.CarInDealership);
            carDAO.updateOne(order.getCar());
            redirectAttributes.addFlashAttribute("success", "Заказ №" + id + " успешно отменен.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/account";
    }

    @PostMapping("/delete/testdrive/{id}")
    public String deleteTestDriveItem(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var testDrive = testDriveScheduleDAO.getById(id);
            if (testDrive == null) {
                redirectAttributes.addFlashAttribute("error", "Тест-Драйв не найден (ошибочный номер)");
                return "redirect:/account";
            }
            if (testDrive.getDateTimeStart().isAfter(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("error", "Ошибка удаления: Тест-Драйв уже идет");
                return "redirect:/account";
            }
            testDrive.setTestDriveStatus(TestDriveStatus.Canceled);
            testDriveScheduleDAO.updateOne(testDrive);
            redirectAttributes.addFlashAttribute("success", "Тест-Драйв №" + id + " успешно отменен.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/account";
    }
}
