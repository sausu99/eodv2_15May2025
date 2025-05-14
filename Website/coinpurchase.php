<?php
include('includes/header.php');
include("includes/session_check.php");
include("includes/connection.php");
include('includes/function.php');
include('language/language.php');


// Get the c_id from the URL parameter
$c_id = isset($_GET['c_id']) ? intval($_GET['c_id']) : 0; // Sanitize the c_id input by ensuring it's an integer
$user_id = $_SESSION['user_id'];
// Fetch item details based on c_id
$query = "SELECT * FROM tbl_coin_list WHERE c_id = ?";
$stmt = mysqli_prepare($mysqli, $query);
mysqli_stmt_bind_param($stmt, "i", $c_id);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$row = mysqli_fetch_assoc($result); // Fetch the data into an associative array

if (isset($_SESSION['user_id'])) {
    $qry = "SELECT * FROM tbl_users WHERE id = ?";
    $user_stmt = mysqli_prepare($mysqli, $qry);
    mysqli_stmt_bind_param($user_stmt, "i", $_SESSION['user_id']);
    mysqli_stmt_execute($user_stmt);
    $user_result = mysqli_stmt_get_result($user_stmt);
    $session_row = mysqli_fetch_assoc($user_result);
}

function getCurrencyFromSettings($mysqli)
{
    // Query to fetch the currency from tbl_settings
    $querySet = "SELECT currency,demo_access FROM tbl_settings";

    // Execute the query
    $resultSet = mysqli_query($mysqli, $querySet);

    if ($resultSet) {
        // Check if a row is returned
        if (mysqli_num_rows($resultSet) > 0) {
            // Fetch the currency value from the result
            $rowSet = mysqli_fetch_assoc($resultSet);
            return $rowSet['currency'];
        }
    }

    // Return a default currency value or handle errors as needed
    return '$'; // Default currency value (you can change this)

}
// Retrieve the currency symbol from the settings
$currency = getCurrencyFromSettings($mysqli);

// Function to get available payment gateways
function getPaymentGateways($mysqli) {
    $query = "SELECT * FROM tbl_payment_gateway WHERE pg_type != 1 AND pg_status = 1";
    $result = mysqli_query($mysqli, $query);
    $gateways = array();

    if ($result && mysqli_num_rows($result) > 0) {
        while ($row = mysqli_fetch_assoc($result)) {
            $gateways[] = $row;
        }
    }

    return $gateways;
}

// Retrieve payment gateways
$paymentGateways = getPaymentGateways($mysqli);

$email_query = "SELECT admin_email, demo_access FROM tbl_settings";
$email_result = mysqli_query($mysqli, $email_query);
$email_info = mysqli_fetch_assoc($email_result);

ini_set('display_errors', '1');
ini_set('display_startup_errors', '1');
error_reporting(E_ALL);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title><?php echo $client_lang['orderSummary']; ?></title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }

        .background-container {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin: 50px auto;
            padding: 20px;
            width: 80%;
            max-width: 1200px;
        }

        .item-details-container {
            display: flex;
            justify-content: space-between;
            border: 1px solid #ddd;
            border-radius: 8px;
            overflow: hidden;
        }

        .left-side {
            width: 50%;
        }

        .right-side {
            width: 50%;
            padding: 20px;
        }

        img {
            width: 100%;
            border-radius: 8px;
            margin-bottom: 20px;
            display: block;
        }

        h2 {
            font-size: 2em;
            margin-bottom: 10px;
        }

        p {
            font-size: 1.2em;
            color: #333;
        }

        .btn {
            padding: 10px 20px;
            font-size: 1.2em;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .btn-success {
            background-color: #28a745;
            color: white;
        }

        .btn-block {
            display: block;
            width: 100%;
        }

        .form-check {
            margin-bottom: 10px;
        }

        .form-check-input {
            margin-right: 10px;
        }

        .form-check-label {
            font-size: 1.2em;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        @media (max-width: 768px) {
            .item-details-container {
                flex-direction: column;
            }

            .left-side,
            .right-side {
                width: 100%;
            }

            img {
                width: 100%;
            }
        }

        .payment-methods {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
            gap: 20px;
            align-items: center;
            justify-content: center;
        }

.payment-option {
    position: relative;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;
    padding: 10px;
    justify-content: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
}

.payment-option:hover {
    transform: translateY(-3px);
    box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2);
}

.payment-label {
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    cursor: pointer;
}

.payment-logo-img {
    margin-bottom: 5px;
    height: 50px;
    width: auto;
}

.payment-name {
    font-size: 0.9em;
    text-transform: capitalize;
    margin-top: 5px;
}

.payment-option input[type="radio"]:checked + .payment-label::after {
    content: '\2713'; /* Unicode for checkmark */
    font-size: 1.5em;
    color: #007bff;
    position: absolute;
    top: 10px;
    right: 10px;
}

.payment-option input[type="radio"] {
    appearance: none; /* Remove the default radio button appearance */
}



        .button-container {
            grid-column: 1 / -1;
        }

        .coming-soon-header {
            grid-column: 1 / -1;
        }

        .coming-soon {
            opacity: 0.5;
            cursor: not-allowed;
        }

        .no-cursor {
            cursor: not-allowed;
        }

        .btn-blue {
            background-color: #007bff;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-blue:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <?php
    $currentURL = "http" . (isset($_SERVER["HTTPS"]) && $_SERVER["HTTPS"] == "on" ? "s" : "") . "://";
    $currentURL .= $_SERVER["SERVER_PORT"] != "80" ? $_SERVER["SERVER_NAME"] . ":" . $_SERVER["SERVER_PORT"] . $_SERVER["REQUEST_URI"] : $_SERVER["SERVER_NAME"] . $_SERVER["REQUEST_URI"];
    ?>

    <div class="background-container">
        <?php if ($row) { ?>
            <div class="item-details-container">
                <div class="left-side">
                    <img src="../seller/images/<?php echo $row['c_image']; ?>" alt="Item Image" width="400" height="400">
                    <h2>&nbsp;&nbsp;<?php echo $row['c_name']; ?></h2>
                    <p>&nbsp;&nbsp;<?php echo $row['c_coin'] . ' ' . $auction_lang['coins'] . ' ' . $invest_lang['for'] . ' ' . $currency . $row['c_amount']; ?></p>
                </div>
                <div class="right-side"><!--removed .php-->
                    <form id="payment-form" action="payment_processor" method="post" onsubmit="return validatePayment();">
                        <input type="hidden" name="amount" value="<?php echo $row['c_amount']; ?>">
                        <input type="hidden" name="item_id" value="<?php echo $row['c_id']; ?>">
                        <input type="hidden" name="coin" value="<?php echo $row['c_coin']; ?>">
                        <input type="hidden" name="current_url" value="<?php echo $currentURL; ?>">
                        <input type="hidden" id="pg_id" name="pg_id" value="">
                        <h3><?php echo $client_lang['orderPaymentMode']; ?></h3>
                        <hr>
                        <div class="payment-methods">
                            <?php
                            foreach ($paymentGateways as $gateway) {
                                echo '<div class="payment-option">';
                                echo '<input type="radio" name="payment_option" id="pg_' . $gateway['pg_id'] . '" value="' . $gateway['pg_link'] . '" data-pg-type="' . $gateway['pg_type'] . '" data-pg-id="' . $gateway['pg_id'] . '">';
                                echo '<label for="pg_' . $gateway['pg_id'] . '" class="payment-label">';
                                echo '<div class="payment-logo">';
                                echo '<img src="seller/images/' . $gateway['pg_image'] . '" alt="' . $gateway['pg_name'] . ' Logo" class="payment-logo-img" onerror="this.onerror=null;this.src=\'placeholder.jpg\';">';
                                echo '</div>';
                                echo '<div class="payment-name">' . '&nbsp;' . $gateway['pg_name'] . '</div>';
                                echo '</label>';
                                echo '</div>';
                            }
                            ?>
                        </div>
                        <br>
                        <button type="submit" class="btn btn-success btn-block" <?php echo ($email_info['demo_access'] == 1) ? 'disabled' : ''; ?>>
                            <?php echo ($email_info['demo_access'] == 1) ? $client_lang['demoModeText'] : $client_lang['orderPayment']; ?>
                        </button>
                    </form>

                </div>
            </div>
        <?php } else { ?>
            <p>Item not found.</p>
        <?php } ?>
    </div>

    <script>
        function validatePayment() {
        var selectedOption = document.querySelector('input[name="payment_option"]:checked');
        if (!selectedOption) {
            alert("Select a payment gateway!");
            return false;
        }
    
        var pgType = selectedOption.getAttribute('data-pg-type');
        var pgId = selectedOption.getAttribute('data-pg-id');
    
        if (pgType == '4') {
            window.location.href = 'payment.php?id=' + pgId + '&coin_id=' + <?php echo $c_id; ?> + '&user_id=' + <?php echo $user_id; ?>;
            return false;
        }
    
        return true;
    }
    </script>
</body>
</html>