<?php //error_reporting(0);


function selectAndInsertWinners($mysqli, $o_id) {
    // Fetch prizes for the lottery
    $prizes_qry = "SELECT * FROM tbl_prizes 
                   LEFT JOIN tbl_items ON tbl_items.item_id = tbl_prizes.item_id 
                   WHERE tbl_prizes.o_id = ? 
                   ORDER BY rank_start";
    $stmt = $mysqli->prepare($prizes_qry);
    $stmt->bind_param("i", $o_id);
    $stmt->execute();
    $prizes_result = $stmt->get_result();
    
    // Fetch all tickets for the lottery
    $ticket_qry = "SELECT * FROM tbl_ticket WHERE o_id = ? ORDER BY RAND()";
    $stmt = $mysqli->prepare($ticket_qry);
    $stmt->bind_param("i", $o_id);
    $stmt->execute();
    $ticket_result = $stmt->get_result();

    // Check if there are tickets available
    if ($ticket_result->num_rows === 0) {
        // No tickets available for this lottery
        return;
    }
    
    // Store all tickets in an array
    $tickets = [];
    while ($ticket = $ticket_result->fetch_assoc()) {
        $tickets[] = $ticket;
    }

    // Initialize the winner rank variable
    $winner_rank = 1;

    // Loop through the prizes and assign winners
    while ($prize = $prizes_result->fetch_assoc()) {
        // Select random tickets based on prize rank range
        $num_winners = $prize['rank_end'] - $prize['rank_start'] + 1;

        // Check if tickets array is not empty before selecting winners
        if (count($tickets) > 0) {
            $selected_winners = array_rand($tickets, min($num_winners, count($tickets))); // Select random winners
            
            // If only one winner is selected, ensure it's an array
            if (!is_array($selected_winners)) {
                $selected_winners = [$selected_winners];
            }

            // Insert each selected winner into tbl_winners
            foreach ($selected_winners as $winner_index) {
                $ticket = $tickets[$winner_index];
                $winning_value = "{$ticket['ball_1']} {$ticket['ball_2']} {$ticket['ball_3']} {$ticket['ball_4']} {$ticket['ball_5']} {$ticket['ball_6']} {$ticket['ball_7']} {$ticket['ball_8']}";

                // Fetch user details for the ticket
                $ticket_qry = "SELECT tbl_ticket.u_id, tbl_users.name, tbl_ticket.ticket_id 
                               FROM tbl_ticket 
                               LEFT JOIN tbl_users ON tbl_users.id = tbl_ticket.u_id 
                               WHERE tbl_ticket.o_id = ? AND tbl_ticket.ticket_id = ?";
                $stmt = $mysqli->prepare($ticket_qry);
                $stmt->bind_param("ii", $o_id, $ticket['ticket_id']);
                $stmt->execute();
                $user_ticket_result = $stmt->get_result();
                $user_ticket = $user_ticket_result->fetch_assoc();

                // Insert the winner into the tbl_winners table with a unique winner rank
                $winner_insert_qry = "INSERT INTO tbl_winners (o_id, u_id, winner_rank, winner_name, participation_id, winning_value) 
                                      VALUES (?, ?, ?, ?, ?, ?)";
                $stmt = $mysqli->prepare($winner_insert_qry);
                $stmt->bind_param("iiisss", $o_id, $user_ticket['u_id'], $winner_rank, $user_ticket['name'], $user_ticket['ticket_id'], $winning_value);
                $stmt->execute();

                // Increment the winner rank for the next insertion
                $winner_rank = ($winner_rank + 1);
 
                // Remove the selected ticket from the array to avoid re-selection
                unset($tickets[$winner_index]);
            }
        }
    }

    // Update tbl_offers.winner_name with "Winner Announced"
    $offer_update_qry = "UPDATE tbl_offers SET winner_name = 'Winner Announced' WHERE o_id = ?";
    $stmt = $mysqli->prepare($offer_update_qry);
    $stmt->bind_param("i", $o_id);
    $stmt->execute();
}



// Use prepared statements to prevent SQL injection
$settingsQry = $mysqli->prepare("SELECT demo_access FROM tbl_settings");
$settingsQry->execute();
$settingsResult = $settingsQry->get_result();
$settingsRow = $settingsResult->fetch_assoc();
$demo_mode = $settingsRow['demo_access'];


// Check if user is already logged in with persistent cookie
if (isset($_COOKIE['remember_me_token']) && !isset($_SESSION['user_id'])) {
    $token = $_COOKIE['remember_me_token'];

    // Use prepared statements to prevent SQL injection
    $qry = $mysqli->prepare("SELECT * FROM tbl_users WHERE remember_me_token = ?");
    $qry->bind_param("s", $token);
    $qry->execute();
    $result = $qry->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();

        // Set session variables
        $_SESSION['user_id'] = $row['id'];
        $_SESSION['user_email'] = $row['email'];

        // No redirection, just set the session variables and stay on the same page
    }
}

function get_vendor_info($vendor_id, $field_name)
{
    global $mysqli;

    $qry_user = "SELECT * FROM tbl_users WHERE id='" . intval($vendor_id) . "'";
    $query1 = mysqli_query($mysqli, $qry_user);

    if (!$query1) {
        return "";
    }

    $row_user = mysqli_fetch_array($query1);
    return $row_user[$field_name] ?? "";
}

function sanitize($input) {
    return htmlspecialchars(strip_tags(trim($input ?? '')), ENT_QUOTES, 'UTF-8');
}


// Function to validate image files
function isValidImage($file) {
    $allowed_mime_types = ['image/jpeg', 'image/png', 'image/gif'];
    $file_mime_type = mime_content_type($file['tmp_name']);
    return in_array($file_mime_type, $allowed_mime_types);
}

// Create Thumbnail
function createThumbnail($src, $dest, $desired_size) {
    $image_info = getimagesize($src);
    $image_type = $image_info[2];

    switch ($image_type) {
        case IMAGETYPE_JPEG:
            $source_image = imagecreatefromjpeg($src);
            break;
        case IMAGETYPE_PNG:
            $source_image = imagecreatefrompng($src);
            break;
        case IMAGETYPE_GIF:
            $source_image = imagecreatefromgif($src);
            break;
        default:
            return false;
    }

    $width = imagesx($source_image);
    $height = imagesy($source_image);
    $scaling_factor = $desired_size / max($width, $height);
    $new_width = (int)($width * $scaling_factor);
    $new_height = (int)($height * $scaling_factor);
    $virtual_image = imagecreatetruecolor($desired_size, $desired_size);

    if ($image_type == IMAGETYPE_PNG || $image_type == IMAGETYPE_GIF) {
        imagealphablending($virtual_image, false);
        imagesavealpha($virtual_image, true);
        $transparent = imagecolorallocatealpha($virtual_image, 255, 255, 255, 127);
        imagefilledrectangle($virtual_image, 0, 0, $desired_size, $desired_size, $transparent);
    } else {
        $white = imagecolorallocate($virtual_image, 255, 255, 255);
        imagefilledrectangle($virtual_image, 0, 0, $desired_size, $desired_size, $white);
    }

    $x_offset = ($desired_size - $new_width) / 2;
    $y_offset = ($desired_size - $new_height) / 2;
    imagecopyresampled($virtual_image, $source_image, $x_offset, $y_offset, 0, 0, $new_width, $new_height, $width, $height);

    switch ($image_type) {
        case IMAGETYPE_JPEG:
            imagejpeg($virtual_image, $dest, 50);
            break;
        case IMAGETYPE_PNG:
            imagepng($virtual_image, $dest);
            break;
        case IMAGETYPE_GIF:
            imagegif($virtual_image, $dest);
            break;
    }

    imagedestroy($source_image);
    imagedestroy($virtual_image);
}

#User Login
function adminUser($id, $password){
    
    global $id,$mysqli;

    $sql = "SELECT * FROM tbl_users where id = '".$id."' and password = '".password_hash($password, PASSWORD_BCRYPT)."'";       
    $result = mysqli_query($mysqli,$sql);
    $num_rows = mysqli_num_rows($result);
     
    if ($num_rows > 0){
        while ($row = mysqli_fetch_array($result)){
            echo $_SESSION['user_id'] = $row['id'];
                        echo $_SESSION['user_email'] = $row['email'];
                                      
        return true; 
        }
    }
    
}


# Insert Data 
function Insert($table, $data){

    global $mysqli;
    //print_r($data);

    $fields = array_keys( $data );  
    $values = array_map( array($mysqli, 'real_escape_string'), array_values( $data ) );
    
   //echo "INSERT INTO $table(".implode(",",$fields).") VALUES ('".implode("','", $values )."');";
   //exit;  
    mysqli_query($mysqli, "INSERT INTO $table(".implode(",",$fields).") VALUES ('".implode("','", $values )."');") or die( mysqli_error($mysqli) );

}

// Update Data, Where clause is left optional
function Update($table_name, $form_data, $where_clause='')
{   
    global $mysqli;
    // check for optional where clause
    $whereSQL = '';
    if(!empty($where_clause))
    {
        // check to see if the 'where' keyword exists
        if(substr(strtoupper(trim($where_clause)), 0, 5) != 'WHERE')
        {
            // not found, add key word
            $whereSQL = " WHERE ".$where_clause;
        } else
        {
            $whereSQL = " ".trim($where_clause);
        }
    }
    // start the actual SQL statement
    $sql = "UPDATE ".$table_name." SET ";

    // loop and build the column /
    $sets = array();
    foreach($form_data as $column => $value)
    {
         $sets[] = "`".$column."` = '".$value."'";
    }
    $sql .= implode(', ', $sets);

    // append the where statement
    $sql .= $whereSQL;
         
    // run and return the query result
    return mysqli_query($mysqli,$sql);
}

 
//Delete Data, the where clause is left optional incase the user wants to delete every row!
function Delete($table_name, $where_clause='')
{   
    global $mysqli;
    // check for optional where clause
    $whereSQL = '';
    if(!empty($where_clause))
    {
        // check to see if the 'where' keyword exists
        if(substr(strtoupper(trim($where_clause)), 0, 5) != 'WHERE')
        {
            // not found, add keyword
            $whereSQL = " WHERE ".$where_clause;
        } else
        {
            $whereSQL = " ".trim($where_clause);
        }
    }
    // build the query
    $sql = "DELETE FROM ".$table_name.$whereSQL;
     
    // run and return the query result resource
    return mysqli_query($mysqli,$sql);
}  
 
//GCM function
function Send_GCM_msg($registration_id,$data)
{
    $data1['data']=$data;
 
    $url = 'https://fcm.googleapis.com/fcm/send';
  
    $registatoin_ids = array($registration_id);
     // $message = array($data);
   
         $fields = array(
             'registration_ids' => $registatoin_ids,
             'data' => $data1,
         );
  
         $headers = array(
             'Authorization: key='.APP_GCM_KEY.'',
             'Content-Type: application/json'
         );
         // Open connection
         $ch = curl_init();
  
         // Set the url, number of POST vars, POST data
         curl_setopt($ch, CURLOPT_URL, $url);
  
         curl_setopt($ch, CURLOPT_POST, true);
         curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
         curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
  
         // Disabling SSL Certificate support temporarly
         curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
  
         curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
  
         // Execute post
         $result = curl_exec($ch);
         if ($result === FALSE) {
             die('Curl failed: ' . curl_error($ch));
         }
  
         // Close connection
         curl_close($ch);
       //echo $result;exit;
}


//Image compress
function compress_image($source_url, $destination_url, $quality) 
{

    $info = getimagesize($source_url);

        if ($info['mime'] == 'image/jpeg')
              $image = imagecreatefromjpeg($source_url);

        elseif ($info['mime'] == 'image/gif')
              $image = imagecreatefromgif($source_url);

      elseif ($info['mime'] == 'image/png')
              $image = imagecreatefrompng($source_url);

        imagejpeg($image, $destination_url, $quality);
    return $destination_url;
}

//Create Thumb Image
function create_thumb_image($target_folder ='',$thumb_folder = '', $thumb_width = '',$thumb_height = '')
 {  
     //folder path setup
         $target_path = $target_folder;
         $thumb_path = $thumb_folder;  
          

         $thumbnail = $thumb_path;
         $upload_image = $target_path;

            list($width,$height) = getimagesize($upload_image);
            $thumb_create = imagecreatetruecolor($thumb_width,$thumb_height);
            switch($file_ext){
                case 'jpg':
                    $source = imagecreatefromjpeg($upload_image);
                    break;
                case 'jpeg':
                    $source = imagecreatefromjpeg($upload_image);
                    break;
                case 'png':
                    $source = imagecreatefrompng($upload_image);
                    break;
                case 'gif':
                    $source = imagecreatefromgif($upload_image);
                     break;
                default:
                    $source = imagecreatefromjpeg($upload_image);
            }
       imagecopyresized($thumb_create, $source, 0, 0, 0, 0, $thumb_width, $thumb_height, $width,$height);
            switch($file_ext){
                case 'jpg' || 'jpeg':
                    imagejpeg($thumb_create,$thumbnail,80);
                    break;
                case 'png':
                    imagepng($thumb_create,$thumbnail,80);
                    break;
                case 'gif':
                    imagegif($thumb_create,$thumbnail,80);
                     break;
                default:
                    imagejpeg($thumb_create,$thumbnail,80);
            }
 }
?>