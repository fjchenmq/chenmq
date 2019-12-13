package com.base.sequence;

import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.TransactionStatus;

/**
 * Created by Administrator on 2019/11/22.
 */

public abstract class TransactionTemplateTransactionStatusContext {
    private static final ThreadLocal<TransactionStatus> TRANSACTION_TEMPLATE_CONTEXT = new NamedThreadLocal("Transaction Template Context");

    public TransactionTemplateTransactionStatusContext() {
    }

    public static void setCurrentTransactionContext(TransactionStatus transactionStatus) {
        TRANSACTION_TEMPLATE_CONTEXT.set(transactionStatus);
    }

    public static void removeCurrentTransactionContext() {
        TRANSACTION_TEMPLATE_CONTEXT.remove();
    }

    public static TransactionStatus getCurrentTransactionContext() {
        return (TransactionStatus)TRANSACTION_TEMPLATE_CONTEXT.get();
    }
}
