<?php
session_start();
include("../../includes/connection.php");

require '../../PayPalHandler.php';


$paymentId = $_GET['paymentId'];
$payerId = $_GET['PayerID'];

$paypalHandler = new PayPalHandler();
$result = $paypalHandler->executePayment($paymentId, $payerId);

$coin_from_ses = $_GET['action']; //item id
$coin_from_sesamount = $_GET['amo']; //amount paid
 
$amount = $coin_from_ses;
$name = $coin_from_ses;

// Update wallet amount
$id = $_SESSION['user_id']; // Assuming the user's ID is stored in a session variable

// Get the current wallet amount
$qry_wallet = "SELECT wallet AS num FROM tbl_users WHERE id = '$id'";
$wallet_result = mysqli_query($mysqli, $qry_wallet);

if ($wallet_result) {
    $total_wallet = mysqli_fetch_array($wallet_result);
    $current_wallet_amount = $total_wallet['num'];

    // Calculate the new wallet amount by adding the current wallet amount and the received payment amount
    $new_wallet_amount = $current_wallet_amount + $amount;

    // Update the wallet amount in the database
    $update_wallet_query = "UPDATE tbl_users SET wallet = '$new_wallet_amount' WHERE id = '$id'";
    $update_result = mysqli_query($mysqli, $update_wallet_query);

    if ($update_result) {


        /* Add Trx Details. */

        $transactionData = array(
            'user_id' => $id,
            'type' => '1',
            'type_no' => '9',
            'date' => date('Y-m-d H:i:s'),
            'money' => $coin_from_sesamount
        );


        // Check the connection
        if ($mysqli->connect_error) {
            die("Connection failed: " . $mysqli->connect_error);
        }

        // Build the SQL query
        $query = "INSERT INTO tbl_transaction (user_id, type, type_no, date, money) VALUES (?, ?, ?, ?, ?)";

        // Prepare the statement
        $stmt = $mysqli->prepare($query);

        // Check if the statement preparation is successful
        if ($stmt) {
            // Bind parameters
            $stmt->bind_param('sdsds', $transactionData['user_id'], $transactionData['type'], $transactionData['type_no'], $transactionData['date'], $transactionData['money']);

            // Execute the statement
            $result = $stmt->execute();

            // Check for success
            if ($result) {
                echo "Insert successful!";
            } else {
                echo "Error: " . $stmt->error;
            }

            // Close the statement
            $stmt->close();
        } else {
            echo "Error: " . $mysqli->error;
        }

        // Close the connection
        $mysqli->close();







        $protocol = isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? 'https' : 'http';
        $host = $_SERVER['HTTP_HOST'];
        $baseUrl = $protocol . '://' . $host;

        $urlx = $baseUrl .'/failed.php';
        $urlc = $baseUrl .'/success.php';

        echo '<script>window.location.href = "' . $urlc . '";</script>';

        // echo $_SESSION['coininfocsrf'];
        unset($_SESSION['coininfocsrf']);

    } else {
        echo '<script>window.location.href = "' . $urlx . '";</script>';
    }
} else {
    echo '<script>window.location.href = "' . $urlx . '";</script>';
}




?>