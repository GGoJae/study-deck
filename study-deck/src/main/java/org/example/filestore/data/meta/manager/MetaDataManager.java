package org.example.filestore.data.meta.manager;

import org.example.filestore.shared.Transactionable;
import org.example.filestore.shared.model.Focus;

import java.io.IOException;

public interface MetaDataManager extends Transactionable {

    Focus getFocus() throws IOException;

    Long nextCategoryId() throws IOException;
}
