<?php
include("../includes/connection.php");
include("/home/jlifoeck/lotterybajar.com/includes/connection.php");

ini_set('display_errors', '1');
ini_set('display_startup_errors', '1');
error_reporting(E_ALL);

// Logging function
function logMessage($message) {
    $logFile = '/home/jlifoeck/lotterybajar.com/cron/cron_log.txt';
    file_put_contents($logFile, date('Y-m-d H:i:s') . " - " . $message . "\n", FILE_APPEND);
}

logMessage("Cron job started.");


// Current date and time
$timezone_query = "SELECT timezone FROM tbl_settings";
$timezone_result = mysqli_query($mysqli, $timezone_query);
$timezone_row = mysqli_fetch_assoc($timezone_result);
$timezone = $timezone_row['timezone'];
date_default_timezone_set($timezone);
$current_date = date('Y-m-d H:i:s');

logMessage("Current date and time: " . $current_date);

// Fetch rows where interest update is due
$query = "
    SELECT *
    FROM tbl_hyip_order 
    WHERE next_interest_update <= '$current_date' 
      AND status = 1";

$result = $mysqli->query($query);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $interest_rate = $row['interest'];
        $order_id = $row['order_id'];
        $plan_id = $row['plan_id'];
        $user_id = $row['user_id'];
        $current_value = $row['current_value'];
        $investment = $row['investment_amount'];
        $next_interest_update = $row['next_interest_update'];
        
       $queryUsers = "SELECT wallet FROM tbl_users WHERE id = '$user_id'";
       $resultUsers = $mysqli->query($queryUsers);
       
       if ($resultUsers && $resultUsers->num_rows > 0) {
           $userRow = $resultUsers->fetch_assoc();
           $currentWallet = $userRow['wallet']; // Fetch wallet from the associative array
       } else {
           logMessage("No wallet found for user_id: $user_id.");
           continue; // Skip the current iteration if no wallet is found
       }

        logMessage("Processing order_id: $order_id & user current current wallet balance is $currentWallet");

        $planDetailsQuery = "SELECT * FROM tbl_hyip WHERE plan_id = $plan_id";
        $planDetailsResult = mysqli_query($mysqli, $planDetailsQuery);
        $planDetailsRow = mysqli_fetch_assoc($planDetailsResult);

        if (strpos($interest_rate, '%') !== false) {
            $percentage = floatval(str_replace('%', '', $interest_rate)) / 100;
            $new_value = $current_value + ($investment * $percentage);
            $new_wallet = $currentWallet + ($investment * $percentage);
            $interestEarned = ($investment * $percentage);
        } else {
            $new_value = $current_value + floatval($interest_rate);
            $new_wallet = $currentWallet + floatval($interest_rate);
            $interestEarned = floatval($interest_rate);
        }

        $nextInterestUpdate = strtotime($next_interest_update) + strtotime($planDetailsRow['plan_repeat_time']) - strtotime('today');
        $new_next_update = date('Y-m-d H:i:s', $nextInterestUpdate);
        
        $update_userquery = "
            UPDATE tbl_users 
            SET wallet = $new_wallet 
            WHERE id = $user_id";
            
        $mysqli->query($update_userquery);
        
        $insert_transaction = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`, `date`, `money`,`comments`) 
                                      VALUES ('$user_id', 15, '15', NOW(), '$interestEarned','Earned Interest')";
            
        $mysqli->query($insert_transaction);

        $update_query = "
            UPDATE tbl_hyip_order 
            SET current_value = $new_value, 
                last_interest_update = '$current_date', 
                next_interest_update = '$new_next_update' 
            WHERE order_id = $order_id";

        if ($mysqli->query($update_query) === TRUE) {
            logMessage("Record updated successfully for order_id $order_id. New wallet balance is $new_wallet");
        } else {
            logMessage("Error updating record for order_id $order_id: " . $mysqli->error);
        }
    }
} else {
    logMessage("No records found for interest update.");
}

$mysqli->close();
logMessage("Cron job ended.");
?>
