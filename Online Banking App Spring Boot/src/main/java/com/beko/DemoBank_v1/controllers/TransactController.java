package com.beko.DemoBank_v1.controllers;

import com.beko.DemoBank_v1.models.PaymentRequest;
import com.beko.DemoBank_v1.models.TransferRequest;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.AccountRepository;
import com.beko.DemoBank_v1.repository.PaymentRepository;
import com.beko.DemoBank_v1.repository.TransactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactRepository transactRepository;

    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestBody Map<String, String> requestMap, HttpSession session) {
        String depositAmount = requestMap.get("deposit_amount");
        String accountID = requestMap.get("account_id");

        if (depositAmount.isEmpty() || accountID.isEmpty()) {
            return ResponseEntity.badRequest().body("Deposit amount and account ID cannot be empty.");
        }

        User user = (User) session.getAttribute("user");
        String user_id = user.getUser_id();
        int acc_id = Integer.parseInt(accountID);
        double depositAmountValue = Double.parseDouble(depositAmount);

        if (depositAmountValue == 0) {
            return ResponseEntity.badRequest().body("Deposit amount cannot be zero.");
        }

        Double currentBalance = accountRepository.getAccountBalance(user_id, acc_id);
        double balance = currentBalance != null ? currentBalance : 0.0;
        double newBalance = balance + depositAmountValue;

        accountRepository.changeAccountsBalanceById(newBalance, acc_id);
        transactRepository.logTransaction(acc_id, "deposit", depositAmountValue, "online", "success", "Deposit Transaction Successfull", LocalDateTime.now());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Amount Deposited Successfully.");
        response.put("accounts", accountRepository.getUserAccountsById(user_id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity transfer(@RequestBody TransferRequest request, HttpSession session) {
        String transfer_from = request.getSourceAccount();
        String transfer_to = request.getTargetAccount();
        String transfer_amount = request.getAmount();

        if (transfer_from.isEmpty() || transfer_to.isEmpty() || transfer_amount.isEmpty()) {
            return ResponseEntity.badRequest().body("The account transferring from and to along with the amount cannot be empty!");
        }

        int transferFromId = Integer.parseInt(transfer_from);
        int transferToId = Integer.parseInt(transfer_to);
        double transferAmount = Double.parseDouble(transfer_amount);

        if (transferFromId == transferToId) {
            return ResponseEntity.badRequest().body("Cannot Transfer Into The Same Account.");
        }

        if (transferAmount == 0) {
            return ResponseEntity.badRequest().body("Cannot Transfer an amount of 0.");
        }

        User user = (User) session.getAttribute("user");
        String user_id = user.getUser_id();

        Double currentBalanceFrom = accountRepository.getAccountBalance(user_id, transferFromId);
        double balanceFrom = currentBalanceFrom != null ? currentBalanceFrom : 0.0;

        if (balanceFrom < transferAmount) {
            transactRepository.logTransaction(transferFromId, "transfer", transferAmount, "online", "failed", "Insufficient funds.", LocalDateTime.now());
            return ResponseEntity.badRequest().body("You have insufficient Funds to perform this transfer.");
        }

        Double currentBalanceTo = accountRepository.getAccountBalance(user_id, transferToId);
        double balanceTo = currentBalanceTo != null ? currentBalanceTo : 0.0;
        double newBalanceFrom = balanceFrom - transferAmount;
        double newBalanceTo = balanceTo + transferAmount;

        accountRepository.changeAccountsBalanceById(newBalanceFrom, transferFromId);
        accountRepository.changeAccountsBalanceById(newBalanceTo, transferToId);
        transactRepository.logTransaction(transferFromId, "Transfer", transferAmount, "online", "success", "Transfer Transaction Successfull", LocalDateTime.now());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer completed successfully.");
        response.put("accounts", accountRepository.getUserAccountsById(user_id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity withdraw(@RequestBody Map<String, String> requestMap, HttpSession session) {
        String withdrawalAmount = requestMap.get("withdrawal_amount");
        String accountId = requestMap.get("account_id");

        if (withdrawalAmount.isEmpty() || accountId.isEmpty()) {
            return ResponseEntity.badRequest().body("Account withdrawing from and withdrawal amount cannot be empty!");
        }

        int account_id = Integer.parseInt(accountId);
        double withdrawal_amount = Double.parseDouble(withdrawalAmount);

        if (withdrawal_amount == 0) {
            return ResponseEntity.badRequest().body("Withdrawal amount cannot be 0 value.");
        }

        User user = (User) session.getAttribute("user");
        String user_id = user.getUser_id();
        Double currentBalance = accountRepository.getAccountBalance(user_id, account_id);
        double balance = currentBalance != null ? currentBalance : 0.0;

        if (balance < withdrawal_amount) {
            transactRepository.logTransaction(account_id, "withdrawal", withdrawal_amount, "online", "failed", "Insufficient funds.", LocalDateTime.now());
            return ResponseEntity.badRequest().body("You have insufficient Funds to perform this withdrawal.");
        }

        double newBalance = balance - withdrawal_amount;
        accountRepository.changeAccountsBalanceById(newBalance, account_id);
        transactRepository.logTransaction(account_id, "Withdrawal", withdrawal_amount, "online", "success", "Withdrawal Transaction Successfull", LocalDateTime.now());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Withdrawal Successfull!");
        response.put("accounts", accountRepository.getUserAccountsById(user_id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    public ResponseEntity payment(@RequestBody PaymentRequest request, HttpSession session) {
        String beneficiary = request.getBeneficiary();
        String account_number = request.getAccount_number();
        String account_id = request.getAccount_id();
        String reference = request.getReference();
        String payment_amount = request.getPayment_amount();

        if (beneficiary.isEmpty() || account_number.isEmpty() || account_id.isEmpty() || payment_amount.isEmpty()) {
            return ResponseEntity.badRequest().body("Beneficiary, account number, account paying from and payment amount cannot be empty.");
        }

        int accountID = Integer.parseInt(account_id);
        double paymentAmount = Double.parseDouble(payment_amount);

        if (paymentAmount == 0) {
            return ResponseEntity.badRequest().body("Payment amount cannot be 0.");
        }

        User user = (User) session.getAttribute("user");
        String user_id = user.getUser_id();
        Double currentBalance = accountRepository.getAccountBalance(user_id, accountID);
        double balance = currentBalance != null ? currentBalance : 0.0;

        if (balance < paymentAmount) {
            String reasonCode = "Could not process payment due to insufficient funds.";
            paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "failed", reasonCode, LocalDateTime.now());
            transactRepository.logTransaction(accountID, "Payment", paymentAmount, "online", "failed", "Insufficient funds.", LocalDateTime.now());
            return ResponseEntity.badRequest().body("You have insufficient Funds to perform this payment.");
        }

        double newBalance = balance - paymentAmount;
        accountRepository.changeAccountsBalanceById(newBalance, accountID);

        String reasonCode = "Payment Processed Successfully!";
        paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "success", reasonCode, LocalDateTime.now());
        transactRepository.logTransaction(accountID, "Payment", paymentAmount, "online", "success", "Payment Transaction Successfull", LocalDateTime.now());

        Map<String, Object> response = new HashMap<>();
        response.put("message", reasonCode);
        response.put("accounts", accountRepository.getUserAccountsById(user_id));
        return ResponseEntity.ok(response);
    }
}