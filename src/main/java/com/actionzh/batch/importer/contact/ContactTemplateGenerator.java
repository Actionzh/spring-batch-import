package com.actionzh.batch.importer.contact;

import com.actionzh.batch.importer.TemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ContactTemplateGenerator {

    @Autowired
    private TemplateGenerator templateGenerator;


    @Autowired
    private ContactImportBatchSettings contactImportBatchSettings;


    public Path generate() {
        return null;
    }

}
