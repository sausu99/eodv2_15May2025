<?php 

include('includes/function.php');
include('language/language.php');
include("includes/connection.php");

// Fetch all distinct o_type values from tbl_offers
$querytime = "SELECT DISTINCT o_type FROM tbl_offers";
$resulttime = mysqli_query($mysqli, $querytime);
$o_types = [];
while ($row = mysqli_fetch_assoc($resulttime)) {
    $o_types[] = $row['o_type'];
}
?>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $client_lang['howTitle']; ?></title>
    <link rel="stylesheet" href="style.css">
    <style>
    body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 20px;
}


section {
    margin-bottom: 20px;
}

h2 {
    color: #333;
}

/* Style the header with a grey background and some padding */
.header {
  overflow: hidden;
  background-color: #f1f1f1;
  padding: 20px 10px;
}

/* Style the header links */
.header a {
  float: left;
  color: black;
  text-align: center;
  padding: 12px;
  text-decoration: none;
  font-size: 18px;
  line-height: 25px;
  border-radius: 4px;
}

/* Style the logo link (notice that we set the same value of line-height and font-size to prevent the header to increase when the font gets bigger */
.header a.logo {
  font-size: 25px;
  font-weight: bold;
}

/* Change the background color on mouse-over */
.header a:hover {
  background-color: #ddd;
  color: black;
}

/* Style the active/current link*/
.header a.active {
  background-color: dodgerblue;
  color: white;
}

/* Float the link section to the right */
.header-right {
  float: right;
}

/* Add media queries for responsiveness - when the screen is 500px wide or less, stack the links on top of each other */
@media screen and (max-width: 500px) {
  .header a {
    float: none;
    display: block;
    text-align: left;
  }
  .header-right {
    float: none;
  }
}

.section-wrapper {
        max-width: 800px;
        margin: 0 auto;
        padding: 20px;
    }

    .section {
        margin-bottom: 40px;
        padding: 20px;
        border-radius: 8px;
        background-color: #f9f9f9;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .section h2 {
        color: #333;
        margin-bottom: 10px;
    }

    .section p {
        margin-bottom: 15px;
    }

    .section ul {
        list-style-type: disc;
        margin-left: 20px;
    }

    .cta-button {
        display: inline-block;
        padding: 10px 20px;
        background-color: dodgerblue;
        color: #fff;
        text-decoration: none;
        border-radius: 4px;
        transition: background-color 0.3s ease;
    }

    .cta-button:hover {
        background-color: #007bff;
    }

</style>
</head>


<body>
   <header class="header">
   
  <a href="index.php" class="logo"><?php echo $client_lang['howTitle']; ?></a>
  <div class="header-right">
      <?php if (in_array(1, $o_types)) { ?>
    <a href="#lowest-unique-bid" ><?php echo $auction_lang['lowestUnique']; ?></a>
      <?php } ?>
      <?php if (in_array(2, $o_types)) { ?>
    <a href="#highest-unique-bid"><?php echo $auction_lang['highestUnique']; ?></a>
        <?php } ?>
        <?php if (in_array(8, $o_types)) { ?>
    <a href="#penny-auction"><?php echo $auction_lang['pennyAuction']; ?></a>
    <?php } ?>
    <?php if (in_array(7, $o_types)) { ?>
    <a href="#english-auction"><?php echo $auction_lang['englishAuction']; ?></a>
    <?php } ?>
    <?php if (in_array(4, $o_types) || in_array(5, $o_types)) { ?>
    <a href="#lottery"><?php echo $auction_lang['lottery']; ?></a>
    <?php } ?>
    <a href="#invest"><?php echo $invest_lang['invest']; ?></a>
</div>
  </header>

    <main>
    <div class="section-wrapper">
        <?php if (in_array(1, $o_types) || in_array(2, $o_types) || in_array(7, $o_types) || in_array(8, $o_types)) { ?>
        <center><img src="/images/static/ic_auction.png"></center>
        <?php } ?>
        <?php if (in_array(1, $o_types)) { ?>
    <section id="lowest-unique-bid">
        <h2><?php echo $client_lang['howLub1']; ?></h2>
        <p><?php echo $client_lang['howLub2']; ?></p>
        <ul>
            <li><?php echo $client_lang['howLub3']; ?></li>
            <li><?php echo $client_lang['howLub4']; ?>
                <br> <?php echo $client_lang['howLub5']; ?>
            </li>
            <li><?php echo $client_lang['howLub6']; ?></li>
            <li><?php echo $client_lang['howLub7']; ?></li>
            <li><?php echo $client_lang['howLub8']; ?></li>
            <li><?php echo $client_lang['howLub9']; ?></li>
            <li><?php echo $client_lang['howLub10']; ?>
                <strong title="<?php echo $client_lang['howLub16']; ?>"><?php echo $client_lang['howLub11']; ?></strong> <?php echo $client_lang['howLub12']; ?> 
                <strong title="<?php echo $client_lang['howLub15']; ?>"><?php echo $client_lang['howLub13']; ?></strong> <?php echo $client_lang['howLub14']; ?>
            </li>
        </ul>
    </section>
    <?php } ?>
    <hr>

        <?php if (in_array(2, $o_types)) { ?>
       <section id="highest-unique-bid">
            <h2><?php echo $client_lang['howHub1']; ?></h2>
            <p><?php echo $client_lang['howHub2']; ?></p>
            <ul>
                <li><?php echo $client_lang['howHub3']; ?></li>
                <li><?php echo $client_lang['howHub4']; ?>
                    <br> <?php echo $client_lang['howHub5']; ?>
                </li>
                <li><?php echo $client_lang['howHub6']; ?></li>
                <li><?php echo $client_lang['howHub7']; ?></li>
                <li><?php echo $client_lang['howHub8']; ?></li>
                <li><?php echo $client_lang['howHub9']; ?></li>
                <li><?php echo $client_lang['howHub10']; ?>
                    <strong title="<?php echo $client_lang['howHub16']; ?>"><?php echo $client_lang['howHub11']; ?></strong> <?php echo $client_lang['howHub12']; ?> 
                    <strong title="<?php echo $client_lang['howHub15']; ?>"><?php echo $client_lang['howHub13']; ?></strong> <?php echo $client_lang['howHub14']; ?>
                </li>
            </ul>
        </section>
        <?php } ?>
        
    <hr>
    <?php if (in_array(8, $o_types)) { ?>
       <section id="penny-auction">
    <h2><?php echo $client_lang['howPenny1']; ?></h2>
    <p><?php echo $client_lang['howPenny2']; ?></p>
    <p><?php echo $client_lang['howPenny3']; ?></p>
    <p><?php echo $client_lang['howPenny4']; ?></p>
    <p><?php echo $client_lang['howPenny5']; ?></p>
    <ul>
        <li><?php echo $client_lang['howPenny6']; ?></li>
        <li><?php echo $client_lang['howPenny7']; ?></li>
        <li><?php echo $client_lang['howPenny8']; ?></li>
        <li><?php echo $client_lang['howPenny9']; ?></li>
        <li><?php echo $client_lang['howPenny10']; ?></li>
    </ul>
</section>
<?php } ?>
<hr>

<?php if (in_array(7, $o_types)) { ?>
     <section id="english-auction">
    <h2><?php echo $client_lang['howEnglish1']; ?></h2>
    <p><?php echo $client_lang['howEnglish2']; ?></p>
    <ul>
        <li><?php echo $client_lang['howEnglish3']; ?></li>
        <li><?php echo $client_lang['howEnglish4']; ?></li>
        <li><?php echo $client_lang['howEnglish5']; ?></li>
        <li><?php echo $client_lang['howEnglish6']; ?></li>
        <li><?php echo $client_lang['howEnglish7']; ?></li>
        <li><?php echo $client_lang['howEnglish8']; ?></li>
        <li><?php echo $client_lang['howEnglish9']; ?></li>
        <li><?php echo $client_lang['howEnglish10']; ?></li>
    </ul>
</section>
<?php } ?>
<hr>
<?php if (in_array(4, $o_types) || in_array(5, $o_types)) { ?>
<center><img src="/images/static/ic_lottery.png"></center>
<section id="lottery">
    <h2><?php echo $client_lang['howLottery1']; ?></h2>
    <p><?php echo $client_lang['howLottery2']; ?></p>
    <ul>
        <li><?php echo $client_lang['howLottery3']; ?></li>
        <li><?php echo $client_lang['howLottery4']; ?></li>
        <li><?php echo $client_lang['howLottery5']; ?></li>
        <li><?php echo $client_lang['howLottery6']; ?></li>
        <li><?php echo $client_lang['howLottery7']; ?></li>
        <li><?php echo $client_lang['howLottery8']; ?></li>
        <li><?php echo $client_lang['howLottery9']; ?></li>
    </ul>
</section>
<?php } ?>
<hr>
<center><img src="/images/static/ic_invest.png"></center>
<section id="invest">
    <h2><?php echo $client_lang['howInvest1']; ?></h2>
    <p><?php echo $client_lang['howInvest2']; ?></p>
    <ul>
        <li><?php echo $client_lang['howInvest3']; ?></li>
        <li><?php echo $client_lang['howInvest4']; ?></li>
        <li><?php echo $client_lang['howInvest5']; ?></li>
        <li><?php echo $client_lang['howInvest6']; ?></li>
        <li><?php echo $client_lang['howInvest7']; ?></li>
    </ul>
</section>
</div>

    </main>

    <script src="script.js"></script>
</body>
</html>