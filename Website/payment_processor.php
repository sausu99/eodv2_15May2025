<?php
session_start();
require __DIR__ . '/vendor/autoload.php';
include_once __DIR__ . "/includes/connection.php";
use Srmklive\PayPal\Services\ExpressCheckout;
use App\payment\instamojo\InstamojoServiceImpl;

$querySettings = "SELECT * FROM tbl_settings";
$resultSettings = mysqli_query($mysqli, $querySettings);
$rowSettings = mysqli_fetch_assoc($resultSettings);
$razorpayKey = $rowSettings['razorpay_key'];

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    foreach ($_POST as $key => $value) {
        //echo "$key: $value<br>";
    }
}

$qry = "select id, email, phone, name from tbl_users where id='" . $_SESSION['user_id'] . "'";
$result = mysqli_query($mysqli, $qry);
$user = mysqli_fetch_assoc($result);
$userEmail = $user['email'];
$userName = $user['name'];
$userPhone = $user['phone'];
$userId = $user['id'];

$pg_option = $_POST["payment_option"];
$queryPG = "SELECT * FROM tbl_payment_gateway WHERE pg_link = '" . $pg_option . "'";
$resultPG = mysqli_query($mysqli, $queryPG);
$rowPG = mysqli_fetch_assoc($resultPG);
$pgId = $rowPG['pg_id'];

switch ($_POST["payment_option"]) {
    case 'razorpay':
        ?>
        <script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
        <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
        <script type="text/javascript">
            // Function to initiate the payment
            function pay_now() {
                // Get the input values
                var name = "<?php echo $_POST['coin']; ?>";
                var current_url = "<?php echo $_POST['current_url']; ?>";
                var amount = "<?php echo $_POST['amount']; ?>";
                var item_id = "<?php echo $_POST['item_id']; ?>"; //id of the item user is purchasing
                var actual_amount = amount * 100; // Convert amount to currency subunits
                var pg_id ="<?php echo $pgId; ?>";
                var description = "Buying <?php echo $_POST['coin']; ?> coin";

                var options = {
                    "key": "<?php echo $razorpayKey; ?>",
                    "amount": actual_amount,
                    "pg_id": pg_id,
                    "currency": "INR",
                    "name": name,
                    "item_id": item_id,
                    "description": description,
                    "image": "razorpay.png",
                    "handler": function (response) {
                        console.log(response);
                        $.ajax({
                            url: 'payment-gateway/razorpay/process_payment', //<!--removed .php-->
                            type: 'POST',
                            data: {
                                'payment_id': response.razorpay_payment_id,
                                'amount': actual_amount,
                                "pg_id": pg_id,
                                'name': name,
                                'item_id': item_id
                            },
                            success: function (data) {
                                console.log(data);
                                window.location.href = 'success'; //<!--removed .php-->
                            }
                        });
                    },
                    "modal": {
                        ondismiss: function () {
                            // Redirect to the cancel URL when the payment modal is dismissed (cancelled)
                            window.location.href = current_url;
                        }
                    }
                };

                var rzp1 = new Razorpay(options);
                rzp1.on('payment.failed', function (response) {
                    window.location.href = 'failed'; //<!--removed .php-->
                });

                rzp1.open();
            }

            // Automatically trigger the payment function when the page loads
            $(document).ready(function () {
                pay_now();
            });
        </script>
        <?php

        break;

    case 'paypal':
        require_once 'PayPalHandler.php'; //<!--removed .php-->
        @session_start();
        @$_SESSION['coininfocsrf'] = $_POST["coin"];

        $protocol = isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? 'https' : 'http';
        $host = $_SERVER['HTTP_HOST'];
        $baseUrl = $protocol . '://' . $host;

        $returnUrl = $baseUrl . '/payment-gateway/paypal/process_payment?action=' . $_POST['coin'] . '&amo=' . $_POST['amount']; //<!--removed .php-->
        $cancelUrl = $_POST['current_url'];
        $amount = $_POST['amount'];
        $info = "Buying " . $_POST["coin"] . " coin";

        $paypalHandler = new PayPalHandler();
        $paypalHandler->createPayment($returnUrl, $cancelUrl, $amount, $info);


        break;

    case 'mpesa':

        include 'mpesa'; //<!--removed .php-->

        break;
        
    case "cashfree":
        require_once("payment-gateway/CashFree/process_payment"); //<!--removed .php-->
    break;

    case 'instamojo':


        if ($_POST['amount'] < 9) {
            $ERROR = "instamojo is available for payment below ₹9, please use any other payment method";
            include_once 'failed'; //<!--removed .php-->
            exit();
        }

        $instamojo = new InstamojoServiceImpl($_POST['amount'], $userEmail);


        $protocol = isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? 'https' : 'http';
        $host = $_SERVER['HTTP_HOST'];
        $uri = $_SERVER['REQUEST_URI'];
        $currentUrl = $protocol . '://' . $host . $uri;
        $arr = explode("/", $currentUrl);
        unset($arr[array_key_last($arr)]);
        $return_url = implode("/", $arr) . "/instamojo"; //<!--removed .php-->




        $instamojo
            ->setPurpose("Coins - " . $_POST['coin'])
            ->setBuyer_name($userName)
            ->setPhone_number($userPhone)
            ->setRedirect_url($return_url)
            ->setWebhook($return_url)
            //->setWebhook('https://site.com/return-instamojo.php')
        ;

        $payment = $instamojo->createPayment();


        if ($payment->isSucceeded()) {


            $pmid = $payment->getPaymentId();

            $query = "INSERT INTO `tbl_wallet_passbook` ( `wp_user`, `wp_package_id`, `wp_order_id`, `wp_date`, `wp_status`)
             VALUES (".$userId.", '".$pgId."', '" . $pmid . "',now(), '1')";

            $result = mysqli_query($mysqli, $query);


            header("location: " . $payment->getLongUrl());
        } else {
            header("location: " . $return_url);
        }


        break;

    default:
                    $ERROR = "Something went wrong";
        break;
}
