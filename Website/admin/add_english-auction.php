<?php
include('includes/header.php');
include('includes/function.php');
include('language/language.php');
require_once("thumbnail_images.class.php");

if (isset($_POST['submit']) and isset($_GET['add'])) {
    // Remove '#' from color value if present
    $o_color = ltrim(sanitize($_POST['o_color']), '#');

    $item_id = sanitize($_POST['item_id']);
    $items_qry = "SELECT * FROM tbl_items WHERE item_id=$item_id";
    $items_result = mysqli_query($mysqli, $items_qry);
    $items_row = mysqli_fetch_assoc($items_result);
    $item_price = $items_row['price'];

    // Other data fields
    $data = [
        'id' => 1,
        'c_id' => sanitize($_POST['c_id']),
        'winner_type' => sanitize($_POST['winner_type']),
        'city_id' => sanitize($_POST['city_id']),
        'item_id' => $item_id,
        'o_date' => sanitize($_POST['o_date']),
        'o_edate' => sanitize($_POST['o_edate']),
        'o_stime' => sanitize($_POST['o_stime']) . ":00", // Add seconds set to 00
        'o_etime' => sanitize($_POST['o_etime']) . ":00", // Add seconds set to 00
        'o_amount' => sanitize($_POST['o_amount']),
        'o_type' => 7,
        'o_color' => $o_color,
        'o_min' => sanitize($_POST['o_min']),
        'o_max' => $item_price,
        'o_price' => $item_price,
        'o_buy' => sanitize($_POST['o_buy']),
        'o_status' => 1
    ];

    $qry = Insert('tbl_offers', $data);
    $_SESSION['msg'] = "36";
    header("location:english-auction.php");
    exit;
}

if (isset($_GET['o_id'])) {
    $user_qry = "SELECT * FROM tbl_offers WHERE o_id='" . sanitize($_GET['o_id']) . "'";
    $user_result = mysqli_query($mysqli, $user_qry);
    $user_row = mysqli_fetch_assoc($user_result);
}

if (isset($_POST['submit']) and isset($_POST['o_id'])) {
    // Remove '#' from color value if present
    $o_color = ltrim(sanitize($_POST['o_color']), '#');

    $item_id = sanitize($_POST['item_id']);
    $items_qry = "SELECT * FROM tbl_items WHERE item_id=$item_id";
    $items_result = mysqli_query($mysqli, $items_qry);
    $items_row = mysqli_fetch_assoc($items_result);
    $item_price = $items_row['price'];

    // Add other data fields to the array
    $data = [
        'c_id' => sanitize($_POST['c_id']),
        'winner_type' => sanitize($_POST['winner_type']),
        'city_id' => sanitize($_POST['city_id']),
        'item_id' => $item_id,
        'o_date' => sanitize($_POST['o_date']),
        'o_edate' => sanitize($_POST['o_edate']),
        'o_stime' => sanitize($_POST['o_stime']) . ":00", // Add seconds set to 00
        'o_etime' => sanitize($_POST['o_etime']) . ":00", // Add seconds set to 00
        'o_amount' => sanitize($_POST['o_amount']),
        'o_color' => $o_color,
        'o_min' => sanitize($_POST['o_min']),
        'o_max' => $item_price,
        'o_buy' => sanitize($_POST['o_buy']),
        'o_price' => $item_price,
    ];

    // Update the database with the collected data
    $user_edit = Update('tbl_offers', $data, "WHERE o_id = '" . sanitize($_POST['o_id']) . "'");

    if ($user_edit > 0) {
        $_SESSION['msg'] = "35";
        header("Location:add_english-auction.php?o_id=" . sanitize($_POST['o_id']));
        exit;
    }
}

$category_qry1 = "SELECT c_name FROM tbl_cat WHERE c_view = 1 ORDER BY RAND() LIMIT 1";
$category_result1 = mysqli_query($mysqli, $category_qry1);
$category_row1 = mysqli_fetch_assoc($category_result1);
$cat = $category_row1['c_name'];

$settings_qry = "SELECT * FROM tbl_settings";
$settings_result = mysqli_query($mysqli, $settings_qry);
$settings_row = mysqli_fetch_assoc($settings_result);
$currency = $settings_row['currency'];

?>
 	
<style>
    .block_wallpaper img {
      width: 200px;
      height: 200px;
       /* Keeps aspect ratio while fitting image */
    }
    
    .fileupload_img.replaced .database-image {
      display: none; /* Hide existing image when replaced class is present */
    }
</style>

<head>
<title><?php if(isset($_GET['o_id'])){?><?php echo $client_lang['edit']; ?><?php }else{?><?php echo $client_lang['add']; ?><?php }?>&nbsp;<?php echo $client_lang['english_auction']; ?></title>
</head>
<style>
    .safe-mode-title {
        display: block;
        text-align: center;
        font-size: 1em;
        font-weight: bold;
        color: #2c3e50; /* A dark color for better contrast */
        margin-bottom: 15px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }
    .safe-mode-description {
        text-align: center;
        font-size: 1em;
        line-height: 1.5;
        margin: 0 20px 20px;
        color: #34495e; /* Slightly lighter color for the description */
    }
</style>
 <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php if(isset($_GET['o_id'])){?><?php echo $client_lang['edit']; ?><?php }else{?><?php echo $client_lang['add']; ?><?php }?>&nbsp;<?php echo $client_lang['english_auction']; ?></div>
                    <p class="control-label-help"><?php echo $client_lang['english_auction_help']; ?><a href="https://documentation.wowcodes.in/guide/english-auction.html" target="_blank"><?php echo $client_lang['learn_more']; ?> -></a></p>
            </div>
          </div>
          <div class="clearfix"></div>
          <div class="row mrg-top">
            <div class="col-md-12">
               
              <div class="col-md-12 col-sm-12">
                <?php if(isset($_SESSION['msg'])){?> 
               	 <div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                	<?php echo $client_lang[$_SESSION['msg']] ; ?></a> </div>
                <?php unset($_SESSION['msg']);}?>	
              </div>
            </div>
          </div>
          <div class="card-body mrg_bottom"> 
            <form action="" name="addedituser" method="post" class="form form-horizontal" enctype="multipart/form-data" >
            	<input  type="hidden" name="o_id" value="<?php echo $_GET['o_id'];?>" />

              <div class="section">
                <div class="section-body">
                    
                    <strong class="safe-mode-title"><?php echo $client_lang['auction_details']; ?></strong>
                    <center><p class="control-label-help"><?php echo $client_lang['auction_details_help']; ?></p></center>
                <hr>
                   
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['item']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['auction_item_help']; ?></p></label>
                       <div class="col-md-6">
                            <select name="item_id" id="item_id" style="width:280px; height:25px;" class="select2" required>
                              <option value=""><?php echo $client_lang['select_item']; ?></option>
                               <?php
                                   $items_qry = "SELECT * FROM tbl_items WHERE item_status =1 ORDER BY item_id DESC";
                                   $items_result = mysqli_query($mysqli, $items_qry);
                                   while($items_row = mysqli_fetch_assoc($items_result)) {
                                   $selected = ($items_row['item_id'] == $user_row['item_id']) ? 'selected' : '';
                                   echo '<option value="'.$items_row['item_id'].'" '.$selected.'>'.$items_row['o_name'].' ('.$client_lang['worth'].' '.$currency.$items_row['price'].')'.'</option>';
                                }
                                ?>
                            </select>
                       </div>
                       <p><a href="add_item.php?add">Add New Item</a></p>
                  </div>
                  
                  <div class="form-group">
                  <label class="col-md-3 control-label"><?php echo $client_lang['category']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['auction_category_help']; ?></p></label>
                     <div class="col-md-6">
                          <select name="c_id" id="c_id" style="width:280px; height:25px;" class="select2" required>
                            <option value=""><?php echo $client_lang['select_category']; ?></option>
                             <?php
                                 $category_qry = "SELECT c_id, c_name, c_image FROM tbl_cat WHERE c_view =1";
                                 $category_result = mysqli_query($mysqli, $category_qry);
                                 while($category_row = mysqli_fetch_assoc($category_result)) {
                                 $selected = ($category_row['c_id'] == $user_row['c_id']) ? 'selected' : '';
                                 echo '<option value="'.$category_row['c_id'].'" '.$selected.'>'.$category_row['c_name'].'</option>';
                              }
                              ?>
                          </select>
                     </div>
                     <p><a href="add_category.php?add">Add New Category</a></p>
                </div>
                
                <div class="form-group">
                  <label class="col-md-3 control-label"><?php echo $client_lang['city']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['city_help']; ?></p></label>
                     <div class="col-md-6">
                          <select name="city_id" id="city_id" style="width:280px; height:25px;" class="select2" required>
                            <option value=""><?php echo $client_lang['select_city']; ?></option>
                             <?php
                                 $city_qry = "SELECT city_id, city_name, city_image FROM tbl_city";
                                 $city_result = mysqli_query($mysqli, $city_qry);
                                 while($city_row = mysqli_fetch_assoc($city_result)) {
                                 $selected = ($city_row['city_id'] == $user_row['city_id']) ? 'selected' : '';
                                 echo '<option value="'.$city_row['city_id'].'" '.$selected.'>'.$city_row['city_name'].'</option>';
                              }
                              ?>
                          </select>
                     </div>
                     <p><a href="add_city.php?add">Add New City</a></p>
                </div>
                
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['safemode']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['safemode_help']; ?></p></label>
                      <div class="col-md-6">                       
                        <select name="winner_type" title="Safe Mode helps you to stay risk free" id="winner_type" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($user_row['winner_type']=='1'){?>selected<?php }?>><?php echo $client_lang['safemode_deactivate']; ?></option>
                            <option value="0" <?php if($user_row['winner_type']=='0'){?>selected<?php }?>><?php echo $client_lang['safemode_activate']; ?></option>
                          
                        </select>
                      </div>
                  </div>
       
                    <strong class="safe-mode-title"><?php echo $client_lang['auction_validity']; ?></strong>
                    <hr>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['auction_valid_from_date']; ?> :-<br><p class="control-label-help"><?php echo $client_lang['auction_valid_from_date_help']; ?></p></label>
                    <div class="col-md-6">
                      <input type="date" name="o_date" title="When the auction should start? and user can place bid" id="o_date"  value="<?php if(isset($_GET['o_id'])){echo $user_row['o_date'];}?>" class="form-control" required>
                    </div>
                  </div>
                  
                    <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['auction_valid_from_time']; ?> :-<br><p class="control-label-help"><?php echo $client_lang['auction_valid_from_time_help']; ?></p></label>
                    <div class="col-md-6">
                      <input type="time" name="o_stime" title="When the auction should start? and user can place bid" id="o_stime" step="60" value="<?php if(isset($_GET['o_id'])){echo $user_row['o_stime'];}?>" class="form-control" required>
                    </div>
                  </div>
                  
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['auction_valid_to_date']; ?> :-<br><p class="control-label-help"><?php echo $client_lang['auction_valid_to_date_help']; ?></p></label>
                    <div class="col-md-6">
                      <input type="date" name="o_edate" title="When the auction should end? and winner should be announced" id="o_edate" value="<?php if(isset($_GET['o_id'])){echo $user_row['o_edate'];}?>" class="form-control" required>
                    </div>
                  </div>
                  
                    <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['auction_valid_to_time']; ?> :-<br><p class="control-label-help"><?php echo $client_lang['auction_valid_to_time_help']; ?></p></label>
                    <div class="col-md-6">
                      <input type="time" name="o_etime" title="When the auction should end? and winner should be announced" id="o_etime" step="60" value="<?php if(isset($_GET['o_id'])){echo $user_row['o_etime'];}?>" class="form-control" required>
                    </div>
                  </div>
                  
                 
                  <strong class="safe-mode-title"><?php echo $client_lang['bidding_details']; ?></strong>
                    <hr>
                    
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['bidding_fees']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['bidding_fees_help']; ?></p></label>
                    <div class="col-md-6">
                         <div class="input-group">
                      <input type="text" name="o_amount" title="bidders will be charged few coins for each bid they place" id="o_amount" placeholder="eg. 1"  value="<?php if(isset($_GET['o_id'])){echo $user_row['o_amount'];}?>" class="form-control" required>
                     <span class="input-group-addon">Coins</span>
                      </div>
                      </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['starting_bid']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['starting_bid_help']; ?></p></label>
                    <div class="col-md-6">
                         <div class="input-group">
                             <span class="input-group-addon"><?php
                                     $currency_qry = "SELECT currency FROM tbl_settings";
                                     $currency_result = mysqli_query($mysqli, $currency_qry);
                                     $currency_row = mysqli_fetch_assoc($currency_result);
                                     echo $currency_row['currency'];?>
                             </span>
                      <input type="text" name="o_min" id="o_min" title="minimum bid a user can place" placeholder="eg. 1"  value="<?php if(isset($_GET['o_id'])){echo $user_row['o_min'];}?>" class="form-control" required>
                      </div>
                      </div>
                  </div>

                  
                <!--  <strong><center>Auction Box Design Colour (for the app)</center></strong>
                    <hr>
                    
                  <div class="form-group">
                    <label class="col-md-3 control-label">Color:</label>
                    <div class="col-md-6">
                        <input type="color" name="o_color" id="o_color" value="<?php if(isset($_GET['o_id'])){echo '#' . $user_row['o_color'];}?>" class="form-control" style="height: 34px; padding: 6px;">
                    </div>
                </div>
                  
                <hr>
                <strong class="safe-mode-title"><?php echo $client_lang['buynow_title']; ?></strong>
                <p class="safe-mode-description"><?php echo $client_lang['buynow_description']; ?></p>
                  
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['buynow']; ?>:-
                     </label>
                    <div class="col-md-6">
                         <div class="input-group">
                             <span class="input-group-addon"><?php
                                     $currency_qry = "SELECT currency FROM tbl_settings";
                                     $currency_result = mysqli_query($mysqli, $currency_qry);
                                     $currency_row = mysqli_fetch_assoc($currency_result);
                                     echo $currency_row['currency'];?>
                             </span>
                      <input type="text" name="o_buy" id="o_buy" placeholder="eg. 79" title="the price of the item if user want's to purchase the item directly" value="<?php if(isset($_GET['o_id'])){echo $user_row['o_buy'];}?>" class="form-control">
                      </div>
                      </div>
                  </div>
                  -->
                  
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="submit" class="btn btn-primary"><?php if(isset($_GET['o_id'])){?><?php echo $client_lang['update']; ?><?php }else{?><?php echo $client_lang['add']; ?><?php }?>&nbsp;<?php echo $client_lang['auction']; ?></button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
   

<?php include('includes/footer.php');?>                  