package com.actionzh.batch.importer;

import com.actionzh.batch.BatchSettings;

public interface ImportBatchSettings extends BatchSettings {

    Integer getLinesToSkip();
}
