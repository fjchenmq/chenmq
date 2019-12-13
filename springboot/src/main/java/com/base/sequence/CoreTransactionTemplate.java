package com.base.sequence;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.springframework.transaction.TransactionException;

import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class CoreTransactionTemplate extends TransactionTemplate {
    public CoreTransactionTemplate(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
        if(this.getTransactionManager() instanceof CallbackPreferringPlatformTransactionManager) {
            return ((CallbackPreferringPlatformTransactionManager)this.getTransactionManager()).execute(this, action);
        } else {
            TransactionStatus status = this.getTransactionManager().getTransaction(this);
            TransactionTemplateTransactionStatusContext.setCurrentTransactionContext(status);

            T result;
            try {
                result = action.doInTransaction(status);
            } catch (RuntimeException var10) {
                this.rollbackOnException(status, var10);
                throw var10;
            } catch (Error var11) {
                this.rollbackOnException(status, var11);
                throw var11;
            } catch (Throwable var12) {
                this.rollbackOnException(status, var12);
                throw new UndeclaredThrowableException(var12, "TransactionCallback threw undeclared checked exception");
            } finally {
                TransactionTemplateTransactionStatusContext.removeCurrentTransactionContext();
            }

            this.getTransactionManager().commit(status);
            return result;
        }
    }

    private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
        this.logger.debug("Initiating transaction rollback on application exception", ex);

        try {
            this.getTransactionManager().rollback(status);
        } catch (TransactionSystemException var4) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            var4.initApplicationException(ex);
            throw var4;
        } catch (RuntimeException var5) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            throw var5;
        } catch (Error var6) {
            this.logger.error("Application exception overridden by rollback error", ex);
            throw var6;
        }
    }
}
