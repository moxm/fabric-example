package com.moxm.fabric.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.shim.ChaincodeBase;
import org.hyperledger.fabric.sdk.shim.ChaincodeStub;

/**
 * Created by Neel on 2017/2/4.
 */
public class FirstExample extends ChaincodeBase {

    private static Log LOG = LogFactory.getLog(FirstExample.class);

    @Override
    public String invoke(ChaincodeStub stub, String function, String[] args) {
        LOG.info("In run, function:"+function);

        switch (function) {
            case "init":
                init(stub, function, args);
                break;
            case "query":
                query(stub, args);
                break;
            case "transfer":
                String re = transfer(stub, args);
                System.out.println(re);
                return re;
            case "put":
                for (int i = 0; i < args.length; i += 2)
                    stub.putState(args[i], args[i + 1]);
                break;
            case "del":
                for (String arg : args)
                    stub.delState(arg);
                break;
            default:
                return transfer(stub, args);
        }

        return null;
    }


    private String  transfer(ChaincodeStub stub, String[] args) {
        System.out.println("in transfer");
        if(args.length!=3){
            System.out.println("Incorrect number of arguments:"+args.length);
            return "{\"Error\":\"Incorrect number of arguments. Expecting 3: from, to, amount\"}";
        }
        String fromName =args[0];
        String fromAm=stub.getState(fromName);
        String toName =args[1];
        String toAm=stub.getState(toName);
        String am =args[2];
        int valFrom=0;
        if (fromAm!=null && !fromAm.isEmpty()){
            try{
                valFrom = Integer.parseInt(fromAm);
            }catch(NumberFormatException e ){
                System.out.println("{\"Error\":\"Expecting integer value for asset holding of "+fromName+" \"}"+e);
                return "{\"Error\":\"Expecting integer value for asset holding of "+fromName+" \"}";
            }
        }else{
            return "{\"Error\":\"Failed to get state for " +fromName + "\"}";
        }

        int valTo=0;
        if (toAm!=null&&!toAm.isEmpty()){
            try{
                valTo = Integer.parseInt(toAm);
            }catch(NumberFormatException e ){
                e.printStackTrace();
                return "{\"Error\":\"Expecting integer value for asset holding of "+toName+" \"}";
            }
        }else{
            return "{\"Error\":\"Failed to get state for " +toName + "\"}";
        }

        int valA =0;
        try{
            valA = Integer.parseInt(am);
        }catch(NumberFormatException e ){
            e.printStackTrace();
            return "{\"Error\":\"Expecting integer value for amount \"}";
        }
        if(valA>valFrom)
            return "{\"Error\":\"Insufficient asset holding value for requested transfer amount \"}";
        valFrom = valFrom-valA;
        valTo = valTo+valA;
        System.out.println("Transfer "+fromName+">"+toName+" am='"+am+"' new values='"+valFrom+"','"+ valTo+"'");
        stub.putState(fromName,""+ valFrom);
        stub.putState(toName, ""+valTo);

        System.out.println("Transfer complete");

        return null;

    }

    public String init(ChaincodeStub stub, String function, String[] args) {
        if(args.length!=4){
            return "{\"Error\":\"Incorrect number of arguments. Expecting 4\"}";
        }
        try{
            int valA = Integer.parseInt(args[1]);
            int valB = Integer.parseInt(args[3]);
            stub.putState(args[0], args[1]);
            stub.putState(args[2], args[3]);
        }catch(NumberFormatException e ){
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
        return null;
    }

    public String query(ChaincodeStub stub, String[] args) {
        if(args.length!=1){
            return "{\"Error\":\"Incorrect number of arguments. Expecting name of the person to query\"}";
        }
        String am =stub.getState(args[0]);
        if (am!=null&&!am.isEmpty()){
            try{
                int valA = Integer.parseInt(am);
                return  "{\"Name\":\"" + args[0] + "\",\"Amount\":\"" + am + "\"}";
            }catch(NumberFormatException e ){
                return "{\"Error\":\"Expecting integer value for asset holding\"}";
            }		}else{
            return "{\"Error\":\"Failed to get state for " + args[0] + "\"}";
        }


    }

    @Override
    public String getChaincodeID() {
        return "FirstExample";
    }

    public static void main(String[] args) throws Exception {
        new FirstExample().start(args);
    }
}
