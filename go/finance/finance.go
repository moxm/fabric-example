/*
Copyright IBM Corp. 2016 All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package main

//WARNING - this chaincode's ID is hard-coded in chaincode_example04 to illustrate one way of
//calling chaincode from a chaincode. If this example is modified, chaincode_example04.go has
//to be modified as well with the new ID of chaincode_example02.
//chaincode_example05 show's how chaincode ID can be passed in as a parameter instead of
//hard-coding.

import (
	"errors"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

// SimpleChaincode example simple Chaincode implementation
type FinanceChaincode struct {
}

func (t *FinanceChaincode) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	return nil, nil
}

// Transaction makes payment of X units from A to B
func (t *FinanceChaincode) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	if function == "delete" {
		// Deletes an entity from its state
		return t.delete(stub, args)
	}
	if function == "pass" {
		return t.pass(stub, args)
	}
	if function == "reject" {
		return t.reject(stub, args)
	}
	if function == "create" {
		return t.create(stub, args)
	}
	if function == "change" {
		return t.change(stub, args)
	}

	return nil, nil
}

// Deletes an entity from state
func (t *FinanceChaincode) delete(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting 1")
	}

	Key := args[0]

	// Delete the key from the state in ledger
	err := stub.DelState(Key)
	if err != nil {
		return nil, errors.New("Failed to delete state")
	}

	return nil, nil
}
// Deletes an entity from state
func (t *FinanceChaincode) create(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 2 {
		return nil, errors.New("Incorrect number of arguments. Expecting 2")
	}

	var err error

	Key := args[0]
	Value := args[1]

	err = stub.PutState(Key, []byte(Value))
	if err != nil {
		return nil, errors.New("Failed to create state")
	}

	return nil, nil
}
// Deletes an entity from state
func (t *FinanceChaincode) change(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 2 {
		return nil, errors.New("Incorrect number of arguments. Expecting 2")
	}

	var err error

	Key := args[0]
	Value := args[1]

	err = stub.PutState(Key, []byte(Value))
	if err != nil {
		return nil, errors.New("Failed to create state")
	}

	return nil, nil
}
// Deletes an entity from state
func (t *FinanceChaincode) reject(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 2 {
		return nil, errors.New("Incorrect number of arguments. Expecting 2")
	}

	var err error

	Key := args[0]
	Value := args[1]

	err = stub.PutState(Key, []byte(Value))
	if err != nil {
		return nil, errors.New("Failed to change state")
	}

	return nil, nil
}
// Deletes an entity from state
func (t *FinanceChaincode) pass(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 2 {
		return nil, errors.New("Incorrect number of arguments. Expecting 2")
	}

	var err error

	Key := args[0]
	Value := args[1]

	err = stub.PutState(Key, []byte(Value))
	if err != nil {
		return nil, errors.New("Failed to change state")
	}

	return nil, nil
}

// Query callback representing the query of a chaincode
func (t *FinanceChaincode) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	if function != "query" {
		return nil, errors.New("Invalid query function name. Expecting \"query\"")
	}
	var Key string // Entities
	var err error

	if len(args) != 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting name of the person to query")
	}

	Key = args[0]

	// Get the state from the ledger
	Avalbytes, err := stub.GetState(Key)
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for " + Key + "\"}"
		return nil, errors.New(jsonResp)
	}

	if Avalbytes == nil {
		jsonResp := "{\"Error\":\"Nil amount for " + Key + "\"}"
		return nil, errors.New(jsonResp)
	}

	jsonResp := "{\"Key\":\"" + Key + "\",\"Value\":\"" + string(Avalbytes) + "\"}"
	fmt.Printf("Query Response:%s\n", jsonResp)
	return Avalbytes, nil
}

func main() {
	err := shim.Start(new(FinanceChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
