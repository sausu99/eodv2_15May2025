<?php
session_start();
include("../../includes/connection.php");

if (isset($_POST['amount']) && isset($_POST['name'])) {
    $amount = $_POST['amount'] / 100;
    $name = $_POST['name'];
    $pg_id = $_POST['pg_id'];

    // Update wallet amount
    $id = $_SESSION['user_id']; // Assuming the user's ID is stored in a session variable

    // Get the current wallet amount
    $qry_wallet = "SELECT wallet AS num FROM tbl_users WHERE id = '$id'";
    $wallet_result = mysqli_query($mysqli, $qry_wallet);

    if ($wallet_result) {
        $total_wallet = mysqli_fetch_array($wallet_result);
        $current_wallet_amount = $total_wallet['num'];

        // Calculate the new wallet amount by adding the current wallet amount and the received payment amount
        $new_wallet_amount = $current_wallet_amount + $_POST['name'];

/* Add Trx Details. */

$transactionData = array(
    'user_id' => $id,
    'pg_id' => $pg_id,
    'type_no' => '9',
    'date' => date('Y-m-d H:i:s'),
    'money' => $amount
);


// Check the connection
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

// Build the SQL query
$query = "INSERT INTO tbl_wallet_passbook (wp_user, wp_package_id, wp_order_id, wp_date, wp_status) VALUES (?, ?, ?, ?, ?)";

// Prepare the statement
$stmt = $mysqli->prepare($query);

// Check if the statement preparation is successful
if ($stmt) {
    // Create variables for the data to be bound (so they can be passed by reference)
    $user_id = $transactionData['user_id'];
    $pg_id = $transactionData['pg_id'];
    $payment_gateway = 'razorpay'; // Literal values need to be assigned to a variable
    $date = $transactionData['date'];
    $status = 1; // Assign literal to variable

    // Bind parameters (make sure to use variables for all parameters)
    $stmt->bind_param('sdsds', $user_id, $pg_id, $payment_gateway, $date, $status);

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
}  else {
    echo "Error: " . $mysqli->error;
}
        // Update the wallet amount in the database
        $update_wallet_query = "UPDATE tbl_users SET wallet = '$new_wallet_amount' WHERE id = '$id'";
        $update_result = mysqli_query($mysqli, $update_wallet_query);

        if ($update_result) {
            
 echo " Wallet amount Added";

            
        } else {
            // Handle the case if wallet update fails
            echo "Failed to update wallet amount";
        }
    } else {
        // Handle the case if fetching the current wallet amount fails
        echo "Failed to fetch current wallet amount";
    }
}


?>
