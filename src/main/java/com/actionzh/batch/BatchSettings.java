package com.actionzh.batch;

public interface BatchSettings {

    String[] getColumns();

    void setColumns(String[] columns);

    Integer getWriterChunkSize();

    Integer getReaderPageSize();

}
