<?php 
include("includes/header.php");
include("includes/session_check.php");
require("includes/function.php");
require("language/language.php");

if(isset($_SESSION['seller_id']))
	{
			 
		$qry="select * from tbl_vendor where id='".$_SESSION['seller_id']."'";
		 
		$result=mysqli_query($mysqli,$qry);
		$row=mysqli_fetch_assoc($result);

	}
	 
if (isset($_POST['submit'])) {
    if (isset($_FILES['image']['name']) && $_FILES['image']['name'] != "") {
        // Check if the uploaded file is an image
        $image_info = getimagesize($_FILES['image']['tmp_name']);
        if ($image_info !== false) {
            $img_res = mysqli_query($mysqli, 'SELECT * FROM tbl_vendor WHERE id=' . $_SESSION['seller_id'] . '');
            $img_res_row = mysqli_fetch_assoc($img_res);

            if ($img_res_row['image'] != "") {
                unlink('images/' . $img_res_row['image']);
            }

            $image = rand(0, 99999).$_POST['email'].'.png';
            $pic1 = $_FILES['image']['tmp_name'];
            $tpath1 = 'images/' . $image;

            copy($pic1, $tpath1);

            $data = array(
                'username' => $_POST['username'],
                'password' => $_POST['password'],
                'email' => $_POST['email'],
                'image' => $image,
                'about' => $_POST['about']
            );

            $channel_edit = Update('tbl_vendor', $data, "WHERE id = '" . $_SESSION['seller_id'] . "'");
        } else {
            // Handle the case when the uploaded file is not an image
            echo "Uploaded file is not a valid image.";
        }
    } else {
        $data = array(
            'username' => $_POST['username'],
            'password' => $_POST['password'],
            'email' => $_POST['email'],
            'about' => $_POST['about']
        );

        $channel_edit = Update('tbl_vendor', $data, "WHERE id = '" . $_SESSION['seller_id'] . "'");
    }

    if ($channel_edit > 0){
        $_SESSION['msg'] = "11";
        header("Location: profile.php");
        exit;
    } 
}


?>
 
	 <div class="row">
      <div class="col-md-12">
        <div class="card">
		  <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title">Your Profile</div>
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
          	  
            <form action="" name="editprofile" method="post" class="form form-horizontal" enctype="multipart/form-data">
              <div class="section">
                <div class="section-body">
                    
                 
                  <div class="form-group">
                    <label class="col-md-3 control-label">Your Name:-</label>
                    <div class="col-md-6">
                      <input type="text" name="email" id="email" value="<?php echo $row['email'];?>" class="form-control" required autocomplete="off">
                    </div>
                  </div>      
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label">Email:-</label>
                    <div class="col-md-6">
                      <input type="text" name="username" id="username" value="<?php echo $row['username'];?>" class="form-control" required autocomplete="off">
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label">Password :-</label>
                    <div class="col-md-6">
                      <input type="password" name="password" id="password" value="<?php echo $row['password'];?>" class="form-control" required autocomplete="off">
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label">About You:-</label>
                    <div class="col-md-6">
                      <input type="textarea" name="about" id="about" value="<?php echo $row['about'];?>" class="form-control" required autocomplete="off">
                    </div>
                  </div>
                  
                  <div class="form-group">
                        <label class="col-md-3 control-label">Profile Image :-
                            <p class="control-label-help">(Recommended Image Size:- 256 * 256)</p>
                        </label>
                        <div class="col-md-6">
                            <div class="fileupload_block">
                                <input type="file" name="image" value="fileupload" id="fileupload" onchange="previewImage(this)">
                                <div class="fileupload_img">
                                        <img src="../seller/images/<?php echo $row['image'];?>" alt="category image" class="database-image">
                                    <img id="preview_img" src="#" alt="Preview image" style="display: none;">
                                </div>
                            </div>
                        </div>
                    </div><br>
                  
                  
                   
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="submit" class="btn btn-primary">Save</button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

        
<?php include("includes/footer.php");?>       
