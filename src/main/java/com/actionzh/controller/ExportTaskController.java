package com.actionzh.controller;

import com.actionzh.batch.launcher.ContactExportJobLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/export")
public class ExportTaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportTaskController.class);

    @Autowired
    private ContactExportJobLauncher contactExportJobLauncher;

    @PostMapping(value = "/contact", produces = "application/text")
    @ResponseBody
    public String create(@RequestParam(value = "search", required = false) String search,
                         @RequestParam(value = "groupId", required = false) Long groupId,
                         @RequestBody(required = false) String fields) throws Exception {
        LOGGER.info(">>> request export contact groupId [{}] searchTerm [{}] fields [{}]", groupId, search, fields);
        return contactExportJobLauncher.launch(search, groupId, fields);
    }
}
