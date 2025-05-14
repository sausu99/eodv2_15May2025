<?php
include('includes/header.php');
include("includes/session_check.php");
include('includes/function.php');
include('language/language.php'); 

if(isset($_SESSION['user_id']))
	{
			 
		$qry="select * from tbl_users where id='".sanitize($_SESSION['user_id'])."'";
		 
		$result=mysqli_query($mysqli,$qry);
		$session_row=mysqli_fetch_assoc($result);

	}

// Check if 'o_id' is set in the URL parameters
if(isset($_GET['o_id']))
{
    // Query to fetch order details based on the provided 'o_id'
    $user_qry = "SELECT * FROM tbl_order 
                left join tbl_users on tbl_users.id = tbl_order.u_id
                LEFT JOIN tbl_items ON tbl_items.item_id = tbl_order.offer_id 
                where tbl_order.o_id='".sanitize($_GET['o_id'])."' AND tbl_order.u_id='".sanitize($_SESSION['user_id'])."'";  
    
    // Execute the query
    $user_result = mysqli_query($mysqli, $user_qry);
    
    if (mysqli_num_rows($user_result) == 0) {
        $title = $client_lang['not_allowed'];
        $description = '';
        $image = 'no_person.gif';
        include("nodata.php");
        exit;
    }
    
    // Fetch the result as an associative array
    $user_row = mysqli_fetch_assoc($user_result);
}

// Function to get currency from settings
function getCurrencyFromSettings($mysqli) {
    // Query to fetch the currency from tbl_settings
    $query = "SELECT currency FROM tbl_settings";
    
    // Execute the query
    $result = mysqli_query($mysqli, $query);

    if ($result) {
        // Check if a row is returned
        if (mysqli_num_rows($result) > 0) {
            // Fetch the currency value from the result
            $row = mysqli_fetch_assoc($result);
            return $row['currency'];
        }
    }

    // Return a default currency value or handle errors as needed
    return '$'; // Default currency value (you can change this)
}
?>
<!-- Adding inline CSS styles for better separation -->
<style>
    /* CSS styles for the table */
    #table {
        width: 100%;
    }
    #table, #th, #td {
        border: 1px solid #dfd7ca;
        border-collapse: collapse;
    }
    #th, #td {
        padding: 15px;
        text-align: left;
    }
    #table#t01 #tr:nth-child(even) {
        background-color: #eee;
    }
    #table#t01 #tr:nth-child(odd) {
        background-color: #fff;
    }
    #table#t01 #th {
        background-color: white;
        color: black;
    }

    /* Additional CSS styles */
    table, th, td {
        border: 0px solid #dfd7ca;
        border-collapse: collapse;
        margin-bottom: 10px;
    }
    .line {
        width: 40%;
        margin-left: 600px;
    }
</style>

<!-- Adding a jQuery library -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<title><?php echo $client_lang['viewOrder']; ?></title>
<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $client_lang['viewOrder']; ?></div>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])){?> 
                            <div class="alert alert-success alert-dismissible" role="alert"> 
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                                <?php echo $client_lang[$_SESSION['msg']] ; ?>
                            </div>
                        <?php unset($_SESSION['msg']);}?>	
                    </div>
                </div>
            </div>
            <div class="card-body mrg_bottom"> 
                <form action="" name="addedituser" method="post" class="form form-horizontal" enctype="multipart/form-data">
                    <input type="hidden" name="o_id" value="<?php echo $_GET['o_id'];?>" />
                    <!-- Customer Details Section -->
                    <div class="col-md-4">
                        <center><h5><b><?php echo $client_lang['viewOrderCDetails']; ?></b></h5></center>
                        <hr />
                        <div class="form-group">
                            <label class="col-md-12 control-label"><b><?php echo $client_lang['viewOrderCName']; ?> :- </b><?php if(isset($_GET['o_id'])){echo $user_row['name'];}?></label>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12 control-label"><b><?php echo $client_lang['viewOrderCEmail']; ?> :- </b><?php if(isset($_GET['o_id'])){echo $user_row['email'];}?></label>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12 control-label"><b><?php echo $client_lang['viewOrderCPhone']; ?> :- </b><?php if(isset($_GET['o_id'])){echo $user_row['phone'];}?></label>
                        </div>
                    </div>
                    <!-- Order Details Section -->
                    <div class="col-md-4">
                        <center><h5><b><?php echo $client_lang['viewOrderODetails']; ?></b></h5></center>
                        <hr />
                        <div class="form-group">
                            <label class="col-md-12 control-label"><b><?php echo $client_lang['viewOrderOId']; ?> :- # </b><?php if(isset($_GET['o_id'])){echo $user_row['o_id'];}?></label>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12 control-label"><b><?php echo $client_lang['viewOrderODate']; ?> </b><?php if(isset($_GET['o_id'])){echo $user_row['order_date'];}?></label>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12 control-label"><b><?php echo $client_lang['viewOrderOAddress']; ?> :- </b><?php if(isset($_GET['o_id'])){echo $user_row['o_address'];}?></label>
                        </div>
                    </div>
                    <!-- Order Item Details Section -->
                    <div class="col-md-12 mrg-top">
                        <table id="t01" class="table table-striped table-bordered table-hover">
                            <tr id='tr'>
                                <th id='th'><?php echo $client_lang['viewOrderOName']; ?></th>
                                <th id='th'></th>
                                <th id='th'><?php echo $client_lang['viewOrderOPay']; ?></th>
                            </tr>
                            <tr>
                                <td><?php if(isset($_GET['o_id'])){echo $user_row['o_name'];}?></td>
                                <td><span class="category_img"><img src="../seller/images/<?php echo $user_row['o_image'];?>" /></span></td>
                                <td>
                                    <?php 
                                    if(isset($_GET['o_id'])){
                                        // Fetch the currency from tbl_settings
                                        $currency = getCurrencyFromSettings($mysqli);
                                        echo $currency . $user_row['pay_amount'];
                                    }
                                    ?>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <!-- Additional Payment Details Section -->
                    <div class="col-md-12 mrg-top">
                        <table style='border: 1px solid #ddd; border-collapse: collapse;' class="table line">
                            <tr>
                                <td><b><?php echo $client_lang['viewOrderOPrice']; ?> :</b></td>
                                <td>
                                    <?php 
                                    if (isset($_GET['o_id'])) {
                                        // Fetch the currency from tbl_settings
                                        $currency = getCurrencyFromSettings($mysqli);
                                        // Check if 'o_price' is set, if it's 0 or null, show '0'
                                        echo $currency . (!empty($user_row['o_price']) ? $user_row['o_price'] : '0') . '/-';
                                    }
                                    ?>
                                </td>
                            </tr>
                            <tr>
                                <td><b><?php echo $client_lang['viewOrderODis']; ?> :</b></td>
                                <td>
                                    <?php 
                                    if(isset($_GET['o_id'])){
                                        // Fetch the currency from tbl_settings
                                        $currency = getCurrencyFromSettings($mysqli);
                                        // Calculate the difference between o_price and pay_amount
                                        $difference = $user_row['o_price'] - $user_row['pay_amount'];
                                        // Display the result
                                        echo $currency . $difference . '/-';
                                    }
                                    ?>
                                </td>
                            </tr>
                            <tr>
                                <td style="color: #337ab7;"><b><?php echo $client_lang['viewOrderOPay']; ?> :</b></td>
                                <td style="color: #337ab7;">
                                    <?php 
                                    if(isset($_GET['o_id'])){
                                        // Fetch the currency from tbl_settings
                                        $currency = getCurrencyFromSettings($mysqli);
                                        echo '<b>' . $currency . $user_row['pay_amount'] . '/-</b>';
                                    }
                                    ?>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<?php include('includes/footer.php');?>
