<?php
include("includes/connection.php");
session_start();

$email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_SPECIAL_CHARS);
$password = filter_input(INPUT_POST, 'password', FILTER_SANITIZE_SPECIAL_CHARS);
$countryCode = filter_input(INPUT_POST, "countryCode", FILTER_SANITIZE_SPECIAL_CHARS);
$phone = filter_input(INPUT_POST, 'phone', FILTER_SANITIZE_SPECIAL_CHARS);
$phone = preg_replace('/\D/', '', $phone);
$countryCode = preg_replace('/\D/', '', $countryCode);
if (strpos($phone, $countryCode) === 0) {
    $phone = substr($phone, strlen($countryCode));
}
$phone = ltrim($phone, '0');
$fullPhoneNumber = $countryCode . $phone;
$fullPhoneNumber = "+" . $fullPhoneNumber;

if ((empty($email)) && (empty($phone))) {
    if (empty($email))
    {
        $_SESSION['msg'] = "1"; 
    }
    else {
        $_SESSION["msg"] = "1134";
    }
    //<!--removed .php-->
    header("Location:login");
    exit;
} else if ($password == "") {
    $_SESSION['msg'] = "2"; 
    //<!--removed .php-->
    header("Location:login");
    exit;		 
} else {
    $qry = "SELECT * FROM tbl_users WHERE (email='" . $email . "' OR CONCAT(country_code, phone)='" . $phone . "') AND password='" . $password . "'";
    $result = mysqli_query($mysqli, $qry);

    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);

        // Set session variables
        $_SESSION['user_id'] = $row['id'];
        $_SESSION['user_email'] = $row['email'];
        check_activation_key($mysqli);

        // Check if share_id is provided
        $share_id = isset($_GET['share_id']) ? $_GET['share_id'] : '';
        if ($share_id) {
            // Store the share_id in the session
            $_SESSION['share_id'] = $share_id;
            
            // Query to search for the share_id in tbl_offers
            $query = "SELECT * FROM tbl_offers WHERE o_id = '$share_id'";
            $result = mysqli_query($mysqli, $query);

            // Check if the offer with the provided share_id exists
            if (mysqli_num_rows($result) > 0) {
                $row_item = mysqli_fetch_assoc($result);

                // Determine the redirect URL based on o_type value and embed the game_id parameter
                switch ($row_item['o_type']) {
                    case 1:
                    case 2:
                    case 7:
                    case 8:
                    case 10:
                    case 11:
                        $redirect_url = 'auction/share/' . $share_id;
                        break;
                    case 4:
                    case 5:
                        $redirect_url = 'lottery/share/' . $share_id;
                        break;
                    default:
                        //<!--removed .php-->
                        $redirect_url = 'home';
                }

                // Redirect the user to the determined URL
                header("Location: $redirect_url");
                exit();
            } else {
                // Offer not found, handle error or redirect to a default page
                echo "The Shared item was not found.";
                exit();
            }
        } else {
            // Generate remember_me_token
            $remember_me_token = bin2hex(random_bytes(32));

            // Update remember_me_token in database
            $update_token_query = "UPDATE tbl_users SET remember_me_token = '$remember_me_token' WHERE id = '{$row['id']}'";
            mysqli_query($mysqli, $update_token_query);

            // Set remember_me_token as a cookie (valid for 30 days)
            setcookie('remember_me_token', $remember_me_token, time() + (30 * 24 * 60 * 60), '/');

            // Reload the page using JavaScript
            //<!--removed .php-->
            echo '<script>
            if (window.self !== window.top) {
                // In an iframe
                window.top.location.reload();
            } else {
                // Not in an iframe
                window.location.href = "home";
            }
            </script>';
            exit;
        }
    } else {
        $_SESSION['msg'] = "4"; 
        //<!--removed .php-->
        header("Location:login");
        exit;
    }
}

function normalize_website_url($url) {
    $url_parts = parse_url($url);
    $scheme = isset($url_parts["scheme"]) ? $url_parts["scheme"] . "://" : '';
    $host = isset($url_parts["host"]) ? preg_replace("/^www\./", '', $url_parts["host"]) : '';
    $path = isset($url_parts["path"]) ? $url_parts["path"] : '';
    $query = isset($url_parts["query"]) ? "?" . $url_parts["query"] : '';
    $fragment = isset($url_parts["fragment"]) ? "#" . $url_parts["fragment"] : '';
    return $scheme . $host . $path . $query . $fragment;
}


function check_activation_key($mysqli) {
    $activation_key = '';
    $is_activated = false;
    $sql = "SELECT activation_key FROM tbl_settings";
    $result = $mysqli->query($sql);
    
    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $activation_key = $row["activation_key"];
        
        $current_url = $_SERVER["HTTP_HOST"];
        $normalized_current_url = normalize_website_url($current_url);
        $verify_activation_url = "https://verify.wowcodes.in/verifyPurchase.php?activation_key={$activation_key}&website={$normalized_current_url}";
        
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $verify_activation_url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $verify_response = curl_exec($ch);
        curl_close($ch);
        
        $verify_result = json_decode($verify_response, true);
        if ($verify_result && $verify_result["success"] == 1) {
            $is_activated = true;
        } else {
            $sql_clear = "UPDATE tbl_settings SET activation_key = NULL";
            if ($mysqli->query($sql_clear) === TRUE) {
                $is_activated = false;
                $activation_key = '';
            }
        }
    }
    return $is_activated;
}
?>