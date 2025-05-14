<?php
define('STRIPE_SECRET_KEY','sk_test_51Co0bzI3O7hxGfOZOYR839K0RpNOtlJTfpGHvswzGOhIbxpSNeFvqT82LK4FyCckDBx8vxEpb7u6MP7ecVIDzbjW00di5mgM4s');
define('STRIPE_PUBLIC_KEY','pk_test_p6lV07clvonkBrq8tUYCckB5');
header('Content-Type: application/json');
$results = array();
require 'vendor/autoload.php';
\Stripe\Stripe::setApiKey(STRIPE_SECRET_KEY);
try{
    $products  = \Stripe\Plan::all();
    $results['response'] = "Success";
    $results['plans'] = $products->data;

}catch (Exception $e){
    $results['response'] = "Error";
    $results['plans'] = $e->getMessage();
}
echo json_encode($results);