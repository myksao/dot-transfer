package com.dot.transfer.infrastructure.configuration;


import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.account.saving.repo.SavingsAccountRepo;
import com.dot.transfer.account.saving.repo.SavingsAccountTransactionRepo;
import com.dot.transfer.accounting.journalentry.constant.JournalEntryType;
import com.dot.transfer.accounting.journalentry.dto.PostJournalEntryReqDTO;
import com.dot.transfer.accounting.journalentry.service.JournalEntryService;
import com.dot.transfer.infrastructure.service.SavingsAccountTransactionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final JournalEntryService journalEntryService;
    private final SavingsAccountRepo savingsAccountRepo;
    private final SavingsAccountTransactionRepo transactionRepo;
    private final PlatformTransactionManager jpaTransactionManager;

    @Bean
    public RepositoryItemReader<SavingsAccountTransaction> reader(){
        return new RepositoryItemReaderBuilder<SavingsAccountTransaction>().name("item_reader")
                .repository(transactionRepo)
                .pageSize(50)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .methodName("fetchSuccessfulTransactions")
                .arguments(false)
                .build();
    }


    @Bean
    public SavingsAccountTransactionProcessor savingsAccountTransactionProcessor(){
        return new SavingsAccountTransactionProcessor();
    }



    @Bean
    public RepositoryItemWriter<SavingsAccountTransaction> writer(){
        RepositoryItemWriter<SavingsAccountTransaction> writer = new RepositoryItemWriter<>();
        writer.setRepository(transactionRepo);
        writer.setMethodName("save");

        return writer;
    }


    @Bean
    public Step runStep() {
        return new StepBuilder("run_step", jobRepository)
                .<SavingsAccountTransaction, SavingsAccountTransaction>chunk(50, jpaTransactionManager)
                .reader(reader())
                .processor(savingsAccountTransactionProcessor())
                .writer(writer())
                .listener(new ItemWriteListener<SavingsAccountTransaction>() {
                    @Override
                    public void beforeWrite(Chunk<? extends SavingsAccountTransaction> items) {
                        ItemWriteListener.super.beforeWrite(items);
                    }
                    @Override
                    public void afterWrite(Chunk<? extends SavingsAccountTransaction> items) {
                        ItemWriteListener.super.afterWrite(items);
                        for (SavingsAccountTransaction transaction : items) {
                            journalEntryService.postJournalEntry(new PostJournalEntryReqDTO(
                                    JournalEntryType.DEBIT,
                                    transaction.getId(),
                                    "Charges (Commission)",
                                    transaction.getCommission(),
                                    new Date()
                            ));

                            journalEntryService.postJournalEntry(new PostJournalEntryReqDTO(
                                    JournalEntryType.DEBIT,
                                    transaction.getId(),
                                    "Charges (Transaction Fee)",
                                    transaction.getTransactionFee(),
                                    new Date()
                            ));

                            savingsAccountRepo.save(transaction.getSavingsAccount());
                        }
                    }

                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends SavingsAccountTransaction> items) {
                        ItemWriteListener.super.onWriteError(exception, items);
                    }
                })
                .faultTolerant()
                .retry(ObjectOptimisticLockingFailureException.class)
                .retryLimit(2)
                .taskExecutor(taskExecutor())
                .build();
    }


    @Bean
    public Job job() {
        return new JobBuilder("run_job", jobRepository)
                .start(runStep())
                .build();
    }



    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(20);
        return taskExecutor;
    }



}
