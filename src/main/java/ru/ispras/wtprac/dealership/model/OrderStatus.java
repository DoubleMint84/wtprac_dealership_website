package ru.ispras.wtprac.dealership.model;

public enum OrderStatus {
    InProcessing,
    InExecution,
    CarInDealership,
    InTestDrive,
    Completed,
    Canceled;

    public boolean isCanceled() {
        return OrderStatus.Canceled.equals(this);
    }
}
