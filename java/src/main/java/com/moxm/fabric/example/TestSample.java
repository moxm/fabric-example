package com.moxm.fabric.example;

import org.hyperledger.fabric.sdk.shim.ChaincodeBase;
import org.hyperledger.fabric.sdk.shim.ChaincodeStub;

/**
 * Created by Neel on 2017/2/9.
 */
public class TestSample extends ChaincodeBase {

    @Override
    public String invoke(ChaincodeStub stub, String function, String[] args) {
        if ("transfer".equals(function)) {
            return null;
        }
        if ("query".equals(function)) {
            return "query : 1000";
        }
        if ("init".equals(function)) {
            return null;
        }
        /*
        switch (function) {
            case "transfer":
                break;
            case "query":
                return "query : 1000";
            case "init":
                break;
        }
        */
        return null;
    }

    @Override
    public String getChaincodeID() {
        return "TestSample";
    }
}
