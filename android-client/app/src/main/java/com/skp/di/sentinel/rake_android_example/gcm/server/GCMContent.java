package com.skp.di.sentinel.rake_android_example.gcm.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GcmContent implements Serializable {

    private List<String> registration_ids;
    private Map<String,String> data;

    public GcmContent(String regId) {
        registration_ids = new LinkedList<String>();
        data = new HashMap<String, String>();

        addRegId(regId);
    }

    public void addRegId(String regId){
        if (null == regId || regId.isEmpty())
            throw new RuntimeException("addRegId(): Registration ID is empty");

        registration_ids.add(regId);
    }

    public void createData(String key, String value){
        data.put(key, value);
    }
}
