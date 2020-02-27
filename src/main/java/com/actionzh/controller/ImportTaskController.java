package com.actionzh.controller;

import com.actionzh.batch.importer.contact.ContactTemplateGenerator;
import com.actionzh.batch.launcher.ContactImportJobLauncher;
import com.actionzh.batch.launcher.ContactUpdateJobLauncher;
import com.actionzh.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/import")
public class ImportTaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportTaskController.class);

    @Autowired
    private AppConfig appConfig;

    private static final String UPLOADED_FOLDER = System.getProperty("java.io.tmpdir");

    /*@Autowired
    private ImportExportTaskService importExportTaskService;

    @Autowired
    private ImportExportTaskFileService importExportTaskFileService;
*/
    @Autowired
    private ContactTemplateGenerator contactTemplateGenerator;

    @Autowired
    private ContactImportJobLauncher contactImportJobLauncher;

    @Autowired
    private ContactUpdateJobLauncher contactUpdateJobLauncher;

    @PostMapping(value = "/contact", produces = "application/json")
    @ResponseBody
    public String importContact(@RequestParam(value = "file") MultipartFile uploadfile,
                                @RequestParam(value = "source", required = false) String source,
                                @RequestParam(value = "campaign", required = false) String campaign,
                                @RequestParam(value = "term", required = false) String term,
                                @RequestParam(value = "medium", required = false) String medium,
                                @RequestParam(value = "content", required = false) String content,
                                @RequestParam(value = "groupId", required = false) Long groupId) throws Exception {
        LOGGER.info(">>> request import contact filename [{}], source [{}] groupId [{}]", uploadfile.getOriginalFilename(), source, groupId);
        /*Long count = importExportTaskService.count(ImportExportStatusEnum.STARTED, ImportExportTypeEnum.IMPORT);
        if (count.intValue() >= appConfig.getImportTaskMaxNum()) {
            throw new Exception("");
        }*/
        if (uploadfile.isEmpty()) {
            throw new Exception("isEmpty");
        }
        Path path;
        try {
            path = saveUploadedFile(uploadfile);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return contactImportJobLauncher.launch(path.toAbsolutePath().toString(), uploadfile.getOriginalFilename(), source, campaign, medium, term, content, groupId);
    }

    private Path saveUploadedFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Path path = Paths.get(UPLOADED_FOLDER + File.separator + new SimpleIdGenerator().generateId() + suffixName);
        return Files.write(path, bytes);
    }


}
