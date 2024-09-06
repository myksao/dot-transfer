package com.dot.transfer.accounting.journalentry.repo;

import com.dot.transfer.accounting.journalentry.domain.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JournalEntryRepo extends JpaRepository<JournalEntry, UUID> {
}
