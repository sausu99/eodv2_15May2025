<?php
require 'vendor/autoload.php';
\Stripe\Stripe::setApiKey('sk_test_51NA6XPDO6wEedkgXD3suYMoCXZDovRyNyLLGihIhlGJF8kK93PkdgroTUO8OrLqDgb0gQdWHiXdu7PsQBZFrKxeL00qEsLaOkB');
$entityBody = file_get_contents('php://input');
$postdata = json_decode($entityBody);
$amount = ($postdata->amount) * 100;
$intent = \Stripe\PaymentIntent::create([
  'amount' => $amount,
  'currency' => 'usd',
]);
 $client_secret = $intent->client_secret;
$arr = array('clientSecret'=>$client_secret);
echo json_encode($arr);
?>


