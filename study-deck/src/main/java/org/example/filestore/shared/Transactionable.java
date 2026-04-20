package org.example.filestore.shared;

public interface Transactionable {
    void transaction();
    void commit();
    void rollback();
}
