package com.actionzh.batch.export.contact;

import com.actionzh.batch.export.ExportBatchSettings;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Created by wangding on 05/07/2017.
 */
@Data
@Validated
public class ContactExportBatchSettings implements ExportBatchSettings {

    @NotNull
    private String[] columns;
    @NotNull
    private Integer writerChunkSize;
    @NotNull
    private Integer readerPageSize;

}
