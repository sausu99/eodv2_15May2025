<?php
session_start();
require_once __DIR__ . "/../../vendor/autoload.php";
include_once  __DIR__ . "/../../includes/connection.php";
use App\payment\mpesa\C2B;
  

if (isset($_POST['amount']) and isset($_POST['item_id'])) {
    
    try {
        
    
        $paymentId = uniqid();
        $amount = $_POST['amount'];
        $item_id = $_POST['item_id'];
        
        // Fetch item details
        $ItemQuery = "SELECT * FROM tbl_offers WHERE o_id = ?";
        $stmt = $mysqli->prepare($ItemQuery);
        $stmt->bind_param('i', $item_id);
        $stmt->execute();
        $result = $stmt->get_result();
        $item = $result->fetch_assoc();
     
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
            
            // Calculate total amount from item details
                $totalAmount = $item['o_price'];
                $payAmount = $item['o_amount'];
            
                // Prepare transaction data
                $transactionData = array(
                    'user_id' => $id,
                    'type' => '1',
                    'type_no' => '9',
                    'date' => date('Y-m-d H:i:s'),
                    'money' => $totalAmount
                );
            
                // Insert transaction record
                $transactionQuery = "INSERT INTO tbl_transaction (user_id, type, type_no, date, money) VALUES (?, ?, ?, ?, ?)";
                $transactionStmt = $mysqli->prepare($transactionQuery);
                if ($transactionStmt) {
                    $transactionStmt->bind_param('iissd', $transactionData['user_id'], $transactionData['type'], $transactionData['type_no'], $transactionData['date'], $transactionData['money']);
                    $transactionResult = $transactionStmt->execute();
                    $transactionStmt->close();
                    if (!$transactionResult) {
                        die("Error inserting transaction record: " . $mysqli->error);
                    }
                } else {
                    die("Error preparing transaction statement: " . $mysqli->error);
                }
            
                // Prepare order data
                $orderData = array(
                    'u_id' => $id,
                    'offer_id' => $item_id,
                    'total_amount' => $totalAmount,
                    'pay_amount' => $payAmount,
                    'order_status' => 1,
                    'order_date' => date('Y-m-d H:i:s'),
                    'o_status' => 1
                );
            
                // Insert order record
                $orderQuery = "INSERT INTO tbl_order (u_id, offer_id, total_amount, pay_amount, order_status, order_date, o_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
                $orderStmt = $mysqli->prepare($orderQuery);
                if ($orderStmt) {
                $orderStmt->bind_param('iiddisi', $orderData['u_id'], $orderData['offer_id'], $orderData['total_amount'], $orderData['pay_amount'], $orderData['order_status'], $orderData['order_date'], $orderData['o_status']);
                $orderResult = $orderStmt->execute();
                $orderStmt->close();
                
                if ($orderResult) {
                    echo "Order placed successfully!";
                    header('Location: ../../success.php');
                    exit(); // Ensure the script stops executing after redirection
                } else {
                    echo "Error placing order: " . $mysqli->error;
                    header('Location: ../../failed.php');
                    exit(); // Ensure the script stops executing after redirection
                }
              } else {
                  echo "Error preparing order statement: " . $mysqli->error;
                  header('Location: ../../failed.php');
                  exit(); // Ensure the script stops executing after redirection
              }

        }else{
            
            throw new Exception("Error Processing Request", 1);
            
        }
    } catch (\Throwable $th) {
        echo $th->getMessage();
    }
    





}

?>