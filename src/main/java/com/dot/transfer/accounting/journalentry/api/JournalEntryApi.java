package com.dot.transfer.accounting.journalentry.api;


import com.dot.transfer.accounting.journalentry.dto.CreateJournalEntryReqDTO;
import com.dot.transfer.accounting.journalentry.dto.CreateJournalEntryResDTO;
import com.dot.transfer.accounting.journalentry.service.JournalEntryService;
import com.dot.transfer.infrastructure.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/entries")
@Slf4j
@RequiredArgsConstructor
public class JournalEntryApi {

    private JournalEntryService journalEntryService;

    @Autowired
    public JournalEntryApi(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CreateJournalEntryResDTO>> createJournalEntry(@Valid @RequestBody CreateJournalEntryReqDTO createJournalEntryReqDto) {
        log.info("Journal Entry Created");
        return ResponseEntity.ok(ApiResponse.success(this.journalEntryService.createJournalEntry(createJournalEntryReqDto)));
    }
}
