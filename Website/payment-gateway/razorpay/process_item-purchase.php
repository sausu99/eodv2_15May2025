<?php
session_start();
include("../../includes/connection.php");


if (isset($_POST['amount']) && isset($_POST['name']) && isset($_POST['item_id'])) {
    // Assuming the user's ID is stored in a session variable
    $user_id = $_SESSION['user_id'];
    $amount = $_POST['amount'] / 100;
    $name = $_POST['name']; //item name
    $item_id = $_POST['item_id']; //item id (o_id)
    
    // Fetch item details
    $ItemQuery = "SELECT * FROM tbl_offers WHERE o_id = ?";
    $stmt = $mysqli->prepare($ItemQuery);
    $stmt->bind_param('i', $item_id);
    $stmt->execute();
    $result = $stmt->get_result();
    $item = $result->fetch_assoc();
    
    // Calculate total amount from item details
    $totalAmount = $item['o_price'];

    // Prepare transaction data
    $transactionData = array(
        'user_id' => $user_id,
        'type' => '1',
        'type_no' => '9',
        'date' => date('Y-m-d H:i:s'),
        'money' => $amount
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
        'u_id' => $user_id,
        'offer_id' => $item_id,
        'total_amount' => $totalAmount,
        'pay_amount' => $amount,
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
        } else {
            echo "Error placing order: " . $mysqli->error;
        }
    } else {
        echo "Error preparing order statement: " . $mysqli->error;
    }
}
?>
