package com.dot.transfer.infrastructure.service;

import com.dot.transfer.account.saving.domain.SavingsAccount;
import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.product.charge.domain.Charge;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;

public class SavingsAccountTransactionProcessor implements ItemProcessor<SavingsAccountTransaction, SavingsAccountTransaction> {


    @Override
    public SavingsAccountTransaction process(SavingsAccountTransaction item) throws ObjectOptimisticLockingFailureException {
        BigDecimal total_charge = BigDecimal.ZERO;
        final SavingsAccount account = item.getSavingsAccount();


        final Charge charge = account.getCharge();

        BigDecimal fee = calculateTransactionFee(item.getAmount(), charge.getCap(), charge.getTransactionFee());
        BigDecimal commission = calculateCommission(fee, charge.getCommission());


        total_charge = fee.add(commission);

        item.setTotalCharges(total_charge);
        item.setCommission(commission);
        item.setTransactionFee(fee);
        item.setAmount(item.getAmount().subtract(total_charge));

        account.setBalance(account.getBalance().subtract(total_charge));

        final BigDecimal deposit = account.calculateTotalDeposit();
        final BigDecimal withdrawal  = account.calculateTotalWithdrawal();

        account.setBalance(deposit.subtract(withdrawal));
        account.setTotalDeposit(deposit);
        account.setTotalWithdrawal(withdrawal);

        return item;
    }

    // Method to calculate the transaction fee
    public static BigDecimal calculateTransactionFee(BigDecimal originalAmount, BigDecimal cap, BigDecimal transactionFee) {
        BigDecimal fee = originalAmount.multiply(transactionFee.divide(BigDecimal.valueOf(100)));
        return fee.min(cap);
    }

    // Method to calculate the commission
    public static BigDecimal calculateCommission(BigDecimal transactionFee, BigDecimal commission) {
        return transactionFee.multiply(commission.divide(BigDecimal.valueOf(100)));
    }
}
