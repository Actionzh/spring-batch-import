package com.actionzh.batch.dto;

import com.actionzh.enums.ImportExportStatusEnum;
import com.actionzh.enums.ImportExportTypeEnum;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ImportExportTaskDTO {

    private static final long serialVersionUID = 4515165806438706891L;

    private ImportExportTypeEnum type;
    private Long jobId;
    private Long instanceId;
    private ImportExportStatusEnum status;
    private String fileName;
    private String uuid;
    private int successCount;
    private int failureCount;

}
