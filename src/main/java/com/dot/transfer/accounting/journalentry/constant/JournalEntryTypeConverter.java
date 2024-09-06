package com.dot.transfer.accounting.journalentry.constant;

import io.hypersistence.utils.hibernate.type.basic.CustomOrdinalEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class JournalEntryTypeConverter extends CustomOrdinalEnumConverter<JournalEntryType> {
    public JournalEntryTypeConverter() {
        super(JournalEntryType.class);
    }

    @Override
    public Integer convertToDatabaseColumn(JournalEntryType journalEntryType) {
        return journalEntryType.getValue();
    }
}
