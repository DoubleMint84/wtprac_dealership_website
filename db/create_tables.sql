-- Создание ENUM-типов
CREATE TYPE OrderStatus AS ENUM ('InProcessing', 'InExecution', 'CarInDealership', 'InTestDrive', 'Completed', 'Canceled');


CREATE TYPE TestDriveStatus AS ENUM ('InProcessing', 'Running', 'Finished', 'Canceled', 'FinishedAfterCrash');


CREATE TYPE CarStatus AS ENUM ('InProcessing', 'InManufacture', 'WaitingForDelivery', 'InDelivery', 'CarInDealership', 'InTestDrive', 'Crashed', 'Sold');



-- Таблица клиентов
CREATE TABLE Client (
    Id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    Address TEXT,
    PhoneNumber VARCHAR(20),
    Passport VARCHAR(50) UNIQUE,
    DrivingLicense VARCHAR(50) UNIQUE,
    PasswordHash TEXT NOT NULL
);

-- Таблица менеджеров
CREATE TABLE Manager (
    Id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(20),
    PasswordHash TEXT NOT NULL
);

-- Таблица производителей
CREATE TABLE Manufacturer (
    Id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Description TEXT
);

-- Таблица марок
CREATE TABLE Brand (
    Id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    ManufacturerId INT REFERENCES Manufacturer(Id) ON DELETE CASCADE,
    Description TEXT,
    Country VARCHAR(100)
);

-- Таблица моделей
CREATE TABLE Model (
    Id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    BrandId INT REFERENCES Brand(Id) ON DELETE CASCADE,
    Year INT,
    AdditionalInfo JSONB
);

-- Таблица конфигураций автомобилей
CREATE TABLE VehicleConfiguration (
    Id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    ModelId INT REFERENCES Model(Id) ON DELETE CASCADE,
    EngineVolume DECIMAL(4,2),
    EnginePower INT,
    FuelConsumption DECIMAL(4,2),
    DoorsCount INT,
    SeatsCount INT,
    TrunkCapacity DECIMAL(6,2),
    Transmission VARCHAR(50),
    HasCruiseControl BOOLEAN,
    BasePrice DECIMAL(10,2),
    DiscountAmount DECIMAL(10,2) DEFAULT 0,
    OctaneNumber INT,
    AdditionalInfo JSONB,
    IsOnSale BOOLEAN DEFAULT TRUE
);

-- Таблица автомобилей
CREATE TABLE Car (
    Id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    ConfigId INT REFERENCES VehicleConfiguration(Id) ON DELETE CASCADE,
    DateOfCreation DATE NOT NULL,
    VIN VARCHAR(50) UNIQUE NOT NULL,
    Price DECIMAL(10,2) NOT NULL,
    Mileage INT DEFAULT 0,
    DateLastTO DATE,
    Color VARCHAR(50),
    Seat VARCHAR(50),
    AdditionalClientCharacteristics JSONB,
    AdditionalCarCharacteristics JSONB,
    CarStatus CarStatus NOT NULL DEFAULT 'InProcessing'
);

-- Таблица тест-драйвов
CREATE TABLE TestDriveSchedule (
    Id SERIAL PRIMARY KEY,
    CarId INT REFERENCES Car(Id) ON DELETE CASCADE,
    TestDriveStatus TestDriveStatus NOT NULL DEFAULT 'InProcessing',
    DateTimeStart TIMESTAMP NOT NULL,
    DateTimeEnd TIMESTAMP NOT NULL,
    ManagerId INT REFERENCES Manager(Id) ON DELETE SET NULL,
    ClientId INT REFERENCES Client(Id) ON DELETE CASCADE
);

-- Таблица заказов
CREATE TABLE "Order" (
    Id SERIAL PRIMARY KEY,
    OrderDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ClientId INT REFERENCES Client(Id) ON DELETE CASCADE,
    ManagerId INT REFERENCES Manager(Id) ON DELETE SET NULL,
    NeedsPreTestDrive BOOLEAN DEFAULT FALSE,
    TestDriveId INT REFERENCES TestDriveSchedule(Id) ON DELETE SET NULL,
    OrderStatus OrderStatus NOT NULL DEFAULT 'InProcessing',
    CarId INT REFERENCES Car(Id) ON DELETE CASCADE
);