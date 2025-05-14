<?php
session_start();
require_once __DIR__ . "/../../vendor/autoload.php";
include_once  __DIR__ . "/../../includes/connection.php";
use App\payment\mpesa\C2B;
  

if (isset($_POST['amount']) and isset($_POST['coin'])) {
    
    try {
        
    
        $paymentId = uniqid();
        $amount = $_POST['amount'];
     
        $mpesa = new C2B($paymentId, $amount);
     
        $mpesa
        ->setPhone_number($_POST['phone'])
        ->processPayment();

     
        $r = $mpesa->getResponse();
     
        if ($r == null) {
            throw new Exception("Error Processing Request", 1);
        }

        if (strtolower($r->output_ResponseCode) == 'ins-0') {
     
    
            // Update wallet amount
            $id = $_SESSION['user_id']; //User's ID is stored in a session variable
        
            // Get the current wallet amount
            $qry_wallet = "SELECT wallet AS num FROM tbl_users WHERE id = '$id'";
            $wallet_result = mysqli_query($mysqli, $qry_wallet);
        
            if ($wallet_result) {
                $total_wallet = mysqli_fetch_array($wallet_result);
                $current_wallet_amount = $total_wallet['num'];
        
                // Calculate the new wallet amount by adding the current wallet amount and the received payment amount
                $new_wallet_amount = $current_wallet_amount + $_POST['coin'];
        
                /* Add Trx Details. */
        
                $transactionData = array(
                    'user_id' => $id,
                    'type' => '1',
                    'type_no' => '9',
                    'date' => date('Y-m-d H:i:s'),
                    'money' => $amount
                );
        
        
                // Check the connection
                if ($mysqli->connect_error) {
                    throw new Exception("Error Connecting to DB");
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
                        
                    } else {
                        throw new Exception("Error Saving Transaction");
                    }
        
                    // Close the statement
                    $stmt->close();
                } else {
                    throw new Exception("Error preparing Transaction");
                }
        
                // Close the connection
                $mysqli->close();
                
                // Update the wallet amount in the database
                $update_wallet_query = "UPDATE tbl_users SET wallet = '$new_wallet_amount' WHERE id = '$id'";
                $update_result = mysqli_query($mysqli, $update_wallet_query);
                echo "ok";
            } else {
                
                // Handle the case if fetching the current wallet amount fails
            throw new Exception("fetching the current wallet amount failed");
            }
        }else{
            
            throw new Exception("Error Processing Request", 1);
            
        }
    } catch (\Throwable $th) {
        echo $th->getMessage();
    }
    





}

?>