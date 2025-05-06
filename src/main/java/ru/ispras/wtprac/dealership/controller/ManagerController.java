package ru.ispras.wtprac.dealership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ispras.wtprac.dealership.DAO.*;
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
    private final BrandDAO brandDAO;
    private final ManufacturerDAO manufacturerDAO;
    private final TestDriveScheduleDAO testDriveScheduleDAO;
    private final ModelDAO modelDAO;
    private final VehicleConfigurationDAO vehicleConfigDAO;

    @GetMapping("/manager/dashboard")
    public String showDashboard() {
        return "manager_dashboard";
    }

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

    @GetMapping("/manager/orders/{id}/delete")
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

    @GetMapping("/manager/cars/{id}/delete")
    public String deleteCar(@PathVariable Long id) {
        carDAO.deleteById(id);
        return "redirect:/manager/car_list";
    }

    @GetMapping("/manager/client_list")
    public String getClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String passport,
            @RequestParam(required = false) String drivingLicense,
            Model model) {

        Collection<Client> clients = clientDAO.getAll();

        // Фильтрация
        if (name != null && !name.isEmpty()) {
            clients = clients.stream()
                    .filter(x -> x.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("name", name);
        }
        if (email != null && !email.isEmpty()) {
            clients = clients.stream()
                    .filter(x -> x.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("email", email);
        }

        if (address != null && !address.isEmpty()) {
            clients = clients.stream()
                    .filter(x -> x.getEmail().toLowerCase().contains(address.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("address", address);
        }
        if (phone != null && !phone.isEmpty()) {
            clients = clients.stream()
                    .filter(x -> x.getEmail().toLowerCase().contains(phone.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("phone", phone);
        }
        if (passport != null && !passport.isEmpty()) {
            clients = clients.stream()
                    .filter(x -> x.getEmail().toLowerCase().contains(passport.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("passport", passport);
        }
        if (drivingLicense != null && !drivingLicense.isEmpty()) {
            clients = clients.stream()
                    .filter(x -> x.getEmail().toLowerCase().contains(drivingLicense.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("drivingLicense", drivingLicense);
        }

        model.addAttribute("clients", clients);
        return "client_list";
    }

    @PostMapping("/manager/clients/{id}/edit")
    public String updateClient(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String passport,
            @RequestParam(required = false) String drivingLicense) {

        Client client = clientDAO.getById(id);
        if (client == null) {
            throw new IllegalArgumentException("Invalid client Id:" + id);
        }

        client.setName(name);
        client.setEmail(email);
        client.setAddress(address);
        client.setPhone(phone);
        client.setPassport(passport);
        client.setDrivingLicense(drivingLicense);

        clientDAO.updateOne(client);
        return "redirect:/manager/client_list";
    }

    @GetMapping("/manager/clients/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientDAO.deleteById(id);
        return "redirect:/manager/client_list";
    }

    @GetMapping("/manager/brand_list")
    public String getBrands(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long manufacturerId,
            @RequestParam(required = false) String country,
            Model model) {

        Collection<Brand> brands = brandDAO.getAll();

        // Фильтрация
        if (name != null && !name.isEmpty()) {
            brands = brands.stream()
                    .filter(x -> x.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("name", name);
        }
        if (manufacturerId != null) {
            brands = brands.stream()
                    .filter(x -> x.getManufacturer().getId().equals(manufacturerId))
                    .collect(Collectors.toList());
            model.addAttribute("manufacturerId", manufacturerId);
        }
        if (country != null && !country.isEmpty()) {
            brands = brands.stream()
                    .filter(x -> x.getCountry().toLowerCase().contains(country.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("country", country);
        }

        model.addAttribute("brands", brands);
        return "brand_list";
    }

    @PostMapping("/manager/brands/{id}/edit")
    public String updateBrand(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long manufacturerId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String country) {

        Brand brand = brandDAO.getById(id);
        if (brand == null) {
            throw new IllegalArgumentException("Invalid client Id:" + id);
        }

        brand.setName(name);

        Manufacturer manufacturer = manufacturerDAO.getById(manufacturerId);
        if (manufacturer == null) {
            throw new IllegalArgumentException("Invalid manufacturer Id:" + id);
        }
        brand.setManufacturer(manufacturer);
        brand.setDescription(description);
        brand.setCountry(country);

        brandDAO.updateOne(brand);
        return "redirect:/manager/brand_list";
    }

    @GetMapping("/manager/brands/{id}/delete")
    public String deleteBrand(@PathVariable Long id) {
        brandDAO.deleteById(id);
        return "redirect:/manager/brand_list";
    }

    @GetMapping("/manager/model_list")
    public String getModels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Integer year,
            Model model) {

        Collection<ru.ispras.wtprac.dealership.model.Model> models = modelDAO.getAll();

        // Фильтрация
        if (name != null && !name.isEmpty()) {
            models = models.stream()
                    .filter(x -> x.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("name", name);
        }
        if (brandId != null) {
            models = models.stream()
                    .filter(x -> x.getBrand().getId().equals(brandId))
                    .collect(Collectors.toList());
            model.addAttribute("brandId", brandId);
        }
        if (year != null) {
            models = models.stream()
                    .filter(x -> x.getYear().equals(year))
                    .collect(Collectors.toList());
            model.addAttribute("year", year);
        }

        model.addAttribute("models", models);
        return "model_list";
    }

    @PostMapping("/manager/models/{id}/edit")
    public String updateModel(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Integer year) {

        ru.ispras.wtprac.dealership.model.Model model = modelDAO.getById(id);
        if (model == null) {
            throw new IllegalArgumentException("Invalid model Id:" + id);
        }

        model.setName(name);

        Brand brand = brandDAO.getById(brandId);
        if (brand == null) {
            throw new IllegalArgumentException("Invalid brand Id:" + id);
        }
        model.setBrand(brand);
        model.setYear(year);

        modelDAO.updateOne(model);
        return "redirect:/manager/model_list";
    }

    @GetMapping("/manager/models/{id}/delete")
    public String deleteModel(@PathVariable Long id) {
        modelDAO.deleteById(id);
        return "redirect:/manager/model_list";
    }

    @GetMapping("/manager/manufacturer_list")
    public String getManufacturers(
            @RequestParam(required = false) String name,
            Model model) {

        Collection<Manufacturer> manufacturers = manufacturerDAO.getAll();

        // Фильтрация
        if (name != null && !name.isEmpty()) {
            manufacturers = manufacturers.stream()
                    .filter(x -> x.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("name", name);
        }

        model.addAttribute("manufacturers", manufacturers);
        return "manufacturer_list";
    }

    @PostMapping("/manager/manufacturers/{id}/edit")
    public String updateManufacturer(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        Manufacturer manufacturer = manufacturerDAO.getById(id);
        if (manufacturer == null) {
            throw new IllegalArgumentException("Invalid client Id:" + id);
        }

        manufacturer.setName(name);
        manufacturer.setDescription(description);

        manufacturerDAO.updateOne(manufacturer);
        return "redirect:/manager/manufacturer_list";
    }

    @GetMapping("/manager/manufacturers/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id) {
        manufacturerDAO.deleteById(id);
        return "redirect:/manager/manufacturer_list";
    }

    @GetMapping("/manager/testdrive_list")
    public String getTestDrives(
            @RequestParam(required = false) String carId,
            @RequestParam(required = false) String testDriveStatus,
            @RequestParam(required = false) String managerId,
            @RequestParam(required = false) String clientId,
            Model model) {

        Collection<TestDriveSchedule> testDriveSchedules = testDriveScheduleDAO.getAll();

        // Обработка managerId
        Long managerIdLong = null;
        if (managerId != null && !managerId.isEmpty()) {
            try {
                managerIdLong = Long.parseLong(managerId);
            } catch (NumberFormatException e) {
                // Обработка неверного формата
            }
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

        Long clientIdLong = null;
        if (clientId != null && !clientId.isEmpty()) {
            try {
                clientIdLong = Long.parseLong(clientId);
            } catch (NumberFormatException e) {
                // Обработка неверного формата
            }
        }

        // Фильтрация
        if (managerIdLong != null) {
            Long finalManagerIdLong = managerIdLong;
            testDriveSchedules = testDriveSchedules.stream()
                    .filter(x -> x.getManager() != null && Objects.equals(x.getManager().getId(), finalManagerIdLong))
                    .collect(Collectors.toList());
        }
        if (carIdLong != null) {
            Long finalCarIdLong = carIdLong;
            testDriveSchedules = testDriveSchedules.stream()
                    .filter(x -> Objects.equals(x.getCar().getId(), finalCarIdLong))
                    .collect(Collectors.toList());
        }
        if (testDriveStatus != null && !testDriveStatus.isEmpty()) {
            testDriveSchedules = testDriveSchedules.stream()
                    .filter(x -> x.getTestDriveStatus().name().equals(testDriveStatus))
                    .collect(Collectors.toList());
            model.addAttribute("testDriveStatus", testDriveStatus);
        }
        if (clientIdLong != null) {
            Long finalClientIdLong = clientIdLong;
            testDriveSchedules = testDriveSchedules.stream()
                    .filter(x -> Objects.equals(x.getClient().getId(), finalClientIdLong))
                    .collect(Collectors.toList());
        }

        model.addAttribute("testDrives", testDriveSchedules);
        model.addAttribute("statuses", TestDriveStatus.values());
        return "testdrive_list";
    }

    @PostMapping("/manager/testdrives/{id}/edit")
    public String updateTestDrive(
            @PathVariable Long id,
            @RequestParam(required = false) Long carId,
            @RequestParam TestDriveStatus testDriveStatus,
            @RequestParam Long managerId,
            @RequestParam Long clientId) {

        TestDriveSchedule testDriveSchedule = testDriveScheduleDAO.getById(id);
        if (testDriveSchedule == null) {
            throw new IllegalArgumentException("Invalid testDrive Id:" + id);
        }

        // Обновление менеджера
        if (carId != null) {
            Car car = carDAO.getById(carId);
            if (car == null) {
                throw new IllegalArgumentException("Invalid car Id:" + carId);
            }
            testDriveSchedule.setCar(car);
        } else {
            testDriveSchedule.setCar(null);
        }

        if (managerId != null) {
            Manager manager = managerDAO.getById(managerId);
            testDriveSchedule.setManager(manager);
        } else {
            testDriveSchedule.setManager(null);
        }

        if (clientId != null) {
            Client client = clientDAO.getById(clientId);
            testDriveSchedule.setClient(client);
        } else {
            testDriveSchedule.setClient(null);
        }

        testDriveSchedule.setTestDriveStatus(testDriveStatus);
        testDriveScheduleDAO.updateOne(testDriveSchedule);
        return "redirect:/manager/testdrive_list";
    }

    @GetMapping("/manager/testdrives/{id}/delete")
    public String deleteTestDrive(@PathVariable Long id) {
        testDriveScheduleDAO.deleteById(id);
        return "redirect:/manager/testdrive_list";
    }

    @GetMapping("/manager/vehicle_config_list")
    public String getVehicleConfigs(
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) BigDecimal engineVolume,
            @RequestParam(required = false) Integer enginePower,
            @RequestParam(required = false) BigDecimal fuelConsumption,
            @RequestParam(required = false) Integer doorsCount,
            @RequestParam(required = false) Integer seatsCount,
            @RequestParam(required = false) BigDecimal trunkCapacity,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) Boolean hasCruiseControl,
            @RequestParam(required = false) BigDecimal basePrice,
            @RequestParam(required = false) BigDecimal discountAmount,
            @RequestParam(required = false) Integer octaneNumber,
            @RequestParam(required = false) Boolean isOnSale,
            Model model) {

        Collection<VehicleConfiguration> list = vehicleConfigDAO.getAll();

        if (modelId != null)     list = list.stream().filter(vc->vc.getModel().getId().equals(modelId)).collect(Collectors.toList());
        if (engineVolume != null) list = list.stream().filter(vc->vc.getEngineVolume().equals(engineVolume)).collect(Collectors.toList());
        if (enginePower != null)  list = list.stream().filter(vc->vc.getEnginePower().equals(enginePower)).collect(Collectors.toList());
        if (fuelConsumption!= null) list=list.stream().filter(vc->vc.getFuelConsumption().equals(fuelConsumption)).collect(Collectors.toList());
        if (doorsCount != null)    list = list.stream().filter(vc->vc.getDoorsCount().equals(doorsCount)).collect(Collectors.toList());
        if (seatsCount != null)    list = list.stream().filter(vc->vc.getSeatsCount().equals(seatsCount)).collect(Collectors.toList());
        if (trunkCapacity!= null)  list=list.stream().filter(vc->vc.getTrunkCapacity().equals(trunkCapacity)).collect(Collectors.toList());
        if (transmission != null && !transmission.isEmpty()) list=list.stream().filter(vc->vc.getTransmission().equalsIgnoreCase(transmission)).collect(Collectors.toList());
        if (hasCruiseControl!= null) list=list.stream().filter(vc->vc.getHasCruiseControl().equals(hasCruiseControl)).collect(Collectors.toList());
        if (basePrice!= null)      list=list.stream().filter(vc->vc.getBasePrice().compareTo(basePrice)==0).collect(Collectors.toList());
        if (discountAmount!= null)  list=list.stream().filter(vc->vc.getDiscountAmount().compareTo(discountAmount)==0).collect(Collectors.toList());
        if (octaneNumber!= null)    list=list.stream().filter(vc->vc.getOctaneNumber().equals(octaneNumber)).collect(Collectors.toList());
        if (isOnSale!= null)        list=list.stream().filter(vc->vc.getIsOnSale().equals(isOnSale)).collect(Collectors.toList());

        model.addAttribute("vehicleConfigs", list);
        return "vehicleconfig_list";
    }

    @PostMapping("/manager/vehicle_configs/{id}/edit")
    public String updateVehicleConfig(
            @PathVariable Long id,
            @RequestParam Long modelId,
            @RequestParam BigDecimal engineVolume,
            @RequestParam Integer enginePower,
            @RequestParam BigDecimal fuelConsumption,
            @RequestParam Integer doorsCount,
            @RequestParam Integer seatsCount,
            @RequestParam BigDecimal trunkCapacity,
            @RequestParam String transmission,
            @RequestParam(defaultValue = "false") Boolean hasCruiseControl,
            @RequestParam BigDecimal basePrice,
            @RequestParam BigDecimal discountAmount,
            @RequestParam Integer octaneNumber,
            @RequestParam(defaultValue = "false") Boolean isOnSale) {

        VehicleConfiguration vc = vehicleConfigDAO.getById(id);
        if (vc == null) throw new IllegalArgumentException("Invalid VC id: " + id);

        // set fields
        vc.setModel(modelDAO.getById(modelId));
        vc.setEngineVolume(engineVolume);
        vc.setEnginePower(enginePower);
        vc.setFuelConsumption(fuelConsumption);
        vc.setDoorsCount(doorsCount);
        vc.setSeatsCount(seatsCount);
        vc.setTrunkCapacity(trunkCapacity);
        vc.setTransmission(transmission);
        vc.setHasCruiseControl(hasCruiseControl);
        vc.setBasePrice(basePrice);
        vc.setDiscountAmount(discountAmount);
        vc.setOctaneNumber(octaneNumber);
        vc.setIsOnSale(isOnSale);

        vehicleConfigDAO.updateOne(vc);
        return "redirect:/manager/vehicle_config_list";
    }

    @GetMapping("/manager/vehicle_configs/{id}/delete")
    public String deleteVehicleConfig(@PathVariable Long id) {
        vehicleConfigDAO.deleteById(id);
        return "redirect:/manager/vehicle_config_list";
    }

}
