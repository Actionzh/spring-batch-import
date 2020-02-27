package com.actionzh.batch.launcher;

import java.util.List;

public class BaseImportLauncher extends BaseLauncher {

    @Override
    public void checkHead(List<String> headers) throws Exception {
        //header is not null
        if (!headers.contains("name")) {
            throw new Exception("first row do not contain 'name'");
        }
    }
}
