package ru.ryzhukvlad.entity.dto;

import ru.ryzhukvlad.entity.Record;
import ru.ryzhukvlad.entity.UserRole;

import java.util.List;

public class RecordsContainerDto {
    private final String userName;
    private final UserRole userRole;
    private final List<Record> records;
    private final int numberOfDoneRecords;
    private final int numberOfActiveRecords;

    public RecordsContainerDto(String userName, UserRole userRole, List<Record> records, int numberOfDoneRecords, int numberOfActiveRecords) {
        this.userName = userName;
        this.userRole = userRole;
        this.records = records;
        this.numberOfDoneRecords = numberOfDoneRecords;
        this.numberOfActiveRecords = numberOfActiveRecords;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole.name();
    }

    public List<Record> getRecords() {
        return records;
    }

    public int getNumberOfDoneRecords() {
        return numberOfDoneRecords;
    }

    public int getNumberOfActiveRecords() {
        return numberOfActiveRecords;
    }
}
