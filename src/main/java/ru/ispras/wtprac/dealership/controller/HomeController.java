package ru.ispras.wtprac.dealership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ispras.wtprac.dealership.DAO.CarDAO;
import ru.ispras.wtprac.dealership.model.Car;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CarDAO carDAO = new CarDAO();

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

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/account")
    public String accountPage(Model model) {
        return "account";
    }

}
