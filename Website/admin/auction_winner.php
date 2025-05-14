<?php 
include('includes/header.php');
include('includes/function.php');
include('language/language.php');

$auction_id = sanitize($_GET['o_id']);
	
	if(isset($auction_id))
	{
		    $user_qry="SELECT * FROM tbl_offers
		               LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
		  	           where tbl_offers.o_id='$auction_id' ";  
		  	           
			$user_result=mysqli_query($mysqli,$user_qry);
			$user_row=mysqli_fetch_assoc($user_result);
	}
	
$querytime = "SELECT currency FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
$currency = $rowtime['currency'];
?>

<head>
<title><?php echo $client_lang['view_winner']; ?></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
</head>

<style>

#table {
  width:100%;
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


table {
  width:100%;
}
table, th, td {
  border: 0px solid #dfd7ca;
  border-collapse: collapse;
      margin-bottom: 10px;
}
th, td {
  padding: 0px;
  text-align: left;
}


.line {
  width:40%;
  
  margin-left: 600px;
}

 th, td {
  border: 0px solid #dfd7ca;
  border-collapse: collapse;
      margin-bottom: 10px;
}




</style>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>



<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title">Auction Winner!</div>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])) { ?> 
                            <div class="alert alert-success alert-dismissible" role="alert"> 
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                                <?php echo $client_lang[$_SESSION['msg']]; ?> 
                            </div>
                            <?php unset($_SESSION['msg']); 
                        } ?>    
                    </div>
                </div>
            </div>
            <div class="card-body mrg_bottom"> 
                <form action="" name="addedituser" method="post" class="form form-horizontal" enctype="multipart/form-data">
                    <input type="hidden" name="o_id" value="<?php echo $auction_id ?>" />
                    <?php
                    date_default_timezone_set("Asia/Calcutta");
                    $time = date('H:i:s');
                    $date1 = date('Y-m-d');
                    $datetime = date('Y-m-d H:i:s');

                    $query = "SELECT * FROM tbl_offers 
                              LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                              WHERE o_date <= '".$date1."' AND o_status = 1 AND tbl_offers.o_id='$auction_id'";

                    $sql = mysqli_query($mysqli, $query) or die(mysqli_error($mysqli));

                    while($data = mysqli_fetch_assoc($sql))
                    {
                        $o_date = $data['o_date'];
                        $o_stime = $data['o_stime'];
                        $o_edate = $data['o_edate'];
                        $o_etime = $data['o_etime'];

                        $start = $o_date . " " . $o_stime;
                        $end = $o_edate . " " . $o_etime;

                            if($data['o_type'] == 1)
                            {
                                $query1 = "SELECT *, COUNT(*) AS num1 FROM tbl_bid 
                                           LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
                                           WHERE tbl_bid.o_id='".$data['o_id']."'
                                           GROUP BY bd_value HAVING num1 = 1  
                                           ORDER BY tbl_bid.bd_value ASC 
                                           LIMIT 0,1";
                            }
                            else if($data['o_type'] == 2)
                            {
                                $query1 = "SELECT *, COUNT(*) AS num1 FROM tbl_bid 
                                           LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
                                           WHERE tbl_bid.o_id='".$data['o_id']."'
                                           GROUP BY bd_value HAVING num1 = 1  
                                           ORDER BY tbl_bid.bd_value DESC 
                                           LIMIT 0,1";
                            }
                            else if($data['o_type'] == 7 || $data['o_type'] == 8)
                            {
                                $query1 = "SELECT * FROM tbl_bid 
                                           LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id
                                           WHERE tbl_bid.o_id='".$data['o_id']."'
                                           ORDER BY tbl_bid.bd_value DESC 
                                           LIMIT 0,1";
                            }

                            $result1 = mysqli_query($mysqli, $query1) or die(mysqli_error($mysqli));
                            $row1 = mysqli_fetch_assoc($result1);
                        
                        ?>
                        <div class="col-md-4">
                            
                            <center><h5><b>Winner Details</b></h5></center>
                            <hr />
                            <div class="form-group">
                                <label class="col-md-12 control-label"><b>Name :- </b><?php echo $row1['name'] ?? 'Not Found'; ?></label>
                            </div>
                            <div class="form-group">
                                <label class="col-md-12 control-label"><b>Email :- </b><?php echo $row1['email'] ?? 'Not Found'; ?></label>
                            </div>
                            <div class="form-group">
                                <label class="col-md-12 control-label"><b>Phone :- </b><?php echo $row1['phone'] ?? 'Not Found'; ?></label>
                            </div>
                            
                        </div>
                        <div class="col-md-4"></div>
                        <div class="col-md-4"></div>
                        <table id='table'>
                            <div class="col-md-12 mrg-top">
                                <table id="t01" class="table table-striped table-bordered table-hover">
                                    <tr id='tr'>
                                        <th id='th'></th>
                                        <th id='th'>Item</th>
                                        <th id='th'>Bid Date</th>
                                        <th id='th'>Bid Value</th>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="category_img"><img src="<?php echo file_exists('../seller/images/'.$data['o_image']) ? '../seller/images/'.$data['o_image'] : 'placeholder.jpg'; ?>" class="img-fluid img-thumbnail" alt="<?php echo $data['o_name']; ?>" style="width: 100px; height: auto;"></span>
                                        </td>
                                        <td>
                                            <?php echo $data['o_name']; ?>
                                        </td>
                                        <td>
                                            <?php echo $row1['bd_date'] ?? 'Not Found'; ?>
                                        </td>
                                        <td>
                                            <?php echo $currency.$row1['bd_value'] ?? 'Not Found'; ?>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </table>
                        <?php
                    }
                    ?>
                </form>
            </div>
        </div>
    </div>
</div>

<?php include('includes/footer.php'); ?>