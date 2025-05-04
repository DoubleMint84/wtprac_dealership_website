package ru.ispras.wtprac.dealership.model;

public enum CarStatus {
    InProcessing,
    InManufacture,
    WaitingForDelivery,
    InDelivery,
    CarInDealership,
    InTestDrive,
    Crashed,
    Sold;

    public boolean isAvailable() {
        return this == CarInDealership || this == InTestDrive;
    }
}
