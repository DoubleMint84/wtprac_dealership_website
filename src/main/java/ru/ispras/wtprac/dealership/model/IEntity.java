package ru.ispras.wtprac.dealership.model;

public interface IEntity<Id> {
    public Id getId();
    public void setId(Id id);
}
