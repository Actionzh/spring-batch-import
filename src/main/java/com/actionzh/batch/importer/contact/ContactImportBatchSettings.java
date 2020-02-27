package com.actionzh.batch.importer.contact;

import com.actionzh.batch.importer.ImportBatchSettings;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class ContactImportBatchSettings implements ImportBatchSettings {

    @NotNull
    private Integer linesToSkip;
    @NotNull
    private Integer writerChunkSize;
    @NotNull
    private Integer readerPageSize;
    @NotNull
    private String[] columns;

}
