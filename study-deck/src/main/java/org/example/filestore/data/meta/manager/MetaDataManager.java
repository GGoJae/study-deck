package org.example.filestore.data.meta.manager;

import org.example.filestore.shared.Transactionable;
import org.example.filestore.shared.model.Focus;

public interface MetaDataManager extends Transactionable {

    Focus getFocus();

    Long nextCategoryId();
}
