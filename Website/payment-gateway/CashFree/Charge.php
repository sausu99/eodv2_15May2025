<?php
$orderId = $_POST['orderId'];
$orderAmount = $_POST['orderAmount'];
$referenceId = $_POST['referenceId'];
$txStatus = $_POST['txStatus'];
$paymentMode = $_POST['paymentMode'];
$txMsg = $_POST['txMsg'];
$txTime = $_POST['txTime'];
$signature = $_POST['signature'];
$domain = $_SERVER['HTTP_HOST'];
$protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] != 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";

ini_set('display_errors', '1');
ini_set('display_startup_errors', '1');
error_reporting(E_ALL);

include("../../includes/connection.php");

$conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT `cashfree_appid`, `cashfree_secret` FROM `tbl_settings` WHERE 1";
$result = $conn->query($sql);
$clientID = "";
$secretKey = "";
while ($row = $result->fetch_assoc()) {
    $secretKey = $row["cashfree_secret"];
    $clientID = $row["cashfree_appid"];
}

$data = $orderId . $orderAmount . $referenceId . $txStatus . $paymentMode . $txMsg . $txTime;
$computedSignature = hash_hmac('sha256', $data, $secretKey, true);
$computedSignature = base64_encode($computedSignature);
if ($signature != $computedSignature) {
    header("Location: " . $protocol . $domain . "/failed.php");
    exit();
}

if ($txStatus == 'SUCCESS') {
    $_SESSION["status"] = "success";
    $oData = explode("-", $orderId);
    $id = $oData[0];
    $qry_wallet = "SELECT wallet FROM tbl_users WHERE `id`=$id";
    $wallet_result = mysqli_query($conn, $qry_wallet);

    if ($wallet_result) {
        $total_wallet = $wallet_result->fetch_assoc();
        $current_wallet_amount = $total_wallet['wallet'];

        $new_wallet_amount = (int)$current_wallet_amount + (int)$oData[1];

        $update_wallet_query = "UPDATE tbl_users SET `wallet`=$new_wallet_amount WHERE `id`=$id";
        $update_result = mysqli_query($conn, $update_wallet_query);
        if ($update_result) {
            header("Location: " . $protocol . $domain . "/success.php");
        } else {
            header("Location: " . $protocol . $domain . "/failed.php");
        }
    }
} else {
    header("Location: " . $protocol . $domain . "/failed.php");
}

$conn->close();
?>
