<?php 
include("includes/header.php");
// include("includes/session_check.php");

require("includes/function.php");
require("language/language.php");

if(isset($_SESSION['user_id'])) {
    $user_id = sanitize($_SESSION['user_id']);
    
    $qry = "SELECT * FROM tbl_users WHERE id='".$user_id."'";
    $result = mysqli_query($mysqli, $qry);
    $row = mysqli_fetch_assoc($result);
}

$user_id = sanitize($_SESSION['user_id']);

if(isset($_POST['submit'])) {
    if($_FILES['image']['name'] != "") {
        $img_res = mysqli_query($mysqli, "SELECT * FROM tbl_users WHERE id=".$user_id);
        $img_res_row = mysqli_fetch_assoc($img_res);

        if($img_res_row['image'] != "") {
            unlink('/seller/images/'.$img_res_row['image']);
        }

        // Validate the image file
        if (isValidImage($_FILES['image'])) {
            $image = rand(0, 99999) . "_" . $img_res_row['name'] . '.png';
            $pic1 = $_FILES['image']['tmp_name'];
            $tpath1 = '/seller/images/'.$image;

            // Move the uploaded file
            if(move_uploaded_file($pic1, $_SERVER['DOCUMENT_ROOT'].$tpath1)) {
                // Create a thumbnail
                $thumbnail_path = '/seller/images/thumbnail_'.$image;
                createThumbnail($_SERVER['DOCUMENT_ROOT'].$tpath1, $_SERVER['DOCUMENT_ROOT'].$thumbnail_path, 150);

                $data = array(
                    'name' => sanitize($_POST['name']),
                    'password' => sanitize($_POST['password']),
                    //'email' => sanitize($_POST['email']),
                    //'phone' => sanitize($_POST['phone']),
                    //'country_code' => sanitize($_POST['countryCode']),
                    'image' => $image
                );

                $channel_edit = Update('tbl_users', $data, "WHERE id = '".sanitize($_SESSION['user_id'])."'"); 
            } else {
                echo "File upload failed.";
            }
        } else {
            echo "Invalid image file.";
        }
    } else {
        $data = array(
            'name' => sanitize($_POST['name']),
            'password' => sanitize($_POST['password']),
            //'email' => sanitize($_POST['email']),
            //'phone' => sanitize($_POST['phone']),
            //'country_code' => sanitize($_POST['countryCode'])
        );

        $channel_edit = Update('tbl_users', $data, "WHERE id = '".sanitize($_SESSION['user_id'])."'"); 
    }

    $_SESSION['msg'] = "profileUpdateSuccess"; 
      //<!--removed .php-->
    header("Location: profile");
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $client_lang['yourProfile']; ?></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@23.1.0/build/css/intlTelInput.css">
    <style>
        .profile-edit-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .profile-edit-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .profile-edit-header h1 {
            margin: 0;
            font-size: 24px;
        }

        .save-button {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }

        .profile-picture-section {
            text-align: center;
            margin-bottom: 20px;
        }

        .profile-picture-wrapper {
            position: relative;
            display: inline-block;
        }

        #profile-picture {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
        }

        .edit-icon {
            position: absolute;
            bottom: 0;
            right: 0;
            background-color: rgba(0, 0, 0, 0.6);
            border-radius: 50%;
            padding: 5px;
            cursor: pointer;
        }

        .edit-icon i {
            color: white;
            font-size: 18px;
        }

        .file-upload-input {
            display: none;
        }

        .profile-info-section {
            display: flex;
            flex-direction: column;
        }

        .profile-info-section label {
            margin-bottom: 5px;
            font-weight: bold;
        }

        .profile-info-section input {
            margin-bottom: 15px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .iti--separate-dial-code {
            width: 100%;
        }
        .readonly-field {
            background-color: #f0f0f0; /* Light grey background */
            color: #6c757d;            /* Grey text color */
            border: 1px solid #ced4da; /* Grey border */
            cursor: not-allowed;       /* Change cursor to indicate read-only */
        }
    </style>
</head>
<body>
    <div class="profile-edit-container">
        
        <form action="" name="editprofile" method="post" enctype="multipart/form-data" id="profile-form">
            <div class="profile-edit-header">
                <h2><?php echo $client_lang['yourProfile']; ?></h2>
                <button type="submit" name="submit" class="save-button"><?php echo $client_lang['profileSave']; ?></button>
            </div>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])){?> 
                        <div class="alert alert-success alert-dismissible" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true"></span></button>
                            <?php echo $client_lang[$_SESSION['msg']]; ?>
                        </div>
                        <?php unset($_SESSION['msg']);}?>   
                    </div>
                </div>
            </div>
            <div class="profile-picture-section">
                <div class="profile-picture-wrapper">
                    <img src="<?php echo (!empty($row['image'])) ? '/seller/images/'.$row['image'] : '/seller/images/default.png'; ?>" alt="Profile Picture" id="profile-picture">
                    <label for="fileupload" class="edit-icon">
                        <i class="fas fa-edit"></i>
                    </label>
                    <input type="file" name="image" id="fileupload" onchange="previewImage(this)" class="file-upload-input">
                </div>
            </div>
            <div class="profile-info-section">
                <label for="name"><?php echo $client_lang["profileName"]?></label>
                <input type="text" name="name" id="name" value="<?php echo $row['name'];?>" required>
                
                <label for="phone"><?php echo $client_lang["profilePhone"]?></label>
                <input type="text" name="phone" id="phone" value="<?php echo htmlspecialchars($row['phone']); ?>" required readonly class="readonly-field">
                <input type="hidden" id="countryCode" name="countryCode" value="<?php echo htmlspecialchars($row['country_code']); ?>">
                
                <label for="email"><?php echo $client_lang["profileEmail"]?></label>
                <input type="text" name="email" id="email" value="<?php echo htmlspecialchars($row['email']); ?>" required readonly class="readonly-field">

                
                <label for="password"><?php echo $client_lang["profilePassword"]?></label>
                <input type="password" name="password" id="password" value="<?php echo $row['password'];?>" required>
                
            </div>
        </form>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@23.1.0/build/js/intlTelInput.min.js"></script>
    <script>
    function previewImage(input) {
        const file = input.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('profile-picture').src = e.target.result;
            }
            reader.readAsDataURL(file);
        }
    }

    $(document).ready(function () {
        const input = document.querySelector("#phone");
        const iti = window.intlTelInput(input, {
            utilsScript: "https://cdn.jsdelivr.net/npm/intl-tel-input@23.1.0/build/js/utils.js",
            initialCountry: "auto",
            separateDialCode: true,
            geoIpLookup: function(callback) {
                fetch('https://ipinfo.io/json')
                    .then(response => response.json())
                    .then(data => callback(data.country))
                    .catch(() => callback('us'));
            }
        });

        // Set initial phone value with country code if available
        if (input.value) {
            iti.setNumber("+" + <?php echo $row['country_code']; ?> + input.value);
        }

        $('#profile-form').on('submit', function(e) {
            var fullPhoneNumber = iti.getNumber();
            var countryCode = iti.getSelectedCountryData().dialCode;

            $('#countryCode').val(countryCode);
        });
    });
    </script>
</body>
</html>
