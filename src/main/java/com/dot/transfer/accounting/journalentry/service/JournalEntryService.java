package com.dot.transfer.accounting.journalentry.service;

import com.dot.transfer.accounting.journalentry.dto.CreateJournalEntryReqDTO;
import com.dot.transfer.accounting.journalentry.dto.CreateJournalEntryResDTO;
import com.dot.transfer.accounting.journalentry.dto.PostJournalEntryReqDTO;
import com.dot.transfer.infrastructure.common.PlatformException;

public interface JournalEntryService {
    CreateJournalEntryResDTO createJournalEntry(CreateJournalEntryReqDTO createJournalEntryReqDto);
    void postJournalEntry(PostJournalEntryReqDTO dto) throws PlatformException;
}
