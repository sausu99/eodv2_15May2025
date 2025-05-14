<?php
include('includes/header.php');
include("includes/session_check.php");

include('includes/function.php');
include('language/language.php');
include("includes/connection.php");


// Assuming you have a session variable for user ID
$user_id = isset($_SESSION['user_id']) ? intval($_SESSION['user_id']) : 0;

// Check if the 'game_id' parameter is set in the URL
$game_id = isset($_GET['o']) ? intval($_GET['o']) : 0;
$item_id = isset($_GET['i']) ? intval($_GET['i']) : 0;

$winner_check_query = "SELECT p.o_id, p.item_id, i.o_image, i.o_name FROM tbl_winners w
                                                 LEFT JOIN tbl_prizes p ON w.winner_rank BETWEEN p.rank_start AND p.rank_end
                                                 LEFT JOIN tbl_offers o ON p.o_id = o.o_id
                                                 LEFT JOIN tbl_items i ON p.item_id = i.item_id
                                                 WHERE w.u_id = '$user_id' AND p.item_id = '$item_id' AND p.o_id = '$game_id'";
$winner_check_result = mysqli_query($mysqli, $winner_check_query);

if (mysqli_num_rows($winner_check_result) > 0) {
    $winnerRow = mysqli_fetch_assoc($winner_check_result);

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $street_address = mysqli_real_escape_string($mysqli, $_POST['street_address']);
        $apt_suite = mysqli_real_escape_string($mysqli, $_POST['apt_suite']);
        $city = mysqli_real_escape_string($mysqli, $_POST['city']);
        $pin_code = mysqli_real_escape_string($mysqli, $_POST['pin_code']);
        $full_address = "$street_address, $apt_suite, $city, $pin_code";

        // Check for existing order
        $checkOrderQuery = "SELECT COUNT(*) as orderCount FROM tbl_order WHERE u_id = '$user_id' AND offer_o_id = $game_id AND offer_id = $item_id";
        $checkOrderResult = mysqli_query($mysqli, $checkOrderQuery);
        $checkOrderRow = mysqli_fetch_assoc($checkOrderResult);

        if ($checkOrderRow['orderCount'] > 0) {
            $_SESSION['msg'] = "something went wrong";
            header('Location: winnings.php');
            exit;
        } else {
            // Fetch offer details
            $offer_query = "SELECT o_price, winning_value, id FROM tbl_offers WHERE o_id = $game_id";
            $offer_result = mysqli_query($mysqli, $offer_query);
            $offer_row = mysqli_fetch_assoc($offer_result);

            $seller_id = $offer_row['id'];
            $total_amount = $offer_row['o_price'];
            $pay_amount = $offer_row['winning_value'];
            $dis_amount = $total_amount - $pay_amount;

            // Insert new order
            $stmt = $mysqli->prepare("INSERT INTO tbl_order (offer_o_id, offer_id, seller_id, u_id, o_address, total_amount, pay_amount, dis_amount, order_status, order_date, o_status) 
                                      VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, NOW(), 1)");
            $stmt->bind_param("iiiissdd", $game_id, $item_id, $seller_id, $user_id, $full_address, $total_amount, $pay_amount, $dis_amount);

            if ($stmt->execute()) {
                $_SESSION['msg'] = "claimSuccess";
                header('Location: winnings.php');
                exit;
            } else {
                echo "Error: " . $stmt->error;
            }
        }
    }
} else {
        $title = $client_lang['not_allowed'];
        $description = '';
        $image = 'no_person.gif';
        include("nodata.php");
        exit;
}

?>

<!-- Include your HTML code for the claim page with the address form -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $client_lang['claimPrize']; ?></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
            margin: auto; /* Center the form horizontally */
            margin-top: 50px; /* Adjust top margin as needed */
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        button {
            background-color: #007bff;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<!-- Your HTML form for user address -->
<form method="post" action="">
    <label for="street_address"><?php echo $client_lang['address1']; ?>:</label>
    <input type="text" id="street_address" name="street_address" required>

    <label for="apt_suite"><?php echo $client_lang['address2']; ?>:</label>
    <input type="text" id="apt_suite" name="apt_suite">

    <label for="city"><?php echo $client_lang['city']; ?>:</label>
    <input type="text" id="city" name="city" required>

    <label for="pin_code"><?php echo $client_lang['zipCode']; ?>:</label>
    <input type="text" id="pin_code" name="pin_code" required>

    <button type="submit"><?php echo $client_lang['claim']; ?></button>
</form>

</body>
</html>

<?php include('includes/footer.php'); ?>
