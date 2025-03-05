-- Создание ENUM-типов
CREATE TYPE order_status AS ENUM ('InProcessing', 'InExecution', 'CarInDealership', 'InTestDrive', 'Completed', 'Canceled');


CREATE TYPE test_drive_status AS ENUM ('InProcessing', 'Running', 'Finished', 'Canceled', 'FinishedAfterCrash');


CREATE TYPE car_status AS ENUM ('InProcessing', 'InManufacture', 'WaitingForDelivery', 'InDelivery', 'CarInDealership', 'InTestDrive', 'Crashed', 'Sold');



-- Таблица клиентов
CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address TEXT,
    phone_number VARCHAR(20),
    passport VARCHAR(50) UNIQUE,
    driving_license VARCHAR(50) UNIQUE,
    password_hash TEXT NOT NULL
);

-- Таблица менеджеров
CREATE TABLE manager (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    password_hash TEXT NOT NULL
);

-- Таблица производителей
CREATE TABLE manufacturer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- Таблица марок
CREATE TABLE brand (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    manufacturer_id INT REFERENCES manufacturer(id) ON DELETE CASCADE,
    description TEXT,
    country VARCHAR(100)
);

-- Таблица моделей
CREATE TABLE model (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand_id INT REFERENCES brand(id) ON DELETE CASCADE,
    year INT,
    additional_info JSONB
);

-- Таблица конфигураций автомобилей
CREATE TABLE vehicle_configuration (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    model_id INT REFERENCES model(id) ON DELETE CASCADE,
    engine_volume DECIMAL(4,2),
    engine_power INT,
    fuel_consumption DECIMAL(4,2),
    doors_count INT,
    seats_count INT,
    trunk_capacity DECIMAL(6,2),
    transmission VARCHAR(50),
    has_cruise_control BOOLEAN,
    base_price DECIMAL(10,2),
    discount_amount DECIMAL(10,2) DEFAULT 0,
    octane_number INT,
    additional_info JSONB,
    is_on_sale BOOLEAN DEFAULT TRUE
);

-- Таблица автомобилей
CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    config_id INT REFERENCES vehicle_configuration(id) ON DELETE CASCADE,
    date_of_creation DATE NOT NULL,
    VIN VARCHAR(50) UNIQUE NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    mileage INT DEFAULT 0,
    date_last_TO DATE,
    color VARCHAR(50),
    seat VARCHAR(50),
    additional_client_characteristics JSONB,
    additional_car_characteristics JSONB,
    car_status car_status NOT NULL DEFAULT 'InProcessing'
);

-- Таблица тест-драйвов
CREATE TABLE test_drive_schedule (
    id SERIAL PRIMARY KEY,
    car_id INT REFERENCES car(id) ON DELETE CASCADE,
    test_drive_status test_drive_status NOT NULL DEFAULT 'InProcessing',
    date_time_start TIMESTAMP NOT NULL,
    date_time_end TIMESTAMP NOT NULL,
    manager_id INT REFERENCES manager(id) ON DELETE SET NULL,
    client_id INT REFERENCES client(id) ON DELETE CASCADE
);

-- Таблица заказов
CREATE TABLE "order" (
    id SERIAL PRIMARY KEY,
    order_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id INT REFERENCES client(id) ON DELETE CASCADE,
    manager_id INT REFERENCES manager(id) ON DELETE SET NULL,
    needs_pre_test_drive BOOLEAN DEFAULT FALSE,
    test_drive_id INT REFERENCES test_drive_schedule(id) ON DELETE SET NULL,
    order_status order_status NOT NULL DEFAULT 'InProcessing',
    car_id INT REFERENCES car(id) ON DELETE CASCADE
);