package ru.ryzhukvlad.entity.dto;

import ru.ryzhukvlad.entity.Record;

import java.util.List;

public record RecordsContainerDto(String userName, List<Record> records, int numberOfDoneRecords,
                                  int numberOfActiveRecords) {
}
