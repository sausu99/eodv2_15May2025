<?php
include("../../includes/connection.php");
$conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
$clientID = "";
$secretKey = "";

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT `cashfree_appid`, `cashfree_secret` FROM `tbl_settings` WHERE 1";
$result = $conn->query($sql);

while ($row = $result->fetch_assoc()) {
    $secretKey = $row["cashfree_secret"];
    $clientID = $row["cashfree_appid"];
}

$id = $_SESSION["user_id"];
$user = "SELECT `name`, `email`, `phone` FROM `tbl_users` WHERE `id`=$id";
$result = $conn->query($user);
$row = $result->fetch_assoc();
$name = $row["name"];
$email = $row["email"];
$phone = $row["phone"];

$amount = $_POST["amount"];
$currency = "INR";
$id = $id . "-" . $_POST["coin"] . "-" . date("ymdhms");
$domain = $_SERVER['HTTP_HOST'];
$protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] != 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";

// Assuming your scripts are located in a specific directory
$relativePath = '/payment-gateway/CashFree';
$returnUrl = $protocol . $domain . $relativePath . "/Charge.php";

$paymentUrl = "https://test.cashfree.com/billpay/checkout/post/submit";
//$paymentUrl = "https://www.cashfree.com/checkout/post/submit";


$data = array(
    "appId" => $clientID,
    "orderId" => $id,
    "orderAmount" => $amount,
    "customerName" => $name,
    "customerEmail" => $email,
    "customerPhone" => $phone,
    "returnUrl" => $returnUrl,
);

ksort($data);
$signatureData = "";
foreach ($data as $key => $value) {
    $signatureData .= $key . $value;
}

$signature = hash_hmac('sha256', $signatureData, $secretKey, true);
$signature = base64_encode($signature);

$data["signature"] = $signature;

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $paymentUrl);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($ch);
curl_close($ch);

echo $response;
?>
