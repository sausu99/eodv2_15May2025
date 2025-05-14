<?php
session_start();
require __DIR__ . '/vendor/autoload.php';
include_once __DIR__ . "/includes/connection.php";
use Srmklive\PayPal\Services\ExpressCheckout;
use App\payment\instamojo\InstamojoServiceImpl;


$payment_id = empty($_GET['payment_request_id']) ? -1 : $_GET['payment_request_id'];

$instamojo = new InstamojoServiceImpl();
$instamojo->getPaymentStatus($payment_id);





if ($instamojo->isSucceeded()==true) {
    $qry = "select * from tbl_users where id='" . $_SESSION['user_id'] . "'";

    $purpose = $instamojo->getPurpose();

    $p = explode("-",$purpose);
    $coin = $p[array_key_last($p)];
    $coin = trim($coin);

    $result = mysqli_query($mysqli, $qry);
    $user = mysqli_fetch_assoc($result);

    $user = json_decode(json_encode($user));

    $amount = $coin;

    $balance = (empty($user->wallet) ? 0 : $user->wallet) + (empty($amount) ? 0 : $amount);

    
    $qry = "select * from tbl_wallet_passbook where wp_order_id='" . $payment_id . "' and wp_user= ".$user->id." and wp_status='0' limit 1";

    $result = mysqli_query($mysqli, $qry);
    $order = mysqli_fetch_assoc($result);
    
    
    
    if (!empty($order['wp_id'])) {

        $update_wallet_query = "UPDATE tbl_users SET wallet = '$balance' WHERE id = '$user->id'";
        $update_result = mysqli_query($mysqli, $update_wallet_query);

        $qry = "update tbl_wallet_passbook set wp_status='1' where wp_id= ". $order['wp_id'];

        $result = mysqli_query($mysqli, $qry);

    }
 

    include_once __DIR__ . '/thank_you.php';
} else {
    include_once __DIR__ . '/faild.php';
}
