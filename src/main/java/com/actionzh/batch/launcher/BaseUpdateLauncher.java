package com.actionzh.batch.launcher;


import java.util.List;

/**
 * Author: likai.yu
 * Date: 2019-10-18 11:16
 **/
public class BaseUpdateLauncher extends BaseLauncher {


    @Override
    public void checkHead(List<String> headers) throws Exception {
        //header is not null
        if (!headers.contains("id")) {
            throw new Exception("first row do not contain 'id'");
        }
    }
}
