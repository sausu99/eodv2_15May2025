<?php
require 'vendor/autoload.php';

use PayPal\Rest\ApiContext;
use PayPal\Auth\OAuthTokenCredential;
use PayPal\Api\Amount;
use PayPal\Api\Payer;
use PayPal\Api\Payment;
use PayPal\Api\RedirectUrls;
use PayPal\Api\Transaction;

include("includes/connection.php");

class PayPalHandler
{
    private $apiContext;
    private $mysqli;

function getCurrencyFromSettings() {
    global $mysqli;
    $query = "SELECT paypal_currency FROM tbl_settings";
    $result = mysqli_query($mysqli, $query);
    if ($result) {
        if (mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            return $row['paypal_currency'];
        }
    }
    return 'USD';
}

function getdatabycol($tablecol) {
    global $mysqli;
    $query = "SELECT $tablecol FROM tbl_settings";
    $result = mysqli_query($mysqli, $query);

    if ($result && mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        $value = $row[$tablecol];

        // Free the result set
        mysqli_free_result($result);

        return $value;
    }

    return null; // or handle the case when no data is found
}


function getCurrencyName($symbol) {
    $currencyMap = array(
        '$' => 'USD',
        '€' => 'EUR',
        '£' => 'GBP',
        '¥' => 'JPY',
        'CAD' => 'CAD',
        'AUD' => 'AUD',
        'CHF' => 'CHF',
        'CNY' => 'CNY',
        'SEK' => 'SEK',
        'SGD' => 'SGD',
        // Add more mappings as needed
    );
    return isset($currencyMap[$symbol]) ? $currencyMap[$symbol] : $symbol;
}



    public function __construct()
    {
        $this->apiContext = new ApiContext(
            new OAuthTokenCredential(
                ''.$this->getdatabycol("paypal_id").'',
                ''.$this->getdatabycol("paypal_secret").''
            )
        );

        // Set the SDK mode
        $this->apiContext->setConfig([
            'mode' => 'live', // or 'sandbox' for test
            'http.ConnectionTimeOut' => 30,
            'log.LogEnabled' => true,
            'log.FileName' => 'paypal.log',
            'log.LogLevel' => 'DEBUG',
        ]);
    }


public  function inr_to_usd_converter($amount_inr) {
    // Replace this with a call to a real API or database to get the latest exchange rate
    $exchange_rate = 0.014; // 1 INR = 0.014 USD

    // Convert the amount
    $amount_usd = $amount_inr * $exchange_rate;

    // Round the result to two decimal places
    $amount_usd = round($amount_usd, 2);

    return $amount_usd;
}





    public function createPayment($returnUrl, $cancelUrl,$amountid,$info)
{
    global $mysqli;
    $payer = new Payer();
    $payer->setPaymentMethod('paypal');

    $mainamount = $this->inr_to_usd_converter($amountid);


    $amount = new Amount();
    $amount->setCurrency($this->getCurrencyFromSettings())
        ->setTotal($mainamount); // Replace with your amount

    $transaction = new Transaction();
    $transaction->setAmount($amount)
        ->setDescription($info); // Replace with your product description

    $redirectUrls = new RedirectUrls();
    $redirectUrls->setReturnUrl($returnUrl) // Pass the return URL dynamically
        ->setCancelUrl($cancelUrl); // Pass the cancel URL dynamically

    $payment = new Payment();
    $payment->setIntent('sale')
        ->setPayer($payer)
        ->setTransactions([$transaction])
        ->setRedirectUrls($redirectUrls);

    try {
        $payment->create($this->apiContext);
        $approvalUrl = $payment->getApprovalLink();

        //echo $approvalUrl;
        echo '<script>window.location.href = "' . $approvalUrl . '";</script>';
        exit;
    } catch (\PayPal\Exception\PayPalConnectionException $ex) {
        echo $ex->getData();
    }
}


    public function executePayment($paymentId, $payerId)
    {
        $payment = Payment::get($paymentId, $this->apiContext);
        $execution = new \PayPal\Api\PaymentExecution();
        $execution->setPayerId($payerId);

        try {
            $result = $payment->execute($execution, $this->apiContext);
            return $result;
        } catch (\PayPal\Exception\PayPalConnectionException $ex) {
            echo $ex->getData();
        }
    }
}
