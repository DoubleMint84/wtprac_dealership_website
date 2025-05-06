package ru.ispras.wtprac.dealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ispras.wtprac.dealership.DAO.ManagerDAO;
import ru.ispras.wtprac.dealership.model.Manager;
import org.springframework.ui.Model;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ManagerAuthController {

    private final ManagerDAO managerDAO;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/manager/register")
    public String showManagerRegistration(Model model) {
        model.addAttribute("managerForm", new Manager());
        return "manager_registration";
    }

    @PostMapping("/manager/register")
    public String processManagerRegistration(
            @ModelAttribute("managerForm") @Valid Manager managerForm,
            BindingResult br,
            Model model
    ) {
        if (managerDAO.findByEmail(managerForm.getEmail()) != null) {
            br.rejectValue("email", "error.manager", "Менеджер с таким email уже существует");
        }
        if (br.hasErrors()) {
            return "manager_registration";
        }
        // Хешируем пароль
        managerForm.setPasswordHash(passwordEncoder.encode(managerForm.getPasswordHash()));
        managerDAO.saveOne(managerForm);
        return "redirect:/manager/register?success";
    }
}