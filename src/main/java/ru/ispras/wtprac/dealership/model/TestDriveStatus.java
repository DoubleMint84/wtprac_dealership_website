package ru.ispras.wtprac.dealership.model;

public enum TestDriveStatus {
    InProcessing,
    Running,
    Finished,
    Canceled,
    FinishedAfterCrash;

    public boolean isCanceled() {
        return TestDriveStatus.Canceled.equals(this);
    }
}
