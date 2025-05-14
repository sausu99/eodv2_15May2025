<?php
include("includes/connection.php");
include('includes/function.php');

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);


// Check if seller ID is provided in the URL
if(isset($_GET['id'])) {
    // Sanitize the input to prevent SQL injection
    $seller_id = mysqli_real_escape_string($mysqli, $_GET['id']);

    // Query to fetch seller details based on seller ID
    $query_seller = "SELECT * FROM tbl_vendor WHERE id = '$seller_id'";
    $result_seller = mysqli_query($mysqli, $query_seller);

    // Check if seller exists
    if(mysqli_num_rows($result_seller) > 0) {
        $seller = mysqli_fetch_assoc($result_seller);
        $seller_name = $seller['email'];
        $seller_email = $seller['username'];
        $seller_about = $seller['about'];
        $seller_profile_picture = $seller['image'];
    } else {
        // Seller not found
        $seller_name = "Seller Not Found";
        $seller_email = "";
        $seller_profile_picture = ""; // You can provide a default profile picture here
    }

    // Query to fetch items added by the seller
    $query_items = "SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE o_type IN(1,2,4,5,7,8,10,11) AND id = '$seller_id'";
    $result_items = mysqli_query($mysqli, $query_items);
} else {
    // Redirect if seller ID is not provided
    header("Location: index.php"); // Redirect to homepage or another appropriate page
    exit();
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Profile</title>
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

        .profile {
            display: flex;
            align-items: center;
        }

        .profile-picture img {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            margin-right: 20px;
            border: 4px solid #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .profile-details {
            flex-grow: 1;
        }

        .profile-details h2 {
            color: #333;
            margin-top: 0;
        }

        .profile-details p {
            color: #666;
            margin-bottom: 10px;
        }

        .contact-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .contact-button:hover {
            background-color: #0056b3;
        }

        .item-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 20px;
            margin-top: 20px;
        }

        .item-box {
            flex: 1 1 300px; /* Adjust this value as needed */
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }

        .item-box img {
            width: 100%;
            border-radius: 8px;
            margin-bottom: 10px;
        }

        .item-box h3 {
            color: #333;
            margin-top: 0;
            font-size: 18px; /* Adjust this value as needed */
        }

        .item-box p {
        color: #666;
        margin-bottom: 10px;
        font-size: 14px; /* Adjust this value as needed */
        display: -webkit-box;
        -webkit-line-clamp: 4; /* Limit to 4 lines */
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

        .item-box .time-left {
            color: #999;
            font-size: 16px; /* Adjust this value as needed */
            margin-bottom: 10px;
        }

        .item-box .view-button {
            display: block;
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
            text-align: center;
            width: 100%;
            max-width: 200px;
            margin: 0 auto;
            font-size: 16px; /* Adjust this value as needed */
        }

        .item-box .view-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Seller Profile</h1>
        <hr><br>
        <div class="profile">
            <div class="profile-picture">
                <img src="<?php echo '../seller/images/'.$seller_profile_picture; ?>" class="img-fluid img-thumbnail" onerror="this.onerror=null;this.src='placeholder.jpg';" alt="<?php echo $seller_name; ?>">
            </div>
            <div class="profile-details">
                <h2><?php echo $seller_name; ?></h2>
                <p><?php echo $seller_about; ?></p>
                <a href="mailto:<?php echo $seller_email; ?>" class="contact-button">Contact Seller</a>
            </div>
        </div>

         <div class="item-container">
            <?php
            // Display items added by the seller 
            if(mysqli_num_rows($result_items) > 0) {
                while($row_item = mysqli_fetch_assoc($result_items)) {
            ?>
                    <div class="item-box">
                        <img src="<?php echo '../seller/images/'.$row_item['o_image']; ?>" class="img-fluid img-thumbnail" onerror="this.onerror=null;this.src='placeholder.jpg';" alt="<?php echo $row_item['o_name']; ?>" width="250" height="250">                        <h3><?php echo $row_item['o_name']; ?></h3>
                        <p><?php echo $row_item['o_desc']; ?></p>
                            <a href="share.php?share_id=<?php echo $row_item['o_id']; ?>" class="view-button"><?php
                              if ($row_item['o_type'] == 1 || $row_item['o_type'] == 2 || $row_item['o_type'] == 7 || $row_item['o_type'] == 8 || $row_item['o_type'] == 10 || $row_item['o_type'] == 11) {
                                echo 'View Auction';
                              } elseif ($row_item['o_type'] == 4 || $row_item['o_type'] == 5) {
                                echo 'View Lottery';
                              } else {
                                echo 'View Item';
                              }
                              ?></a>
                                    </div>
                            <?php
                                }
                            } else {
                                echo "<p>No items found.</p>";
                            }
                            ?>
        </div>
    </div>
</body>
</html>