package ru.ispras.wtprac.dealership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ispras.wtprac.dealership.DAO.CarDAO;
import ru.ispras.wtprac.dealership.model.Car;
import ru.ispras.wtprac.dealership.model.Client;

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

    @GetMapping("/car/{id}")
    public String carDetailPage(@PathVariable("id") final Long id, Model model) {
        final Car car = carDAO.getById(id);
        model.addAttribute("car", car);
        return "carpage";
    }

    @GetMapping("/login_registration")
    public String loginRegistrationPage(Model model) {
        model.addAttribute("userForm", new Client());
        return "login_registration";
    }

}
