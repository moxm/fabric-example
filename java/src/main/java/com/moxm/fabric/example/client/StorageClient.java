package com.moxm.fabric.example.client;

import org.hyperledger.fabric.sdk.Chain;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.shim.ChaincodeBase;
import org.hyperledger.fabric.sdk.shim.ChaincodeStub;

/**
 * Created by Neel on 2017/2/14.
 */
public class StorageClient {

    public static void main(String []args) throws Exception {
        System.out.println("----------------");

        Chain chain = Chain.createNewInstance("storage_cc", HFClient.createNewInstance());
        chain.addOrderer(new Orderer("grpc://localhost:8050", "", null));

    }
}
