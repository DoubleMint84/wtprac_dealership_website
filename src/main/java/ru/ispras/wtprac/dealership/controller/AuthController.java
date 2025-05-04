package ru.ispras.wtprac.dealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import ru.ispras.wtprac.dealership.DAO.ClientDAO;
import ru.ispras.wtprac.dealership.model.Client;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final ClientDAO clientDAO;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistration(Model model) {
        model.addAttribute("userForm", new Client());
        return "login_registration";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "redirect:/";
    }

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
}
