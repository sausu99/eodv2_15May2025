<?php include("includes/connection.php");
 	  include("includes/function.php");
 	  include("send_email.php"); // Include the send_email.php file
 	  
 	  $cur = $row['currency'];
 	  
 	  ini_set('display_errors', '1');
      ini_set('display_startup_errors', '1');
      error_reporting(E_ALL);
 
 	  
 	  require_once 'vendor/autoload.php';

      use Twilio\Rest\Client;
      
      // Fetch Twilio SID and Auth Token from tbl_settings
      $settings_query = "SELECT twilio_sid, twilio_token, timezone FROM tbl_settings";
      $settings_result = mysqli_query($mysqli, $settings_query);
      $settings_row = mysqli_fetch_assoc($settings_result);
      
      
      date_default_timezone_set($settings_row['timezone']);
      $datetime = date('Y-m-d H:i:s');
      $time = date('H:i:s');
	  $date = date('Y-m-d');
      
      $sid = $settings_row['twilio_sid'];
      $token = $settings_row['twilio_token'];
      
      $twilio = new Client($sid, $token);
      

	  $file_path = 'http://'.$_SERVER['SERVER_NAME'] . dirname($_SERVER['REQUEST_URI']).'/';
	  
	  $home_path = 'http://'.$_SERVER['SERVER_NAME'].'/';
	
	
	
	
	
if (isset($_GET['postUserLogin'])) {
    if ($_POST['phone'] !== "") {
        
        $identifier = "9339932830";
        $password = $_POST['password'];

        // Sanitize input to prevent SQL injection
        $identifier = mysqli_real_escape_string($mysqli, $identifier);
        $password = mysqli_real_escape_string($mysqli, $password);

        // Check if the identifier resembles an email address
        if (strpos($identifier, '@') === false) {
            // Login with phone number
            $query1 = "SELECT * FROM tbl_users WHERE phone = '$identifier' AND password = '' AND status = 1"; 
        } else {
            // Login with email
            $query1 = "SELECT * FROM tbl_users WHERE email = '$identifier' AND password = '' AND status = 1";
        }

        $result1 = mysqli_query($mysqli, $query1);
        $num_rows1 = mysqli_num_rows($result1);
        $row1 = mysqli_fetch_assoc($result1);

        if ($num_rows1 > 0) {
            if ($row1['ban'] == 1) {
                $set['JSON_DATA'][] = array('msg' => 'Access Denied, User is Banned!!', 'success' => '0');
            } else {
                $set['JSON_DATA'][] = array(
                    'msg' => 'Welcome back to the App',
                    'success' => '1',
                    'id' => $row1['id'],
                    'name' => $row1['name'],
                    'email' => $row1['email'],
                    'password' => $row1['password'],
                    'image' => $row1['image'],
                    'phone' => $row1['phone'],
                    'wallet' => $row1['wallet'],
                    'code' => $row1['code'],
                    'refferal_code' => $row1['refferal_code'],
                    'network' => $row1['network'],
                    'ban' => $row1['ban'],
                    'status' => $row1['status']
                );
            }
        } else if ($password != ""){
    

            // Check if the identifier resembles an email address
            if (strpos($identifier, '@') === false) {
                // Login with phone number
                $query = "SELECT * FROM tbl_users WHERE phone = '$identifier' AND password = '$password' AND status = 1"; 
            } else {
                // Login with email
                $query = "SELECT * FROM tbl_users WHERE email = '$identifier' AND password = '$password' AND status = 1";
            }

            $result = mysqli_query($mysqli, $query);
            $num_rows = mysqli_num_rows($result);
            $row = mysqli_fetch_assoc($result);

            if ($num_rows > 0) {
                if ($row['ban'] == 1) {
                    $set['JSON_DATA'][] = array('msg' => 'Access Denied, User is Banned!!', 'success' => '0');
                } else {
                    $set['JSON_DATA'][] = array(
                        'msg' => 'Welcome back to the App',
                        'success' => '1',
                        'id' => $row['id'],
                        'name' => $row['name'],
                        'email' => $row['email'],
                        'password' => $row['password'],
                        'image' => $row['image'],
                        'phone' => $row['phone'],
                        'wallet' => $row['wallet'],
                        'code' => $row['code'],
                        'refferal_code' => $row['refferal_code'],
                        'network' => $row['network'],
                        'ban' => $row['ban'],
                        'status' => $row['status']
                    );
                }
            } else {
                $set['JSON_DATA'][] = array('msg' => 'Invalid credentials', 'success' => '0');
            }
        }
    } else {
        $set['JSON_DATA'][] = array('msg' => 'Invalid credentials', 'success' => '0');
    }

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}



else if(isset($_GET['timezone']))
 	{
  		 $jsonObj4= array();

		
			$row['date'] = $datetime;
		
			
			array_push($jsonObj4,$row); 
			
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

else if(isset($_GET['add_kyc'])) {
    

    $facility_front_image=rand(0,99999)."_".$_FILES['id_front']['name'];
    $facility_back_image=rand(0,99999)."_".$_FILES['id_back']['name'];

    $front_tpath1='images/'.$facility_front_image; 		
    $back_tpath1='images/'.$facility_back_image; 		

    $front_pic1=compress_image($_FILES["id_front"]["tmp_name"], $front_tpath1, 80);
    $back_pic1=compress_image($_FILES["id_back"]["tmp_name"], $back_tpath1, 80);

    $front_thumbpath='images/thumbs/'.$facility_front_image;		
    $back_thumbpath='images/thumbs/'.$facility_back_image;		

    $front_thumb_pic1=create_thumb_image($front_tpath1,$front_thumbpath,'200','200');   
    $back_thumb_pic1=create_thumb_image($back_tpath1,$back_thumbpath,'200','200');   


      // Check if u_id already exists
    $check_query = "SELECT u_id FROM tbl_kyc WHERE u_id = '" . $_POST['u_id'] . "'";
    $check_result = mysqli_query($mysqli, $check_query);

    if (mysqli_num_rows($check_result) > 0) {
        // If u_id already exists, send an error message
        $error_message = array('error' => 'KYC for the User already exists');
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode($error_message);
        die();
    }
    $qry1 = "INSERT INTO tbl_kyc
            (u_id, id_type, id_number, id_front, id_back, id_country, dob, id_firstname, id_lastname, kyc_status)
            VALUES (
                '".$_POST['u_id']."',
                '".$_POST['id_type']."',
                '".$_POST['id_number']."',
                '".$facility_front_image."',
                '".$facility_back_image."',
                '".$_POST['id_country']."',
                '".$_POST['dob']."',
                '".$_POST['id_firstname']."',
                '".$_POST['id_lastname']."',
                '1'
            )";

    $result1 = mysqli_query($mysqli, $qry1);
    $last_id = mysqli_insert_id($mysqli);

    $set['JSON_DATA'][]=array('msg'=>'KYC Submitted','kyc_id'=>$last_id);
    header( 'Content-Type: application/json; charset=utf-8' );

  echo json_encode($set);
    die();
}

else if(isset($_GET['kyc_details']))
 	{
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_kyc
		WHERE u_id='".$_GET['u_id']."'";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
			$row['id_type'] = $data['id_type'];
			$row['id_number'] = $data['id_number'];
			$row['id_country'] = $data['id_country'];
			$row['dob'] = $data['dob'];
			$row['id_firstname'] = $data['id_firstname'];
			$row['id_lastname'] = $data['id_lastname'];
			$row['id_front'] = $data['id_front'];
			$row['id_back'] = $data['id_back'];
			$row['kyc_status'] = $data['kyc_status'];
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
else if (isset($_GET['get_achievements'])) {
    $jsonObj = array();

    // Validate user ID
    if (isset($_POST['user_id']) && !empty($_POST['user_id'])) {
        $user_id = mysqli_real_escape_string($mysqli, $_POST['user_id']);

        // Query to fetch user achievements
        $query = "SELECT ua.*, a.name, a.description, a.category,a.color,a.image, a.target_value, a.points 
                  FROM tbl_user_achievements ua
                  JOIN tbl_achievements a ON ua.achievement_id = a.achievement_id
                  WHERE ua.user_id = '$user_id'";
        $sql = mysqli_query($mysqli, $query) or die(mysqli_error($mysqli));

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['achievement_id'] = $data['achievement_id'];
            $row['name'] = $data['name'];
            $row['description'] = $data['description'];
            $row['color'] = ($data['color'] == 0) ? '000000' : $data['color'];
            $row['image'] = $file_path . 'images/' . $data['achievement_image'];
            $row['category'] = $data['category'];
            $row['goal'] = $data['target_value'];
            $row['points'] = $data['points'];
            $row['status'] = $data['status'];
            $row['progress'] = $data['progress'];
            $row['earned_at'] = $data['earned_at'];

            array_push($jsonObj, $row);
        }

        $set['JSON_DATA'] = $jsonObj;

        header('Content-Type: application/json; charset=utf-8');
        echo json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    } else {
        $set['JSON_DATA'] = array('error' => 'User ID is required');
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    }
    die();
}
 
 
//update achievement as claimed
else if(isset($_GET['update_achievements']))
 	{
 	    $user_id = $_POST['user_id'];
 	    $achievement_id = $_POST['a_id'];


		$user_edit= "UPDATE tbl_user_achievements SET status = '3' WHERE user_id= '$user_id' AND achievement_id= '$achievement_id'"; 	 
		
   		$user_res = mysqli_query($mysqli,$user_edit);	
	  	
	  	$set['JSON_DATA'][]=array('msg'=>'Updated','success'=>'1');		 
		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();	
	}
	
//get scratch card
else if(isset($_GET['get_consolation']))
{
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_scratch
		WHERE u_id='".$_POST['u_id']."'"; 
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


        while($data = mysqli_fetch_assoc($sql))
        {
            $row['s_id'] = $data['s_id'];
            $row['s_name'] = $data['s_name'];
            $row['s_desc'] = $data['s_desc'];
            $row['s_colour'] = $data['s_colour'];
            $row['s_sdate'] = $data['s_sdate'];
            $row['s_stime'] = $data['s_stime'];
            $row['s_etime'] = $data['s_etime'];
            $row['s_edate'] = $data['s_edate'];
            $row['s_type'] = $data['s_type'];
            $row['s_code'] = $data['s_code'];
            $row['s_link'] = $data['s_link'];
            
            $s_sdate = $data['s_sdate'];
		    $s_stime = $data['s_stime'];
		    $s_edate = $data['s_edate'];
		    $s_etime =  $data['s_etime'];
		    
		    $start = $s_sdate." ".$s_stime;
		    $end = $s_edate." ".$s_etime;
        
            if ($start <= $datetime) {
                $row['s_status'] = $data['s_status'];
            } else {
                $row['s_status'] = "0";
            }
        
            if ($end <= $datetime) {
                $row['s_expired'] = "1";
            } else {
                $row['s_expired'] = "0";
            }
        
            array_push($jsonObj4,$row); 
        }
        
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 else if (isset($_GET['get_multi_winners'])) {
    $jsonObj4 = array();

    // Retrieve the maximum number of winners allowed for the offer
    $offerQuery = "SELECT o_winners, o_name, o_type, winner_type FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_id = '" . $_POST['o_id'] . "'";
    $offerResult = mysqli_query($mysqli, $offerQuery) or die(mysqli_error());
    $offerData = mysqli_fetch_assoc($offerResult);
    $maxWinners = $offerData['o_winners'];
    $offerType = $offerData['o_type'];
    $winnerType = $offerData['winner_type'];

    // Check if o_type is 1 and winner_type is 1
    if ($offerType == 1 && $winnerType == 1) {
        // Query to retrieve winners for o_type = 1 and winner_type = 1
               $query = "SELECT 
            tbl_users.id AS user_id,
            tbl_users.name AS user_name,
            tbl_users.image AS user_image,
            tbl_users.date AS user_date,
            tbl_offers.o_name,
            COUNT(CASE WHEN tbl_bid.o_id = '" . $_POST['o_id'] . "' THEN tbl_bid.u_id  END) AS total_occurrences,
            MAX(CASE WHEN tbl_bid.o_id = '" . $_POST['o_id'] . "' THEN tbl_bid.bd_value             END) AS winning_value
            FROM 
                tbl_bid
            LEFT JOIN 
                tbl_users ON tbl_bid.u_id = tbl_users.id
            LEFT JOIN 
                tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
            WHERE 
                tbl_bid.o_id = '" . $_POST['o_id'] . "'
             GROUP BY bd_value HAVING total_occurrences = 1 ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) ASC LIMIT " . (int)$maxWinners;


        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = $data['user_date'];
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    } 
    
    elseif ($offerType == 2 && $winnerType == 1) {
        // Query to retrieve winners for o_type = 2
               $query = "SELECT 
            tbl_users.id AS user_id,
            tbl_users.name AS user_name,
            tbl_users.image AS user_image,
            tbl_users.date AS user_date,
            tbl_offers.o_name,
            COUNT(CASE WHEN tbl_bid.o_id = '" . $_POST['o_id'] . "' THEN tbl_bid.u_id  END) AS total_occurrences,
            MAX(CASE WHEN tbl_bid.o_id = '" . $_POST['o_id'] . "' THEN tbl_bid.bd_value             END) AS winning_value
            FROM 
                tbl_bid
            LEFT JOIN 
                tbl_users ON tbl_bid.u_id = tbl_users.id
            LEFT JOIN 
                tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
            WHERE 
                tbl_bid.o_id = '" . $_POST['o_id'] . "'
             GROUP BY bd_value HAVING total_occurrences = 1 ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC LIMIT " . (int)$maxWinners;
            

        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = $data['user_date'];
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    } elseif (($offerType == 4 || $offerType == 5) && $winnerType == 1) {
       // Query to retrieve winners for o_type = 4
                              $query = "SELECT *
                                        FROM tbl_bid
                                        LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
                                        WHERE tbl_bid.o_id = '" . $_POST['o_id'] . "'
                                        ORDER BY RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bid.bd_id))
                                        LIMIT " . (int)$maxWinners;


            

        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['u_id'];
            $row['winner_name'] = $data['name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['image'];
            $row['winner_join_date'] = $data['date'];
            
            $row['winning_value'] = $data['bd_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    } 
    
    
    elseif ($offerType == 7 && $winnerType == 1) {
        // Query to retrieve winners for o_type = 7
                      $query = "SELECT 
                tbl_users.id AS user_id,
                tbl_users.name AS user_name,
                tbl_users.image AS user_image,
                tbl_users.date AS user_date,
                tbl_bid.bd_value AS winning_value,
                tbl_offers.o_name,
                tbl_bid.bd_amount AS winning_amount
            FROM 
                tbl_bid
            LEFT JOIN 
                tbl_users ON tbl_bid.u_id = tbl_users.id
            LEFT JOIN 
                tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
            WHERE 
                tbl_bid.o_id = '" . $_POST['o_id'] . "'
            ORDER BY 
                CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC
            LIMIT " . (int)$maxWinners;


        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = $data['user_date'];
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    }
    
    elseif ($offerType == 8 && $winnerType == 1) {
        // Query to retrieve winners for o_type = 7
                      $query = "SELECT 
                tbl_users.id AS user_id,
                tbl_users.name AS user_name,
                tbl_users.image AS user_image,
                tbl_users.date AS user_date,
                tbl_bid.bd_value AS winning_value,
                tbl_offers.o_name,
                tbl_bid.bd_amount AS winning_amount
            FROM 
                tbl_bid
            LEFT JOIN 
                tbl_users ON tbl_bid.u_id = tbl_users.id
            LEFT JOIN 
                tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
            WHERE 
                tbl_bid.o_id = '" . $_POST['o_id'] . "'
            ORDER BY 
                CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC
            LIMIT " . (int)$maxWinners;


        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = $data['user_date'];
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    }
    
    //-------------------------- Bot Code ----------------------------//
    
     // Check if o_type is 1 and winner_type is bot
    else if ($offerType == 1 && $winnerType != 1) {

             $query = "SELECT 
                        tbl_bot.bot_id AS user_id,
                        tbl_bot.bot_name AS user_name,
                        tbl_bot.bot_image AS user_image,
                        tbl_offers.o_name,
                        ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) AS winning_value
                    FROM 
                        tbl_bot
                    LEFT JOIN 
                        tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'
                        left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                    WHERE
                        NOT EXISTS (
                            SELECT 1
                            FROM tbl_bid
                            WHERE tbl_bid.o_id = '" . $_POST['o_id'] . "'
                            AND tbl_bid.bd_value = ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) *             100, 2)
                            )
                            AND ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) > tbl_offers                        .o_min
                            ORDER BY 
                                winning_value ASC
                            LIMIT " . (int)$maxWinners;

        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = '01-01-2021';
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    }
    
     // Check if o_type is 2 and winner_type is bot
    else if ($offerType == 2 && $winnerType != 1) {

             $query = "SELECT 
                        tbl_bot.bot_id AS user_id,
                        tbl_bot.bot_name AS user_name,
                        tbl_bot.bot_image AS user_image,
                        tbl_offers.o_name,
                        ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) AS winning_value
                    FROM 
                        tbl_bot
                    LEFT JOIN 
                        tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'
                        left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                    WHERE
                        NOT EXISTS (
                            SELECT 1
                            FROM tbl_bid
                            WHERE tbl_bid.o_id = '" . $_POST['o_id'] . "'
                            AND tbl_bid.bd_value = ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) *             100, 2)
                            )
                            AND ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) < tbl_offers                        .o_max
                            ORDER BY 
                                winning_value DESC
                            LIMIT " . (int)$maxWinners;

        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = '01-01-2021';
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    }
    
     // Check if o_type is 4 and winner_type is bot
    elseif (($offerType == 4 || $offerType == 5)  && $winnerType != 1) {

                    $query = "SELECT 
            CONCAT('B', tbl_bot.bot_id) AS user_id,
            tbl_bot.bot_name AS user_name,
            tbl_bot.bot_image AS user_image,
            CONCAT(
                CHAR(FLOOR(65 + RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bid.bd_id)) * 26)), -- First letter (A-Z)
                CHAR(FLOOR(65 + RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bid.bd_id)) * 26)), -- Second letter (A-Z)
                LPAD(FLOOR(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bid.bd_id)) * 10000), 4, '0') -- Four random digits (0000-9999)
            ) AS winning_value,
            (SELECT bot_id FROM tbl_bot ORDER BY RAND() LIMIT 1) AS bot_id,
            tbl_offers.o_name,
            tbl_bid.bd_amount AS winning_amount
        FROM 
            tbl_bid
        LEFT JOIN 
            tbl_bot ON tbl_bid.u_id = tbl_bot.bot_id
        LEFT JOIN 
            tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'
            left join tbl_items on tbl_items.item_id = tbl_offers.item_id
        WHERE 
            tbl_bid.o_id = '" . $_POST['o_id'] . "'
        GROUP BY 
            user_id  -- Ensures each user ID appears only once in the result set
        ORDER BY 
            winning_value
        LIMIT " . (int)$maxWinners;

        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = '01-01-2021';
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    }
    
    
    // Check if o_type is 7 and winner_type is bot
    else if ($offerType == 7 && $winnerType != 1) {

             $query = "SELECT 
         tbl_bot.bot_id AS user_id,
         tbl_bot.bot_name AS user_name,
         tbl_bot.bot_image AS user_image,
         tbl_offers.o_name,
         ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2)         AS winning_value
        FROM 
            tbl_bot
        LEFT JOIN 
            tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'         
            left join tbl_items on tbl_items.item_id = tbl_offers.item_id
            ORDER BY 
    winning_value DESC
        LIMIT " . (int)$maxWinners;

        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = '01-01-2021';
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    }
    
    // Check if o_type is 8 and winner_type is bot
    else if ($offerType == 8 && $winnerType != 1) {

             $query = "SELECT 
         tbl_bot.bot_id AS user_id,
         tbl_bot.bot_name AS user_name,
         tbl_bot.bot_image AS user_image,
         tbl_offers.o_name,
         ROUND(RAND(CONCAT('" . $_POST['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2)         AS winning_value
        FROM 
            tbl_bot
        LEFT JOIN 
            tbl_offers ON tbl_offers.o_id = '" . $_POST['o_id'] . "'    left join tbl_items on tbl_items.item_id = tbl_offers.item_id     ORDER BY 
    winning_value DESC
        LIMIT " . (int)$maxWinners;

        $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

        while ($data = mysqli_fetch_assoc($sql)) {
            $row['winner_id'] = $data['user_id'];
            $row['winner_name'] = $data['user_name'];
            $row['winner_image'] =  $file_path . 'images/' . $data['user_image'];
            $row['winner_join_date'] = '01-01-2021';
            
            $row['winning_value'] = $data['winning_value'];
            $row['winning_prize'] = $data['o_name'];

            array_push($jsonObj4, $row);
        }
    }

    $set['JSON_DATA'] = $jsonObj4;  

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

// Get all offers winners
else if (isset($_GET['get_offers_winner'])) {
    $jsonObj4 = array();

               $query = "SELECT 
                tbl_offers.*, 
                tbl_vendor.*
            FROM 
                tbl_offers
            LEFT JOIN 
                tbl_vendor ON tbl_offers.id = tbl_vendor.id
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
            WHERE 
                tbl_offers.o_edate <= '" . $date . "' 
                AND tbl_offers.o_type IN ('1', '2', '4', '5', '7', '8') 
                AND tbl_offers.o_status = 1 
            ORDER BY 
                tbl_offers.o_edate DESC";
    
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

    while ($data = mysqli_fetch_assoc($sql)) {
        $row = array(); // Initialize $row for each iteration

        $o_date = $data['o_date'];
        $o_stime = $data['o_stime'];

        $o_edate = $data['o_edate'];
        $o_etime = $data['o_etime'];

        $start = $o_date . " " . $o_stime;
        $end = $o_edate . " " . $o_etime;

        if ($end <= $datetime) {
           // Starting of lowest unique bid winner code
if ($data['o_type'] == '1' && $data['winner_type'] == 1) {
    $query1 = "SELECT *, COUNT(*) as num1 FROM tbl_bid 
               LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
               WHERE tbl_bid.o_id='" . $data['o_id'] . "'
               GROUP BY bd_value HAVING num1 = 1 ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) ASC LIMIT 0,1";

    $result1 = mysqli_query($mysqli, $query1) or die(mysqli_error());

    $row1 = mysqli_fetch_assoc($result1);

    if ($row1['u_id'] == 0) {
        $row['lu_id'] = "0";
        $row['lname'] = "no unique bid";
        $row['limage'] = "";
        $row['lbd_value'] = "1";
        $row['lbd_amount'] = "0";
        $row['l_won'] = "0";
    } else {
        $row['lu_id'] = $row1['u_id'];
        $row['lname'] = $row1['name'];
        $row['limage'] = $row1['image'];
        $row['lbd_value'] = $row1['bd_value'];
        $row['lbd_amount'] = $row1['bd_amount'];

        // Update tbl_offers with winner_name and winning_value
        $updateQuery = "UPDATE tbl_offers SET winner_name = '" . $row1['name'] . "', winning_value = '" . $row1['bd_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
        mysqli_query($mysqli, $updateQuery);

        if ($row1['u_id'] == $_POST['u_id']) {
            $row['l_won'] = "1";
        } else {
            $row['l_won'] = "0";
        }
    }
}

            
 // Starting of highest unique bid winner code
            elseif ($data['o_type'] == '2' && $data['winner_type'] == 1) {
                $query11 = "SELECT *, COUNT(*) as num1 FROM tbl_bid 
                            LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
                            WHERE tbl_bid.o_id='" . $data['o_id'] . "'
                            GROUP BY bd_value HAVING num1 = 1 ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC LIMIT 0,1";

                $result11 = mysqli_query($mysqli, $query11) or die(mysqli_error());

                $row11 = mysqli_fetch_assoc($result11);

                if ($row11['u_id'] == 0) {
                    $row['hu_id'] = "0";
                    $row['hname'] = "no unique bid";
                    $row['himage'] = "";
                    $row['hbd_value'] = "";
                    $row['hbd_amount'] = "0";
                    $row['h_won'] = "0";
                } else {
                    $row['hu_id'] = $row11['u_id'];
                    $row['hname'] = $row11['name'];
                    $row['himage'] = $row11['image'];
                    $row['hbd_value'] = $row11['bd_value'];
                    $row['hbd_amount'] = $row11['bd_amount'];
                    
                     // Update tbl_offers with winner_name and winning_value
        $updateQuery = "UPDATE tbl_offers SET winner_name = '" . $row11['name'] . "', winning_value = '" . $row11['bd_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
        mysqli_query($mysqli, $updateQuery);

                    if ($row11['u_id'] == $_POST['u_id']) {
                        $row['h_won'] = "1";
                    } else {
                        $row['h_won'] = "0";
                    }
                }
            } 

            // Starting of raffle winner code
            elseif (($data['o_type'] == '4' || $data['o_type'] == '5') && $data['winner_type'] == 1) {
                $query11 = "SELECT * FROM tbl_bid
                                        LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
                                        WHERE tbl_bid.o_id = '" . $data['o_id'] . "'
                                        ORDER BY RAND(CONCAT('" . $data['o_id'] . "', tbl_bid.bd_id))
                                        LIMIT 1";

                $result11 = mysqli_query($mysqli, $query11) or die(mysqli_error());

                $row11 = mysqli_fetch_assoc($result11);

                if ($row11['u_id'] == 0) {
                    $row['ru_id'] = "0";
                    $row['rname'] = "no tickets purchased";
                    $row['rimage'] = "";
                    $row['rbd_value'] = "";
                    $row['rbd_amount'] = "0";
                    $row['r_won'] = "0";
                } else {
                    $row['ru_id'] = $row11['u_id'];
                    $row['rname'] = $row11['name'];
                    $row['rimage'] = $row11['image'];
                    $row['rbd_value'] = $row11['bd_value'];
                    $row['rbd_amount'] = $row11['bd_amount'];
                    
                     // Update tbl_offers with winner_name and winning_value
        $updateQuery = "UPDATE tbl_offers SET winner_name = '" . $row11['name'] . "', winning_value = '" . $row11['bd_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
        mysqli_query($mysqli, $updateQuery);

                    if ($row11['u_id'] == $_POST['u_id']) {
                        $row['r_won'] = "1";
                    } else {
                        $row['r_won'] = "0";
                    }
                }
            } 

            // Starting of highest bid winner code
            elseif (($data['o_type'] == '7' || $data['o_type'] == '8') && $data['winner_type'] == 1) {
                $query11 = "SELECT * FROM tbl_bid 
                            LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
                            WHERE tbl_bid.o_id='" . $data['o_id'] . "'
                            ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC LIMIT 0,1";

                $result11 = mysqli_query($mysqli, $query11) or die(mysqli_error());

                $row11 = mysqli_fetch_assoc($result11);

                if ($row11['u_id'] == 0) {
                    $row['du_id'] = "0";
                    $row['dname'] = "no bids placed";
                    $row['dimage'] = "";
                    $row['dbd_value'] = "";
                    $row['dbd_amount'] = "0";
                    $row['d_won'] = "0";
                } else {
                    $row['du_id'] = $row11['u_id'];
                    $row['dname'] = $row11['name'];
                    $row['lname'] = $row11['name'];
                    $row['dimage'] = $row11['image'];
                    $row['dbd_value'] = $row11['bd_value'];
                    $row['dbd_amount'] = $row11['bd_amount'];
                    
                     // Update tbl_offers with winner_name and winning_value
        $updateQuery = "UPDATE tbl_offers SET winner_name = '" . $row11['name'] . "', winning_value = '" . $row11['bd_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
        mysqli_query($mysqli, $updateQuery);

                    if ($row11['u_id'] == $_POST['u_id']) {
                        $row['d_won'] = "1";
                    } else {
                        $row['d_won'] = "0";
                    }
                }
            } 
            
            //----------------BOT CODE----------------//
            
           else if ($data['o_type'] == '1' && $data['winner_type'] != 1) {

                        $query1 = "SELECT 
                        tbl_bot.bot_id AS u_id,
                        tbl_bot.bot_name AS name,
                        tbl_bot.bot_image AS image,
                        tbl_items.o_name,
                        ROUND(RAND(CONCAT('" . $data['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) AS winning_value
                    FROM 
                        tbl_bot
                    LEFT JOIN 
                        tbl_offers ON tbl_offers.o_id = '" . $data['o_id'] . "'
                        left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                    WHERE
                        NOT EXISTS (
                            SELECT 1
                            FROM tbl_bid
                            WHERE tbl_bid.o_id = '" . $data['o_id'] . "'
                            AND tbl_bid.bd_value = ROUND(RAND(CONCAT('" . $data['o_id'] . "', tbl_bot.bot_id, RAND())) *             100, 2)
                            )
                            AND ROUND(RAND(CONCAT('" . $data['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) > tbl_offers                        .o_min
                            ORDER BY 
                                winning_value ASC
                            LIMIT 1";

                        $result1 = mysqli_query($mysqli, $query1) or die(mysqli_error());
                    
                        $row1 = mysqli_fetch_assoc($result1);
                    
                        if ($row1['u_id'] == 0) {
                            $row['lu_id'] = "0";
                            $row['lname'] = "no unique bid";
                            $row['limage'] = "";
                            $row['lbd_value'] = "1";
                            $row['lbd_amount'] = "0";
                            $row['l_won'] = "0";
                        } else {
                            $row['lu_id'] = $row1['u_id'];
                            $row['lname'] = $row1['name'];
                            $row['limage'] = $row1['image'];
                            $row['lbd_value'] = $row1['winning_value'];
                            $row['lbd_amount'] = $row1['winning_value'];
                    
                            // Update tbl_offers with winner_name and winning_value
                            $updateQuery = "UPDATE tbl_offers SET winner_name = '" . $row1['name'] .                     "', winning_value = '" . $row1['winning_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
                            mysqli_query($mysqli, $updateQuery);
                    
                            if ($row1['u_id'] == $_POST['u_id']) {
                                $row['l_won'] = "1";
                            } else {
                                $row['l_won'] = "0";
                            }
                        }
                    }

             // Starting of highest unique bid winner code
                        elseif ($data['o_type'] == '2' && $data['winner_type'] != 1) {
                            $query11 = "SELECT 
                        tbl_bot.bot_id AS u_id,
                        tbl_bot.bot_name AS name,
                        tbl_bot.bot_image AS image,
                        tbl_items.o_name,
                        ROUND(RAND(CONCAT('" . $data['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) AS winning_value
                    FROM 
                        tbl_bot
                    LEFT JOIN 
                        tbl_offers ON tbl_offers.o_id = '" . $data['o_id'] . "'
                        left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                    WHERE
                        NOT EXISTS (
                            SELECT 1
                            FROM tbl_bid
                            WHERE tbl_bid.o_id = '" . $data['o_id'] . "'
                            AND tbl_bid.bd_value = ROUND(RAND(CONCAT('2', tbl_bot.bot_id, RAND())) *             100, 2)
                            )
                            AND ROUND(RAND(CONCAT('" . $data['o_id'] . "', tbl_bot.bot_id, RAND())) * 100, 2) < tbl_offers                        .o_max
                            ORDER BY 
                                winning_value DESC
                            LIMIT 1";

                $result11 = mysqli_query($mysqli, $query11) or die(mysqli_error());

                $row11 = mysqli_fetch_assoc($result11);

                if ($row11['u_id'] == 0) {
                    $row['hu_id'] = "0";
                    $row['hname'] = "no unique bid";
                    $row['himage'] = "";
                    $row['hbd_value'] = "";
                    $row['hbd_amount'] = "0";
                    $row['h_won'] = "0";
                } else {
                    $row['hu_id'] = $row11['u_id'];
                    $row['hname'] = $row11['name'];
                    $row['himage'] = $row11['image'];
                    $row['hbd_value'] = $row11['winning_value'];
                    $row['hbd_amount'] = $row11['winning_value'];
                    
                     // Update tbl_offers with winner_name and winning_value
        $updateQuery = "UPDATE tbl_offers SET winner_name = '" . $row11['name'] . "', winning_value = '" . $row11['winning_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
        mysqli_query($mysqli, $updateQuery);

                    if ($row11['u_id'] == $_POST['u_id']) {
                        $row['h_won'] = "1";
                    } else {
                        $row['h_won'] = "0";
                    }
                }
            } 
            
             // Starting of raffle winner code bot
            elseif (($data['o_type'] == '4' || $data['o_type'] == '5') && $data['winner_type'] != 1) {
                $query11 = "SELECT 
            CONCAT('B', tbl_bot.bot_id) AS user_id,
            tbl_bot.bot_name AS user_name,
            tbl_bot.bot_image AS user_image,
            CONCAT(
                CHAR(FLOOR(65 + RAND(CONCAT('" . $data['o_id'] . "', tbl_bid.bd_id)) * 26)), -- First letter (A-Z)
                CHAR(FLOOR(65 + RAND(CONCAT('" . $data['o_id'] . "', tbl_bid.bd_id)) * 26)), -- Second letter (A-Z)
                LPAD(FLOOR(RAND(CONCAT('" . $data['o_id'] . "', tbl_bid.bd_id)) * 10000), 4, '0') -- Four random digits (0000-9999)
            ) AS winning_value,
            (SELECT bot_id FROM tbl_bot ORDER BY RAND() LIMIT 1) AS bot_id,
            tbl_items.o_name,
            tbl_bid.bd_amount AS winning_amount
        FROM 
            tbl_bid
        LEFT JOIN 
            tbl_bot ON tbl_bid.u_id = tbl_bot.bot_id
        LEFT JOIN 
            tbl_offers ON tbl_offers.o_id = '" . $data['o_id'] . "'
            left join tbl_items on tbl_items.item_id = tbl_offers.item_id
        WHERE 
            tbl_bid.o_id = '" . $data['o_id'] . "'
        GROUP BY 
            user_id  -- Ensures each user ID appears only once in the result set
        ORDER BY 
            winning_value
        LIMIT 0, 1";

                $result11 = mysqli_query($mysqli, $query11) or die(mysqli_error());

                $row11 = mysqli_fetch_assoc($result11);

                if ($row11['user_id'] == 0) {
                    $row['ru_id'] = "0";
                    $row['rname'] = "no tickets purchased";
                    $row['rimage'] = "";
                    $row['rbd_value'] = "";
                    $row['rbd_amount'] = "0";
                    $row['r_won'] = "0";
                } else {
                    $row['ru_id'] = $row11['user_id'];
                    $row['rname'] = $row11['user_name'];
                    $row['rimage'] = $row11['image'];
                    $row['rbd_value'] = $row11['winning_value'];
                    $row['rbd_amount'] = $row11['winning_value'];
                    
                     // Update tbl_offers with winner_name and winning_value
        $updateQuery = "UPDATE tbl_offers SET winner_name = '" . $row11['user_name'] . "', winning_value = '" . $row11['winning_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
        mysqli_query($mysqli, $updateQuery);

                    if ($row11['u_id'] == $_POST['u_id']) {
                        $row['r_won'] = "1";
                    } else {
                        $row['r_won'] = "0";
                    }
                }
            } 
            
            // Starting of 7 & 8
                        elseif (($data['o_type'] == '7' || $data['o_type'] == '8') && $data['winner_type'] != 1) {
                            $query11 = "SELECT 
                                    tbl_bot.bot_id AS u_id,
                                    tbl_bot.bot_name AS name,
                                    tbl_bot.bot_image AS image,
                                    tbl_items.o_name,
                        ROUND(1 + RAND(CONCAT('" . $data['o_id'] . "', tbl_bot.bot_id, RAND())) * 99                  , 2) AS winning_value
                        FROM 
                            tbl_bot
                        LEFT JOIN 
                            tbl_offers ON tbl_offers.o_id = '" . $data['o_id'] . "'   
                            left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                        ORDER BY 
                            winning_value DESC 
                        LIMIT 0, 1";

                $result11 = mysqli_query($mysqli, $query11) or die(mysqli_error());

                $row11 = mysqli_fetch_assoc($result11);

                if ($row11['u_id'] == 0) {
                    $row['du_id'] = "0";
                    $row['dname'] = "no unique bid";
                    $row['dimage'] = "";
                    $row['dbd_value'] = "";
                    $row['dbd_amount'] = "0";
                    $row['d_won'] = "0";
                } else {
                    $row['du_id'] = $row11['u_id'];
                    $row['dname'] = $row11['name'];
                    $row['dimage'] = $row11['image'];
                    $row['dbd_value'] = $row11['winning_value'];
                    $row['dbd_amount'] = $row11['winning_value'];
                    
                     // Update tbl_offers with winner_name and winning_value
        $updateQuery = "UPDATE tbl_offers SET winning_value = '" . $row11['winning_value'] . "' WHERE o_id = '" . $data['o_id'] . "'";
        mysqli_query($mysqli, $updateQuery);

                    if ($row11['u_id'] == $_POST['u_id']) {
                        $row['d_won'] = "1";
                    } else {
                        $row['d_won'] = "0";
                    }
                }
            } 


            $query12 = "SELECT COUNT(*) as num1 , SUM(bd_amount) as bd_amount1 FROM tbl_bid 
                        WHERE tbl_bid.o_id='" . $data['o_id'] . "' and tbl_bid.u_id='" . $_POST['u_id'] . "'";

            $result12 = mysqli_query($mysqli, $query12) or die(mysqli_error());

            $row12 = mysqli_fetch_assoc($result12);

            if ($row12['num1'] == 0) {
                $row['total_bids'] = "0";
                $row['total_amount'] = "0";
            } else {
                $row['total_bids'] = $row12['num1'];
                $row['total_amount'] = $row12['bd_amount1'];
            }

            $rate4 = "SELECT * FROM tbl_order
                      WHERE tbl_order.u_id = '".$_POST['u_id']."' AND tbl_order.offer_id='" . $data['o_id'] . "'";

            $rateresult4 = mysqli_query($mysqli, $rate4) or die(mysqli_error());
            $num_rows4 = mysqli_num_rows($rateresult4);

            if ($num_rows4 > 0) {
                $row['o_click'] = "1";
            } else {
                $row['o_click'] = "0";
            }

            $row['o_id'] = $data['o_id'];
            $row['seller_id'] = $data['id'];
            $row['seller'] = $data['email'];
            $row['seller_image'] = $file_path . 'images/' . $data['image'];
            $row['joining_date'] = $data['joining_date'];
            $row['seller_website'] = $data['link'];
            $row['o_name'] = $data['o_name'];
            $row['o_color'] = $data['o_color'];
            $row['o_winners'] = $data['o_winners'];
            $row['o_image'] = $file_path . 'images/' . $data['o_image'];
            $row['o_date'] = $data['o_date'];
            $row['o_edate'] = $data['o_edate'];
            $row['o_amount'] = $data['o_amount'];
            $row['o_type'] = $data['o_type'];
            $row['o_price'] = $data['o_price'];
            $row['o_status'] = $data['o_status'];
            

            array_push($jsonObj4, $row);
        }
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
 
// Get winners
else if (isset($_GET['get_winners'])) 
{
    
    $user_id = !empty($_POST['user_id']) ? $_POST['user_id'] : 0;
    
    $jsonObj4 = array();
    $date = mysqli_real_escape_string($mysqli, $date); // Sanitize date input
    
    $query = "
        SELECT 
            tbl_offers.*, 
            tbl_vendor.*, 
            tbl_items.*,
            tbl_users.*, tbl_users.id AS u_id
        FROM 
            tbl_offers
        LEFT JOIN 
            tbl_vendor ON tbl_offers.id = tbl_vendor.id
        LEFT JOIN 
            tbl_items ON tbl_items.item_id = tbl_offers.item_id
        LEFT JOIN 
            tbl_users ON tbl_users.id = tbl_offers.winner_id
        WHERE 
            tbl_offers.o_edate <= ? 
            AND tbl_offers.o_type NOT IN (3,6,9) 
            AND tbl_offers.o_status = 1 
        ORDER BY 
            tbl_offers.o_id DESC";

    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("s", $date);
    $stmt->execute();
    $result = $stmt->get_result();

    while ($data = $result->fetch_assoc()) {
        
        $o_id = $data['o_id'];
        $winner_type = $data['winner_type'];
        $winner_name = $data['winner_name'];
        $o_date = $data['o_date'];
        $o_stime = $data['o_stime'];

        $o_edate = $data['o_edate'];
        $o_etime = $data['o_etime'];

        $start = $o_date . " " . $o_stime;
        $end = $o_edate . " " . $o_etime;
        
    
    if ($end <= $datetime) {
        
        if ($data['o_type'] != 5) {
            $row = array(
                'o_id' => $data['o_id'],
                'o_name' => $data['o_name'],
                'o_image' => !empty($data['o_image']) ? $file_path . 'images/thumbs/' . $data['o_image'] : $file_path . 'placeholder.jpg',
                'o_edate' => $data['o_edate'],
                'o_type' => $data['o_type'],
                'prize_pool' => $data['o_price'],
                'winner_name' => !empty($data['winner_name']) ? $data['name'] : 'No Winner',
                'winning_value' => !empty($data['winning_value']) ? $data['winning_value'] : '0',
                'is_winner' => ($data['winner_id'] == $user_id) ? 1 : 0,
                'multiple_winner' => 0
            );
        } else {
            
            if ($winner_type == 1 & $winner_name != "Winner Announced") {
                selectAndInsertWinners($mysqli, $o_id);
            }
            
            // Calculate total prize pool
                $prizePoolQuery = "
                    SELECT 
                        SUM(tbl_items.price) AS total_prize_pool
                    FROM 
                        tbl_prizes 
                    LEFT JOIN 
                        tbl_items ON tbl_items.item_id = tbl_prizes.item_id 
                    WHERE 
                        tbl_prizes.o_id = ?";
                
                $prizePoolStmt = $mysqli->prepare($prizePoolQuery);
                $prizePoolStmt->bind_param("i", $data['o_id']);
                $prizePoolStmt->execute();
                $prizePoolResult = $prizePoolStmt->get_result();
                $prizePoolRow = $prizePoolResult->fetch_assoc();
                $total_prize_pool = $prizePoolRow['total_prize_pool'];
                
            $row = array(
                'o_id' => $data['o_id'],
                'o_edate' => $data['o_edate'],
                'o_type' => $data['o_type'],
                'prize_pool' => $total_prize_pool,
                'multiple_winner' => 1
            );

            // Fetch winners
            $winnerQuery = "
                SELECT 
                    tbl_prizes.item_id, 
                    tbl_items.o_image, 
                    tbl_items.price, 
                    tbl_items.o_name, 
                    tbl_winners.u_id, 
                    tbl_winners.winner_rank,
                    tbl_winners.winner_name, 
                    tbl_winners.winning_value, 
                    tbl_users.image AS user_image
                FROM 
                    tbl_winners
                INNER JOIN
                    tbl_prizes ON tbl_prizes.o_id = tbl_winners.o_id
                    AND tbl_winners.winner_rank BETWEEN tbl_prizes.rank_start AND tbl_prizes.rank_end
                INNER JOIN 
                    tbl_items ON tbl_items.item_id = tbl_prizes.item_id 
                LEFT JOIN 
                    tbl_users ON tbl_users.id = tbl_winners.u_id 
                WHERE 
                    tbl_winners.o_id = ?
                GROUP BY 
                    tbl_winners.u_id
                ORDER BY 
                    tbl_winners.winner_rank";

            $winnerStmt = $mysqli->prepare($winnerQuery);
            $winnerStmt->bind_param("i", $data['o_id']);
            $winnerStmt->execute();
            $winnerResult = $winnerStmt->get_result();

            $winners = array();
            while ($winnerData = $winnerResult->fetch_assoc()) {
                $winner = array(
                    
                    'is_winner' => ($winnerData['u_id'] == $user_id) ? 1 : 0,
                    'rank' => !empty($winnerData['winner_rank']) ? $winnerData['winner_rank'] : '0',
                    'winner_name' => !empty($winnerData['winner_name']) ? $winnerData['winner_name'] : 'No Winner',
                    'winning_value' => !empty($winnerData['winning_value']) ? $winnerData['winning_value'] : '0',
                    'o_name' => $winnerData['o_name'],
                    'o_image' => $file_path . 'images/thumbs/' . $winnerData['o_image'],
                    'item_worth' => $winnerData['price'],
                );
                $winners[] = $winner;
            }

            $row['winners'] = $winners;
        }

        array_push($jsonObj4, $row);
     }
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    die();
}

// Get winners inside
else if (isset($_GET['get_winners_id'])) 
{
    
    $user_id = $_POST['user_id'];
    $o_id = $_POST['o_id'];
    //$user_id = 2;
    //$o_id = 476;
    
    $jsonObj4 = array();
    $date = mysqli_real_escape_string($mysqli, $date); // Sanitize date input
    
    $query = "
        SELECT 
            tbl_offers.*, 
            tbl_vendor.*, 
            tbl_items.*,
            tbl_users.*, tbl_users.id AS u_id, tbl_users.image AS user_image
        FROM 
            tbl_offers
        LEFT JOIN 
            tbl_vendor ON tbl_offers.id = tbl_vendor.id
        LEFT JOIN 
            tbl_items ON tbl_items.item_id = tbl_offers.item_id
        LEFT JOIN 
            tbl_users ON tbl_users.id = tbl_offers.winner_id
        WHERE 
            tbl_offers.o_id = ? 
            AND tbl_offers.o_status = 1 
        ORDER BY 
            tbl_offers.o_id DESC";

    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("s", $o_id);
    $stmt->execute();
    $result = $stmt->get_result();

    while ($data = $result->fetch_assoc()) {
        
            $o_price = $data['o_price'];
            $item_id = $data['item_id'];
            $winning_value = $data['winning_value'];
        
            $rate4 = "SELECT * FROM tbl_order
                      WHERE tbl_order.u_id = '$user_id' AND tbl_order.offer_id='$item_id'";

            $rateresult4 = mysqli_query($mysqli, $rate4) or die(mysqli_error());
            $num_rows4 = mysqli_num_rows($rateresult4);

            if ($num_rows4 > 0) {
                $o_click = "1";
            } else {
                $o_click = "0";
            }
            
            $query11="SELECT *, COUNT(*)
                     as num1 , COUNT(tbl_bid.u_id) AS total_user FROM tbl_bid 
                     where tbl_bid.o_id='$o_id' 
                     GROUP BY tbl_bid.u_id";

		   $result11 = mysqli_query($mysqli,$query11)or die(mysqli_error());

		   $row11=mysqli_fetch_assoc($result11);
    

			if($row11['num1']== null)
			{
		            $total_bids = "0";
		          
			}else
			{	
			  	    $total_bids = $row11['num1'];
			     
			}  
			
		    $query111="SELECT COUNT(DISTINCT u_id) AS total_users
                       FROM (
                           SELECT u_id FROM tbl_bid WHERE o_id = '".$o_id."'
                           UNION
                           SELECT u_id FROM tbl_ticket WHERE o_id = '".$o_id."'
                       ) AS combined_users";

		    $result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());

		    $row111=mysqli_fetch_assoc($result111);
    

			if($row111['total_users']== null)
			{
		             $total_users = "0";
		          
			}else
			{	
			  	    $total_users = 	$row111['total_users'];
			     
			}
			
			if ($winning_value != NULL) {
                $winner_discount = (string) round((1 - ($winning_value / $o_price)) * 100);
            } else {
                $winner_discount = "0"; // Default to 0% discount if the original price is zero
            }
            
            if($join_date != null)
			{
		            $join_date = $data['date'];
		          
			}else
			{	
			  	    $join_date = '2024-01-01';
			     
			}
			
			
            $joining_date = new DateTime($join_date);
            // Format the date to "Month Year"
            $formatted_joining_date = $joining_date->format('F Y');

        
        
        if ($data['o_type'] != 5) {
            $row = array(
                
                'total_bids' => $total_bids,
                'total_users' => $total_users,
                'winner_discount' => $winner_discount,
                
                'joining_date' => $formatted_joining_date,
                'user_image' => !empty($data['user_image']) ? $file_path . 'images/' . $data['user_image'] : $file_path . 'images/placeholder_user.jpg',
    
                'o_id' => $data['o_id'],
                'item_id' => $data['item_id'],
                'o_name' => $data['o_name'],
                'o_image' => $file_path . 'images/' . $data['o_image'],
                'o_image1' => $file_path . 'images/' . $data['o_image1'],
                'o_image2' => $file_path . 'images/' . $data['o_image2'],
                'o_image3' => $file_path . 'images/' . $data['o_image3'],
                'o_image4' => $file_path . 'images/' . $data['o_image4'],
                'o_edate' => $data['o_edate'],
                'o_type' => $data['o_type'],
                'prize_pool' => $data['o_price'],
                'winner_name' => $data['name'],
                'winning_value' => $data['winning_value'],
                'is_winner' => ($data['winner_id'] == $user_id) ? 1 : 0,
                'multiple_winner' => 0,
                'order_placed' => $o_click
            );
            
        } else {
            $row = array(
                'o_id' => $data['o_id'],
                'o_edate' => $data['o_edate'],
                'o_type' => $data['o_type'],
                'o_image' => $file_path . 'images/' . $data['o_image'],
                'o_image1' => $file_path . 'images/' . $data['o_image1'],
                'o_image2' => $file_path . 'images/' . $data['o_image2'],
                'o_image3' => $file_path . 'images/' . $data['o_image3'],
                'o_image4' => $file_path . 'images/' . $data['o_image4'],
                'prize_pool' => $data['o_price'],
                'multiple_winner' => 1,
            );

            // Fetch winners
            $winnerQuery = "SELECT 
                    tbl_prizes.item_id, 
                    tbl_items.o_image, 
                    tbl_items.price, 
                    tbl_items.o_name, 
                    tbl_winners.u_id, 
                    tbl_winners.winner_rank,
                    tbl_winners.winner_name, 
                    tbl_winners.winning_value, 
                    tbl_users.image AS user_image
                FROM 
                    tbl_winners
                INNER JOIN
                    tbl_prizes ON tbl_prizes.o_id = tbl_winners.o_id
                    AND tbl_winners.winner_rank BETWEEN tbl_prizes.rank_start AND tbl_prizes.rank_end
                INNER JOIN 
                    tbl_items ON tbl_items.item_id = tbl_prizes.item_id 
                LEFT JOIN 
                    tbl_users ON tbl_users.id = tbl_winners.u_id 
                WHERE 
                    tbl_winners.o_id = ?
                GROUP BY 
                    tbl_winners.u_id
                ORDER BY 
                    tbl_winners.winner_rank";
                            
            $winnerStmt = $mysqli->prepare($winnerQuery);
            $winnerStmt->bind_param("i", $data['o_id']);
            $winnerStmt->execute();
            $winnerResult = $winnerStmt->get_result();

            $winners = array();
            while ($winnerData = $winnerResult->fetch_assoc()) {
                
            $winner_id = $winnerData['u_id'];
            $item_id = $winnerData['item_id'];
            
            $rate4 = "SELECT * FROM tbl_order
                      WHERE tbl_order.u_id = '$winner_id' AND tbl_order.offer_id='$item_id'";

            $rateresult4 = mysqli_query($mysqli, $rate4) or die(mysqli_error());
            $num_rows4 = mysqli_num_rows($rateresult4);

            if ($num_rows4 > 0) {
                $o_click = "1";
            } else {
                $o_click = "0";
            }
            
                $winner = array(
                    
                    'is_winner' => ($winnerData['u_id'] == $user_id) ? 1 : 0,
                    'user_image' => !empty($winnerData['user_image']) ? $file_path . 'images/' . $winnerData['user_image'] : $file_path . 'images/placeholder_user.jpg',
                    'order_placed' => $o_click,
                    'rank' => $winnerData['winner_rank'],
                    'winner_name' => !empty($winnerData['winner_name']) ? $winnerData['winner_name'] : 'No Winner',
                    'winning_value' => !empty($winnerData['winning_value']) ? $winnerData['winning_value'] : '0',
                    'o_name' => $winnerData['o_name'],
                    'o_image' => $file_path . 'images/thumbs/' . $winnerData['o_image'],
                    'item_worth' => $winnerData['price'],
                    'item_id' => $winnerData['item_id']
                );
                $winners[] = $winner;
            }

            $row['winners'] = $winners;
        }

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    die();
}

//get reviews
else if(isset($_GET['get_reviews']))
{
  		 $jsonObj4= array();	

		$query="SELECT *
                FROM tbl_reviews
                LEFT JOIN tbl_offers ON tbl_offers.item_id = tbl_reviews.item_id
                LEFT JOIN tbl_users ON tbl_users.id = tbl_reviews.user_id
                WHERE tbl_offers.o_id ='".$_POST['o_id']."'"; 
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


        while($data = mysqli_fetch_assoc($sql))
        {
            $row['rating'] = $data['rating'];
            $row['review'] = $data['comment'];
            $row['reviewed_on'] = $data['created_at'];
            $row['user'] = $data['name'];
            $row['user_image'] =   $file_path.'images/'.$data['image'];
        
            array_push($jsonObj4,$row); 
        }
        
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 else if(isset($_GET['add_review']))
    {
        
    $user_id = mysqli_real_escape_string($mysqli, $_POST['user_id']);
    $item_id = mysqli_real_escape_string($mysqli, $_POST['item_id']);
    $rating = mysqli_real_escape_string($mysqli, $_POST['rating']);
    $comment = mysqli_real_escape_string($mysqli, $_POST['review']);
    $datetime = date('Y-m-d H:i:s'); // Current date and time

    $qry1 = "INSERT INTO tbl_reviews (
        user_id,
        item_id,
        rating,
        comment,
        created_at
    ) VALUES (
        '$user_id',
        '$item_id',
        '$rating',
        '$comment',
        '$datetime'
    )";

    $result1 = mysqli_query($mysqli, $qry1);

    if ($result1) {
        $set['JSON_DATA'][] = array('msg' => "Reviewed Successfully");
    } else {
        $set['JSON_DATA'][] = array('msg' => "Error adding review: " . mysqli_error($mysqli));
    }

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

else if(isset($_GET['update_review']))
{
    $user_id = mysqli_real_escape_string($mysqli, $_POST['user_id']);
    $item_id = mysqli_real_escape_string($mysqli, $_POST['item_id']);
    $rating = mysqli_real_escape_string($mysqli, $_POST['rating']);
    $comment = mysqli_real_escape_string($mysqli, $_POST['review']);
    $datetime = date('Y-m-d H:i:s'); // Current date and time

    $qry1 = "UPDATE tbl_reviews SET 
                    rating = '$rating',
                    comment = '$comment',
                    updated_at = '$datetime'
                    WHERE user_id = '$user_id' AND item_id = '$item_id'";

    $result1 = mysqli_query($mysqli, $qry1);

    if ($result1) {
        $set['JSON_DATA'][] = array('msg' => "Review Updated Successfully");
    } else {
        $set['JSON_DATA'][] = array('msg' => "Error updating review: " . mysqli_error($mysqli));
    }

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
 
else if (isset($_GET['update_consolation'])) {
    // Assuming you have a database connection $mysqli

  
    $s_id = $_POST['s_id'];
    $s_status = $_POST['s_status'];
    
    // Use prepared statement to prevent SQL injection
    $query = "UPDATE tbl_scratch SET s_status=? WHERE s_id=?";
    $stmt = mysqli_prepare($mysqli, $query);

    // Bind parameters
    mysqli_stmt_bind_param($stmt, "si", $s_status, $s_id);

    // Execute the statement
    $success = mysqli_stmt_execute($stmt);

    // Check the number of affected rows
    $affected_rows = mysqli_stmt_affected_rows($stmt);

    if ($success) {
        if ($affected_rows > 0) {
            // Update successful
            $set['JSON_DATA'][] = array('msg' => "Congratulations! Rows updated: $affected_rows", 'success' => '1');
        } else {
            // No rows were affected
            $set['JSON_DATA'][] = array('msg' => "No rows were updated.", 'success' => '0');
        }
    } else {
        // Update failed
        $set['JSON_DATA'][] = array('msg' => "Error: " . mysqli_error($mysqli), 'success' => '0');
    }

    // Close the statement
    mysqli_stmt_close($stmt);

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
 
 //get all notifications
else if(isset($_GET['get_notification']))
 	{
  		 $jsonObj4= array();	
  		 
  		 $user_id = sanitize($_POST['u_id']);

		$query="SELECT *, COUNT(*)
             as ava FROM tbl_notifications  where o_id ='".$_POST['o_id']."' and u_id ='$user_id'";
    	$result = mysqli_query($mysqli,$query)or die(mysqli_error());

		   $row=mysqli_fetch_assoc($result);

            if ($row['ava'] == 0)
                {
                    $set['JSON_DATA'][]=array('msg' => "Yes",'success'=>'1','ava'=>$row['ava']);
                }
            else
                {
                    $set['JSON_DATA'][]=array('msg' => "No",'success'=>'0','ava'=>$row['ava']);
                }
		

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
//get notification history
else if(isset($_GET['get_notification_history']))
 	{
  		 $jsonObj4= array();	
  		 
  		 $user_id = sanitize($_POST['u_id']);

        $query = "SELECT * FROM tbl_notifications
        WHERE u_id='$user_id' AND status IN (1, 2)"; 
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


        while($data = mysqli_fetch_assoc($sql))
        {
            $row['id'] = $data['id'];
            $row['u_id'] = $data['u_id'];
            $row['tittle'] = $data['tittle'];
            $row['body'] = $data['body'];
            $row['image'] = $data['image'];
            $row['link'] = $data['link'];
            $row['time'] = $data['time'];
            $row['action'] = $data['action'];
            $row['status'] = $data['status'];
            
            
            array_push($jsonObj4,$row); 
        }
        
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
//delete notification
else if(isset($_GET['delete_notification'])) {
    $jsonObj4 = array();

    $array = explode(',', $_POST['ids']);
    foreach($array as $id){
        $query = "UPDATE tbl_notifications SET status = 0 WHERE u_id='" . $_POST['u_id'] . "' AND id='$id'";
        $sql = mysqli_query($mysqli, $query);
        
        if ($sql) {
            // Update successful
            $set['JSON_DATA'][] = array('msg' => "Notifications Deleted !", 'success' => '1');
        } else {
            // Update failed
            $set['JSON_DATA'][] = array('msg' => "Error: " . mysqli_error($mysqli), 'success' => '0');
        }   
    }

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}


//delete all notification
else if(isset($_GET['delete_notification_all']))
 	{
	    $id = sanitize($_POST['u_id']);
	   
		$user_edit= "UPDATE tbl_notifications SET status = 0 WHERE u_id= $id"; 	 
		
   		$user_res = mysqli_query($mysqli,$user_edit);	
	  	
	  	$set['JSON_DATA'][]=array('msg'=>'Updated','success'=>'1');		 
		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();	
	}
	
else if(isset($_GET['set_notification_token'])) {
    
    $id = sanitize($_POST['u_id']);
    $token = sanitize($_POST['fcm_token']);
   
    if (!empty($token)) { // Check if fcm_token is not NULL
        $user_edit = "INSERT INTO `tbl_fcm_token`(`u_id`, `fcm_token`, `date`) VALUES ('$id', '$token', NOW())";
        
        $user_res = mysqli_query($mysqli, $user_edit);    
        
        $set['JSON_DATA'][] = array('msg' => 'Welcome Back!', 'success' => '1');   
        
        header('Content-Type: application/json; charset=utf-8');
        echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
        die();
    } else {
        
        $set['JSON_DATA'][] = array('msg' => 'Logout Success!', 'success' => '1');   
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(array('error' => $error_message));
        die();
    }
}

else if(isset($_GET['update_wishlist'])) {
    
    $id = sanitize($_POST['u_id']);
    $item = sanitize($_POST['item_id']);
    $type = sanitize($_POST['type']);
   
    if ($type == 'add') { 
        $user_edit = "INSERT INTO `tbl_wishlist`(`user_id`, `item_id`, `created_at`) VALUES ('$id', '$item', NOW())";
        $user_res = mysqli_query($mysqli, $user_edit);    
        
        if ($user_res) {
            $set['JSON_DATA'][] = array('msg' => 'Item Added to Wishlist!', 'success' => '1');   
        } else {
            $set['JSON_DATA'][] = array('msg' => 'Failed to add item to Wishlist!', 'success' => '0');   
        }
        
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
        die();
    } else if ($type == 'delete') { 
        $user_edit = "DELETE FROM `tbl_wishlist` WHERE item_id ='$item' AND user_id = '$id'";
        $user_res = mysqli_query($mysqli, $user_edit);    
        
        if ($user_res) {
            $set['JSON_DATA'][] = array('msg' => 'Item Removed from Wishlist!', 'success' => '1');   
        } else {
            $set['JSON_DATA'][] = array('msg' => 'Failed to delete item from Wishlist!', 'success' => '0');   
        }
        
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
        die();
    }
}

//mark notifications as read
else if(isset($_GET['read_all_notifications']))
 	{
 	    
 	    $id = sanitize($_POST['u_id']);
	   
		$user_edit= "UPDATE tbl_notifications SET status = 2 WHERE u_id= $id AND status=1"; 	 
		
   		$user_res = mysqli_query($mysqli,$user_edit);	
	  	
	  	$set['JSON_DATA'][]=array('msg'=>'Updated','success'=>'1');		 
		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();	
	}

//users update profile
else if(isset($_GET['postUserProfileUpdate']))
{
    
    $user_id = sanitize($_POST['id']);
    $phone = sanitize($_POST['phone']);
    $password = sanitize($_POST['password']);
    $name = sanitize($_POST['name']);
    $email = sanitize($_POST['email']);

		$sql = "SELECT * FROM tbl_users where id = '$user_id' ";
		$res = mysqli_query($mysqli,$sql);
		$row = mysqli_fetch_assoc($res);
		
		if($_FILES['image']['name'] != "")
		{	
			if($row['image'] !== "") 
			{
			 	unlink('images/'.$row['image']); 
			 	unlink('images/thumbs/'.$row['image']); 
			}

			$facility_image=rand(0,99999)."_".$_FILES['image']['name'];
		   //Main Image
		   	$tpath1='images/'.$facility_image; 		
			$pic1=compress_image($_FILES["image"]["tmp_name"], $tpath1, 80);
		 	$thumbpath='images/thumbs/'.$facility_image;		
	       	$thumb_pic1=create_thumb_image($tpath1,$thumbpath,'200','200');   
 		}
 		else
 		{
 			$facility_image = $row['image'] ;
 		}

	 
			$user_edit= "UPDATE tbl_users SET 
			name='$name',
			email='$email',
			phone='$phone',
			password='$password',
			image = '".$facility_image."'
			WHERE id = '$user_id'";	 
   		
   		$user_res = mysqli_query($mysqli,$user_edit);	
	  	
		$set['JSON_DATA'][]=array('msg'=>'Updated','success'=>'1');
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

//user wallet update
else if(isset($_GET['postGameWon']))
{
            $user_id = sanitize($_POST['user_id']);
            $postWallet = sanitize($_POST['wallet']);
    
        	if($user_id!="")
			{
			    $qry = "SELECT * FROM tbl_users WHERE id = '$user_id'";	 
				$result = mysqli_query($mysqli,$qry);
				$num_rows = mysqli_num_rows($result);
				$row = mysqli_fetch_assoc($result);
		
		        if ($num_rows > 0)
				{
				    
				    
			         $id=$row['id'];
	    		     $wallet=$row['wallet'];
	    		     $update_amount = $postWallet;
	    		     $newwallet=$wallet+$update_amount;   
			    
				     $user_edit= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	 
				     $user_res = mysqli_query($mysqli,$user_edit);	

				     
					 $qry1="INSERT INTO tbl_transaction (`user_id`,`type`,`type_no`,`date`,`money`) 
				VALUES ('$id',3,'$r2','$datetime','20')"; 
	            
	            $result1=mysqli_query($mysqli,$qry1); 		
				     
				     $set['JSON_DATA'][]=array('msg'=>'Winnings Added','success'=>'1');

				}
			}
    	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}
//user wallet update
else if(isset($_GET['postGameLoss']))
{
            $user_id = sanitize($_POST['user_id']);
            $postWallet = sanitize($_POST['wallet']);

        	if($user_id!="")
			{
			    $qry = "SELECT * FROM tbl_users WHERE id = '$user_id'";	 
				$result = mysqli_query($mysqli,$qry);
				$num_rows = mysqli_num_rows($result);
				$row = mysqli_fetch_assoc($result);
		
		        if ($num_rows > 0)
				{
				    
				    
			         $id=$row['id'];
	    		     $wallet=$row['wallet'];
	    		     $update_amount = $postWallet;
	    		     $newwallet=$wallet+$update_amount;   
			    
				     $user_edit= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	 
				     $user_res = mysqli_query($mysqli,$user_edit);	

				     
					 $qry1="INSERT INTO tbl_transaction (`user_id`,`type`,`type_no`,`date`,`money`) 
				VALUES ('$id',4,'$r2','$datetime','10')"; 
	            
	            $result1=mysqli_query($mysqli,$qry1); 		
				     
				     $set['JSON_DATA'][]=array('msg'=>'Winnings Added','success'=>'1');

				}
			}
    	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}
//get all settings
else if(isset($_GET['settings']))
 	{ 
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_settings WHERE id='1'";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
			$row['showad'] = $data['showad'];
			$row['support_email'] = $data['admin_email'];
			$row['coinvalue'] = $data['coinvalue'];
			$row['otp_system'] = $data['otp_system'];
			$row['name'] = $data['app_name'];
			$row['demo_access'] = $data['demo_access'];
			$row['ads_reward'] = $data['ads_reward'];
			$row['logo'] = $data['app_logo'];
			$row['vungle_app'] = $data['vungle_app'];
			$row['vungle_id'] = $data['vungle_placement_rewarded'];
			$row['adcolony_app'] = $data['adcolony_app'];
			$row['adcolony_id'] = $data['adcolony_rewarded'];
			$row['unity_game'] = $data['unity_game'];
			$row['unity_id'] = $data['unity_rewarded'];
			$row['currency'] = $data['currency'];
			$row['paypal_currency'] = $data['paypal_currency'];
			$row['app_privacy_policy'] = stripslashes($data['app_privacy_policy']);
 			$row['admob_rewarded'] = $data['admob_rewarded'];
 			$row['admob_interstitial'] = $data['admob_interstitial'];
 			$row['admob_banner'] = $data['admob_banner'];
 			$row['facebook_rewarded'] = $data['fb_rewarded'];
 			$row['fb_interstitial'] = $data['fb_interstitial'];
 			$row['fb_banner'] = $data['fb_banner'];
			$row['applovin_rewarded'] = $data['applovin_rewarded'];
 			$row['startio_rewarded'] = $data['startio_rewarded'];
 			$row['ironsource_rewarded'] = $data['ironsource_rewarded'];
			$row['mpesa_key'] = $data['mpesa_key'];
			$row['mpesa_code'] = $data['mpesa_code'];
			$row['paypal_id'] = $data['paypal_id'];
			$row['paypal_secret'] = $data['paypal_secret'];
			$row['flutterwave_public'] = $data['flutterwave_public'];
			$row['flutterwave_encryption'] = $data['flutterwave_encryption'];
			$row['razorpay'] = $data['razorpay_key'];
			$row['stripe'] = $data['stripe_key'];
			$row['currency'] = $data['currency'];
			$row['how_to_play'] = stripslashes($data['how_to_play']);
			$row['about_us'] = stripslashes($data['about_us']);

			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
else if(isset($_GET['checkRegistrationPhone'])) {
   
    $cpp = sanitize($_POST['country_code']);
    $phone = sanitize($_POST['phone']);
    
    $user_edit = "SELECT * FROM tbl_users WHERE phone = '".$phone."' AND country_code = '".$cpp."' AND status='1'";
    $user_res = mysqli_query($mysqli, $user_edit);
    $num_rows = mysqli_num_rows($user_res);
    
    // Check if phone number exists
    if ($num_rows > 0) {
        $set['JSON_DATA'][]=array('msg'=>'Phone number is already in use','success'=>'0');
    } else {
        $set['JSON_DATA'][]=array('msg'=>'Phone number is available','success'=>'1');
    }
    
    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

else if(isset($_GET['checkRegistrationEmail'])) {
    $email = sanitize($_POST['email']);

    $user_edit = "SELECT * FROM tbl_users WHERE email = '".$email."' AND status='1'";
    $user_res = mysqli_query($mysqli, $user_edit);
    $num_rows = mysqli_num_rows($user_res);
    
    // Check if email address exists
    if ($num_rows > 0) {
        $set['JSON_DATA'][]=array('msg'=>'Email is already in use','success'=>'0');
    } else {
        $set['JSON_DATA'][]=array('msg'=>'Email is available','success'=>'1');
    }
    
    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

else if(isset($_GET['postUserRegister']))
 {
     
     $email_id = sanitize($_POST['email']);
     $userName = sanitize($_POST['name']);
     $userPassword = sanitize($_POST['password']);
     $userPhone = sanitize($_POST['phone']);
     $userReferral = sanitize($_POST['refferal_code']);


		$qry = "SELECT * FROM tbl_users WHERE email = '".$email_id."' "; 
		$result = mysqli_query($mysqli,$qry);
		$num_rows = mysqli_num_rows($result);
		$row = mysqli_fetch_assoc($result);
		
    	if ($num_rows > 0)
		{
			$set['JSON_DATA'][]=array('msg' => "Email address is already used!",'success'=>'0');
		}
		else
		{ 

        if($_FILES['image']['name'] != "")
		{
              $category_image=rand(0,99999)."_".$_FILES['image']['name'];
       
       //Main Image
     $tpath1='images/'.$category_image;        
       $pic1=compress_image($_FILES["image"]["tmp_name"], $tpath1, 80);
   
    //Thumb Image 
     $thumbpath='images/thumbs/'.$category_image;   
       $thumb_pic1=create_thumb_image($tpath1,$thumbpath,'300','300');   
		}else{
		    $image='';
		}
       
			$refferal_code = $userReferral;
			
		    $rand=rand(1000,999999);

		     $qry1="INSERT INTO tbl_users 
				  (`login_type`,
				  `name`,
				  `email`,
				   `image`,
				   password,
				   `phone`,
				    `device_id`,
				   `wallet`,
				    `code`,
				    `refferal_code`,
				  `status`
				) VALUES (
					'1',
					'$userName',
					'$email_id',
					'".$image."',
					'$userPassword',
					'$userPhone',
					'',
					'10',						
					'$rand',
					'$userReferral',
					'0'
				)"; 
            
            $result1 = mysqli_query($mysqli,$qry1);
            $last_id = mysqli_insert_id($mysqli);  
            
            $qrys = "SELECT * FROM tbl_users WHERE id = '".$last_id."'"; 
			$results = mysqli_query($mysqli,$qrys);
			$row = mysqli_fetch_assoc($results);
			$firstid=$row['id'];
												 
			$set['JSON_DATA'][]	=	array(    
                           'msg' 	=>	"succecesfull login",
 						  'login_type' 	=>	$row['login_type'],
 						  'id' 	=>	$row['id'],
 						  'name'	=>	$row['name'],
 						  'email'	=>	$row['email'],
 						   'password'	=>	$row['password'],
 						  'image'	=>	$row['image'],
 						  'phone'	=>	$row['phone'],
 						  'wallet'	=>	$row['wallet'],
 						  'code'	=>	$row['code'],
 						   'refferal_code'	=>	$row['refferal_code'],
 						  'network'	=>	$row['network'],
 						  'status'	=>	$row['status']
		     								);
		    		 
				
			if($refferal_code!="")
		    {
			    $qry = "SELECT * FROM tbl_users WHERE code = '".$refferal_code."'";	 
	    		$result = mysqli_query($mysqli,$qry);
	    		$num_rows = mysqli_num_rows($result);
	    		$row = mysqli_fetch_assoc($result);
	 	    	$r2 = $row['refferal_code'];
	 	    	$c2 = $row['id'];

		        if ($num_rows > 0)
	    		{
	    		     $id=$row['id'];

	    		     $wallet=$row['wallet'];
	    		     $first1=2;   
	    		     $newwallet=$wallet+$first1 ;
	    		     

	    		     $network=$row['network'];
	    		     $newnetwork=$network+1; 
				
				 $network_qry="INSERT INTO tbl_network (`user_id`,`level`,`money`,`refferal_user_id`,`status`) 
				               VALUES ('$id','1','$first1','$r2','1')"; 

					$result1=mysqli_query($mysqli,$network_qry); 
	            

	                $first_level= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	 
	    		    $first_level_earn1 = mysqli_query($mysqli,$first_level);
	    		    
		                  $id=$row['id'];
	    		      $date = date("M-d-Y h:i:s");  
	    		      
	    		     
	    		     	$qry1="INSERT INTO tbl_transaction (`user_id`,`type`,`type_no`,`date`,`money`) 
				VALUES ('$id',2,'$r2','$date','$first1')"; 
	            
	            $result1=mysqli_query($mysqli,$qry1);  		

		
	    		}
	    		

	    	}


		
	
}
	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
	}
else if(isset($_GET['postuserstatus']))
{
 			$email_id = sanitize($_POST['email']);
		
		$qry = "SELECT * FROM tbl_users WHERE email = '".$email_id."'"; 
		$result = mysqli_query($mysqli,$qry);
		$num_rows = mysqli_num_rows($result);
		$row = mysqli_fetch_assoc($result);
		
    	if ($num_rows > 0)
		{
			$set['JSON_DATA'][]	=	array( 
 						  
 			'user_status'		=>'1',	  
 			'id' => $row['id'],
	        'login_type'=>$row['login_type'],
	        'name'=>$row['name'],
	        'email'=>$row['email'],
	        'image'=>$row['image'],
	        'phone'=>$row['phone'],
	        'device_id'=>$row['device_id'],
	        'wallet'=>$row['wallet'],
	         'network'=>$row['network'],
            'ban'	=>	$row['ban'],
	        'code'=>$row['code'],	       
	        'status'=>$row['status'],
		     								
		     								
		     								
		   );
		}		 
		else
		{
		    	$set['JSON_DATA'][]	=	array( 
		     								  'user_status'	=>'0'
		     								);
		    		 
        }
 			header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 	}


else if(isset($_GET['getUserProfile']))
    {

    $userId = sanitize($_POST['id']);

    // Prepare and execute query using prepared statement with JOIN and COALESCE
    $qry = $mysqli->prepare("
        SELECT u.id, u.login_type, u.name, u.email, u.password, u.image, u.phone, 
               u.device_id, u.wallet, u.network, u.ban, u.code, u.status, 
               COALESCE(k.kyc_status, 0) AS kyc_status
        FROM tbl_users u
        LEFT JOIN tbl_kyc k ON u.id = k.u_id
        WHERE u.id = ?
    ");
    $qry->bind_param("i", $userId);
    $qry->execute();
    $result = $qry->get_result();
    $row = $result->fetch_assoc();
	  			
	$set['JSON_DATA'][]	=	array(
	    	'id' => $row['id'],
	        'login_type'=>$row['login_type'],
	        'name'=>$row['name'],
	        'email'=>$row['email'],
	         'password'=>'hidden',
	        'image'=>$row['image'],
	        'phone'=>$row['phone'],
	        'device_id'=>$row['device_id'],
	        'wallet'=>$row['wallet'],
	         'network'=>$row['network'],
	         'ban'	=>	$row['ban'],
	         'kyc_status' => $row['kyc_status'],
	        'code'=>$row['code'],	       
	        'status'=>$row['status'],
	    							);
	    							
	    $insertlogin = "INSERT INTO tbl_logcat (u_id, log_time) VALUES ('$userId', NOW())";
        mysqli_query($mysqli, $insertlogin);

	   	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

else if(isset($_GET['get_current_balance']))
    {

    $userId = sanitize($_POST['user_id']);

    // Prepare and execute query using prepared statement with JOIN and COALESCE
    $qry = $mysqli->prepare("
        SELECT  u.wallet
        FROM tbl_users u
        WHERE u.id = ?
    ");
    $qry->bind_param("i", $userId);
    $qry->execute();
    $result = $qry->get_result();
    $row = $result->fetch_assoc();
	  			
	$set['JSON_DATA'][]	=	array('wallet'=>$row['balance']);

	   	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

//user wallet update
else if(isset($_GET['postUserwalletUpdate']))
{
    
        $user_id = sanitize($_POST['user_id']);
        $package_id = sanitize($_POST['package_id']);
        $order_id = sanitize($_POST['order_id']);
        $postWallet = sanitize($_POST['wallet']);
        
        	if($user_id!="")
			{
			    $qry = "SELECT * FROM tbl_users WHERE id = '$user_id'";	 
				$result = mysqli_query($mysqli,$qry);
				$num_rows = mysqli_num_rows($result);
				$row = mysqli_fetch_assoc($result);
		
		        if ($num_rows > 0)
				{
				    
				    
			         $id=$row['id'];
	    		     $wallet=$row['wallet'];
	    		     $update_amount=$postWallet;
	    		     $newwallet=$wallet+$update_amount;   
			    
				     $user_edit= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	 
				     $user_res = mysqli_query($mysqli,$user_edit);
				     
				     $qry2 ="INSERT INTO tbl_wallet_passbook ( `wp_user`, `wp_package_id`, `wp_order_id`, `wp_date`, `wp_status`)
			VALUES ('$user_id','$package_id','$order_id','".$datetime."',1 )"; 
            
            $result2 = mysqli_query($mysqli,$qry2);  		
				     
				     $set['JSON_DATA'][]=array('msg'=>'Coins Added','success'=>'1');

				}
			}
    	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

//user wallet update (for games, ads, scratch cards, achievements)
else if(isset($_GET['post_addUserBal']))
{
    
        $user_id = sanitize($_POST['user_id']);
        $postWallet = sanitize($_POST['wallet']);
        $transactionType = sanitize($_POST['type']);

        	if($user_id!="")
			{
			    $qry = "SELECT * FROM tbl_users WHERE id = '$user_id'";	 
				$result = mysqli_query($mysqli,$qry);
				$num_rows = mysqli_num_rows($result);
				$row = mysqli_fetch_assoc($result);
		
		        if ($num_rows > 0)
				{
				    
				    
			         $id=$row['id'];
	    		     $wallet=$row['wallet'];
	    		     $update_amount = $postWallet;
	    		     $newwallet=$wallet+$update_amount;   
			    
				     $user_edit= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	 
				     $user_res = mysqli_query($mysqli,$user_edit);
				     

				     $qry2 ="INSERT INTO tbl_transaction (`user_id`,`type`,`type_no`,`date`,`money`) 
			VALUES ('$user_id','$transactionType','0','".$datetime."','$postWallet' )"; 
            
            $result2 = mysqli_query($mysqli,$qry2);  		
				     
				     $set['JSON_DATA'][]=array('msg'=>'Coins Added','success'=>'1');

				}
			}
    	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}
 
//get all offers
else if(isset($_GET['get_offers']))
 	{
  		 $jsonObj4= array();	
  		 
  		 $city_id = sanitize($_POST['city_id']);

		$query="SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_type != 6 AND tbl_offers.city_id= '$city_id' AND o_status='1' ";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['id'] = $data['id'];
			    $row['c_id'] = $data['c_id'];
			    $row['city_id'] = $data['city_id'];
			    $row['c_name'] = $data['c_name'];
			    $row['c_desc'] = $data['c_desc'];
			    $row['c_color'] = $data['c_color'];
			    $row['c_image'] =  $file_path.'images/'.$data['c_image'];
			    $row['c_view'] = $data['c_view'];
			    $row['o_id'] = $data['o_id'];
			    $row['o_name'] = $data['o_name'];
			    $row['o_image'] =  $file_path.'images/'.$data['o_image'];
			    $row['o_image1'] =  $file_path.'images/'.$data['o_image1'];
			    $row['o_image2'] =  $file_path.'images/'.$data['o_image2'];
			    $row['o_image3'] =  $file_path.'images/'.$data['o_image3'];
			    $row['o_image4'] =  $file_path.'images/'.$data['o_image4'];
			    $row['o_desc'] = $data['o_desc'];
			    $row['o_link'] = $data['o_link'];
			    $row['o_date'] = $data['o_date'];
			    $row['o_edate'] = $data['o_edate'];
			    $row['o_stime'] = $data['o_stime'];
			    $row['o_etime'] = $data['o_etime'];
			    $row['o_amount'] = $data['o_amount'];
			    $row['o_type'] = $data['o_type'];
			    $row['o_winners'] = $data['o_winners'];
			    $row['o_min'] = $data['o_min'];
			    $row['o_max'] = $data['o_max'];
			    $row['o_qty'] = $data['o_qty'];
			    $row['o_color'] = $data['o_color'];
			    $row['bid_increment'] = $data['bid_increment'];	
			    $row['time_increment'] = $data['time_increment'];	
			    $row['o_price'] = $data['o_price'];
			    $row['o_buy'] = $data['o_buy'];
			    $row['o_status'] = $o_status;
			    $row['c_status'] = $data['c_status'];
			
			
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 //get hyip
else if(isset($_GET['get_hyip']))
 	{
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_hyip WHERE plan_status = 1";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['plan_id'] = $data['plan_id'];
			    $row['plan_name'] = $data['plan_name'];
			    $row['plan_short_description'] = $data['plan_short_description'];
			    $row['plan_description'] = $data['plan_description'];
                // Check if minimum and maximum are equal
                if ($data['plan_minimum'] == $data['plan_maximum']) {
                    $row['investment'] = $data['plan_minimum'];
                } else {
                    $row['investment'] = $data['plan_minimum'] . '-' . $data['plan_maximum'];
                }
                $row['plan_interest_frequency'] = $data['plan_interest_frequency'];
                $row['plan_minimum'] = $data['plan_minimum'];
                $row['plan_maximum'] = $data['plan_maximum'];
			    $row['plan_interest'] = $data['plan_interest'];
			    $row['plan_interest_type'] = $data['plan_interest_type'];
			    $row['plan_color'] = $data['plan_color'];
			    $row['plan_duration'] = $data['plan_duration'];
			    $row['compound_interest'] =  $data['plan_compound_interest'];
			    $row['plan_penalty'] = $data['plan_penalty'];
			    $row['plan_penalty_type'] = $data['plan_penalty_type'];
			    $row['plan_cancelable'] = $data['plan_cancelable'];
			    $row['plan_lifetime'] = $data['plan_lifetime'];
			    $row['plan_capital_back'] = $data['plan_capital_back'];
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
//mark hyip as cancelled
else if(isset($_GET['cancel_hyip_plan']))
 	{

	    $user_id = sanitize($_POST['u_id']);
	    $plan_id = sanitize($_POST['plan_id']);
	    
	    $planDetailsQuery = "SELECT * FROM tbl_hyip_order WHERE order_id = $plan_id AND user_id= $user_id AND status= 1";
        $planDetailsResult = mysqli_query($mysqli, $planDetailsQuery);
        $planDetailsRow = mysqli_fetch_assoc($planDetailsResult);
        $currentValue = $planDetailsRow['current_value'];
        
        if (mysqli_num_rows($planDetailsResult) > 0) 
        {
	    
	    $checkBalanceQuery = "SELECT wallet FROM tbl_users WHERE id = $user_id";
        $checkBalanceResult = mysqli_query($mysqli, $checkBalanceQuery);
        $checkBalanceRow = mysqli_fetch_assoc($checkBalanceResult);
        $currentBalance = $checkBalanceRow['wallet'];
        
        $newwallet = $currentBalance + $currentValue;   
        $user_edit = "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$user_id."'"; 
        $result1 = mysqli_query($mysqli,$user_edit);
        
        $qry1 = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`, `date`, `money`) 
                         VALUES ('$user_id', 11, '11', NOW(), '$currentValue')";
        $result_insert = mysqli_query($mysqli, $qry1);
	   
		$user_edit= "UPDATE tbl_hyip_order SET status = '2' WHERE order_id= $plan_id AND user_id= $user_id"; 	 
		
   		$user_res = mysqli_query($mysqli,$user_edit);	
	  	
	  	$set['JSON_DATA'][]=array('msg'=>'Updated','success'=>'1');	
	  	
        }
        else
        {
            $set['JSON_DATA'][]=array('msg'=>'Plan not found or already redeemed','success'=>'0');
        }
		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();	
	}


if (isset($_GET['get_profile'])) {
    $jsonObj4 = array();
    
    $user_id = sanitize($_POST['u_id']);
    
    //Fetch Coins earned via scratch card
    $coinsEarnedQuery = "SELECT COUNT(s_id) as coin_count FROM tbl_scratch WHERE u_id = '$user_id'";
    $coinsEarnedSql = mysqli_query($mysqli, $coinsEarnedQuery) or die(mysqli_error());
    $result = mysqli_fetch_assoc($coinsEarnedSql);
    // Check if coin_count is empty and set it to 0 if needed
    $coin_count = isset($result['coin_count']) ? $result['coin_count'] : "0";


    // Fetch user data
    $query = "SELECT * FROM tbl_users WHERE id='$user_id' ";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

    while ($data = mysqli_fetch_assoc($sql)) {
        $row['name'] = $data['name'];
        $row['image'] = $file_path . 'images/' . $data['image'];
        $row['joined_on'] = $data['date'];
        $row['coins_earned'] = $coin_count;

        array_push($jsonObj4, $row);
    }

    // Fetch games played by the user
    $gamesPlayedQuery = "SELECT o_id, COUNT(*) as play_count FROM tbl_bid WHERE u_id ='$user_id' GROUP BY o_id";
    $gamesPlayedSql = mysqli_query($mysqli, $gamesPlayedQuery) or die(mysqli_error());

    while ($gameData = mysqli_fetch_assoc($gamesPlayedSql)) {
        $gameRow['total_plays'] = $gameData['play_count'];

        // Fetch details of the first bid for the game
        $bidDetailsQuery = "SELECT b.*, i.o_image, i.o_name FROM tbl_bid b
                            JOIN tbl_offers o ON b.o_id = o.o_id
                            left join tbl_items i on i.item_id = o.item_id
                            WHERE b.o_id = " . $gameData['o_id'] . " AND b.u_id ='$user_id' 
                            LIMIT 1";
        $bidDetailsSql = mysqli_query($mysqli, $bidDetailsQuery) or die(mysqli_error());

        if ($bidData = mysqli_fetch_assoc($bidDetailsSql)) {
            $gameRow['played_on'] = $bidData['bd_date'];
            $gameRow['item_image'] = $file_path . 'images/' .$bidData['o_image'];
            $gameRow['item_name'] = $bidData['o_name'];

            array_push($jsonObj4, $gameRow);
        }
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

else if (isset($_GET['get_penny_bid'])) {
    $jsonObj4 = array();
    
    $o_id = sanitize($_POST['o_id']);

    // Fetch user data
    $query = "SELECT tbl_bid.*, tbl_users.name, tbl_users.image FROM tbl_bid 
              INNER JOIN tbl_users ON tbl_bid.u_id = tbl_users.id 
              WHERE tbl_bid.o_id='$o_id' ORDER BY tbl_bid.bd_id DESC";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

    while ($data = mysqli_fetch_assoc($sql)) {
        $row['user_id'] = $data['u_id'];
        $row['user_name'] = $data['name'];
        $row['user_image'] = $file_path . 'images/' . $data['image'];
        $row['value'] = $data['bd_value'];
        $row['played_on'] = $data['bd_date'];

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}


else if (isset($_GET['get_seller_items'])) {
    	$jsonObj4= array();	
		
		$seller_id = sanitize($_POST['id']);

		$query13= "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id left join tbl_cat on tbl_cat.c_id= tbl_offers.c_id WHERE o_type NOT IN (6) AND ( o_date <= '".$date."' and o_edate >= '".$date."' ) and id='$seller_id' and o_status = 1 and c_status='1' ORDER BY `tbl_offers`.`o_id` ASC";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;
		//    exit();
			if( $start <= $datetime  && $end >= $datetime )
			 {
				$query111="SELECT *, COUNT(*)
				as num2 FROM tbl_bid 
				where tbl_bid.o_id='".$data['o_id']."' ";
			   
						   $result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
			   
						  $row111=mysqli_fetch_assoc($result111);
				   
			   
						   if($row111['num2']== null)
						   {
									$row['num2'] = "0";
								 
						   }else
						   {	
						             $row['current'] = 	$datetime;
									 $row['date'] = 	$date;
									 $row['time'] = 	$time;
									 $row['total_bids'] = 	$row111['num2'];
								
						   }             
								
			$row['id'] = $data['id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			$row['c_desc'] = $data['c_desc'];
			$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] =  $file_path.'images/'.$data['o_image'];
			$row['o_image1'] =  $file_path.'images/'.$data['o_image1'];
			$row['o_image2'] =  $file_path.'images/'.$data['o_image2'];
			$row['o_image3'] =  $file_path.'images/'.$data['o_image3'];
			$row['o_image4'] =  $file_path.'images/'.$data['o_image4'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_color'] = $data['o_color'];
			$row['o_link'] = $data['o_link'];
			$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_type'] = $data['o_type'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			$row['bid_increment'] = $data['bid_increment'];	
			$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $data['o_status'];
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 // Top Deals Api
 else if (isset($_GET['get_featured_item'])) {
    		 
        $jsonObj4= array();
        
        $city_id = sanitize($_POST['city_id']);
		
		

		$query13= "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id left join tbl_cat on tbl_cat.c_id= tbl_offers.c_id WHERE o_type IN (1,2,4,5,7,8) AND ( o_date <= '".$date."' and o_edate >= '".$date."' ) and city_id='$city_id' and o_status = 1 and c_status='1' ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC LIMIT 10";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;
		//    exit();
			if( $start <= $datetime  && $end >= $datetime )
			 {
				$query111="SELECT *, COUNT(*)
				as num2 FROM tbl_bid 
				where tbl_bid.o_id='".$data['o_id']."' ";
			   
						   $result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
			   
						  $row111=mysqli_fetch_assoc($result111);
				   
			   
						   if($row111['num2']== null)
						   {
									$row['num2'] = "0";
								 
						   }else
						   {	
						             $row['current'] = 	$datetime;
									 $row['date'] = 	$date;
									 $row['time'] = 	$time;
									 $row['total_bids'] = 	$row111['num2'];
								
						   }             
								
			$row['id'] = $data['id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			$row['c_desc'] = $data['c_desc'];
			$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] =  $file_path.'images/'.$data['o_image'];
			$row['o_image1'] =  $file_path.'images/'.$data['o_image1'];
			$row['o_image2'] =  $file_path.'images/'.$data['o_image2'];
			$row['o_image3'] =  $file_path.'images/'.$data['o_image3'];
			$row['o_image4'] =  $file_path.'images/'.$data['o_image4'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_link'] = $data['o_link'];
			$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_type'] = $data['o_type'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			$row['bid_increment'] = $data['bid_increment'];	
			$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $data['o_status'];
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

 

//get all offers
else if(isset($_GET['get_max_value'])) {
    $jsonObj4 = array();
    
    $o_id = sanitize($_POST['o_id']);

    $query = "SELECT bd_value, COUNT(*) as num1
              FROM tbl_bid
              WHERE tbl_bid.o_id = '$o_id'
              GROUP BY bd_value
              HAVING num1 = 1  
              ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC 
              LIMIT 1";
    $sql = mysqli_query($mysqli, $query);

    $num_rows = mysqli_num_rows($sql);
    if ($num_rows > 0) {
        $data = mysqli_fetch_assoc($sql);
        $row['bd_value'] = $data['bd_value'];

        // Update tbl_offers with the max bd_value
        $query1 = "UPDATE tbl_offers SET o_min = '" . $data['bd_value'] . "'
                   WHERE tbl_offers.o_id = '$o_id'";
        $sql1 = mysqli_query($mysqli, $query1);

        array_push($jsonObj4, $row);
    } else {
        $row['bd_value'] = "0.0";
        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

else if(isset($_GET['get_coin_list']))
{
    
    $jsonObj4 = array();

    $query = "SELECT * FROM `tbl_coin_list` WHERE c_status = 1 AND c_id !=1 ORDER BY `tbl_coin_list`.`c_id` ASC";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error($mysqli));
    
    $settingsQuery = "SELECT coinvalue FROM tbl_settings";
    $settingsResult = mysqli_query($mysqli, $settingsQuery);
    $settingsRow = mysqli_fetch_assoc($settingsResult);
    $coin_full_price = $settingsRow['coinvalue'];

    while ($data = mysqli_fetch_assoc($sql))
    {
        $coin_mrp = (string)($coin_full_price * $data['c_coin']);
        
        if ($coin_mrp == 0) {
            $coin_discount = "0"; // handles 0 value
        } else {
            $coin_discount = (string)round((($coin_mrp - $data['c_amount']) / $coin_mrp) * 100);
        }


        $row['c_id'] = $data['c_id'];
        $row['c_name'] = $data['c_name'];
        $row['c_image'] = $file_path . 'images/' . $data['c_image'];
        $row['c_coin'] = $data['c_coin'];
        $row['c_amount'] = $data['c_amount'];
        $row['c_full_price'] = $coin_mrp;
        $row['c_discount'] = $coin_discount;
        $row['c_status'] = $data['c_status'];

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

 
else if (isset($_GET['get_bot_name'])) {
    $jsonObj4 = array();

    $query = "SELECT * FROM `tbl_bot` WHERE bot_status = 1 ORDER BY RAND() LIMIT 1";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

    while ($data = mysqli_fetch_assoc($sql)) {
        $row['bot_name'] = $data['bot_name'];
        $row['bot_image'] = $file_path . 'images/' . $data['bot_image'];

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

else if (isset($_GET['get_wishlist'])) {
    $jsonObj4 = array();
    
    $user_id = sanitize($_POST['user_id']);

    $query = "SELECT * FROM `tbl_wishlist`
              left join tbl_items on tbl_items.item_id = tbl_wishlist.item_id
              WHERE user_id = $user_id ORDER BY wishlist_id DESC";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

    while ($data = mysqli_fetch_assoc($sql)) {
        
        $item_id = $data['item_id'];
        
        $response4 = array();  
        
        $rate5="SELECT * 
                    FROM tbl_offers
                    WHERE tbl_offers.item_id = '$item_id'";

		   $rateresult5 = mysqli_query($mysqli,$rate5)or die(mysqli_error());
	   
    	   while($data4 = mysqli_fetch_assoc($rateresult5))
    		{
    		    
    		    $row4['o_id'] = $data4['o_id'];
			    $row4['o_type'] = $data4['o_type'];
                                    
    		              array_push($response4, $row4);   
    		}
    		
        $row['item_id'] = $data['item_id'];
        $row['o_name'] = $data['o_name'];
        $row['o_image'] = $file_path . 'images/thumbs/' . $data['o_image'];
        $row['available_items'] = $response4;

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

//get all category
else if(isset($_GET['get_category']))
 	{
  		 $jsonObj4= array();	

		$query13="SELECT * FROM tbl_cat WHERE c_status='1' ";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['c_id'] = $data['c_id'];
			$row['c_name'] = $data['c_name'];
			$row['c_desc'] = $data['c_desc'];
			$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			$row['c_view'] = $data['c_view'];
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 //get active payment gateway
else if(isset($_GET['get_payment_gateway']))
 	{
  		 $jsonObj4= array();	

		$query13="SELECT * FROM tbl_payment_gateway WHERE pg_type !=2 AND pg_status='1' ";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['pg_id'] = $data['pg_id'];
			$row['pg_name'] = $data['pg_name'];
			$row['pg_image'] =  $file_path.'images/'.$data['pg_image'];
			$row['pg_type'] = $data['pg_type'];
			
			if ($row['pg_type'] == 4)
			{
			    $row['pg_link'] = $home_path.'payment.php?id='.$data['pg_id'];
			}
			else
			{
			    $row['pg_link'] = $data['pg_link'];
			}
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
//get category item
else if(isset($_GET['get_category_item']))
{
    // Initialize result array
    $jsonObj4 = array();
    
    $postArea = sanitize($_POST['area']);
    $postCity_id = sanitize($_POST['city_id']);
    $postC_id = sanitize($_POST['c_id']);
    
    // New array to store tbl_cat.c_view values
    $catViewArray = array();
    
    //FORCED  C_VIEW TO 2 BECAUSE OF AUCTION
    $queryCatView = "SELECT 2 AS c_view FROM tbl_cat";
    $resultCatView = mysqli_query($mysqli, $queryCatView) or die(mysqli_error());

    while ($catViewData = mysqli_fetch_assoc($resultCatView)) {
        $catViewArray[] = $catViewData['c_view'];
    }

    // Combine c_view values into a string separated by '-'
    $combinedCatView = implode('-', $catViewArray);

    // Add combinedCatView to jsonObj4 as a new array
    $jsonObj4[] = array("c_view" => $combinedCatView);

    $area = isset($postArea) ? $postArea : '';
    $city_id = isset($postCity_id) ? $postCity_id : '';
    $c_id = isset($postC_id) ? $postC_id : '';

    // Adjusting the query based on the value of "area"
    switch ($area) {
        case 'live':
            // Query for live
            $query13 = "SELECT * FROM tbl_offers 
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id 
                WHERE (o_type IN (1,2,7,8,10,11) OR (o_type = 6 AND o_price = 1)) 
                AND (o_edate > '$date') OR (o_edate = '$date' AND o_etime > '$time')
                AND tbl_offers.city_id = '$city_id' 
                AND tbl_offers.c_id = '$c_id'  
                AND o_status = 1 
                AND c_status = '1' 
                ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
            break;
        case 'upcoming':
            // Query for upcoming
            $query13 = "SELECT * FROM tbl_offers 
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id 
                WHERE (o_type IN (5,4) OR (o_type = 6 AND o_price = 1)) 
                AND (o_edate > '$date') OR (o_edate = '$date' AND o_etime > '$time')
                AND tbl_offers.city_id = '$city_id' 
                AND tbl_offers.c_id = '$c_id' 
                AND o_status = 1 
                AND c_status = '1' 
                ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
            break;
        case 'shop':
            // Query for shop
            $query13 = "SELECT * FROM tbl_offers 
                left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id 
                WHERE (o_type IN (3,9) OR (o_type = 6 AND o_price = 2)) 
                AND (o_edate > '$date') OR (o_edate = '$date' AND o_etime > '$time')
                AND tbl_offers.city_id = '$city_id' 
                AND tbl_offers.c_id = '$c_id' 
                AND o_status = 1 
                AND c_status = '1' 
                ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
            break;
        default:
            // Default query
            $query13 = "SELECT * FROM tbl_offers 
                        left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                        LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id 
                        WHERE (o_edate > '$date' OR (o_edate = '$date' AND o_etime > '$time'))
                        AND tbl_offers.city_id = '$city_id'
                        AND tbl_offers.c_id = '$c_id'
                        AND o_status = 1 
                        AND c_status = '1' 
                        ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
            break;
    }
    
    $result13 = mysqli_query($mysqli, $query13) or die(mysqli_error());

    while ($data = mysqli_fetch_assoc($result13)) {
        $start = $data['o_date'] . " " . $data['o_stime'];
        $end = $data['o_edate'] . " " . $data['o_etime'];
        $o_status = 0; // Default status

        // Check if the offer is currently active
        if ($start <= $datetime && $end >= $datetime) {
            $o_status = 1;
        }
   
        // Fetch bid information
        $queryBid = "SELECT COUNT(*) as num2
                           FROM (
                               SELECT o_id FROM tbl_ticket WHERE o_id = '".$data['o_id']."'
                               UNION ALL
                               SELECT o_id FROM tbl_bid WHERE o_id = '".$data['o_id']."'
                           ) as combined_entries";
        $resultBid = mysqli_query($mysqli, $queryBid) or die(mysqli_error());
        $rowBid = mysqli_fetch_assoc($resultBid);
        $totalBids = isset($rowBid['num2']) ? $rowBid['num2'] : 0;

        // Construct offer details
        $offerDetails = array(
            'id' => $data['id'],
            'c_id' => $data['c_id'],
            'city_id' => $data['city_id'],
            'c_name' => $data['c_name'],
            'c_desc' => $data['c_desc'],
            'c_color' => $data['c_color'],
            'c_image' => $file_path . 'images/' . $data['c_image'],
            'c_view' => $data['c_view'],
            'o_id' => $data['o_id'],
            'o_name' => $data['o_name'],
            'o_image' => $file_path . 'images/' . $data['o_image'],
            'o_image1' => $file_path . 'images/' . $data['o_image1'],
            'o_image2' => $file_path . 'images/' . $data['o_image2'],
            'o_image3' => $file_path . 'images/' . $data['o_image3'],
            'o_image4' => $file_path . 'images/' . $data['o_image4'],
            'o_desc' => $data['o_desc'],
            'o_link' => $data['o_link'],
            'o_date' => $data['o_date'],
            'o_edate' => $data['o_edate'],
            'o_stime' => $data['o_stime'],
            'o_etime' => $data['o_etime'],
            'o_amount' => $data['o_amount'],
            'o_type' => $data['o_type'],
            'o_winners' => $data['o_winners'],
            'o_min' => $data['o_min'],
            'o_max' => $data['o_max'],
            'o_qty' => $data['o_qty'],
            'o_color' => $data['o_color'],
            'bid_increment' => $data['bid_increment'],
            'time_increment' => $data['time_increment'],
            'o_price' => $data['o_price'],
            'o_buy' => $data['o_buy'],
            'o_status' => $o_status,
            'c_status' => $data['c_status'],
            'total_bids' => $totalBids
        );


        $jsonObj4[] = $offerDetails;
        
    }

    // Prepare response data
    $response['JSON_DATA'] = $jsonObj4;

    // Output JSON response
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($response, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    die();
}

//get items
else if(isset($_GET['get_items']))
  	{
  		$jsonObj4= array();	
  		
  		$region_id = sanitize($_POST['city_id']);
  		$user_id = sanitize($_POST['user_id']);
  		//$region_id = 1;
  		//$user_id = 1;

		          // WHERE (o_type IN (1,2,7,8,10,11) OR (o_type = 6 AND o_price = 1)) AND ( o_date <= '".$date."' and o_edate >= '".$date."' )
		$query13= "SELECT * FROM tbl_offers
		           left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                   LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id
		           WHERE (o_type IN (1,2,4,5,7,8,10,11) OR (o_type = 6 AND o_price = 1))
		           AND tbl_offers.city_id = '$region_id' 
		           and o_status = 1 and c_status='1' ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;
		    

			//if( $start <= $datetime  && $end >= $datetime )
		   if( $start >= $datetime )
			 {
			     $o_status = 0;
			 }
			 else if( $end >= $datetime )
			 {
			   $o_status = 1;
			 }
			
			if( $end >= $datetime )
			 {
			   
			     
				$query111="SELECT COUNT(*) as num2
                           FROM (
                               SELECT o_id FROM tbl_ticket WHERE o_id = '".$data['o_id']."'
                               UNION ALL
                               SELECT o_id FROM tbl_bid WHERE o_id = '".$data['o_id']."'
                           ) as combined_entries";

				$result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
				$row111=mysqli_fetch_assoc($result111);
				   
			   
				if($row111['num2']== null)
				{
				    $row['num2'] = "0";
								 
				}else
				{	
				    $row['total_bids'] = 	$row111['num2'];
				} 
				
				$query1111="SELECT COUNT(tbl_wishlist.wishlist_id) as wishlist_status FROM tbl_wishlist 
                            where tbl_wishlist.item_id='".$data['item_id']."' AND tbl_wishlist.user_id='$user_id' ";

		        $result1111 = mysqli_query($mysqli,$query1111)or die(mysqli_error());
		        $row1111=mysqli_fetch_assoc($result1111);
    

			    if($row1111['wishlist_status']== null)
			    {
		                 $row['wishlist_status'] = "0";
		              
			    }else
			    {	
			      	    $row['wishlist_status'] = 	$row1111['wishlist_status'];
			         
			    } 
								
			$row['id'] = $data['id'];
			$row['item_id'] = $data['item_id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			//$row['c_desc'] = $data['c_desc'];
			//$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			//$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] =  $file_path.'images/thumbs/'.$data['o_image'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_link'] = $data['o_link'];
			$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_type'] = $data['o_type'];
			$row['o_winners'] = $data['o_winners'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			//$row['bid_increment'] = $data['bid_increment'];	
			//$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			//$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $o_status;
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
//get product
else if(isset($_GET['get_product']))
  	{
  		 $jsonObj4= array();	
  		 
  		 $o_id = sanitize($_POST['o_id']);

		 $query13= "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id WHERE tbl_offers.o_id = '$o_id'";
		 $sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		 
			$query111="SELECT *, COUNT(*)
				as num2 FROM tbl_bid 
				where tbl_bid.o_id='".$data['o_id']."' ";
			   
						   $result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
			   
						  $row111=mysqli_fetch_assoc($result111);
				   
			   
						   if($row111['num2']== null)
						   {
									$row['num2'] = "0";
								 
						   }else
						   {	
						             $row['current'] = 	$datetime;
									 $row['date'] = 	$date;
									 $row['time'] = 	$time;
									 $row['total_bids'] = 	$row111['num2'];
								
						   }             
								
			$row['id'] = $data['id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			$row['c_desc'] = $data['c_desc'];
			$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] =  $file_path.'images/'.$data['o_image'];
			$row['o_image1'] =  $file_path.'images/'.$data['o_image1'];
			$row['o_image2'] =  $file_path.'images/'.$data['o_image2'];
			$row['o_image3'] =  $file_path.'images/'.$data['o_image3'];
			$row['o_image4'] =  $file_path.'images/'.$data['o_image4'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_link'] = $data['o_link'];
			$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_color'] = $data['o_color'];
			$row['o_type'] = $data['o_type'];
			$row['o_winners'] = $data['o_winners'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			$row['bid_increment'] = $data['bid_increment'];	
			$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $data['o_status'];
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

 //get shop
else if(isset($_GET['get_redeem']))
  	{
  		$jsonObj4= array();	
  		
  		$region_id = sanitize($_POST['city_id']);
  		$user_id = sanitize($_POST['user_id']);
  		// $region_id = 1;
  		// $user_id = 1;

		$query13= "SELECT * FROM tbl_offers
		           left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                   LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id
		           WHERE (o_type = 3 OR (o_type = 6 AND o_price = 3)) AND ( o_date <= '".$date."' and o_edate >= '".$date."' )
		           AND tbl_offers.city_id = '$region_id' 
		           and o_status = 1 and c_status='1' ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;

			if( $start <= $datetime  && $end >= $datetime )
			 {
				$query111="SELECT *, COUNT(*)
				as num2 FROM tbl_bid 
				where tbl_bid.o_id='".$data['o_id']."' ";

				$result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
				$row111=mysqli_fetch_assoc($result111);
				   
			   
				if($row111['num2']== null)
				{
				    $row['num2'] = "0";
								 
				}else
				{	
				    $row['total_bids'] = 	$row111['num2'];
				} 
				
				$query1111="SELECT COUNT(tbl_wishlist.wishlist_id) as wishlist_status FROM tbl_wishlist 
                            where tbl_wishlist.item_id='".$data['item_id']."' AND tbl_wishlist.user_id='$user_id' ";

		        $result1111 = mysqli_query($mysqli,$query1111)or die(mysqli_error());
		        $row1111=mysqli_fetch_assoc($result1111);
    

			    if($row1111['wishlist_status']== null)
			    {
		                 $row['wishlist_status'] = "0";
		              
			    }else
			    {	
			      	    $row['wishlist_status'] = 	$row1111['wishlist_status'];
			         
			    } 
								
			$o_type = $data['o_type'];
			$row['id'] = $data['id'];
			$row['item_id'] = $data['item_id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			//$row['c_desc'] = $data['c_desc'];
			//$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			//$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			if ($o_type == 6)
			{
			    $row['o_image'] =  $file_path.'images/thumbs/'.$data['o_image'];
			}
			else
			{
			    $row['o_image'] =  $file_path.'images/'.$data['o_image'];
			}
			$row['o_image1'] =  $file_path.'images/'.$data['o_image1'];
			$row['o_image2'] =  $file_path.'images/'.$data['o_image2'];
			$row['o_image3'] =  $file_path.'images/'.$data['o_image3'];
			$row['o_image4'] =  $file_path.'images/'.$data['o_image4'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_link'] = $data['o_link'];
			//$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			//$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_type'] = $data['o_type'];
			$row['o_winners'] = $data['o_winners'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			//$row['bid_increment'] = $data['bid_increment'];	
			//$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			//$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $data['o_status'];
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 //get shop
else if(isset($_GET['get_shop']))
  	{
  		$jsonObj4= array();	
  		
  		$region_id = sanitize($_POST['city_id']);
  		$user_id = sanitize($_POST['user_id']);
  		//$region_id = 1;
  		//$user_id = 1;

		$query13= "SELECT * FROM tbl_offers
		           left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                   LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id
		           WHERE (o_type IN (9) OR (o_type = 6 AND o_price = 2)) AND ( o_date <= '".$date."' and o_edate >= '".$date."' )
		           AND tbl_offers.city_id = '$region_id' 
		           and o_status = 1 and c_status='1' ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];
		    $o_type =  $data['o_type'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;

			if( $start <= $datetime  && $end >= $datetime )
			 {
				$query111="SELECT *, COUNT(*)
				as num2 FROM tbl_bid 
				where tbl_bid.o_id='".$data['o_id']."' ";

				$result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
				$row111=mysqli_fetch_assoc($result111);
				   
			   
				if($row111['num2']== null)
				{
				    $row['num2'] = "0";
								 
				}else
				{	
				    $row['total_bids'] = 	$row111['num2'];
				} 
				
				$query1111="SELECT COUNT(tbl_wishlist.wishlist_id) as wishlist_status FROM tbl_wishlist 
                            where tbl_wishlist.item_id='".$data['item_id']."' AND tbl_wishlist.user_id='$user_id' ";

		        $result1111 = mysqli_query($mysqli,$query1111)or die(mysqli_error());
		        $row1111=mysqli_fetch_assoc($result1111);
    

			    if($row1111['wishlist_status']== null)
			    {
		                 $row['wishlist_status'] = "0";
		              
			    }else
			    {	
			      	    $row['wishlist_status'] = 	$row1111['wishlist_status'];
			         
			    } 
								
			$row['id'] = $data['id'];
			$row['item_id'] = $data['item_id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			//$row['c_desc'] = $data['c_desc'];
			//$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			//$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			if ($o_type == 6)
			{
			    $row['o_image'] =  $file_path.'images/thumbs/'.$data['o_image'];
			}
			else
			{
			    $row['o_image'] =  $file_path.'images/'.$data['o_image'];
			}
			$row['o_image1'] =  $file_path.'images/'.$data['o_image1'];
			$row['o_image2'] =  $file_path.'images/'.$data['o_image2'];
			$row['o_image3'] =  $file_path.'images/'.$data['o_image3'];
			$row['o_image4'] =  $file_path.'images/'.$data['o_image4'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_link'] = $data['o_link'];
			$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_type'] = $data['o_type'];
			$row['o_winners'] = $data['o_winners'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			//$row['bid_increment'] = $data['bid_increment'];	
			//$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			//$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $data['o_status'];
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
//get time
else if(isset($_GET['get_time']))
  	{
  		$jsonObj4= array();	
		
		$query13= "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id left join tbl_cat on tbl_cat.c_id= tbl_offers.c_id WHERE ( o_date <= '".$date."' and o_edate >= '".$date."' ) and o_status = 1 and c_status='1' ORDER BY `tbl_offers`.`o_id` ASC";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		if($data = mysqli_fetch_assoc($sql))
		{

			
				 $row['current'] = 	$datetime;          
		
			
			array_push($jsonObj4,$row); 
			

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }


//get vendor details
else if(isset($_GET['get_seller'])) {
    $jsonObj4 = array();

    // Get the base URL dynamically
    $seller_url_base = 'https://' . $_SERVER['HTTP_HOST'] . '/seller.php?id=';

    $query = "SELECT * FROM tbl_vendor ";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error());

    while ($data = mysqli_fetch_assoc($sql)) {
        $row['id'] = $data['id'];
        $row['name'] = $data['email'];
        $row['image'] =  $file_path.'images/'.$data['image'];
        $row['about'] = $data['about'];
        $row['ratting'] = $data['ratting'];
        $row['link'] = $seller_url_base . $data['id']; // Construct the link
        $row['join_date'] = $data['joining_date'];

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
 
else if (isset($_GET['get_prizes'])) {
    
    $jsonObj = array();
    $lottery_id = sanitize($_POST['o_id']);

    $query = "SELECT *,
              CASE
                  WHEN rank_start = rank_end THEN CAST(rank_start AS CHAR)
                  ELSE CONCAT(rank_start, '-', rank_end)
              END AS rank_display
              FROM tbl_prizes
              left join tbl_items on tbl_items.item_id = tbl_prizes.item_id 
              WHERE o_id = '$lottery_id'";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error($mysqli));

    while ($data = mysqli_fetch_assoc($sql)) {
        $row['rank'] = $data['rank_display'];
        $row['prize_image'] = $file_path.'images/thumbs/'.$data['o_image'];
        $row['prize_name'] = $data['o_name'];
        array_push($jsonObj, $row);
    }

    $set['JSON_DATA'] = $jsonObj;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

//get all offers
else if(isset($_GET['get_wallet_passbook']))
 	{
  		$jsonObj4= array();
  		 
  		$user_id = sanitize($_POST['user_id']);

		$query="SELECT * FROM tbl_wallet_passbook 
		Left join tbl_coin_list on tbl_coin_list.c_id= tbl_wallet_passbook.wp_package_id
		WHERE wp_user = '$user_id'";	 
		
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['wp_id'] = $data['wp_id'];
			$row['wp_user'] = $data['wp_user'];
			$row['wp_package_id'] = $data['wp_package_id'];
			$row['wp_coins'] = $data['wp_coins'];
			$row['wp_amount'] = $data['wp_amount'];
			
			$row['c_name'] = $data['c_name'];
			$row['c_coin'] = $data['c_coin'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			$row['c_amount'] = $data['c_amount'];
			
			$row['wp_order_id'] = $data['wp_order_id'];
			$row['wp_date'] = $data['wp_date'];
			$row['wp_status'] = $data['wp_status'];

			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
// Get lottery inner details
else if (isset($_GET['get_lottery_id'])) {
    $jsonObj4 = array();

    $lottery_id = sanitize($_POST['o_id']);  
    //$lottery_id = 1;  

    $query = "SELECT * FROM tbl_offers 
              LEFT JOIN tbl_lottery_balls ON tbl_lottery_balls.lottery_balls_id = tbl_offers.lottery_balls_id
              WHERE o_id = '$lottery_id'";

    $sql = mysqli_query($mysqli, $query) or die(mysqli_error($mysqli));

    if ($data = mysqli_fetch_assoc($sql)) {
        $row['normal_ball_start'] = $data['normal_ball_start'];
        $row['normal_ball_end'] = $data['normal_ball_end'];
        $row['normal_ball_limit'] = $data['normal_ball_limit'];
        
        $row['premium_ball_start'] = $data['premium_ball_start'];
        $row['premium_ball_end'] = $data['premium_ball_end'];
        $row['premium_ball_limit'] = $data['premium_ball_limit'];
        
        $row['ticket_price'] = $data['o_amount'];

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

//add bid
else if(isset($_GET['add_bid']))
{

          $offer_id = sanitize($_POST['o_id']);
          $entered_bid = sanitize($_POST['bd_value']);
          $user_id = sanitize($_POST['u_id']);
          
          //$offer_id = 83;
          //$entered_bid = 1.56;
          //$user_id = 1;

          $qry1 = "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_id = '$offer_id'"; 
          $result1 = mysqli_query($mysqli,$qry1);
          $num_rows1 = mysqli_num_rows($result1);
	      $row1 = mysqli_fetch_assoc($result1);

            $bidding_fees = $row1['o_amount'];
            $o_date = $row1['o_date'];
		    $o_stime = $row1['o_stime'];
		    $o_edate = $row1['o_edate'];
		    $o_etime =  $row1['o_etime'];
		    $o_type = $row1['o_type'];
		    
		    $game_name = $row1['o_name'];
            $current_winner_id = $row1['winner_id'];
            $current_winner_bid = $row1['winning_value'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;

		    // 1. Check for existing duplicate bids
                $check_duplicate_query = "SELECT bd_id FROM tbl_bid WHERE o_id = '$offer_id' AND bd_value = '$entered_bid'";
                $duplicate_result = mysqli_query($mysqli, $check_duplicate_query);
            
                if (mysqli_num_rows($duplicate_result) > 0) {
                  $bidStatus = 2; 
            
                  // Update existing duplicate bids' statuses
                  $update_duplicate_query = "UPDATE tbl_bid SET bid_status = 2 WHERE o_id = '$offer_id' AND bd_value = '$entered_bid'";
                  mysqli_query($mysqli, $update_duplicate_query);
                } else {
                  $bidStatus = 1;  // Bid is initially unique
                }

			if( $start <= $datetime  && $end >= $datetime )
			{
			    
			
			    $qry = "SELECT * FROM tbl_users WHERE id = '$user_id'"; 
	        	$result = mysqli_query($mysqli,$qry);
	        	$num_rows = mysqli_num_rows($result);
	        	$row = mysqli_fetch_assoc($result);
        		       
		         $id=$row['id'];
    		     $wallet=$row['wallet'];
    		     $update_amount= $bidding_fees;
        		
        		if ($num_rows > 0 and $wallet >= $update_amount and $entered_bid )
        		{
            		       
                		$newwallet=$wallet-$update_amount;   
            		    
            			 	$user_edit= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	
            			  $result1=mysqli_query($mysqli,$user_edit);  
            			  
                	        $qry1="INSERT INTO tbl_bid 
                					(`u_id`,
                					`o_id`,
                					`bd_value`,
                					`bd_amount`,
                					`bd_date`,
                					bd_status
                				 
                				) VALUES (
                					'$user_id',
                					'$offer_id',
                					'$entered_bid',
                					'$bidding_fees',
                					'$date',
                					1
                				)"; 
                            
                        $result1=mysqli_query($mysqli,$qry1); 
                            
    //check the bid_status again
    
    if ($bidStatus!=NULL) {
          
      // Update bids' statuses with 1 or 2
      $update_duplicate_query = "UPDATE tbl_bid SET bid_status = $bidStatus WHERE o_id = '$offer_id' AND bd_value = '$entered_bid'";
      mysqli_query($mysqli, $update_duplicate_query);
    }
    
    // lowest unique bid auction
      if ($o_type == 1) {
          $unique_lowest_query = "SELECT *, COUNT(*) as num1 FROM tbl_bid 
                                  where tbl_bid.o_id = '$offer_id' GROUP BY bd_value having num1 = 1 ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) ASC LIMIT 0,1";
          $result = mysqli_query($mysqli, $unique_lowest_query);

          if ($result && mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
             $bd_id = $row['bd_id'];
             $new_winner_id = $row['u_id'];
             $new_winner_bid = $row['bd_value'];
             
             // GET winner Name
             $winnerNameQuery = "SELECT * FROM tbl_users where tbl_users.id = '$new_winner_id' LIMIT 0,1";
             $winnerNameResult = mysqli_query($mysqli, $winnerNameQuery);
             $winnerNameRow = mysqli_fetch_assoc($winnerNameResult);
             $winnerName = $winnerNameRow['name'];
             
             // Update any previous bid_status = 3 to 1 (unique to unique but not lowest)
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 1 WHERE o_id = '$offer_id' AND bid_status = 3";
              mysqli_query($mysqli, $update_old_unique_query);
              
               if ($current_winner_id !== null && $current_winner_id != $new_winner_id) {
                 $userId = $current_winner_id;
                 $title = 'Bid Again';
                 $body = 'Someone outbidded you';
                 $action ='1';
                 sendEmailNotification($userId, $game_name, "not winning anymore as someone else outbidded you!", $mysqli);
             }
            
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 3 WHERE bd_id = $bd_id";
              mysqli_query($mysqli, $update_old_unique_query);
              
              $update_winner = "UPDATE tbl_offers 
                                SET winner_id = '$new_winner_id', 
                                    winning_value = '$new_winner_bid', 
                                    winner_name = '$winnerName' 
                                WHERE o_id = '$offer_id'";
              mysqli_query($mysqli, $update_winner);
              
              if ($current_winner_id !== null && $new_winner_id !== null && $current_winner_id != $new_winner_id) {
              $userId =  $new_winner_id;
              sendEmailNotification($userId, $game_name, "now unique and currently you are winning the auction!", $mysqli);
              }
            
          }

      }
      // highest unique bid auction
       elseif ($o_type == 2) { 
      
          $unique_highest_query = "SELECT *, COUNT(*) as num1 FROM tbl_bid 
                                   where tbl_bid.o_id = '$offer_id' GROUP BY bd_value having num1 = 1  ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC LIMIT 0,1";
          $result1 = mysqli_query($mysqli, $unique_highest_query);

          if ($result1 && mysqli_num_rows($result1) > 0) {
            $row1 = mysqli_fetch_assoc($result1);
             $bd_id1 = $row1['bd_id'];
             $new_winner_id1 = $row1['u_id'];
             $new_winner_bid1 = $row1['bd_value'];
             
             // GET winner Name
             $winnerNameQuery = "SELECT * FROM tbl_users where tbl_users.id = '$new_winner_id1' LIMIT 0,1";
             $winnerNameResult = mysqli_query($mysqli, $winnerNameQuery);
             $winnerNameRow = mysqli_fetch_assoc($winnerNameResult);
             $winnerName = $winnerNameRow['name'];
            
             // Update any previous bid_status = 3 to 1
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 1 WHERE o_id = '$offer_id' AND bid_status = 3";
              mysqli_query($mysqli, $update_old_unique_query);
              
               if ($current_winner_id !== null && $current_winner_id != $new_winner_id1) {
                 $userId = $current_winner_id;
                 sendEmailNotification($userId, $game_name, "not winning anymore as someone else outbidded you!", $mysqli);
             }
            
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 3 WHERE bd_id = $bd_id1";
              mysqli_query($mysqli, $update_old_unique_query);
              
              $update_winner = "UPDATE tbl_offers 
                                SET winner_id = '$new_winner_id1', 
                                    winning_value = '$new_winner_bid1', 
                                    winner_name = '$winnerName' 
                                WHERE o_id = '$offer_id'";
              mysqli_query($mysqli, $update_winner);
              
              if ($current_winner_id !== null && $new_winner_id1 !== null && $current_winner_id != $new_winner_id1) {
              $userId =  $new_winner_id1;
              sendEmailNotification($userId, $game_name, "now unique and currently you are winning the auction!", $mysqli);
              }
            
          }

      }
      // english auction 
      elseif ($o_type == 7) { 
      
      $english_auction_query = "SELECT * FROM tbl_bid 
                               where tbl_bid.o_id = '$offer_id' ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC LIMIT 0,1";
      $english_auction_result = mysqli_query($mysqli, $english_auction_query);

      if ($english_auction_result && mysqli_num_rows($english_auction_result) > 0) {
        $english_auction_row = mysqli_fetch_assoc($english_auction_result);
         $english_auction_winner_id = $english_auction_row['u_id'];
         $english_auction_winner_bid = $english_auction_row['bd_value'];
         
             // GET winner Name
             $winnerNameQuery = "SELECT * FROM tbl_users where tbl_users.id = '$english_auction_winner_id' LIMIT 0,1";
             $winnerNameResult = mysqli_query($mysqli, $winnerNameQuery);
             $winnerNameRow = mysqli_fetch_assoc($winnerNameResult);
             $winnerName = $winnerNameRow['name'];
          
         if ($current_winner_id !== null && $current_winner_id != $english_auction_winner_id) {
             $userId = $current_winner_id;
             sendEmailNotification($userId, $game_name, "not winning anymore as someone else outbidded you!", $mysqli);
         }
          
          $update_winner = "UPDATE tbl_offers 
                                SET winner_id = '$english_auction_winner_id', 
                                    winning_value = '$english_auction_winner_bid', 
                                    winner_name = '$winnerName' 
                                WHERE o_id = '$offer_id'";
          mysqli_query($mysqli, $update_winner);
          }
        
      }
      elseif ($o_type == 8) { 
          $o_min = $row1['o_min'];
          $bid_increment = $row1['bid_increment'];
          $time_increment = $row1['time_increment'];
            
          $new_penny_auction_bid = $o_min + $bid_increment;
          
           // GET winner Name
           $winnerNameQuery = "SELECT * FROM tbl_users where tbl_users.id = '$user_id' LIMIT 0,1";
           $winnerNameResult = mysqli_query($mysqli, $winnerNameQuery);
           $winnerNameRow = mysqli_fetch_assoc($winnerNameResult);
           $winnerName = $winnerNameRow['name'];
            
          // Calculate new end time
          $current_end_datetime = $row1['o_edate'] . ' ' . $row1['o_etime'];
          $new_end_datetime = date('Y-m-d H:i:s', strtotime($current_end_datetime . ' + ' . $time_increment . ' seconds'));
            
          // Split the new datetime into date and time components
          $new_end_date = date('Y-m-d', strtotime($new_end_datetime));
          $new_end_time = date('H:i:s', strtotime($new_end_datetime));
            
          $update_winner = "UPDATE tbl_offers 
                            SET winner_id = ?, 
                                winning_value = ?,
                                winner_name = ?,
                                o_min = ?, 
                                o_edate = ?, 
                                o_etime = ? 
                            WHERE o_id = ?";
            
          $stmt = $mysqli->prepare($update_winner);
          $stmt->bind_param('idddssi', $user_id, $entered_bid, $winnerName, $entered_bid, $new_end_date, $new_end_time, $offer_id);
          $stmt->execute();
          $stmt->close();
      }
        
      // reverse auction
      elseif ($o_type == 10) { 
      
      $reverse_auction_query = "SELECT * FROM tbl_bid 
                               where tbl_bid.o_id = '$offer_id' ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) ASC LIMIT 0,1";
      $reverse_auction_result = mysqli_query($mysqli, $reverse_auction_query);

      if ($reverse_auction_result && mysqli_num_rows($reverse_auction_result) > 0) {
        $reverse_auction_row = mysqli_fetch_assoc($reverse_auction_result);
         $reverse_auction_winner_id = $reverse_auction_row['u_id'];
         $reverse_auction_winner_bid = $reverse_auction_row['bd_value'];
         
         // GET winner Name
         $winnerNameQuery = "SELECT * FROM tbl_users where tbl_users.id = '$reverse_auction_winner_id' LIMIT 0,1";
         $winnerNameResult = mysqli_query($mysqli, $winnerNameQuery);
         $winnerNameRow = mysqli_fetch_assoc($winnerNameResult);
         $winnerName = $winnerNameRow['name'];
          
         if ($current_winner_id !== null && $current_winner_id != $reverse_auction_winner_id) {
             $userId = $current_winner_id;
             sendEmailNotification($userId, $game_name, "not winning anymore as someone else outbidded you!", $mysqli);
         }
         
         $update_winner = "UPDATE tbl_offers 
                                SET winner_id = '$reverse_auction_winner_id', 
                                    winning_value = '$reverse_auction_winner_bid',
                                    o_max = '$reverse_auction_winner_bid',
                                    winner_name = '$winnerName' 
                                WHERE o_id = '$offer_id'";
          mysqli_query($mysqli, $update_winner);

          }
        
      }
        
      $bidStatusQuery = "SELECT bid_status FROM tbl_bid where tbl_bid.bd_value = '$entered_bid' LIMIT 0,1"; 
	    $bidStatusResult = mysqli_query($mysqli,$bidStatusQuery);
	    $bidStatusRow = mysqli_fetch_assoc($bidStatusResult);
	    
	    $newBidStatus = $bidStatusRow['bid_status'];

		$qry11="INSERT INTO tbl_transaction (`user_id`,`type`,`type_no`,`date`,`money`) 
				VALUES ('$user_id',1,'$offer_id','$date','$bidding_fees')"; 
	            
	            $result11=mysqli_query($mysqli,$qry11);  		
 		
		                
		    			$set['JSON_DATA'][]=array('msg' => "Your Bid has been Submitted",'success'=>'1','bid_status' => $newBidStatus);
                		        
    		     }
    		     else
    		     {
    		         	$set['JSON_DATA'][]=array('msg' => "some thing went wrong!",'success'=>'0');
    		     }
			} else
    		     {
    		         	$set['JSON_DATA'][]=array('msg' => "Auction already ended!",'success'=>'0');
    		     }
		      

		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die(); 
	}
	
//add ticket
else if(isset($_GET['add_ticket']))
{

    // Sanitize POST inputs
    $lottery_id = sanitize($_POST['o_id']);
    $ball1 = empty($_POST['ball_1']) ? "NULL" : sanitize($_POST['ball_1']);
    $ball2 = empty($_POST['ball_2']) ? "NULL" : sanitize($_POST['ball_2']);
    $ball3 = empty($_POST['ball_3']) ? "NULL" : sanitize($_POST['ball_3']);
    $ball4 = empty($_POST['ball_4']) ? "NULL" : sanitize($_POST['ball_4']);
    $ball5 = empty($_POST['ball_5']) ? "NULL" : sanitize($_POST['ball_5']);
    $ball6 = empty($_POST['ball_6']) ? "NULL" : sanitize($_POST['ball_6']);
    $ball7 = empty($_POST['ball_7']) ? "NULL" : sanitize($_POST['ball_7']);
    $ball8 = empty($_POST['ball_8']) ? "NULL" : sanitize($_POST['ball_8']);
    $user_id = sanitize($_POST['user_id']);

    // Fetch lottery offer details
    $qry1 = "SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE o_id = '$lottery_id'";
    $result1 = mysqli_query($mysqli, $qry1);
    $row1 = mysqli_fetch_assoc($result1);

    $ticket_price = $row1['o_amount'];
    $o_date = $row1['o_date'];
    $o_stime = $row1['o_stime'];
    $o_edate = $row1['o_edate'];
    $o_etime = $row1['o_etime'];
    $game_name = $row1['o_name'];

    $start = $o_date . " " . $o_stime;
    $end = $o_edate . " " . $o_etime;
    $datetime = date('Y-m-d H:i:s'); // Current datetime

    if ($start <= $datetime && $end >= $datetime) {

        // Fetch user details
        $qry = "SELECT * FROM tbl_users WHERE id = '$user_id'";
        $result = mysqli_query($mysqli, $qry);
        $row = mysqli_fetch_assoc($result);

        $id = $row['id'];
        $wallet = $row['wallet'];
        $update_amount = $ticket_price;

        if ($wallet >= $update_amount) {

            // Deduct ticket price from user's wallet
            $newwallet = $wallet - $update_amount;
            $user_edit = "UPDATE tbl_users SET wallet='$newwallet' WHERE id='$id'";
            mysqli_query($mysqli, $user_edit);

            // Insert ticket details into tbl_ticket
            $qry1 = "INSERT INTO tbl_ticket 
                        (`u_id`, `o_id`, `ball_1`, `ball_2`, `ball_3`, `ball_4`, `ball_5`, `ball_6`, `ball_7`, `ball_8`, `ticket_price`, `purchase_date`, `ticket_status`) 
                     VALUES 
                        ('$user_id', '$lottery_id', '$ball1', '$ball2', '$ball3', '$ball4', '$ball5', '$ball6', '$ball7', '$ball8', '$ticket_price', '$datetime', 1)";
            mysqli_query($mysqli, $qry1);
            
            // Get the last inserted ticket ID
            $ticket_id = mysqli_insert_id($mysqli);

            // Generate unique_ticket_id
            $current_date = date('Ymd'); // Format the date as YYYYMMDD
            $unique_ticket_id = $ticket_id . "_" . $current_date . "_" . $lottery_id;

            // Update the unique_ticket_id in tbl_ticket
            $update_unique_id_query = "UPDATE tbl_ticket SET unique_ticket_id='$unique_ticket_id' WHERE ticket_id='$ticket_id'";
            mysqli_query($mysqli, $update_unique_id_query);

            // Insert transaction details into tbl_transaction
            $ticket_id = mysqli_insert_id($mysqli); // Get the last inserted ticket ID
            $qry11 = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`, `date`, `money`) 
                      VALUES ('$user_id', 1, '$ticket_id', '$datetime', '$ticket_price')";
            mysqli_query($mysqli, $qry11);

            $set['JSON_DATA'][] = array('msg' => "Ticket Purchased", 'success' => '1', 'ticket_id' => $unique_ticket_id);

        } else {
            $set['JSON_DATA'][] = array('msg' => "Insufficient wallet balance", 'success' => '0');
        }

    } else {
        $set['JSON_DATA'][] = array('msg' => "Lottery already ended", 'success' => '0');
    }

    header('Content-Type: application/json; charset=utf-8');
    echo str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
	
else if(isset($_GET['add_bid_multi']))
{
    
    if($_POST['add_bid_multi'] != "")
    {
        $someJSON = $_POST['add_bid_multi'];
        $someArray1 = json_decode($someJSON, true);

        for ($x = 0; $x <= count($someArray1)-1 ; $x++) 
        {
            $qry = "SELECT * FROM tbl_users WHERE id = '".$someArray1[$x]["u_id"]."'"; 
            $result = mysqli_query($mysqli,$qry);
            $num_rows = mysqli_num_rows($result);
            $row = mysqli_fetch_assoc($result);

            $qry1 = "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_id = '".trim($someArray1[$x]["o_id"])."'"; 
            $result1 = mysqli_query($mysqli,$qry1);
            $num_rows1 = mysqli_num_rows($result1);
            $row1 = mysqli_fetch_assoc($result1);
            $o_type = $row1['o_type'];
            $game_name = $row1['o_name'];
            $current_winner_id = $row1['winner_id'];
            $current_winner_bid = $row1['winner_value'];

            $id = $row['id'];
            $wallet = $row['wallet'];
            $update_amount = $someArray1[$x]["bd_amount"];

            if ($num_rows > 0 && $wallet >= $update_amount )
            {
                $newwallet = $wallet - $update_amount;   
                $user_edit = "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'"; 
                $result1 = mysqli_query($mysqli,$user_edit);  

                // Check for duplicate bids and update bid_status accordingly
                $check_duplicate_query = "SELECT bd_id FROM tbl_bid WHERE o_id = '".trim($someArray1[$x]["o_id"])."' AND bd_value = '".$someArray1[$x]["bd_value"]."'";
                $duplicate_result = mysqli_query($mysqli, $check_duplicate_query);

                if (mysqli_num_rows($duplicate_result) > 0) {
                    $bidStatus = 2; // Duplicate bid
                    $update_duplicate_query = "UPDATE tbl_bid SET bid_status = 2 WHERE o_id = '".trim($someArray1[$x]["o_id"])."' AND bd_value = '".$someArray1[$x]["bd_value"]."'";
                    mysqli_query($mysqli, $update_duplicate_query);
                } else {
                    $bidStatus = 1; // Unique bid
                }
                
                $qry1 = "INSERT INTO tbl_bid 
                        (`u_id`,
                        `o_id`,
                        `bd_value`,
                        `bd_amount`,
                        `bd_date`,
                        bd_status
                        ) VALUES (
                            '".trim($someArray1[$x]["u_id"])."',
                            '".trim($someArray1[$x]["o_id"])."',
                            '".$someArray1[$x]["bd_value"]."',
                            '".$someArray1[$x]["bd_amount"]."',
                            '$date',
                            1
                        )"; 
                $result1 = mysqli_query($mysqli,$qry1);   

                // Update bid_status
                if ($bidStatus != NULL) {
                    $update_bid_status_query = "UPDATE tbl_bid SET bid_status = $bidStatus WHERE o_id = '".trim($someArray1[$x]["o_id"])."' AND bd_value = '".$someArray1[$x]["bd_value"]."'";
                    mysqli_query($mysqli, $update_bid_status_query);
                }
                
        if ($o_type == 1) { // Lowest unique bid
          $unique_lowest_query = "SELECT *, COUNT(*) as num1 FROM tbl_bid 
                                  where tbl_bid.o_id = '".trim($someArray1[$x]["o_id"])."' GROUP BY bd_value having num1 = 1  ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) ASC LIMIT 0,1";
          $result = mysqli_query($mysqli, $unique_lowest_query);

          if ($result && mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
             $bd_id = $row['bd_id'];
             $new_winner_id = $row['u_id'];
             $new_winner_bid = $row['bid_value'];
             
             // Update any previous bid_status = 3 to 1
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 1 WHERE o_id = '".trim($someArray1[$x]["o_id"])."' AND bid_status = 3";
              mysqli_query($mysqli, $update_old_unique_query);
              
               if ($current_winner_id !== null && $current_winner_id != $new_winner_id) {
                 $userId = $current_winner_id;
                 sendEmailNotification($userId, $game_name, "not winning anymore as someone else outbidded you!", $mysqli);
             }
            
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 3 WHERE bd_id = $bd_id";
              mysqli_query($mysqli, $update_old_unique_query);
              
              $update_winner = "UPDATE tbl_offers SET winner_id = $new_winner_id WHERE o_id = '".trim($someArray1[$x]["o_id"])."'";
              mysqli_query($mysqli, $update_winner);
              
              if ($current_winner_id !== null && $new_winner_id !== null && $current_winner_id != $new_winner_id) {
              $userId =  $new_winner_id;
              sendEmailNotification($userId, $game_name, "now unique and currently you are winning the auction!", $mysqli);
              }
            
          }

      } elseif ($o_type == 2) { 
      
          $unique_highest_query = "SELECT *, COUNT(*) as num1 FROM tbl_bid 
                                   where tbl_bid.o_id = '".trim($someArray1[$x]["o_id"])."' GROUP BY bd_value having num1 = 1  ORDER BY CAST(tbl_bid.bd_value AS DECIMAL(18,2)) DESC LIMIT 0,1";
          $result1 = mysqli_query($mysqli, $unique_highest_query);

          if ($result1 && mysqli_num_rows($result1) > 0) {
            $row1 = mysqli_fetch_assoc($result1);
             $bd_id1 = $row1['bd_id'];
             $new_winner_id1 = $row1['u_id'];
             $new_winner_bid1 = $row1['bid_value'];
            
             // Update any previous bid_status = 3 to 1
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 1 WHERE o_id = '".trim($someArray1[$x]["o_id"])."' AND bid_status = 3";
              mysqli_query($mysqli, $update_old_unique_query);
              
               if ($current_winner_id !== null && $current_winner_id != $new_winner_id1) {
                 $userId = $current_winner_id;
                 sendEmailNotification($userId, $game_name, "not winning anymore as someone else outbidded you!", $mysqli);
             }
            
              $update_old_unique_query = "UPDATE tbl_bid SET bid_status = 3 WHERE bd_id = $bd_id1";
              mysqli_query($mysqli, $update_old_unique_query);
              
              $update_winner = "UPDATE tbl_offers SET winner_id = $new_winner_id1 WHERE o_id = '".trim($someArray1[$x]["o_id"])."'";
              mysqli_query($mysqli, $update_winner);
              
              if ($current_winner_id !== null && $new_winner_id1 !== null && $current_winner_id != $new_winner_id1) {
              $userId =  $new_winner_id1;
              sendEmailNotification($userId, $game_name, "now unique and currently you are winning the auction!", $mysqli);
              }
            
          }

      }

                $qry11 = "INSERT INTO tbl_transaction (`user_id`,`type`,`type_no`,`date`,`money`) 
                        VALUES ('".$someArray1[$x]["u_id"]."',1,'".$someArray1[$x]["o_id"]."','$date','".$someArray1[$x]["bd_amount"]."')"; 
                $result11 = mysqli_query($mysqli,$qry11);  

                $set['JSON_DATA'][]=array('msg' => "Your Bid has been Submitted",'success'=>'1');
            } else {
                $set['JSON_DATA'][]=array('msg' => "Something went wrong for bid with user ID ".$someArray1[$x]["u_id"].". Insufficient funds or user not found.",'success'=>'0');
            }		        
        }
    }
    else
    {
        $set['JSON_DATA'][]=array('msg' => "Something went wrong 1...!",'success'=>'0');
    }

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die(); 
}

else if(isset($_GET['get_bid']))
{
 		 $jsonObj4= array();
 		 
 		 $offer_id = $_POST['o_id'];

		$qry = "SELECT *,MIN(bd_value) as mins  FROM tbl_bid WHERE o_id = '$offer_id'"; 
		$result = mysqli_query($mysqli,$qry);
		// $row = mysqli_fetch_assoc($result);
		while($data = mysqli_fetch_assoc($result))
		{
			echo $data['mins'];
				$qry1 = "SELECT * FROM tbl_bid WHERE o_id = '$offer_id'"; 
		$result1 = mysqli_query($mysqli,$qry1);
		// $row = mysqli_fetch_assoc($result);
		while($data1 = mysqli_fetch_assoc($result1))
		{
			$input = array($data1['u_id']);

			$result = array_unique($input);
			var_dump($result);
		
			
		}
			array_push($jsonObj4,$row); 
		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

//get all offers
else if(isset($_GET['get_ticket_all']))
 	{
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_bid 
		left join tbl_offers on tbl_offers.o_id = tbl_bid.o_id
		left join tbl_items on tbl_items.item_id = tbl_offers.item_id
		WHERE tbl_offers.o_type =4 OR tbl_offers.o_type =5 ORDER BY `tbl_bid`.`bd_id` DESC";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['bd_id'] = $data['bd_id'];
			$row['u_id'] = $data['u_id'];
			$row['name'] = $data['name'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] = $file_path.'images/'.$data['o_image'];
			$row['o_type'] = $data['o_type'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_price'] = $data['o_price'];
			$row['o_buy'] = $data['o_buy'];
			$row['bd_value'] = $data['bd_value'];
			$row['bd_amount'] = $data['bd_amount'];
			$row['bd_date'] = $data['bd_date'];
			$row['bd_status'] = $data['bd_status'];
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 //get ticket
else if(isset($_GET['get_ticket']))
 	{
  		 $jsonObj4= array();	
  		 
  		 $o_id = sanitize($_POST['o_id']);
  		 $u_id = sanitize($_POST['user_id']);

		$query="SELECT * FROM tbl_ticket 
		        left join tbl_offers on tbl_offers.o_id = tbl_ticket.o_id
		        left join tbl_lottery_balls on tbl_lottery_balls.lottery_balls_id = tbl_offers.lottery_balls_id
		        WHERE tbl_ticket.o_id = '$o_id' AND tbl_ticket.u_id = '$u_id' ORDER BY `tbl_ticket`.`ticket_id` DESC";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['ball_1'] = $data['ball_1'];
			$row['ball_2'] = $data['ball_2'];
			$row['ball_3'] = $data['ball_3'];
			$row['ball_4'] = $data['ball_4'];
			$row['ball_5'] = $data['ball_5'];
			$row['ball_6'] = $data['ball_6'];
			$row['ball_7'] = $data['ball_7'];
			$row['ball_8'] = $data['ball_8'];
			$row['purchase_date'] = $data['purchase_date'];
			$row['unique_ticket_id'] = $data['unique_ticket_id'];
			$row['ticket_price'] = $data['ticket_price'];
			$row['draw_date'] = $data['o_edate'];
			
			$row['normal_ball_limit'] = $data['normal_ball_limit'];
			$row['premium_ball_limit'] = $data['premium_ball_limit'];
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

//get all offers
else if(isset($_GET['get_bid_user']))
 	{
 	    
 	    $user_id = $_POST['u_id'];
  		 $jsonObj4= array();	

		$query="SELECT 
                    tbl_offers.*,
                    tbl_items. *,
                    COUNT(tbl_bid.o_id) AS total_bids
                FROM tbl_offers
                LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
                LEFT JOIN tbl_bid ON tbl_bid.o_id = tbl_offers.o_id
                WHERE tbl_bid.u_id = '$user_id'
                GROUP BY tbl_offers.o_id, tbl_items.o_name
                ORDER BY tbl_offers.o_id DESC";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];
    
		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;
		    
		    $status = 2; // Default status
    
            // Check if the offer is currently active
            if ($start <= $datetime && $end >= $datetime) {
                $status = 1;
            }
			 
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] = $file_path.'images/thumbs/'.$data['o_image'];
			$row['o_type'] = $data['o_type'];
			$row['o_price'] = $data['o_price'];
			$row['total_bids'] = $data['total_bids'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_etime'] = $data['o_etime'];
			$row['status'] = $status;
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

//get Referrals
else if(isset($_GET['get_referral']))
 	{
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_network
		left join tbl_users on tbl_users.id= tbl_network.refferal_user_id
		WHERE user_id='".$_POST['u_id']."' ORDER BY `tbl_network`.`id` DESC";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['user_id'] = $data['user_id'];
			$row['referral_bonus'] = $data['money'].' Coins';
			$row['name'] = $data['name'];
			$row['email'] = $data['email'];
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

//get Games
else if(isset($_GET['get_games']))
 	{
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_games";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['rps_status'] = $data['rps_status'];
			$row['rps_image'] = $file_path.'images/'.$data['rps_image'];
			$row['rps_min'] = $data['rps_min'];
			$row['rps_max'] = $data['rps_max'];
			$row['rps_win'] = $data['rps_win'];
			$row['rps_chance'] = $data['rps_chance'];
			
			
			$row['gn_status'] = $data['gn_status'];
            $row['gn_image'] = $file_path.'images/'.$data['gn_image'];
            $row['gn_min'] = $data['gn_min'];
            $row['gn_max'] = $data['gn_max'];
            $row['gn_win'] = $data['gn_win'];
            $row['gn_chance'] = $data['gn_chance'];


			$row['spin_status'] = $data['spin_status'];
			$row['spin_image'] = $file_path.'images/'.$data['spin_image'];
			$row['spin_min'] = $data['spin_min'];
			$row['spin_max'] = $data['spin_max'];
			$row['spin_win'] = $data['spin_win_min'];
			$row['spin_chance'] = $data['spin_win_max'];
			
			$row['ouc_status'] = $data['ouc_status'];
            $row['ouc_image'] = $file_path.'images/'.$data['ouc_image'];
            $row['ouc_amount'] = $data['ouc_amount'];
            $row['ouc_bonus1'] = $data['ouc_bonus1'];
            $row['ouc_bonus2'] = $data['ouc_bonus2'];
            $row['ouc_bonus3'] = $data['ouc_bonus3'];
            $row['ouc_min'] = $data['ouc_min'];
            $row['ouc_max'] = $data['ouc_max'];
            $row['ouc_win_min'] = $data['ouc_win_min'];
            $row['ouc_win_max'] = $data['ouc_win_max'];

			
			$row['ct_status'] = $data['ct_status'];
            $row['ct_image'] = $file_path.'images/'.$data['ct_image'];
            $row['ct_min'] = $data['ct_min'];
            $row['ct_max'] = $data['ct_max'];
            $row['ct_win'] = $data['ct_win'];
            $row['ct_chance'] = $data['ct_chance'];
            
            $row['cric_status'] = $data['cric_status'];
            $row['cric_image'] = $file_path.'images/'.$data['cric_image'];
            $row['cric_min'] = $data['cric_min'];
            $row['cric_max'] = $data['cric_max'];
            $row['cric_win'] = $data['cric_win'];
            $row['cric_chance'] = $data['cric_chance'];


			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

//get all transaction
 else if (isset($_GET['get_transaction'])) {
    $jsonObj = array();
    $userId = mysqli_real_escape_string($mysqli, $_POST['user_id']);

    $query = "
        SELECT 
            t.*,
            i.o_name, i.o_image,
            u1.name AS user_name, u1.image AS user_image
        FROM 
            tbl_transaction t
        LEFT JOIN 
            tbl_offers o ON t.type IN (1, 9) AND t.type_no = o.o_id
        LEFT JOIN 
            tbl_items i ON t.type IN (1, 9) AND o.item_id = i.item_id
        LEFT JOIN 
            tbl_users u1 ON t.type IN (2, 3) AND t.type_no = u1.id
        WHERE 
            t.user_id = ?
        ORDER BY 
            t.id DESC";

    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    while ($data = $result->fetch_assoc()) {
        $row = array();
        $row['id'] = $data['id'];
        $row['date'] = $data['date'];

        switch ($data['type']) {
            case 1:
                $row['type_name'] = "Coins Spended";
                $row['type_details'] = $data['o_name'];
                $row['type_image'] = '/thumbs/'.$data['o_image'];
                $row['money'] = '- '.$data['money'];
                break;
            case 2:
                $row['type_name'] = "Refer Earning";
                $row['type_details'] = $data['user_name'];
                $row['type_image'] = $data['user_image'];
                $row['money'] = '+ '.$data['money'];
                break;
            case 3:
                $row['type_name'] = "Referral Bonus";
                $row['type_details'] = $data['user_name'];
                $row['type_image'] = $data['user_image'];
                $row['money'] = '+ '.$data['money'];
                break;
            case 4:
                $row['type_name'] = "Signup Bonus";
                $row['type_details'] = "Welcome to the App";
                $row['type_image'] = "/static/wallet_transaction.png";
                $row['money'] = '+ '.$data['money'];
                break;
            case 5:
                $row['type_name'] = "Coins Purchased";
                $row['type_details'] = "from reseller";
                $row['type_image'] = "/static/wallet_transaction.png";
                $row['money'] = '+ '.$data['money'];
                break;
            case 6:
                $row['type_name'] = "Watch and Earn";
                $row['type_details'] = "Earned by watching Video";
                $row['type_image'] = "/static/watch.png";
                $row['money'] = '+ '.$data['money'];
                break;
            case 7:
                $row['type_name'] = "Game";
                $row['type_details'] = "Played Game";
                $row['type_image'] = "/static/game.png";
            
                // Check if the money value starts with a minus sign
                if (strpos($data['money'], '-') === 0) {
                    // If it starts with '-', display it as is
                    $row['money'] = $data['money'];
                } else {
                    // If it doesn't start with '-', add a '+' at the beginning
                    $row['money'] = '+ ' . $data['money'];
                }
                break;
            case 8:
                $row['type_name'] = "Scratch Card";
                $row['type_details'] = "Earned by Scratching card";
                $row['type_image'] = "/static/scratch_card.png";
                $row['money'] = '+ '.$data['money'];
                break;
            case 9:
                $row['type_name'] = "Transaction Refunded";
                $row['type_details'] = $data['o_name'];
                $row['type_image'] = '/thumbs/'.$data['o_image'];
                $row['money'] = '+ '.$data['money'];
                break;
            case 10:
                $row['type_name'] = "Investment Created";
                $row['type_details'] = "Coins spent for Investment";
                $row['type_image'] = "/static/investment_create.png";
                $row['money'] = '- '.$data['money'];
                break;
            case 11:
                $row['type_name'] = "Investment Cancelled";
                $row['type_details'] = "Investment Withdrawed";
                $row['type_image'] = "/static/investment_cancel.png";
                $row['money'] = '+ '.$data['money'];
                break;
            case 12:
                $row['type_name'] = "Coins Recharged";
                $row['type_details'] = "from coin shop";
                $row['type_image'] = "/static/wallet_transaction.png";
                $row['money'] = '+ '.$data['money'];
                break;
            case 13:
                $row['type_name'] = "Manual Adjustment";
                $row['type_details'] = $data['comments'];
                $row['type_image'] = "/static/manual_adjustment.png"; 
                $row['money'] = $data['money'];
                break;
            case 14:
                $row['type_name'] = "Reward Adjustment ";
                $row['type_details'] = $data['comments'];
                $row['type_image'] = "/static/reward_adjustment.png";
                $row['money'] = $data['money'];
                break;
            case 15:
                $row['type_name'] = "Interest Earned ";
                $row['type_details'] = $data['comments'];
                $row['type_image'] = "/static/wallet_transaction.png";
                $row['money'] = $data['money'];
                break;
        }

        array_push($jsonObj, $row);
    }

    $set['JSON_DATA'] = $jsonObj;

    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    die();
}
 
//penny bid update
 else if(isset($_GET['post_penny_bid']))
{
            
            
        	if($_POST['o_id']!="")
			{
			    $qry = "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_id = '".$_POST['o_id']."'";	 
				$result = mysqli_query($mysqli,$qry);
				$num_rows = mysqli_num_rows($result);
				$row = mysqli_fetch_assoc($result);
		
		        if ($num_rows > 0)
				{
				    
				    
			         $o_id=$row['o_id'];
			    
				     $user_edit= "UPDATE tbl_offers SET o_etime='".$_POST['o_etime']."' , o_min='".$_POST['o_min']."' , o_edate='".$_POST['o_edate']."' WHERE o_id = '".$o_id."'";	 
				     $user_res = mysqli_query($mysqli,$user_edit);	


				     $set['JSON_DATA'][]=array('msg'=>'Bid Placed','success'=>'1');

				}
			}
    	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

//get all offers live
 else if(isset($_GET['get_offers_live']))
  	{
  		$jsonObj4= array();	

		$query13= "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id left join tbl_cat on tbl_cat.c_id= tbl_offers.c_id WHERE o_type NOT IN (3, 9) AND ( o_date <= '".$date."' and o_edate >= '".$date."' ) and o_status = 1 and c_status='1' ORDER BY `tbl_offers`.`o_id` ASC";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];
		    
		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;
		//    exit();
			if( $start <= $datetime  && $end >= $datetime )
			 {
				$query111="SELECT *, COUNT(*)
				as num2 FROM tbl_bid 
				where tbl_bid.o_id='".$data['o_id']."' ";
			   
						   $result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
			   
						  $row111=mysqli_fetch_assoc($result111);
				   
			   
						   if($row111['num2']== null)
						   {
									$row['num2'] = "0";
								 
						   }else
						   {	
						             $row['current'] = 	$datetime;
									 $row['date'] = 	$date;
									 $row['time'] = 	$time;
									 $row['total_bids'] = 	$row111['num2'];
								
						   }             
								
			$row['id'] = $data['id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			$row['c_desc'] = $data['c_desc'];
			$row['c_color'] = $data['c_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] =  $file_path.'images/'.$data['o_image'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_link'] = $data['o_link'];
			$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_type'] = $data['o_type'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			$row['bid_increment'] = $data['bid_increment'];	
			$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $data['o_status'];
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
//get upcoming
 else if(isset($_GET['get_offers_upcomming']))
  	{
  		$jsonObj4= array();	
  		
  		$region_id = $_POST['city_id'];
  		$user_id = $_POST['user_id'];
  		//$region_id = 1;
  		//$user_id = 1;

		$query13= "SELECT * FROM tbl_offers
		           left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                   LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id
		           WHERE (o_type IN (4,5) OR (o_type = 6 AND o_price = 1))
		           AND tbl_offers.city_id = '$region_id' 
		           and o_status = 1 and c_status='1' ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
		$sql = mysqli_query($mysqli,$query13)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
		    $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;
		    
		   //if( $start <= $datetime  && $end >= $datetime )
		   if( $start >= $datetime )
			 {
			     $o_status = 0;
			 }
			 else if( $end >= $datetime )
			 {
			   $o_status = 1;
			 }

			if($end >= $datetime )
			 {
				$query111="SELECT COUNT(*) as num2
                           FROM (
                               SELECT o_id FROM tbl_ticket WHERE o_id = '".$data['o_id']."'
                               UNION ALL
                               SELECT o_id FROM tbl_bid WHERE o_id = '".$data['o_id']."'
                           ) as combined_entries";

				$result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());
				$row111=mysqli_fetch_assoc($result111);
				   
			   
				if($row111['num2']== null)
				{
				    $row['num2'] = "0";
								 
				}else
				{	
				    $row['total_bids'] = 	$row111['num2'];
				} 
				
				$query1111="SELECT COUNT(tbl_wishlist.wishlist_id) as wishlist_status FROM tbl_wishlist 
                            where tbl_wishlist.item_id='".$data['item_id']."' AND tbl_wishlist.user_id='$user_id' ";

		        $result1111 = mysqli_query($mysqli,$query1111)or die(mysqli_error());
		        $row1111=mysqli_fetch_assoc($result1111);
    

			    if($row1111['wishlist_status']== null)
			    {
		                 $row['wishlist_status'] = "0";
		              
			    }else
			    {	
			      	    $row['wishlist_status'] = 	$row1111['wishlist_status'];
			         
			    } 
								
			$row['id'] = $data['id'];
			$row['item_id'] = $data['item_id'];
			$row['c_id'] = $data['c_id'];
			$row['city_id'] = $data['city_id'];
			$row['c_name'] = $data['c_name'];
			//$row['c_desc'] = $data['c_desc'];
			//$row['c_color'] = $data['c_color'];
			$row['o_color'] = $data['o_color'];
			$row['c_image'] =  $file_path.'images/'.$data['c_image'];
			//$row['c_view'] = $data['c_view'];
			$row['o_id'] = $data['o_id'];
			$row['o_name'] = $data['o_name'];
			$row['o_image'] =  $file_path.'images/thumbs/'.$data['o_image'];
			$row['o_desc'] = $data['o_desc'];
			$row['o_link'] = $data['o_link'];
			$row['o_date'] = $data['o_date'];
			$row['o_edate'] = $data['o_edate'];
			$row['o_stime'] = $data['o_stime'];
			$row['o_etime'] = $data['o_etime'];
			$row['o_amount'] = $data['o_amount'];
			$row['o_type'] = $data['o_type'];
			$row['o_winners'] = $data['o_winners'];
			$row['o_min'] = $data['o_min'];
			$row['o_max'] = $data['o_max'];
			$row['o_qty'] = $data['o_qty'];
			//$row['bid_increment'] = $data['bid_increment'];	
			//$row['time_increment'] = $data['time_increment'];	
			$row['o_price'] = $data['o_price'];
			//$row['o_buy'] = $data['o_buy'];
			$row['o_status'] = $o_status;
			$row['c_status'] = $data['c_status'];
		
			
			array_push($jsonObj4,$row); 
			}

		}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }

 //get all offers
else if(isset($_GET['get_offers_id']))
 	{
  		 $jsonObj4= array();
  		 
  	 //    $offer_id = 498;
  		//  $user_id = 1;
  		 $offer_id = $_POST['o_id'];
  		 $user_id = $_POST['u_id'];
  		 
		$query="SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_id='$offer_id' ";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
		   $query11="SELECT COUNT(*) as num2
                           FROM (
                               SELECT o_id FROM tbl_ticket WHERE o_id = '".$data['o_id']."'
                               UNION ALL
                               SELECT o_id FROM tbl_bid WHERE o_id = '".$data['o_id']."'
                           ) as combined_entries";

				$result11 = mysqli_query($mysqli,$query11)or die(mysqli_error());
				$row11=mysqli_fetch_assoc($result11);
				   
			   
				if($row11['num2']== null)
				{
				    $row['total_bids'] = "0";
								 
				}else
				{	
				    $row['total_bids'] = 	$row11['num2'];
				} 
			
		    $query111="SELECT COUNT(DISTINCT tbl_bid.u_id) as total_users FROM tbl_bid 
                       where tbl_bid.o_id='".$data['o_id']."' ";

		    $result111 = mysqli_query($mysqli,$query111)or die(mysqli_error());

		    $row111=mysqli_fetch_assoc($result111);
    

			if($row111['total_users']== null)
			{
		             $row['total_users'] = "0";
		          
			}else
			{	
			  	    $row['total_users'] = 	$row111['total_users'];
			     
			} 
			
			$query11111="SELECT id,email,about,ratting,image,link,joining_date FROM tbl_vendor 
                       where tbl_vendor.id='".$data['id']."' ";

		    $result11111 = mysqli_query($mysqli,$query11111)or die(mysqli_error());
		    $row11111=mysqli_fetch_assoc($result11111);
            
            $row['seller_name'] = 	$row11111['email'];
            $row['seller_about'] = 	$row11111['about'];
            $row['seller_ratting'] = 	$row11111['ratting'];
            $row['seller_image'] =  $file_path.'images/'.$row11111['image'];
            $row['seller_link'] = 	$row11111['link'];
            $row['seller_join_date'] = 	$row11111['joining_date'];
			     
			
			
			$query1111="SELECT COUNT(tbl_wishlist.wishlist_id) as wishlist_status FROM tbl_wishlist 
                        where tbl_wishlist.item_id='".$data['item_id']."' AND tbl_wishlist.user_id='$user_id' ";

		    $result1111 = mysqli_query($mysqli,$query1111)or die(mysqli_error());

		    $row1111=mysqli_fetch_assoc($result1111);
    

			if($row1111['wishlist_status']== null)
			{
		             $row['wishlist_status'] = "0";
		          
			}else
			{	
			  	    $row['wishlist_status'] = 	$row1111['wishlist_status'];
			     
			} 
			
			$response = array();   
			  
			$rate2="SELECT *,SUM(tbl_bid.bd_amount) as amount FROM tbl_bid 
			        left join tbl_users on tbl_users.id= tbl_bid.u_id
	    	        left join tbl_offers on tbl_offers.o_id = tbl_bid.o_id
	    	        left join tbl_items on tbl_items.item_id = tbl_offers.item_id
                    where tbl_bid.o_id='".$data['o_id']."'
                    GROUP BY tbl_bid.u_id
                    ORDER BY `amount` DESC";

		    $rateresult2 = mysqli_query($mysqli,$rate2)or die(mysqli_error());
	   
    	    while($data1 = mysqli_fetch_assoc($rateresult2))
    		{
    		    	$rate22="SELECT * FROM tbl_bid 
                             where tbl_bid.o_id='".$data1['o_id']."' and tbl_bid.u_id='".$data1['u_id']."' ";

		            $rateresult22 = mysqli_query($mysqli,$rate22)or die(mysqli_error());
		   	   	    $num_rows = mysqli_num_rows($rateresult22);
		      
		   
                            $row1['bd_id'] = $data1['bd_id'];
                            $row1['u_id'] = $data1['u_id'];
                            $row1['name'] = $data1['name'];
                            $row1['image'] = $data1['image'];
                            $row1['o_id'] = $data1['o_id'];
	 	            	    $row1['bd_value'] = $data1['bd_value'];   
                            $row1['bd_amount'] = $data1['amount'];
	 	            	    $row1['bd_date'] = $data1['bd_date']; 
	 	            	    $row['bid_increment'] = $data['bid_increment'];	
			                $row['time_increment'] = $data['time_increment'];
                            $row1['Total_amount'] = $num_rows;           
    		                
    		                array_push($response, $row1);   
    		         
    		}
			 
				
			$response1 = array();   
			  
			$rate3="SELECT * FROM tbl_bid 
			        left join tbl_users on tbl_users.id= tbl_bid.u_id
			        left join tbl_offers on tbl_offers.o_id = tbl_bid.o_id
			        left join tbl_items on tbl_items.item_id = tbl_offers.item_id
 			        where tbl_bid.o_id='".$data['o_id']."' and tbl_bid.u_id='$user_id' ORDER BY `tbl_bid`.`bd_value` ASC";

		   $rateresult3 = mysqli_query($mysqli,$rate3)or die(mysqli_error());
	   
    	   while($data2 = mysqli_fetch_assoc($rateresult3))
    		{
    		    
    		    	if($data['o_type'] == 1)
			        {
    		        
    		            if( $data2['bid_status'] == 1)
        			    {
        		            $row2['value'] = "Unique not Lowest";
        		        }
				        else if( $data2['bid_status'] == 2)
        			    {
        		            $row2['value'] = "Not Unique";
            		    }
	        			else if( $data2['bid_status'] == 3)
        	    		{
        		            $row2['value'] = "Unique - Winning";
        		        }
    		    
    		    
		        	}

				    else if($data['o_type'] == 2)
			        {
    		            if( $data2['bid_status'] == 1)
        			    {
        		            $row2['value'] = "Unique not Highest";
        		        }
				        else if( $data2['bid_status'] == 2)
        			    {
        		            $row2['value'] = "Not Unique";
        		        }
				        else if( $data2['bid_status'] == 3)
        			    {
        		            $row2['value'] = "Unique - Winning";
        		        }	    
			        }
			
			        else if($data['o_type'] == 4 || $data['o_type'] == 5)
			        {
        	    	      $row2['value'] = "Ticket Purchased";
			        }
			        else if($data['o_type'] == 7 || $data['o_type'] == 8)
			        {
        	    	      $row2['value'] = "Bid Placed";
			        }
			
                          $row2['bd_id'] = $data2['bd_id'];
                          $row2['u_id'] = $data2['u_id'];
                          $row2['name'] = $data2['name'];
                          $row2['image'] = $data2['image'];
                          $row2['o_id'] = $data2['o_id'];
	 	            	  $row2['bd_value'] = $data2['bd_value'];   
                          $row2['bd_amount'] = $data2['bd_amount'];
	 	            	  $row2['bd_date'] = $data2['bd_date'];           
                                    
    		              array_push($response1, $row2);   
    		} 
    		
    		$response2 = array();   
			  
			$rate4="SELECT * FROM tbl_ticket 
	                left join tbl_offers on tbl_offers.o_id = tbl_ticket.o_id
	                left join tbl_lottery_balls on tbl_lottery_balls.lottery_balls_id = tbl_offers.lottery_balls_id
	                WHERE tbl_ticket.o_id = '".$data['o_id']."' AND tbl_ticket.u_id = '$user_id' ORDER BY `tbl_ticket`.`ticket_id` DESC LIMIT 1";

		   $rateresult4 = mysqli_query($mysqli,$rate4)or die(mysqli_error());
	   
    	   while($data3 = mysqli_fetch_assoc($rateresult4))
    		{
    		    
    		                $row3['ball_1'] = $data3['ball_1'];
			                $row3['ball_2'] = $data3['ball_2'];
			                $row3['ball_3'] = $data3['ball_3'];
			                $row3['ball_4'] = $data3['ball_4'];
			                $row3['ball_5'] = $data3['ball_5'];
			                $row3['ball_6'] = $data3['ball_6'];
			                $row3['ball_7'] = $data3['ball_7'];
			                $row3['ball_8'] = $data3['ball_8'];

			                $row3['normal_ball_limit'] = $data3['normal_ball_limit'];
			                $row3['premium_ball_limit'] = $data3['premium_ball_limit'];
			                
        	    	        $row3['value'] = "Ticket Purchased";
                                    
    		              array_push($response2, $row3);   
    		}
    		
    		$response3 = array();  
    		
    		$rate5="SELECT * 
                    FROM tbl_offers
                    LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
                    WHERE tbl_offers.o_type = '".$data['o_type']."' AND city_id = '".$data['city_id']."'
                    ORDER BY RAND(), CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC
                    LIMIT 15";

		   $rateresult5 = mysqli_query($mysqli,$rate5)or die(mysqli_error());
	   
    	   while($data4 = mysqli_fetch_assoc($rateresult5))
    		{
    		    $o_date = $data4['o_date'];
                $o_stime = $data4['o_stime'];
        
                $o_edate = $data4['o_edate'];
                $o_etime = $data4['o_etime'];
        
                $start = $o_date . " " . $o_stime;
                $end = $o_edate . " " . $o_etime;
                
             if ($start <= $datetime && $end >= $datetime) {
    		    
    		    $queryTotalEntry="SELECT COUNT(*) as num2
                           FROM (
                               SELECT o_id FROM tbl_ticket WHERE o_id = '".$data4['o_id']."'
                               UNION ALL
                               SELECT o_id FROM tbl_bid WHERE o_id = '".$data4['o_id']."'
                           ) as combined_entries";

				$resultTotalEntry = mysqli_query($mysqli,$queryTotalEntry)or die(mysqli_error());
				$rowTotalEntry=mysqli_fetch_assoc($resultTotalEntry);
				   
			   
				if($rowTotalEntry['num2']== null)
				{
				    $row4['total_bids'] = "0";
								 
				}else
				{	
				    $row4['total_bids'] = 	$rowTotalEntry['num2'];
				}
				
				$query1111="SELECT COUNT(tbl_wishlist.wishlist_id) as wishlist_status FROM tbl_wishlist 
                            where tbl_wishlist.item_id='".$data4['item_id']."' AND tbl_wishlist.user_id='$user_id' ";

		        $result1111 = mysqli_query($mysqli,$query1111)or die(mysqli_error());
		        $row1111=mysqli_fetch_assoc($result1111);
    

			    if($row1111['wishlist_status']== 0)
			    {
		                 $row4['wishlist_status'] = "0";
		              
			    }else
			    {	
			      	    $row4['wishlist_status'] = 	$row1111['wishlist_status'];
			         
			    } 
    		    
    		    $row4['o_id'] = $data4['o_id'];
			    $row4['id'] = $data4['id'];
			    $row4['item_id'] = $data4['item_id'];
			    $row4['o_name'] = $data4['o_name'];
			    $row4['o_image'] =  $file_path.'images/'.$data4['o_image'];
			    $row4['o_image1'] =  $file_path.'images/'.$data4['o_image1'];
			    $row4['o_image2'] =  $file_path.'images/'.$data4['o_image2'];
			    $row4['o_image3'] =  $file_path.'images/'.$data4['o_image3'];
			    $row4['o_image4'] =  $file_path.'images/'.$data4['o_image4'];
			    $row4['o_desc'] = $data4['o_desc'];
			    $row4['o_date'] = $data4['o_date'];
			    $row4['o_edate'] = $data4['o_edate'];
			    $row4['o_stime'] = $data4['o_stime'];
			    $row4['o_etime'] = $data4['o_etime'];
			    $row4['o_amount'] = $data4['o_amount'];
			    $row4['o_type'] = $data4['o_type'];
			    $row4['o_min'] = $data4['o_min'];
			    $row4['o_max'] = $data4['o_max'];
			    $row4['o_qty'] = $data4['o_qty'];
			    $row4['o_price'] = $data4['o_price'];
			    $row4['o_buy'] = $data4['o_buy'];
			    $row4['bid_increment'] = $data4['bid_increment'];	
			    $row4['time_increment'] = $data4['time_increment'];
			    $row4['o_status'] = $data4['o_status'];
                                    
    		              array_push($response3, $row4);   
    		}
    	}
    		
    		if($data['winner_id'] == 0)
    		{
    		    $row['won_name'] = 'No One';
    		}
    		else
    		{
    		    $row['won_name'] = $data['winner_name'];
    		}
    		$row['won_id'] = $data['winner_id'];
    		
    		if($data['o_type'] !== 1 || $data['o_type'] !== 2)
			{
			    $row['winning_bid'] = $data['winning_value'];
			}
          
			    $row['o_id'] = $data['o_id'];
			    $row['seller_id'] = $data['id'];
			    $row['item_id'] = $data['item_id'];
			    $row['o_name'] = $data['o_name'];
			    $row['o_image'] =  $file_path.'images/'.$data['o_image'];
			    $row['o_image1'] =  $file_path.'images/'.$data['o_image1'];
			    $row['o_image2'] =  $file_path.'images/'.$data['o_image2'];
			    $row['o_image3'] =  $file_path.'images/'.$data['o_image3'];
			    $row['o_image4'] =  $file_path.'images/'.$data['o_image4'];
			    $row['o_desc'] = $data['o_desc'];
			    $row['o_date'] = $data['o_date'];
			    $row['o_edate'] = $data['o_edate'];
			    $row['o_stime'] = $data['o_stime'];
			    $row['o_etime'] = $data['o_etime'];
			    $row['o_amount'] = $data['o_amount'];
			    $row['o_type'] = $data['o_type'];
			    $row['o_min'] = $data['o_min'];
			    $row['o_max'] = $data['o_max'];
			    $row['o_qty'] = $data['o_qty'];
			    $row['o_price'] = $data['o_price'];
			    $row['o_buy'] = $data['o_buy'];
			    $row['bid_increment'] = $data['bid_increment'];	
			    $row['time_increment'] = $data['time_increment'];
			    $row['all_bid'] = $response;
			    $row['user_bid'] = $response1;
			    $row['user_ticket'] = $response2;
			    $row['similar_items'] = $response3;
			    $row['o_status'] = $data['o_status'];
	    
			    array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 //get all mystery boxes data
else if(isset($_GET['get_mystery_id']))
 	{
  		 $jsonObj4= array();

  		 $offer_id = $_POST['o_id'];
  		 $user_id = $_POST['u_id'];
  		
  		//  $offer_id = 455;
  		//  $user_id = 1;
  		 
		$query="SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE o_id='$offer_id' ";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
		   
			
			$response = array();   
			  
			$rate2="SELECT i.o_image 
                           FROM tbl_items i
                           JOIN tbl_prizes p ON i.item_id = p.item_id
                           JOIN tbl_offers o ON p.o_id = o.o_id
                           WHERE p.o_id = '".$data['o_id']."'
                           ORDER BY p.rank_start DESC";

		    $rateresult2 = mysqli_query($mysqli,$rate2)or die(mysqli_error());
	   
    	    while($data1 = mysqli_fetch_assoc($rateresult2))
    		{
		      
                            $row4['o_image'] =  $file_path.'images/'.$data1['o_image'];        
    		                
    		                array_push($response, $row4);   
    		         
    		}
			 
				
			$response1 = array();   
			  
			$rate3="SELECT i.o_image, i.o_name, i.price, p.chance, p.type
                           FROM tbl_items i
                           JOIN tbl_prizes p ON i.item_id = p.item_id
                           JOIN tbl_offers o ON p.o_id = o.o_id
                           WHERE p.o_id = '".$data['o_id']."'
                           ORDER BY p.rank_start DESC";

		   $rateresult3 = mysqli_query($mysqli,$rate3)or die(mysqli_error());
	   
    	   while($data2 = mysqli_fetch_assoc($rateresult3))
    		{
			
                          $row2['o_image'] =  $file_path.'images/'.$data2['o_image']; 
                          $row2['o_name'] = $data2['o_name'];
                          $row2['o_price'] = $data2['price'];
                          $row2['win_chance'] = $data2['chance'];
                          $row2['color'] = ($data2['type'] == 0) ? '000000' : $data2['type'];

                                    
    		              array_push($response1, $row2);   
    		} 
    		
    		$response2 = array();   
			  
			$rate4="SELECT i.o_image, i.o_name, i.price, p.chance, p.type
                           FROM tbl_items i
                           JOIN tbl_prizes p ON i.item_id = p.item_id
                           JOIN tbl_offers o ON p.o_id = o.o_id
                           WHERE p.o_id = '".$data['o_id']."'
                           ORDER BY p.rank_start DESC";

		   $rateresult4 = mysqli_query($mysqli,$rate4)or die(mysqli_error());
	   
    	   while($data3 = mysqli_fetch_assoc($rateresult4))
    		{
    		    
    		              $row2['o_image'] =  $file_path.'images/'.$data2['o_image']; 
                          $row2['o_name'] = $data3['o_name'];
                          $row2['winner_name'] = !empty($data3['winner']) ? $data3['winner'] : 'No Winner';
                          $row2['date'] = !empty($data3['date']) ? $data3['date'] : '2021-06-21';
                          $row2['color'] = ($data3['type'] == 0) ? '000000' : $data3['type'];

                                    
    		              array_push($response2, $row2);   
    		}
          
			    $row['o_id'] = $data['o_id'];
			    $row['o_name'] = $data['o_name'];
			    $row['o_image'] =  $file_path.'images/'.$data['o_image'];
			    $row['o_desc'] = $data['o_desc'];
			    $row['o_date'] = $data['o_date'];
			    $row['o_edate'] = $data['o_edate'];
			    $row['o_stime'] = $data['o_stime'];
			    $row['o_etime'] = $data['o_etime'];
			    $row['o_amount'] = $data['o_amount'];
			    $row['o_price'] = $data['o_price'];
			    $row['prize_images'] = $response;
			    $row['prizes'] = $response1;
			    $row['recent_drops'] = $response2;
			    $row['o_status'] = $data['o_status'];
	    
			    array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
 else if(isset($_GET['get_mystery_results']))
{
    $jsonObj4 = array();	

    $user_id = sanitize($_POST['user_id']);
    $o_id = sanitize($_POST['o_id']);

    // Create an array for the response
    $row['o_name'] = 'Test';
    $row['o_id'] = $o_id;

    // Assign random values to block1, block2, and block3
    $row['block1'] = rand(1, 10); // You can adjust the range as needed
    $row['block2'] = rand(1, 10);
    $row['block3'] = rand(1, 10);

    // Push the result into the JSON array
    array_push($jsonObj4, $row); 

    // Prepare and return the JSON response
    $set['JSON_DATA'] = $jsonObj4;	
    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

 
else if(isset($_GET['add_hyip_order'])) {
    
        $user = $_POST['u_id'];
        $plan = $_POST['plan_id'];
        $investment = $_POST['investment_amount'];
        
        $checkBalanceQuery = "SELECT wallet FROM tbl_users WHERE id = $user";
        $checkBalanceResult = mysqli_query($mysqli, $checkBalanceQuery);
        $checkBalanceRow = mysqli_fetch_assoc($checkBalanceResult);
        $currentBalance = $checkBalanceRow['wallet'];
        
        $planDetailsQuery = "SELECT * FROM tbl_hyip WHERE plan_id = $plan";
        $planDetailsResult = mysqli_query($mysqli, $planDetailsQuery);
        $planDetailsRow = mysqli_fetch_assoc($planDetailsResult);


        // Calculate the next interest update time
        $nextInterestUpdate = strtotime($datetime) + strtotime($planDetailsRow['plan_repeat_time']) - strtotime('today');
        $nextInterestUpdate = date('Y-m-d H:i:s', $nextInterestUpdate);
        
        if ($planDetailsRow['plan_interest_type'] == 1) 
        {
                $interestType= $planDetailsRow['plan_interest'].'%';
        } 
        else 
        {
            $interestType= $planDetailsRow['plan_interest'];
        }
        
        if($currentBalance >= $investment)
        {
   
        $user_edit = "INSERT INTO `tbl_hyip_order`(`user_id`, `plan_id`,`interest`,`investment_amount`,`current_value`,`last_interest_update` ,`order_date`,`next_interest_update`,`status`) VALUES ('$user', '$plan','$interestType','$investment','$investment','$datetime','$datetime','$nextInterestUpdate',1)";
        $user_res = mysqli_query($mysqli, $user_edit);    
        
        $newwallet = $currentBalance - $investment;   
        $user_edit = "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$user."'"; 
        $result1 = mysqli_query($mysqli,$user_edit);
        
        $qry1 = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`, `date`, `money`) 
                         VALUES ('$user', 10, '10', NOW(), '$investment')";
        $result_insert = mysqli_query($mysqli, $qry1);
        
        $set['JSON_DATA'][] = array('msg' => 'Plan Created!', 'success' => '1');  
        }
        else
        {
            $set['JSON_DATA'][] = array('msg' => 'Insufficient Balance!', 'success' => '0'); 
        }
        
        header('Content-Type: application/json; charset=utf-8');
        echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
        die();
    
}

//get all offers
else if(isset($_GET['get_hyip_plan']))
 	{
  		 $jsonObj4= array();	

		$query="SELECT * FROM tbl_hyip_order 
		left join tbl_hyip on tbl_hyip.plan_id = tbl_hyip_order.plan_id
		WHERE user_id='".$_POST['u_id']."' ORDER BY `tbl_hyip_order`.`order_id` DESC";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
			 
			$row['plan_id'] = $data['order_id'];
			$row['plan_name'] = $data['plan_name'];
			$row['plan_short_description'] = $data['plan_short_description'];
			$row['investment_amount'] = $data['investment_amount'];
			$row['current_value'] = $data['current_value'];
			$row['last_update'] = $data['last_interest_update'];
			$row['next_update'] = $data['next_interest_update'];
			$row['plan_interest'] = $data['plan_interest'];
			$row['plan_interest_frequency'] = $data['plan_interest_frequency'];
			$row['plan_color'] = $data['plan_color'];
			$row['plan_interest_type'] = $data['plan_interest_type'];
			$row['plan_cancelable'] = $data['plan_cancelable'];
			$row['plan_cancel_charge'] = $data['plan_cancel_charge'];
			$row['expires_on'] = $data['expires_on'];
			$row['order_date'] = $data['order_date'];
			$row['plan_status'] = $data['status'];

			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 
else if(isset($_GET['add_address']))
    {
        
    $user_id = mysqli_real_escape_string($mysqli, $_POST['u_id']);
    $address_line1 = mysqli_real_escape_string($mysqli, $_POST['address_line1']);
    $address_line2 = mysqli_real_escape_string($mysqli, $_POST['address_line2']);
    $city = mysqli_real_escape_string($mysqli, $_POST['city']);
    $state = mysqli_real_escape_string($mysqli, $_POST['state']);
    $postal_code = mysqli_real_escape_string($mysqli, $_POST['postal_code']);
    $country = mysqli_real_escape_string($mysqli, $_POST['country']);
    $address_type = mysqli_real_escape_string($mysqli, $_POST['address_type']);
    $nickname = mysqli_real_escape_string($mysqli, $_POST['nickname']);
    $datetime = date('Y-m-d H:i:s'); // Current date and time

    $qry1 = "INSERT INTO tbl_address (
        u_id,
        address_line1,
        address_line2,
        city,
        state,
        postal_code,
        country,
        address_type,
        nickname,
        created_at
    ) VALUES (
        '$user_id',
        '$address_line1',
        '$address_line2',
        '$city',
        '$state',
        '$postal_code',
        '$country',
        '$address_type',
        '$nickname',
        '$datetime'
    )";

    $result1 = mysqli_query($mysqli, $qry1);

    if ($result1) {
        $set['JSON_DATA'][] = array('msg' => "Address Added Successfully");
    } else {
        $set['JSON_DATA'][] = array('msg' => "Error adding address: " . mysqli_error($mysqli));
    }

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

else if(isset($_GET['edit_address']))
{
    $address_id = mysqli_real_escape_string($mysqli, $_POST['address_id']);
    $user_id = mysqli_real_escape_string($mysqli, $_POST['u_id']);
    $address_line1 = mysqli_real_escape_string($mysqli, $_POST['address_line1']);
    $address_line2 = mysqli_real_escape_string($mysqli, $_POST['address_line2']);
    $city = mysqli_real_escape_string($mysqli, $_POST['city']);
    $state = mysqli_real_escape_string($mysqli, $_POST['state']);
    $postal_code = mysqli_real_escape_string($mysqli, $_POST['postal_code']);
    $country = mysqli_real_escape_string($mysqli, $_POST['country']);
    $address_type = mysqli_real_escape_string($mysqli, $_POST['address_type']);
    $nickname = mysqli_real_escape_string($mysqli, $_POST['nickname']);

    $qry1 = "UPDATE tbl_address SET 
        address_line1 = '$address_line1',
        address_line2 = '$address_line2',
        city = '$city',
        state = '$state',
        postal_code = '$postal_code',
        country = '$country',
        address_type = '$address_type',
        nickname = '$nickname',
        updated_at = '$datetime'
        WHERE address_id = '$address_id' AND u_id = '$user_id'";

    $result1 = mysqli_query($mysqli, $qry1);

    if ($result1) {
        $set['JSON_DATA'][] = array('msg' => "Address Updated Successfully");
    } else {
        $set['JSON_DATA'][] = array('msg' => "Error updating address: " . mysqli_error($mysqli));
    }

    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

//get address
else if(isset($_GET['get_address']))
 	{
 	$user_id = mysqli_real_escape_string($mysqli, $_POST['u_id']);

    $jsonObj4 = array();

    // Query to fetch addresses for the given user ID
    $query = "SELECT * FROM tbl_address WHERE u_id='$user_id'";
    $sql = mysqli_query($mysqli, $query) or die(mysqli_error($mysqli));

    // Fetch and prepare address data
    while ($data = mysqli_fetch_assoc($sql)) {
        $row = array(
            'address_id' => $data['address_id'], // Updated to match the table structure
            'u_id' => $data['u_id'],
            'address_line1' => $data['address_line1'], // Updated to match the table structure
            'address_line2' => $data['address_line2'], // Updated to match the table structure
            'city' => $data['city'],
            'state' => $data['state'],
            'postal_code' => $data['postal_code'],
            'country' => $data['country'],
            'address_type' => $data['address_type'],
            'nickname' => $data['nickname'],
            'created_at' => $data['created_at'],
            'updated_at' => $data['updated_at'] // Added to reflect the updated table structure
        );

        array_push($jsonObj4, $row);
    }

    $set['JSON_DATA'] = $jsonObj4;

    // Return JSON response
    header('Content-Type: application/json; charset=utf-8');
    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
 
else if(isset($_GET['delete_address']))
 	{

	     // Retrieve POST data
         $user_id = mysqli_real_escape_string($mysqli, $_POST['u_id']);
         $address_id = mysqli_real_escape_string($mysqli, $_POST['address_id']);
     
         // SQL query to delete the address
         $query_delete_address = "DELETE FROM tbl_address WHERE address_id = '$address_id' AND u_id = '$user_id'";
     
         // Execute the query
         if (mysqli_query($mysqli, $query_delete_address)) {
             $set['JSON_DATA'][] = array('msg' => 'Address Deleted Successfully', 'success' => '1');
         } else {
             $set['JSON_DATA'][] = array('msg' => 'Error Deleting Address: ' . mysqli_error($mysqli), 'success' => '0');
         }
     
         // Return JSON response
         header('Content-Type: application/json; charset=utf-8');
         echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
         die();
}
 
else if(isset($_GET['add_order']))
 	{
 	    
 	    
 	    $user_id = sanitize($_POST['u_id']);
 	    $item_id = sanitize($_POST['item_id']);
 	    $o_amount = sanitize($_POST['total_amount']);
 	    $o_id = sanitize($_POST['offer_id']);
 	    $redeem_item = 0;
 	    
 	    if ($o_id != "")
 	    {
 	    
 	        $qrysItem = "SELECT * FROM tbl_offers left join tbl_items on tbl_items.item_id = tbl_offers.item_id WHERE tbl_offers.o_id = '$o_id' LIMIT 0,1"; 
		    $resultsItem = mysqli_query($mysqli,$qrysItem);
		    $rowItem = mysqli_fetch_assoc($resultsItem);
		    $o_type = $rowItem['o_type'];
		    $o_price = $rowItem['o_price'];
		    $discount_amount = $o_price - $o_amount;
		    $seller_id = $rowItem['id'];
		    $item_id_offer = $rowItem['item_id'];
		    
		    if($o_type == 3)
		    	{
		    	
		    	    $qry = "SELECT * FROM tbl_users WHERE id = '".$user_id."'";	 
		    		$result = mysqli_query($mysqli,$qry);
		    		$num_rows = mysqli_num_rows($result);
		    		$row = mysqli_fetch_assoc($result);
		    		    
		    		    
		    	         $id=$row['id'];
	        		     $wallet=$row['wallet'];
	        		     $update_amount = $o_amount;
	        		     $newwallet = $wallet - $update_amount;   
	        		     
	        		 if($wallet < $update_amount)
	        		 {
	        		     $set['JSON_DATA'][] = array('msg' => "Insufficient Balance", 'success' => '0');
    
                         header('Content-Type: application/json; charset=utf-8');
                         echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
                         die();
	        		 }
		    	    
		    		     $user_edit= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	 
		    		     $user_res = mysqli_query($mysqli,$user_edit);	
    
		    		     
		    			 $qry1="INSERT INTO tbl_transaction (`user_id`,`type`,`type_no`,`date`,`money`) 
		    		            VALUES ('$id',1,'$r2','$datetime','$update_amount')"; 
	                     
	                     $result1=mysqli_query($mysqli,$qry1);
	                     
	                     $redeem_item = 1;
		    		
		    	}
		    
		    if($o_type == 3 || $o_type == 9)
		    	{
		    	
		    	// INSERT into tbl_bid
                   $qry2 = "INSERT INTO tbl_bid
                               (u_id,
                               `o_id`,
                               `bd_value`,
                               `bd_amount`,
                               `bd_date`,
                               `bd_status`
                				) VALUES (
                                   '".$user_id."',
                                   '".$o_id."',
                                   '".$o_price."',
                					'".$o_amount."',
                					'".$datetime."',
                						'2'
                				)"; 
                   $result2 = mysqli_query($mysqli, $qry2);
		    	}
 	    }
 	    else if ($item_id != "")
 	    {
 	      $qrysItem1 = "SELECT * FROM tbl_items WHERE tbl_items.item_id = '$item_id' LIMIT 0,1"; 
		  $resultsItem1 = mysqli_query($mysqli,$qrysItem1);
		  $rowItem1 = mysqli_fetch_assoc($resultsItem1);
		  $o_price = $rowItem1['price'];
 	      $discount_amount = ($o_price - $o_amount);
 	      $item_id_offer = $item_id;
 	      
 	    }
			
 	    
		$qry1="INSERT INTO tbl_order
                (u_id,
                `offer_id`,
                `total_amount`,
                `dis_amount`,
                `pay_amount`,
                `offer_o_id`,
                `redeem_item`,
                `seller_id`,
                `o_address`,
                `order_date`,
                `o_status`
				) VALUES (
                    '".$user_id."',
                    '".$item_id_offer."',
                    '".$o_price."',
		    		'".$discount_amount."',
					'".$o_amount."',
					'".$o_id."',
					'".$redeem_item."',
					'".$seller_id."',
					'".trim($_POST['o_address'])."',
					'".$datetime."',
						'1'
				)"; 
            
        $result1 = mysqli_query($mysqli,$qry1);
        $last_id = mysqli_insert_id($mysqli); 
        $tbl_order_id = $last_id;

        $qrys = "SELECT * FROM tbl_order WHERE o_id = '".$last_id."'"; 
		$results = mysqli_query($mysqli,$qrys);
		$row = mysqli_fetch_assoc($results);
			
			

												 
			$set['JSON_DATA'][]	=	array(
                                  'msg' 	=>	"Order Placed Successfuly",
			                      'o_id' 	=>	$row['o_id'],
			                      'u_id' 	=>	$row['u_id'],
 								 'offer_id'	=>	$row['offer_id'],
 								 'total_amount'	=>	$row['total_amount'],
 							     'dis_amount'	=>	$row['dis_amount'],
 							     'pay_amount'	=>	$row['pay_amount'],
 							 	 'o_address'	=>	$row['o_address'],
 							     'order_date'	=>	$row['order_date'],
 						       	 'o_status'	=>	$row['o_status'],
								 'order_status'	=>	$row['order_status'],
		     								);	
		     								
		   // Insert order status
        $qryOrderLogs="INSERT INTO tbl_order_logs
                (order_id,
                `order_status`,
                `modified_by`,
                `date`,
                `status`
				) VALUES (
                    '".$tbl_order_id."',
                    '1',
                    '0',
                    '".$datetime."',
		    		'1')"; 
            
        $resultOrderLogs = mysqli_query($mysqli,$qryOrderLogs);
            
		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

//get all offers
else if(isset($_GET['get_order_user']))
 	{
 	    

        $user_id = sanitize($_POST['u_id']);

  		 $jsonObj4= array();	

		$query="SELECT
                      tbl_order.*, 
                      tbl_items.*
                FROM tbl_order 
                LEFT JOIN tbl_items ON tbl_items.item_id = tbl_order.offer_id
                WHERE tbl_order.u_id = '$user_id'
                ORDER BY tbl_order.o_id DESC";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());


		while($data = mysqli_fetch_assoc($sql))
		{
  		    $order_id = $data['o_id'];
  		    $redeem_item = $data['redeem_item']; // checks if it is a redeem item
		    
		    $response = array();   
			  
			$rate1="SELECT order_status, date FROM tbl_order_logs 
			        WHERE order_id = $order_id
                    ORDER BY `order_logs_id` ASC";

		    $rateresult1 = mysqli_query($mysqli,$rate1)or die(mysqli_error());
	   
    	    while($data1 = mysqli_fetch_assoc($rateresult1))
    		{
		      
		   
                            $row1['status'] = $data1['order_status'];
                            $row1['date'] = $data1['date'];
    		                
    		                array_push($response, $row1);   
    		     
    		}
    		
    		$item_id = $data['offer_id'];
    		
    		$query12 = "SELECT *
                              FROM tbl_reviews
                              WHERE tbl_reviews.user_id ='".$user_id."' AND tbl_reviews.item_id ='".$item_id."'";

            $result12 = mysqli_query($mysqli, $query12) or die(mysqli_error());

            $row12 = mysqli_fetch_assoc($result12);

            if ($row12['rating'] == NULL) {
                $row['rating'] = "";
                $row['review'] = "";
            } else {
                $row['rating'] = $row12['rating'];
                $row['review'] = $row12['comment'];
            }
            
            $query11111="SELECT email FROM tbl_vendor 
                       where tbl_vendor.id='".$data['seller_id']."' ";

		    $result11111 = mysqli_query($mysqli,$query11111)or die(mysqli_error());
		    $row11111=mysqli_fetch_assoc($result11111);
		    
		    $seller_name = $row11111['email'];
		    
            if ($seller_name != NULL)
            {
                $row['seller_name'] = 	$seller_name;
            }
            else {
                $row['seller_name'] = 	'Admin';
            }
			 
			$row['o_id'] = $data['o_id'];
			$row['u_id'] = $data['u_id'];
			$row['name'] = 'User';
			$row['offer_id'] = $data['offer_id'];
			$row['o_name'] = $data['o_name'];
			if($redeem_item == 1)
			{
			    $row['o_type'] = 3;
			} else 
			{
			    $row['o_type'] = 9;
			}
			$row['o_image'] =  $file_path.'images/thumbs/'.$data['o_image'];
			$row['total_amount'] = $data['total_amount'];
			$row['dis_amount'] = $data['dis_amount'];
			$row['pay_amount'] = $data['pay_amount'];
			$row['o_address'] = $data['o_address'];
			$row['order_date'] = $data['order_date'];
			$row['order_status'] = $data['order_status'];
			$row['status_history'] = $response;
		
			
			array_push($jsonObj4,$row); 
			}
		
		$set['JSON_DATA'] = $jsonObj4;	

		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
 }
 


if (isset($_GET['forgotpassword'])) {
    $phone = $_POST['phone'];

	// Fetch Twilio SID and Auth Token from tbl_settings
    $settings_query = "SELECT twilio_sid, twilio_token,otp_system FROM tbl_settings";
    $settings_result = mysqli_query($mysqli, $settings_query);
    $settings_row = mysqli_fetch_assoc($settings_result);

    $text = rand(1000, 9999);

    $qry1 = "SELECT * FROM tbl_users WHERE phone = '" . $phone . "' AND status='1'";
    $result1 = mysqli_query($mysqli, $qry1);
    $row1 = mysqli_fetch_assoc($result1);
    $num_rows = mysqli_num_rows($result1);
    $password = $row1['password'];
    $country = $row1['country_code'];
    $full_number = "+" . $country . $phone;

    if ($num_rows > 0) {
        $user_edit = "UPDATE tbl_users SET confirm_code='" . $text . "'  WHERE phone = '" . $phone . "' AND status='1'";
        $user_res = mysqli_query($mysqli, $user_edit);


            $account_sid = $settings_row['twilio_sid'];
            $auth_token = $settings_row['twilio_token'];

            // Twilio integration
            require_once 'vendor/autoload.php'; // Adjust the path to the Twilio library

            $twilio = new Twilio\Rest\Client($account_sid, $auth_token);

            try {
                $message = $twilio->messages
                    ->create(
                        "$full_number", // to
                        array(
                            "from" => "+13656021115", // Your Twilio phone number
                            "body" => "Your verification code for testing WowCodes.in demo app is $text"
                        )
                    );

                if ($message->sid) {
                    $set['JSON_DATA'][] = array('msg' => "Enter Verification Code", 'success' => '1');
                } else {
                    $set['JSON_DATA'][] = array('msg' => "Failed to send code via Twilio", 'success' => '0');
                }
            } catch (Exception $e) {
                $set['JSON_DATA'][] = array('msg' => "Twilio Exception: " . $e->getMessage(), 'success' => '0');
            }
        
        header('Content-Type: application/json; charset=utf-8');
        echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
        die();
    } else {
        $set['JSON_DATA'][] = array('msg' => "Mobile Number is not Registered", 'success' => '0');

        header('Content-Type: application/json; charset=utf-8');
        echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
        die();
    }
}

//get city
else if(isset($_GET['get_city']))
{
	  $jsonObj4= array();	

   $query="SELECT * FROM tbl_city WHERE city_status = 1";
   $sql = mysqli_query($mysqli,$query)or die(mysqli_error());

   while($data = mysqli_fetch_assoc($sql))
   {
		
	   $row['city_id'] = $data['city_id'];
	   $row['city_name'] = $data['city_name'];
	   $row['city_image'] =  $file_path.'images/'.$data['city_image'];
	   $row['city_bw_image'] =  $file_path.'images/'.$data['city_bw_image'];
   
	   
	   array_push($jsonObj4,$row); 
	   }
   
   $set['JSON_DATA'] = $jsonObj4;	

   
   header( 'Content-Type: application/json; charset=utf-8' );
   echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
   die();
}

//user verify otp from setting screen
else if(isset($_GET['mobilenumberverify_setting'])) 
	{
	   
      		$phone = $_POST['phone'];
      		$confirm_code = $_POST['confirm_code'];
      		 
      		$querycode = "SELECT referral_bonus,refercode_bonus FROM tbl_settings";
            $resultcode = mysqli_query($mysqli, $querycode);
            $rowcode = mysqli_fetch_assoc($resultcode); 
            $refferal_bonus1 = $rowcode['refferal_bonus'];

             $qry1 = "SELECT * FROM tbl_users WHERE phone = '".$phone."'  and confirm_code='".$confirm_code."' ";	 
			$result1 = mysqli_query($mysqli,$qry1);
			$row = mysqli_fetch_assoc($result1);
			 $num_rows = mysqli_num_rows($result1);

	        
	        if ($num_rows > 0) {
                    $user_edit1 = "UPDATE tbl_users SET status = 1 WHERE phone = '".$phone."' AND confirm_code='".$confirm_code."'";
                    $user_res1 = mysqli_query($mysqli, $user_edit1);
                
                    $qry2 = "SELECT * FROM tbl_users WHERE phone = '".$phone."' AND status = 1";
                    $result2 = mysqli_query($mysqli, $qry2);
                    $row1 = mysqli_fetch_assoc($result2);
                    $refferal_code = $row1['refferal_code'];
                
                    if ($refferal_code != "" && $refferal_code != "0") {
                        $qry2 = "SELECT * FROM tbl_users WHERE code = '".$refferal_code."'";
                        $result2 = mysqli_query($mysqli, $qry2);
                        $num_rows = mysqli_num_rows($result2);
                
                        if ($num_rows > 0) {
                            $row2 = mysqli_fetch_assoc($result2);
                            $referred_user_id = $row2['id']; //id of the user whose code was used
                            $id = $row1['id']; //id of the user who used the code
                
                            // Insert initial referrer data
                            $network_qry = "INSERT INTO tbl_network (`user_id`, `level`, `money`, `refferal_user_id`, `status`) 
                                            VALUES ('$referred_user_id', '1', '$refferal_bonus1', '$id', '1')";
                            $result_network_insert = mysqli_query($mysqli, $network_qry);
                
                            try {
                                // Execute the SQL query to calculate referral bonuses for each referrer
                                $sql_query = "
                                    WITH RECURSIVE NetworkHierarchy AS (
                                        SELECT user_id, refferal_user_id, 1 AS num
                                        FROM tbl_network
                                        WHERE refferal_user_id = ".$id."
                        
                                        UNION ALL
                        
                                        SELECT n.user_id, n.refferal_user_id, nh.num + 1
                                        FROM tbl_network AS n
                                        JOIN NetworkHierarchy AS nh ON n.refferal_user_id = nh.user_id
                                    )
                                    SELECT nh.user_id AS referrer_id,
                                           nh.num AS referral_level,
                                           rb.referral_bonus AS bonus_amount
                                    FROM NetworkHierarchy AS nh
                                    JOIN tbl_referral_bonus AS rb ON nh.num = rb.level";
                
                                $result_sql = mysqli_query($mysqli, $sql_query);
                
                                if ($result_sql) {
                                    while ($row_sql = mysqli_fetch_assoc($result_sql)) {
                                        $referrer_id = $row_sql['referrer_id'];
                                        $referral_level = $row_sql['referral_level'];
                                        $bonus_amount = $row_sql['bonus_amount'];
                
                                        // Calculate the bonus for the current referrer at this level
                                        $bonus_for_referrer = $bonus_amount; // Modify this calculation based on your logic
                
                                        // Update the wallet of the referrer with the calculated bonus amount
                                        $update_query = "UPDATE tbl_users SET wallet = wallet + ".$bonus_for_referrer." WHERE id = ".$referrer_id;
                                        $result_update = mysqli_query($mysqli, $update_query);
                
                                        // Insert transaction record for the bonus amount received
                                        $date = date('Y-m-d');
                                        $qry1 = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`, `date`, `money`) 
                                                 VALUES ('$referrer_id', 3, '$id', '$date', '$bonus_for_referrer')";
                                        $result_insert = mysqli_query($mysqli, $qry1);
                                    }
                                }
                            } catch (Exception $e) {
                                // Handle the case where the WITH RECURSIVE query is not supported
                                error_log('Recursive query failed: ' . $e->getMessage());
                                // Optionally, add fallback logic here
                            }
                        }
                    }
                
                    $set['JSON_DATA'][] = array(
                        'msg' => "Profile Updated Successfully",
                        'success' => '1',
                        'id' => $row1['id'],
                        'login_type' => $row1['login_type'],
                        'name' => $row1['name'],
                        'email' => $row1['email'],
                        'password' => $row1['password'],
                        'image' => $row1['image'],
                        'phone' => $row1['phone'],
                        'wallet' => $row1['wallet'],
                        'code' => $row1['code'],
                        'refferal_code' => $row1['refferal_code'],
                        'confirm_code' => $row1['confirm_code'],
                        'network' => $row1['network'],
                        'ban' => $row1['ban'],
                        'status' => $row1['status']
                    );
                } else {
                    $set['JSON_DATA'][] = array('msg' => "Enter Correct Verification Code", 'success' => '0');
                }

	        
	        	 	 header( 'Content-Type: application/json; charset=utf-8' );
    	          echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    	    	 die();
	}

else if(isset($_GET['change_password']))
 	{

	    $phone = $_POST['phone'];
	   
		$user_edit= "UPDATE tbl_users SET password='".$_POST['password']."' WHERE phone = '".$phone."' and status='1'"; 	 
		
   		$user_res = mysqli_query($mysqli,$user_edit);	
	  	
	  	$set['JSON_DATA'][]=array('msg'=>'Updated','success'=>'1');		 
		
		header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();	
	}
	
//user registration
if(isset($_GET['postUsermobileRegister'])) 
	{
	    
	       // Fetch Twilio SID and Auth Token and otp_system from tbl_settings
	        $settings_query = "SELECT twilio_sid, twilio_token,otp_system, signup_bonus FROM tbl_settings";
	        $settings_result = mysqli_query($mysqli, $settings_query);
	        $settings_row = mysqli_fetch_assoc($settings_result);
	   
      	     $phone = $_POST['phone'];
      	     $device_id = $_POST['device_id'];
      	     $email = $_POST['email'];
             $text = ($settings_row['otp_system'] == 1) ? rand(1000, 9999) : '1234';
             $rand=rand(1000,999999);
             $country = $_POST['country_code'];
             $name = $_POST['name'];
             $full_number = "+" . $country . $phone;

             
            if($_POST['phone']!=""  )
	        {
    			    
                $qry1 = "SELECT * FROM tbl_users WHERE ( phone = '".$phone."' or device_id = '".$device_id."' or email = '".$email."') and status = 1 "; 	 
    			$result1 = mysqli_query($mysqli,$qry1);
    			$row1 = mysqli_fetch_assoc($result1);
    			$num_rows = mysqli_num_rows($result1);
    
    			
        	        if ($num_rows > 0 )
        			{
        			    if($row1['ban'] == 1)
        			    {
        			        	$set['JSON_DATA'][]=array('msg' => "Access Denied, User is Banned!!",'success'=>'0');
        		

                	 	 header( 'Content-Type: application/json; charset=utf-8' );
            	          echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
            	    	 die();
        			    }else {
                                if ($row1['phone'] == $phone) {
                                    $set['JSON_DATA'][] = array('msg' => "Your mobile number is already registered. Please Login to use the app.", 'success' => '0');
                                } elseif ($row1['device_id'] == $device_id) {
                                    $set['JSON_DATA'][] = array('msg' => "Multiple Accounts from same device is not allowed.", 'success' => '0');
                                } elseif ($row1['email'] == $email) {
                                    $set['JSON_DATA'][] = array('msg' => "Email ID already used, contact admin.", 'success' => '0');
                                }

                	 	 header( 'Content-Type: application/json; charset=utf-8' );
            	          echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
            	    	 die();
        			    }
        			    
                    
        
        			}else
        			{
        			    
        			    $qry2 = "SELECT * FROM tbl_users WHERE phone = '".$phone."' and status='0'"; 	 
            			$result2 = mysqli_query($mysqli,$qry2);
            			$row2 = mysqli_fetch_assoc($result2);
            			$num_rows2 = mysqli_num_rows($result2);
    
    			
        	       if ($num_rows2 > 0) {
                $user_edit = "UPDATE tbl_users SET confirm_code='" . $text . "' WHERE phone = '" . $phone . "' and status ='0'";
                $user_res = mysqli_query($mysqli, $user_edit);

                if ($settings_row['otp_system'] == 1) {

                    $account_sid = $settings_row['twilio_sid'];
                    $auth_token = $settings_row['twilio_token'];

                    // Twilio integration
                    require_once 'vendor/autoload.php'; // Adjust the path to the Twilio library

                    $twilio = new Twilio\Rest\Client($account_sid, $auth_token);

                    try {
                        $message = $twilio->messages
                            ->create(
                                "$full_number", // to
                                array(
                                    "from" => "+13656021115", // Your Twilio phone number
                                    "body" => "Hi $name, Your verification code is $text"
                                )
                            );

                        if ($message->sid) {
                            $set['JSON_DATA'][] = array('msg' => "Enter Verification Code", 'success' => '1');
                        } else {
                            $set['JSON_DATA'][] = array('msg' => "Failed to send code via Twilio", 'success' => '0');
                        }
                    } catch (Exception $e) {
                        $set['JSON_DATA'][] = array('msg' => "Twilio Exception: " . $e->getMessage(), 'success' => '0');
                    }

                    header('Content-Type: application/json; charset=utf-8');
                    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
                    die();
                } else {
                    $set['JSON_DATA'][] = array('msg' => "Welcome!", 'success' => '1');
                    header('Content-Type: application/json; charset=utf-8');
                    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
                    die();
                }
        	   }
                    else
        			{
        
        		        
          			$insert_user1="INSERT INTO tbl_users 
        				  (
        				    `login_type`,
        				    `name`,
        				    `email`,
        			        `image`,
        				    `password`,
        				    `country_code`,
        				    `phone`,
        				    `device_id`,
        				    `date`,
        				     wallet,
        				     code,
        				     refferal_code,
        				    `confirm_code`,
        				    `ban`,
        				    `status`
        				  ) VALUES (
        					    '1',
        					    '".trim($_POST['name'])."',
        					    '".trim($_POST['email'])."',
        					    '',
        					    '".$_POST['password']."',
        					    '".$_POST['country_code']."',
                                '".trim($_POST['phone'])."',
                                '".trim($_POST['device_id'])."',
                                '".$datetime."',
                                '".$settings_row['signup_bonus']."',
                                '$rand',
                                '".$_POST['refferal_code']."',
                                '".$text."',
                            	0,
                            	0
			            	)"; 	    
            
           
        	            $result1=mysqli_query($mysqli,$insert_user1); 
        	            $last_id = mysqli_insert_id($mysqli);
        	            
        	            if ($result1) {
                                        // Insert transaction record for the signup bonus
                                        $qry1 = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`,`date`, `money`) 
                                         VALUES ('$last_id', 4, '$last_id', '$date', '".$rowtime['signup_bonus']."')";
                                         $result_insert = mysqli_query($mysqli, $qry1);

        	            }
        	            
                            if ($settings_row['otp_system'] == 1) {

                    $account_sid = $settings_row['twilio_sid'];
                    $auth_token = $settings_row['twilio_token'];

                    // Twilio integration
                    require_once 'vendor/autoload.php'; // Adjust the path to the Twilio library

                    $twilio = new Twilio\Rest\Client($account_sid, $auth_token);

                    try {
                        $message = $twilio->messages
                            ->create(
                                "$full_number", // to
                                array(
                                    "from" => "+13656021115", // Your Twilio phone number
                                    "body" => "Hi $name, Your verification code for testing WowCodes.in demo app is $text"
                                )
                            );

                        if ($message->sid) {
                            $set['JSON_DATA'][] = array('msg' => "Enter Verification code", 'success' => '1');
                        } else {
                            $set['JSON_DATA'][] = array('msg' => "Failed to send code via Twilio", 'success' => '0');
                        }
                    } catch (Exception $e) {
                        $set['JSON_DATA'][] = array('msg' => "Twilio Exception: " . $e->getMessage(), 'success' => '0');
                    }

                    header('Content-Type: application/json; charset=utf-8');
                    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
                    die();
                } else { 
                    // $set['JSON_DATA'][] = array('msg' => "Your verification code is $text as verification system is disabled", 'success' => '1');
                    header('Content-Type: application/json; charset=utf-8');
                    echo $val = str_replace('\\/', '/', json_encode($set, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
                    die();
                }
        	   }
                
	        }
            

	}else{
        			    
	    	$set['JSON_DATA'][]=array('msg' => "Please enter correct phone number...",'success'=>'0');


	 	 header( 'Content-Type: application/json; charset=utf-8' );
          echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    	 die();
    
        
    }
}

//user wallet update
else if(isset($_GET['post_user_wallet_update']))
{
        	if($_POST['user_id']!="")
			{
			    $qry = "SELECT * FROM tbl_users WHERE id = '".$_POST['user_id']."'";	 
				$result = mysqli_query($mysqli,$qry);
				$num_rows = mysqli_num_rows($result);
				$row = mysqli_fetch_assoc($result);
		
		        if ($num_rows > 0)
				{
				    
				    
			         $id=$row['id'];
	    		     $wallet=$row['wallet'];
	    		     $update_amount=$_POST['wallet'];
	    		     $newwallet=$wallet+$update_amount;   
			    
				     $user_edit= "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$id."'";	 
				     $user_res = mysqli_query($mysqli,$user_edit);	


				     $set['JSON_DATA'][]=array('msg'=>'Updated','success'=>'1');

				}
			}
    	header( 'Content-Type: application/json; charset=utf-8' );
	    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
		die();
}

?>