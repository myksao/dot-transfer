package main

import (
	"bytes"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"net/http"
	"os"
	"strconv"
	"time"

	"github.com/joho/godotenv"
)

var TOTAL = 50

var AMOUNT = "520.000000"

var amt = 30.53

var URL = "http://localhost:8081/api/v1/savings"

type Transaction struct {
	From   string `json:"from"`
	To     string `json:"to"`
	Amount string `json:"amount"`
	Note   string `json:"note"`
}

type Deposit struct {
	Amount    string `json:"amount"`
	AccountNo string `json:"account_no"`
	Note      string `json:"note"`
}

func RandomString() string {
	rand.Seed(time.Now().UnixNano())
	const charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	result := make([]byte, 20)
	for i := range result {
		result[i] = charset[rand.Intn(len(charset))]
	}
	return string(result)
}

func Rest(URL string, Signature string, data []byte) (int, string, error) {
	var err error
	client := &http.Client{}
	http.DefaultTransport.(*http.Transport).TLSClientConfig = &tls.Config{InsecureSkipVerify: true}
	req, err := http.NewRequest(http.MethodPost, URL, bytes.NewBuffer(data))
	if err != nil {
		return 0, "", err
	}
	req.Header.Del("Content-Length")
	req.Header.Set("Content-Type", "application/json")
	resp, err := client.Do(req)
	if err != nil {
		return 0, "", err
	}
	defer resp.Body.Close()
	body, _ := ioutil.ReadAll(resp.Body)
	return resp.StatusCode, string(body), nil
}

func Credit() {
	var LogBody string
	filename := "credit.txt"
	for i := 0; i < TOTAL; i++ {
		transaction := Deposit{
			AccountNo: "9597496000",
			Amount:    AMOUNT,
			Note:      "LOAD TEST CREDIT TD",
		}
		BodyByte, _ := json.Marshal(transaction)
		StatusCode, ResBody, _ := Rest(fmt.Sprintf("%s/deposit", URL), "", BodyByte)
		LogBody = "Number: " + strconv.Itoa(i) + " StatusCode: " + strconv.Itoa(StatusCode) + " Body: " + ResBody
		if StatusCode == 200 {
			fmt.Printf("Credit Iteration %d:\nStatusCode %d:\n\n:%s:\n", i+1, StatusCode, "-------------")
		} else {
			appendToFile(filename, ResBody)
			fmt.Printf("Credit Iteration %d:\nStatusCode %d:\n\n:%s:\n", i+1, StatusCode, "-------------")
		}
		appendToFile(filename, LogBody)
	}
}

func Debit() {
	filename := "debit.txt"
	var LogBody string
	for i := 0; i < TOTAL; i++ {
		transaction := Transaction{
			From:   "9597496000",
			To:     "9481537100",
			Amount: AMOUNT,
			Note:   "LOAD TEST DEBIT TD",
		}
		BodyByte, _ := json.Marshal(transaction)
		StatusCode, ResBody, _ := Rest(fmt.Sprintf("%s/transfer", URL), "", BodyByte)
		LogBody = "Number: " + strconv.Itoa(i) + " StatusCode: " + strconv.Itoa(StatusCode) + " Body: " + ResBody
		if StatusCode == 200 {
			fmt.Printf("Debit Iteration %d:\nStatusCode %d:\n\n:%s:\n", i+1, StatusCode, "-------------")
		} else {
			appendToFile(filename, ResBody)
			fmt.Printf("Debit Iteration %d:\nStatusCode %d:\n\n:%s:\n", i+1, StatusCode, "-------------")
		}
		appendToFile(filename, LogBody)

	}
}

func appendToFile(filename string, text string) error {
	file, err := os.OpenFile(filename, os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0644)
	if err != nil {
		return err
	}
	defer file.Close()
	fileInfo, err := file.Stat()
	if err != nil {
		return err
	}
	if fileInfo.Size() > 0 {
		if _, err := file.WriteString("\n"); err != nil {
			return err
		}
	}
	if _, err := file.WriteString(text + "\n"); err != nil {
		return err
	}
	return nil
}

func main() {
	godotenv.Load()
	os.Remove("credit.txt")
	os.Remove("debit.txt")

	go Credit()
	go Debit()

	time.Sleep(200 * time.Minute)

	fmt.Println("--- process completed ---")

}
