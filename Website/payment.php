<?php
include("includes/connection.php");
include('includes/function.php');

// ini_set('display_errors', '1');
// ini_set('display_startup_errors', '1');
// error_reporting(E_ALL);

// Check if payment ID is provided in the URL
if(isset($_GET['id'])) {
    // Sanitize the input to prevent SQL injection
    $payment_gateway_id = mysqli_real_escape_string($mysqli, $_GET['id']);

    // Query to fetch payment gateway details based on payment gateway ID
    $query_payment_gateway = "SELECT * FROM tbl_payment_gateway WHERE pg_id = '$payment_gateway_id'";
    $result_payment_gateway = mysqli_query($mysqli, $query_payment_gateway);

    // Check if payment gateway exists
    if(mysqli_num_rows($result_payment_gateway) > 0) {
        $payment_gateway = mysqli_fetch_assoc($result_payment_gateway);
        $payment_details = $payment_gateway['pg_details'];
    } else {
        $payment_details = "Not Found";
    }
} else {
    // Redirect if payment gateway ID is not provided
    header("Location: index.php"); // Redirect to homepage or another appropriate page
    exit();
}

// Fetch currency settings
$settingsQuery = "SELECT currency FROM tbl_settings";
$settingsResult = mysqli_query($mysqli, $settingsQuery);
$settingsRow = mysqli_fetch_assoc($settingsResult);
$currency = $settingsRow['currency'];

if(isset($_GET['coin_id'])) {
    $item_type = 1; // Coin Purchase
    $user_id = $_GET['user_id'];
    $item_id = $_GET['coin_id'];
    $address_id = $_GET['address_id'];

    $coinQuery = "SELECT * FROM tbl_coin_list WHERE c_id = $item_id";
    $coinResult = mysqli_query($mysqli, $coinQuery);
    $coinRow = mysqli_fetch_assoc($coinResult);

    $amount = $coinRow['c_amount'];
    $coinValue = $coinRow['c_coin'];
    $itemName = $coinRow['c_name'];
} else if(isset($_GET['item_id'])) {
    $item_type = 2; // Item Purchase
    $user_id = $_GET['user_id'];
    $item_id = $_GET['item_id'];
    $address_id = $_GET['address_id'];

    $itemQuery = "SELECT * FROM tbl_offers LEFT JOIN tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_id = $item_id";
    $itemResult = mysqli_query($mysqli, $itemQuery);
    $itemRow = mysqli_fetch_assoc($itemResult);

    $amount = ($itemRow['o_buy'] == 0) ? $itemRow['o_amount'] : $itemRow['o_buy'];
    $itemName = $itemRow['o_name'];
}

$stmt = $mysqli->prepare("SELECT * FROM tbl_address WHERE address_id = ? AND u_id = ?");
$stmt->bind_param("ii", $address_id, $user_id); // "ii" means both params are integers
$stmt->execute();
$result = $stmt->get_result();
$addressRow = $result->fetch_assoc();

$addressLine1 = $addressRow['address_line1'];
$addressLine2 = $addressRow['address_line2'];
$city = $addressRow['city'];
$state = $addressRow['state'];
$postal_code = $addressRow['postal_code'];
$country = $addressRow['country'];
$final_address = $addressLine1.', '.$addressLine2.', <br>'.$city.', '.$state.', <br>'.$postal_code.', '.$country;


// Handle form submission
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $transaction_id = mysqli_real_escape_string($mysqli, $_POST['transaction_id']);
    $payment_date = mysqli_real_escape_string($mysqli, $_POST['payment_date']);
    $upload_dir = 'seller/images/'; // Directory where the file will be saved
    $upload_file = $upload_dir . basename($_FILES["payment_screenshot"]["name"]);
    $file_name = basename($_FILES["payment_screenshot"]["name"]);

    // Check if the transaction ID already exists
    $checkTransactionQuery = "SELECT * FROM tbl_manual_payments WHERE transaction_id = '$transaction_id'";
    $checkTransactionResult = mysqli_query($mysqli, $checkTransactionQuery);
    
    if (mysqli_num_rows($checkTransactionResult) > 0) {
        $error_message = "Order Already Processed for this transaction id. Please use a different ID.";
    } else {
        // Check if file is a valid image
        $check = getimagesize($_FILES["payment_screenshot"]["tmp_name"]);
        if ($check !== false) {
            if (move_uploaded_file($_FILES["payment_screenshot"]["tmp_name"], $upload_file)) {
                // Insert payment details into database
                $query_insert_payment = "INSERT INTO tbl_manual_payments (pg_id, transaction_id, amount, user_id, item_id, item_type, payment_date, payment_screenshot, transaction_status) VALUES ('$payment_gateway_id', '$transaction_id', '$amount', '$user_id', '$item_id', '$item_type', '$payment_date', '$file_name', 1)";
                if (mysqli_query($mysqli, $query_insert_payment)) {
                    $success_message = "Payment details submitted successfully!<br>We will verify the payment and process your order soon!";
                } else {
                    $error_message = "Error submitting payment details: " . mysqli_error($mysqli);
                }
            } else {
                $error_message = "Error uploading file.";
            }
        } else {
            $error_message = "File is not an image.";
        }
    }
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 800px;
            margin: 20px auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            color: #333;
        }

        form {
            display: none; /* Initially hide the form */
            flex-direction: column;
            align-items: center;
        }

        label {
            margin: 10px 0 5px;
            font-weight: bold;
            color: #555;
        }

        input[type="text"], input[type="date"], input[type="number"], input[type="file"] {
            width: 70%;
            padding: 10px;
            margin: 5px 0 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
        }

        input[type="submit"], .paid-button {
            width: 200px;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            text-align: center;
            text-decoration: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 10px;
        }

        input[type="submit"]:hover, .paid-button:hover {
            background-color: #45a049;
        }

        .message {
            text-align: center;
            color: green;
            font-weight: bold;
        }

        .error {
            text-align: center;
            color: red;
            font-weight: bold;
        }

        hr {
            border: 0;
            border-top: 1px solid #eee;
            margin: 20px 0;
        }

        .instructions {
            text-align: center;
            color: #666;
            font-size: 18px;
        }
    </style>
    <script>
        function showForm() {
            document.getElementById('paymentForm').style.display = 'flex';
            document.getElementById('paidButton').style.display = 'none';
        }
    </script>
</head>
<body>
    <div class="container">
    <h1>Payment Details</h1>
    <hr><br>
    <p class="instructions"><?php echo $payment_details; ?></p>
    <p>You are Buying: <?php echo $itemName; ?> <?php if ($item_type == 1) { echo "Pack"; }?> </p>
    <?php if ($item_type == 1) { echo "<p>Coins you will receive: $coinValue Coins</p>"; }?>
    <p>Amount to Pay: <?php echo $currency . $amount; ?></p>
    <hr>

    <?php if (!isset($_GET['coin_id'])): // Check if coin_id is not set ?>
        <div style="display: flex;">
            <div style="font-weight: bold; padding-right: 10px;">Shipping Address:</div>
            <div><?php echo $final_address; ?></div>
        </div>
    <?php endif; ?>

    <?php
    if (isset($success_message)) {
        echo "<p class='message'>$success_message</p>";
    }
    if (isset($error_message)) {
        echo "<p class='error'>$error_message</p>";
    }
    ?>
    <center>
        <?php if (!isset($success_message)) { ?>
            <button id="paidButton" class="paid-button" onclick="showForm()">Paid? Click here</button>
        <?php } ?>
    </center>
    <form id="paymentForm" method="post" action="" enctype="multipart/form-data">
        <h3>Fill this form so that we can confirm the payment</h3>
        <label for="transaction_id">Transaction ID</label>
        <input type="text" id="transaction_id" name="transaction_id" required>

        <label for="payment_date">Payment Date</label>
        <input type="date" id="payment_date" name="payment_date" required>

        <label for="payment_screenshot">Upload Payment Screenshot</label>
        <input type="file" id="payment_screenshot" name="payment_screenshot" required>

        <input type="submit" value="Submit">
    </form>
</div>
</body>
</html>
