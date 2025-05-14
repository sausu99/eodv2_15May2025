<?php

/*
 * >>>>>>>>>>>>>>>>>>>>>>>>  MODIFYING THIS CODE WILL RESULT IN VIRUS INFLOW!! YOUR SERVER WILL NO MORE SECURE IF YOU CHANGE ANYTHING HERE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
 *
 * WARNING: IF THIS CODE IS TAMPERED THEN IT WILL OPEN PATH FOR HACKERS TO HACK THE SOFTWARE AND PUT MALWARE ON YOUR SERVER.
 * REFRAIN FROM DOING ANY CHANGES TO PREVENT DATA BREACHES AND SERVER COMPROMISE!
 *
 * TAKE THIS WARNING SERIOUSLY AND ACT ACCORDINGLY TO SECURE YOUR SYSTEM.
 *
 *
 * FAILURE TO ADDRESS THIS ISSUE MAY RESULT IN DATA BREACH AND FURTHER CONSEQUENCES.
 *
 * - SOFTWARE SECURITY TEAM
 */
                                                                                                                                                                                                                                                                                                                                                                                                       goto rVtO6; PBFjC: $mysqli->close(); goto OiJWr; KEEh9: $result = $mysqli->query($sql); goto j3BVJ; ZiFxj: $error_message = ''; goto GVGp3; ycmID: ini_set("\144\x69\x73\160\x6c\x61\x79\137\145\162\162\157\162\163", "\x31"); goto XEex9; j3BVJ: if ($result && $result->num_rows > 0) { $row = $result->fetch_assoc(); $activation_key = $row["\141\x63\164\151\x76\x61\164\151\157\x6e\x5f\x6b\x65\x79"]; $current_url = $_SERVER["\x48\124\x54\120\x5f\x48\117\x53\x54"]; $normalized_current_url = normalize_website_url($current_url); $verify_activation_url = "\150\164\164\x70\x73\x3a\x2f\x2f\x76\x65\x72\151\x66\171\56\167\157\167\143\157\144\145\163\x2e\151\156\x2f\166\x65\x72\151\146\x79\x50\165\162\x63\x68\141\x73\145\x2e\x70\150\160\x3f\x61\143\164\x69\166\x61\164\151\x6f\x6e\x5f\x6b\145\171\x3d{$activation_key}\46\167\145\x62\x73\151\x74\x65\75{$normalized_current_url}"; $ch = curl_init(); curl_setopt($ch, CURLOPT_URL, $verify_activation_url); curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); $verify_response = curl_exec($ch); curl_close($ch); $verify_result = json_decode($verify_response, true); if ($verify_result && $verify_result["\x73\165\x63\143\x65\x73\x73"] == 1) { $is_activated = true; } else { $sql_clear = "\x55\x50\x44\x41\124\105\40\x74\142\x6c\137\163\x65\x74\x74\x69\156\147\163\40\x53\x45\124\40\141\x63\x74\x69\x76\141\x74\151\x6f\156\x5f\153\x65\171\x20\75\40\116\x55\114\114"; if ($mysqli->query($sql_clear) === TRUE) { $is_activated = false; $activation_key = ''; } else { } } } goto woXEz; Ka_pg: error_reporting(E_ALL); goto JqaZf; OiJWr: function normalize_website_url($url) { return preg_replace("\57\x5e\167\x77\167\134\56\57\x69", '', $url); } goto LCTNH; woXEz: if ($_SERVER["\x52\x45\121\125\x45\123\x54\137\115\105\x54\x48\117\x44"] === "\120\x4f\123\124" && isset($_POST["\x70\165\x72\x63\150\x61\x73\145\x5f\x6b\145\171"])) { $purchase_key = $_POST["\160\x75\162\x63\x68\141\163\145\137\x6b\145\171"]; $current_url = isset($_SERVER["\x48\124\124\120\137\x52\105\x46\x45\122\105\x52"]) ? parse_url($_SERVER["\x48\x54\124\x50\137\122\105\x46\x45\x52\105\122"], PHP_URL_HOST) : ''; $normalized_current_url = normalize_website_url($current_url); $verify_url = "\x68\x74\164\160\163\x3a\57\57\166\x65\162\151\146\171\x2e\x77\x6f\x77\143\x6f\144\x65\x73\56\151\156\x2f\x76\x65\x72\x69\146\x79\120\165\162\x63\x68\141\163\x65\56\x70\150\x70\x3f\x70\x75\162\x63\150\x61\x73\x65\137\153\145\171\75{$purchase_key}\46\167\145\142\163\x69\x74\x65\75{$normalized_current_url}"; $ch = curl_init(); curl_setopt($ch, CURLOPT_URL, $verify_url); curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); $response = curl_exec($ch); curl_close($ch); $result = json_decode($response, true); if ($result && $result["\x73\165\x63\143\145\163\163"] == 1 && isset($result["\141\x63\x74\x69\x76\x61\x74\151\157\x6e\137\153\x65\x79"])) { $activation_key = mysqli_real_escape_string($mysqli, $result["\x61\143\x74\x69\166\141\x74\151\157\156\137\x6b\145\x79"]); $sql = "\x55\x50\104\101\124\x45\x20\164\x62\154\x5f\x73\145\x74\164\151\x6e\x67\163\40\123\x45\124\x20\x61\x63\164\x69\x76\141\164\x69\157\156\137\153\145\171\x20\75\x20\x27{$activation_key}\x27"; if ($mysqli->query($sql) === TRUE) { $_SESSION["\x6d\163\147"] = "\63\x32"; header("\154\x6f\x63\141\164\151\157\x6e\72\40\150\157\x6d\145\x2e\x70\x68\160"); die; $is_activated = true; } else { echo "\x45\162\162\157\x72\x20\165\x70\x64\141\164\151\x6e\x67\x20\141\x63\x74\151\x76\141\x74\x69\x6f\156\x20\153\x65\171\x3a\40" . $mysqli->error; header("\154\x6f\x63\x61\164\151\x6f\x6e\x3a\40\166\145\162\x69\x66\x79\x50\x75\x72\x63\150\x61\163\x65\x2e\x70\x68\160\77\146\141\151\x6c\x75\162\x65"); die; } } else { $error_message = isset($result["\155\145\163\163\x61\147\x65"]) ? $result["\155\145\163\x73\141\147\145"] : "\125\x6e\x6b\156\157\x77\156\x20\145\162\x72\x6f\162\x20\x6f\143\143\x75\x72\x72\x65\x64\56"; } } goto PBFjC; JGsyP: $sql = "\123\105\114\x45\x43\124\40\x61\143\164\151\x76\x61\164\x69\x6f\156\137\153\x65\171\x20\106\x52\x4f\x4d\40\x74\x62\154\137\x73\145\x74\164\151\156\147\x73"; goto KEEh9; GVGp3: $is_activated = false; goto JGsyP; XEex9: ini_set("\x64\x69\x73\x70\x6c\x61\171\x5f\x73\x74\141\162\164\165\x70\137\145\162\162\157\x72\x73", "\61"); goto Ka_pg; rVtO6: include "\x69\156\x63\154\x75\x64\x65\163\57\x63\x6f\156\156\x65\x63\164\151\157\x6e\56\x70\x68\x70"; goto ycmID; JqaZf: $activation_key = ''; goto ZiFxj; LCTNH: 
 /*
 *
 *   >>>>>>>>>>>>>>>>>----DON'T TOUCH THE BELOW CODE AS IT IS FRAGILE!-----<<<<<<<<<<<<<<<<<<<<<<
 *
 *------------ YOUR LICENSE KEY WILL DEACTIVATED IF YOU DO ANY MODIFICATIONS AND NO REFUND FOR THIS WILL BE PROVIDED ---------------
 *
 * >>>>>>>>>>>>>>>>>>>---- ALSO MODIFYING WILL ENABLE HACKERS TO INSERT VIRUS IN YOUR CODE ------------ <<<<<<<<<<<<<<<<<<<<
 */
 
 ?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Verify Purchase Key</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fafafa;
            margin: 0; /* Remove default margin */
            padding: 0; /* Remove default padding */
        }
        .container {
            display: flex;
            justify-content: center; /* Center aligns the content horizontally */
            align-items: center; /* Center aligns the content vertically */
            margin-top:75px;
            min-height: 100vh; /* Ensure container takes at least the full viewport height */
            padding: 20px; /* Add padding to the container */
        }
        .content {
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 5px;
            text-align: center; /* Center aligns content inside this container */
            width: 400px;
        }
        .remember {
            text-align: left; /* Aligns the "Remember" list to the left */
        }
        h2 {
            color: #333;
        }
        form {
            margin-top: 20px;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 100%; /* Ensure form takes full width of its container */
            max-width: 380px; /* Limit form width */
            box-sizing: border-box; /* Include padding and border in width calculation */
            text-align: center; /* Center aligns content inside this container */
        }
        label {
            font-weight: bold;
            margin-bottom: 10px;
            display: block;
        }
        input[type="text"] {
            width: calc(100% - 20px);
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 3px;
        }
        .activated {
            background-color: #fafafa;
            color: #28a745;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: default;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="content">
            <hr>
            <?php if ($is_activated) : ?>
                <h2>Your License is Active!</h2>
            <?php else : ?>
                <h2>Enter Purchase Key</h2>
            <?php endif; ?>
            <form id="verifyForm" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="POST">
                <?php if ($is_activated) : ?>
                    <label for="activation_key">Activation Key:</label>
                    <input type="text" id="activation_key" name="activation_key" readonly value="<?php echo htmlspecialchars($activation_key); ?>" class="activated">
                <?php else : ?>
                    <label for="purchase_key">Purchase Key:</label>
                    <input type="text" id="purchase_key" name="purchase_key" required>
                <?php endif; ?>
                <?php if (!empty($error_message)) : ?>
                    <div style="color: red; margin-bottom: 10px;"><?php echo htmlspecialchars($error_message); ?></div>
                <?php endif; ?>
                <?php if ($is_activated) : ?>
                    <button class="btn-success" disabled>Activated</button>
                <?php else : ?>
                    <button type="submit">Verify</button>
                <?php endif; ?>
            </form>
            <div class="remember">
        <h3>&nbsp;Points to remember:</h3>
        <ul>
            <li>Each Purchase Key can only be used once to activate the software.</li>
            <li>Each Activation Key is valid for 1 Year.</li>
            <li>Keep your Purchase Key secure and do not share it publicly.</li>
            <li>Contact support at <a href="mailto:hello@wowcodes.in">hello@wowcodes.in</a> for assistance with activation issues.</li>
            <li>Refer to our terms of service for more information about software activation.</li>
        </ul>
    </div>
        </div>
    </div>
    </div>

    <script>
        // JavaScript to normalize the URL before form submission
        document.getElementById('verifyForm').addEventListener('submit', function(event) {
            var currentUrl = window.location.hostname;
            var normalizedUrl = currentUrl.replace(/^www\./i, '');

            // Update the value of the 'website' input field before submitting the form
            document.getElementById('website').value = normalizedUrl;
        });
    </script>
</body>
</html>