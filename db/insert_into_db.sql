-- Вставка данных в таблицу Manufacturer
INSERT INTO Manufacturer (Name, Description) VALUES
('Toyota', 'Японский производитель автомобилей'),
('BMW Group', 'Немецкий автопроизводитель премиум-класса'),
('Ford', 'Американская автомобильная компания'),
('Hyundai', 'Южнокорейский производитель автомобилей'),
('Tesla', 'Производитель электромобилей');

-- Вставка данных в таблицу Brand
SELECT * FROM Brand;
INSERT INTO Brand (Name, Description, ManufacturerId, Country) VALUES
('Toyota', 'Японский бренд автомобилей', 1, 'Япония'),
('BMW', 'Бренд премиальных автомобилей', 2, 'Германия'),
('Ford', 'Американский автопроизводитель', 3, 'США'),
('Hyundai', 'Южнокорейский бренд автомобилей', 4, 'Южная Корея'),
('Tesla', 'Производитель электрокаров', 5, 'США');

-- Вставка данных в таблицу Model
INSERT INTO Model (Name, BrandId, Year) VALUES
('Corolla', 1, 2022),
('X5', 2, 2023),
('Mustang', 3, 2021),
('Elantra', 4, 2022),
('Model S', 5, 2023);

-- Вставка данных в таблицу VehicleConfiguration
INSERT INTO VehicleConfiguration (Name, ModelId, EngineVolume, EnginePower, FuelConsumption, DoorsCount, SeatsCount, TrunkCapacity, Transmission, HasCruiseControl, BasePrice, DiscountAmount, OctaneNumber, AdditionalInfo, IsOnSale) VALUES
('Corolla 1.8L', 1, 1.8, 140, 6.5, 4, 5, 450, 'CVT', TRUE, 25000, 1000, 95, '{"features": ["ABS", "ESP"]}', TRUE),
('BMW X5 3.0L', 2, 3.0, 300, 9.5, 5, 5, 650, 'Automatic', TRUE, 70000, 5000, 98, '{"features": ["Sport Mode", "Heated Seats"]}', TRUE),
('Ford Mustang 5.0L', 3, 5.0, 450, 12.0, 2, 4, 380, 'Manual', FALSE, 55000, 2000, 100, '{"features": ["Launch Control", "Track Mode"]}', TRUE),
('Hyundai Elantra 2.0L', 4, 2.0, 160, 7.0, 4, 5, 500, 'Automatic', TRUE, 27000, 1500, 95, '{"features": ["LED Lights", "Sunroof"]}', TRUE),
('Tesla Model S Plaid', 5, NULL, 1020, NULL, 4, 5, 750, 'Automatic', TRUE, 120000, 10000, NULL, '{"features": ["Autopilot", "Ludicrous Mode"]}', TRUE);

-- Вставка данных в таблицу Car
INSERT INTO Car (Name, ConfigId, DateOfCreation, VIN, Price, Mileage, DateLastTO, Color, Seat, AdditionalClientCharacteristics, AdditionalCarCharacteristics, CarStatus) VALUES
('Toyota Corolla White', 1, '2023-01-10', 'JTDBE32K603218947', 24000, 1000, '2024-01-10', 'White', 'Fabric', '{"customFeatures": ["Alloy Wheels"]}', '{"serviceHistory": ["Oil Change"]}', 'CarInDealership'),
('BMW X5 Black', 2, '2023-02-15', 'WBANV93549C139876', 68000, 500, '2024-02-15', 'Black', 'Leather', '{"customFeatures": ["Tinted Windows"]}', '{"serviceHistory": ["Brake Check"]}', 'CarInDealership'),
('Ford Mustang Red', 3, '2023-03-20', '1FAFP45X5YF206781', 53000, 2000, '2024-03-20', 'Red', 'Leather', '{"customFeatures": ["Sports Exhaust"]}', '{"serviceHistory": ["New Tires"]}', 'InTestDrive'),
('Hyundai Elantra Blue', 4, '2023-04-05', 'KMHDN46D24U136245', 26000, 800, '2024-04-05', 'Blue', 'Fabric', '{"customFeatures": ["Rear Camera"]}', '{"serviceHistory": ["Battery Check"]}', 'CarInDealership'),
('Tesla Model S White', 5, '2023-05-12', '5YJSA1E26MF168239', 110000, 300, '2024-05-12', 'White', 'Vegan Leather', '{"customFeatures": ["Performance Upgrade"]}', '{"serviceHistory": ["Software Update"]}', 'Sold');

-- Вставка данных в таблицу Client
INSERT INTO Client (Name, Email, Address, PhoneNumber, Passport, DrivingLicense, PasswordHash) VALUES
('Иван Иванов', 'ivan.ivanov@example.com', 'Москва, ул. Ленина, 10', '+79031234567', '1234567890', '77 12 345678', 'password1'),
('Петр Петров', 'petr.petrov@example.com', 'Санкт-Петербург, Невский пр., 20', '+79165554433', '0987654321', '78 45 987654', 'password2'),
('Сергей Смирнов', 'sergey.smirnov@example.com', 'Екатеринбург, ул. Чапаева, 15', '+79218887766', '1122334455', '66 33 112233', 'password3'),
('Анна Козлова', 'anna.kozlova@example.com', 'Новосибирск, ул. Мира, 30', '+79335556677', '6677889900', '54 78 667788', 'password4'),
('Елена Сидорова', 'elena.sidorova@example.com', 'Казань, ул. Пушкина, 25', '+79443332211', '5566778899', '16 91 556677', 'password5');

-- Вставка данных в таблицу Manager
INSERT INTO Manager (Name, Email, PhoneNumber, PasswordHash) VALUES
('Алексей Соколов', 'alexey.sokolov@example.com', '+79051112233', 'password1'),
('Дмитрий Орлов', 'dmitry.orlov@example.com', '+79168889900', 'password2'),
('Мария Белова', 'maria.belova@example.com', '+79225557788', 'password3'),
('Виктор Иванченко', 'victor.ivanchenco@example.com', '+79336668899', 'password4'),
('Ольга Карпова', 'olga.karpova@example.com', '+79447779900', 'password5');

-- Вставка данных в таблицу TestDriveSchedule
INSERT INTO TestDriveSchedule (CarId, TestDriveStatus, DateTimeStart, DateTimeEnd, ManagerId, ClientId) VALUES
(3, 'Running', '2025-02-01 10:00:00', '2025-02-01 11:00:00', 1, 1),
(4, 'Finished', '2025-02-05 14:00:00', '2025-02-05 15:00:00', 2, 2),
(1, 'Canceled', '2025-02-10 09:30:00', '2025-02-10 10:30:00', 3, 3),
(2, 'InProcessing', '2025-02-15 16:00:00', '2025-02-15 17:00:00', 4, 4),
(5, 'FinishedAfterCrash', '2025-02-20 12:00:00', '2025-02-20 13:00:00', 5, 5);
