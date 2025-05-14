<?php
include ("language/language.php");
include ('includes/function.php');
include ('includes/header.php');
include ("includes/connection.php");

if (isset($_SESSION['user_id'])) {

    $qry = "select * from tbl_users where id='" . $_SESSION['user_id'] . "'";

    $result = mysqli_query($mysqli, $qry);
    $row = mysqli_fetch_assoc($result);

}


$querytime = "SELECT timezone,currency FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);

date_default_timezone_set($rowtime['timezone']);
$time = date('H:i:s');
$date1 = date('Y-m-d');

$qry_auctions = "SELECT * FROM tbl_offers
                     LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                     WHERE ((o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                     AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "'))) 
                     AND o_status = 1 AND o_type IN (1,2,7, 8) ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
$result_auctions = mysqli_query($mysqli, $qry_auctions);

$qry_banners = "SELECT * FROM tbl_offers
                    LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                    WHERE ((o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                    AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "'))) 
                    AND o_status = 1 AND (o_type = 6 AND o_price = 1) ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";
$result_banners = mysqli_query($mysqli, $qry_banners);

$qry_featured = "SELECT * FROM tbl_offers
                     LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                     WHERE ((o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                     AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "'))) 
                     AND o_status = 1 AND c_id=2 AND o_type IN (1,2,4,5,7,8) ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC LIMIT 0,3";
$result_featured = mysqli_query($mysqli, $qry_featured);

// Get available categories for filtering 
$query_cat = "SELECT DISTINCT c_id, c_name, c_image FROM tbl_cat WHERE c_id >= 3"; // Assuming c_id starts from 3
$result_cat = mysqli_query($mysqli, $query_cat);

$hyip_qry = "SELECT * FROM tbl_hyip
						 WHERE plan_status = '1'
						 ORDER BY tbl_hyip.plan_id DESC LIMIT 3";

$hyip_result = mysqli_query($mysqli, $hyip_qry);
?>
<!DOCTYPE html>
<html lang="en">

<head>
    <title><?php echo APP_NAME; ?></title>
    <link rel="manifest" href="manifest.json">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js"
        integrity="sha512-XW9zRM0zYFVzyqcsWJgGDGq+Bx9rKskdPe0o+D1/1bKQb9jqLTBbZ1vXu/f1UzNp64C5lHb3mAxZo8fC9rCCwA=="
        crossorigin="anonymous"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/vendor.css">
    <link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue-sky.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/red.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/yellow.css">
    <link rel="icon" href="/images/profile.png" type="image/x-icon">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/css/home.css">
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-regular-straight/css/uicons-regular-straight.css'>
    <style>
        body {
            background-color: #f1f2f4;
        }

        /* HYIP PLANS */
        .package-card {
            background: linear-gradient(145deg, #2a2a2a, #3b3b3b);
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin: 10px;
            width: 30rem;
            border: 1px solid #444;
            color: white;
            /* Set text color to white */
        }

        .package-parent-container {
            display: flex;
            flex-wrap: wrap;
            gap: 5rem;
            justify-content: center;
        }

        .package-big-container {
            width: 30rem;
        }

        .gradient-text {
            background: black;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .package-header {
            font-size: 25px;
            margin: 0;
            font-weight: bold;
        }

        .package-range {
            font-size: 15px;
            color: black;
            margin: 10px 0;
            font-weight: 600;
        }

        .package-return {
            text-transform: uppercase;
            background: linear-gradient(145deg, #111, #222);
            padding: 16px;
            border-radius: 16px;
            font-size: 1.5rem;
            font-weight: 900;
            margin-left: 5rem;
            margin-right: 5rem;
        }

        .package-card__title {
            font-size: 24px;
            margin-bottom: 10px;
            color: white;
            /* Set text color to white */
        }

        .package-card__subtitle {
            font-size: 15px;
            color: #777;
            margin-bottom: 20px;
            color: white;
            /* Set text color to white */
        }

        .package-card__features {
            list-style: none;
            padding: 0;
            margin-bottom: 20px;
            text-align: center;
            /* Align list items to the left */
        }

        .package-card__features li {
            margin-bottom: 5px;
            font-size: 15px;
            font-weight: 600;
            background: black;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            /* Set text color to white */
        }

        .package-card__range {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
            color: white;
            /* Set text color to white */
        }

        .package-button {
            background: linear-gradient(145deg, #ffcc00, #efdd86, #ffcc00, #efdd86, #ffcc00);
            color: black !important;
            font-weight: 700;
            border-radius: 9999px !important;
            font-size: 18px !important;
            background-color: #ffcc00 !important;
        }

        .package-button:hover {
            background-color: #ffcc00 !important;
            background: none;
        }

        .btn--base {
            background-color: #213343;
            color: #fff;
            border: none;
            border-radius: 5px;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn--base:hover {
            background-color: #192733;
            color: #fff;
        }

        .pagination_item_block {
            margin-top: 20px;
            /* Added margin to create space between pagination and package cards */
        }

        .badge {
            font-size: 14px;
            padding: 5px 10px;
            border-radius: 5px;
        }

        .badge--success {
            background-color: #28a745;
            color: #fff;
        }

        .badge--warning {
            background-color: #ffc107;
            color: #212529;
        }

        /*TEXT between line*/
        .hr-with-text {
            display: flex;
            align-items: center;
            text-align: center;
            margin: 20px 0;
        }

        .hr-with-text hr {
            flex-grow: 1;
            border: none;
            border-top: 1px solid #000;
        }

        .hr-with-text span {
            padding: 0 10px;
            white-space: nowrap;
        }

        /* Banners */
        .banner-container {
            display: flex;
            flex-direction: row;
            transition: transform 0.4s ease-in-out;
            overflow-x: auto;
            /* Enable horizontal scrolling */
            scrollbar-width: none;
            /* Firefox */
            -ms-overflow-style: none;
            /* Internet Explorer 10+ */
            max-height: 300px;
        }

        .banner-container::-webkit-scrollbar {
            display: none;
            /* WebKit browsers */
        }

        .banner-container img {
            width: 100%;
            /* Ensure the width adjusts according to the height */
            object-fit: cover;
            /* Maintain aspect ratio and cover the container */
            height: auto !important;
            max-height: 175px;
        }

        .banner-slider {
            background-color: #FFF;
            width: 99%;
            /* Ensure the slider spans the full width */
            overflow: hidden;
            z-index: 99;
            position: relative;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 15rem;
            height: auto !important;
        }

        #prev-btn,
        #next-btn {
            font-size: 35px;
            font-weight: bold;
            cursor: pointer;
            user-select: none;
            /* Standard property */
            -webkit-user-select: none;
            /* Safari */
            -moz-user-select: none;
            /* Firefox */
            -ms-user-select: none;
            /* Internet Explorer/Edge */
        }

        #next-btn:hover,
        #prev-btn:hover {
            color: #b7b7b7;
        }

        .new-designed-auction-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-left: 15rem;
            margin-right: 15rem;
            margin-bottom: 3rem;
        }

        .new-designed-auction-header h3 {
            margin: 0;
        }

        .parent-wrap-container {
            border: 1px solid #0000003b;
            background-color: white;
            margin-left: 10px;
            margin-right: 10px;
            padding: 32px;
            border-radius: 5px;
        }

        .footer-fix-boxer {
            margin-bottom: 20px;
        }

        .package-card {
            background: linear-gradient(145deg, #fefefe, #ffffff);
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin: 10px;
            border: 1px solid #4444443d;
            color: white;
        }

        @media (max-width: 900px)
        {
            #slider-forward {
                min-width: 200px;
            }

            .banner-slider {
                gap: 0rem;
                width: unset;
            }

            .new-designed-auction-header {
                margin: 0;
            }

            .new-designed-auction-header h3 {
                font-size: 14px;
            }

            .btn--base {
                font-size: 12px;
            }
        }
    </style>
    <!-- Icon -->
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-regular-straight/css/uicons-regular-straight.css'>
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-solid-straight/css/uicons-solid-straight.css'>
    <link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.2.0/uicons-bold-rounded/css/uicons-bold-rounded.css'>
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-solid-rounded/css/uicons-solid-rounded.css'>
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-regular-rounded/css/uicons-regular-rounded.css'>
</head>

<body>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
    <div class="banner-slider parent-wrap-container">
        <!--<div id="prev-btn">⟨</div>-->
        <a id="slider-forward" target="_blank">
            <div class="banner-container">
            </div>
        </a>
        <!--<div id="next-btn">⟩</div>-->
    </div>
    <script>
        const bannerContainer = document.querySelector('.banner-container');
        const sliderForward = document.querySelector("#slider-forward");
        const prevBtn = document.querySelector('#prev-btn');
        const nextBtn = document.querySelector('#next-btn');

        let currentSlide = 0;

        // Fetch banner data from database and create slides
        const bannerData = <?php echo json_encode(mysqli_fetch_all($result_banners, MYSQLI_ASSOC)) ?>;
        console.log(bannerData);


        function isValidUrl(url) {
            const pattern = new RegExp(
                '^(https?:\\/\\/)?' + // protocol
                '((([a-zA-Z0-9\\-\\._]+)\\.[a-zA-Z]{2,})|' + // domain name and extension
                'localhost|' + // localhost
                '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
                '(\\:\\d+)?(\\/[-a-zA-Z0-9%_.~+]*)*' + // port and path
                '(\\?[;&a-zA-Z0-9%_.~+=-]*)?' + // query string
                '(\\#[-a-zA-Z0-9_]*)?$', // fragment locator
                'i' // case insensitive
            );

            return pattern.test(url);
        }

        class PicturePreviewSlider {
            constructor(container, images, f, counts = true, imagesAlts = [], captions = []) {
                this.previewSlides = [];
                this.previewSelectors = [];
                this.fList = [];
                this.count = 1;
                this.totalCount = images.length;
                this.sliderIndex = 0;
                this.previewCaptions = captions;
                this.f = f;

                for (const image of images) {
                    const div = document.createElement("div");
                    div.classList.add("picture-preview-slide");

                    if (counts) {
                        const divCount = document.createElement("div");
                        divCount.classList.add("picture-preview-count");
                        divCount.innerHTML = this.count++ + " / " + this.totalCount;
                        div.appendChild(divCount);
                    }

                    const img = document.createElement("img");
                    img.src = "../seller/images/thumbs/" + image.o_image;

                    this.fList.push(String(image.o_link));

                    if (imagesAlts.length !== 0) {
                        img.alt = imagesAlts[this.count++ - 1];
                    }

                    div.appendChild(img);

                    this.previewSlides.push(div);
                    container.appendChild(div);
                }

                this.count = 1;

                if (this.previewCaptions.length !== 0) {
                    const caption = document.createElement("div");
                    caption.classList.add("picture-preview-caption");

                    this.previewCaption = document.createElement("p");
                    this.previewCaption.classList.add("picture-preview-caption-text");
                    caption.appendChild(this.previewCaption);

                    container.appendChild(caption);
                }

                this.ShowSlide(this.sliderIndex);
            }

            ShowSlide() {
                for (const slide of this.previewSlides) {
                    slide.style.display = "none";
                }

                this.previewSlides[this.sliderIndex].style.display = "block";

                if (isValidUrl(this.fList[this.sliderIndex])) {
                    this.f.href = this.fList[this.sliderIndex];
                }
                else {
                      //<!--removed .php-->
                    this.f.href = "seller?id=" + this.fList[this.sliderIndex].match(/o_id=(\d+)/)[1];
                }

                if (this.previewCaptions.length !== 0) {
                    this.previewCaption.innerHTML = this.previewCaptions[this.sliderIndex];
                }
            }

            GetActiveSlideElement() {
                return this.previewSlides[this.sliderIndex];
            }

            GetCurrentSlideIndex() {
                return this.sliderIndex;
            }

            NextSlide() {
                if (this.sliderIndex === (this.totalCount - 1)) {
                    this.sliderIndex = 0;
                }
                else {
                    this.sliderIndex++;
                }

                this.ShowSlide();
            }

            PreviousSlide() {
                if (this.sliderIndex === 0) {
                    this.sliderIndex = this.totalCount - 1;
                }
                else {
                    this.sliderIndex--;
                }

                this.ShowSlide();
            }

            ChangeSlide(index) {
                this.sliderIndex = index;
                this.ShowSlide();
            }
        }

        const slider = new PicturePreviewSlider(bannerContainer, bannerData, sliderForward, false);

        setInterval(() => {
            slider.NextSlide();
        }, 5000);

        nextBtn.addEventListener("click", () => {
            slider.NextSlide();
        });
        prevBtn.addEventListener("click", () => {
            slider.PreviousSlide();
        });

    </script>
    <br>
    <div class="parent-wrap-container">
        <div class="new-designed-auction-header">
            <h3>
                <center><strong><?php echo $client_lang['liveAuctions']; ?></strong></center>
              <!--removed .php-->
            </h3><a href="auctions"
                class="btn--base btn-md mt-4"><?php echo $client_lang['liveAuctionsAll']; ?></a>
        </div>

        <div class="live-lotteries">
            <div class="category-filter"></div>

            <?php
            // Query all auction that meet the criteria
            $qry_lottery_all = "SELECT *
                        FROM tbl_offers
                        LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                        LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id
                        WHERE (tbl_offers.o_date < '" . $date1 . "' OR (tbl_offers.o_date = '" . $date1 . "' AND tbl_offers.o_stime <= '" . $time . "'))
                        AND (tbl_offers.o_edate > '" . $date1 . "' OR (tbl_offers.o_edate = '" . $date1 . "' AND tbl_offers.o_etime >= '" . $time . "'))
                        AND tbl_offers.o_status = 1 
                        AND tbl_offers.o_type IN (1,2,7,8) 
                        ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC LIMIT 0,3";
            $result_lottery_all = mysqli_query($mysqli, $qry_lottery_all);
            ?>

            <div class="row justify-content-center auction-fix">
                <?php while ($row_lottery = mysqli_fetch_assoc($result_lottery_all)) { ?>
                        <div>

                            <div class="auction-item">
                            <?php
                        $item_id = $row_lottery["item_id"];
                        $user_id = $_SESSION["user_id"];
                        
                        $sSql = "SELECT * FROM tbl_wishlist WHERE item_id = ? AND user_id = ?";
                        $stmt = $mysqli->prepare($sSql);
                        $stmt->bind_param("ii", $item_id, $user_id);
                        $stmt->execute();
                        $result = $stmt->get_result();
                        
                        if ($result->num_rows > 0) {
                        $wishlistSrc = "images/static/heart-filled.svg";
                        }
                        else
                        {
                        $wishlistSrc = "images/static/heart.svg";
                        }
                    ?>
                    <img src="<?php echo $wishlistSrc; ?>" data="<?php echo $row_lottery['item_id']; ?>" id="wishlist" alt="wishlist" title="Wishlist" onclick="AddWishlist(this);" />

                            <div class="image">
                                <a href="<?php
                                    echo 'auction';
                                    $lowercaseString = strtolower($row_lottery["o_name"]);
                                    $finalString = str_replace(' ', '-', $lowercaseString);
                                    ?>/<?php echo $finalString; ?>/<?php echo $row_lottery['o_id']; ?>">
                                    <img src="placeholder.jpg"
                                        data-src="<?php echo '/seller/images/thumbs/' . $row_lottery['o_image']; ?>"
                                        class="lazyload img-fluid img-thumbnail" alt="<?php echo $row_lottery['o_name']; ?>"
                                        style="vertical-align: middle;">
                                </a>
                            </div>
                            <div class="auction-content">
                                <h5><?php echo $row_lottery['o_name']; ?></h5>
                                <h6 class="description"><?php echo $row_lottery['o_desc']; ?></h6>
                                <?php if ($row_lottery['o_type'] == 1 || $row_lottery['o_type'] == 2) { ?>
                                    <div class="current-bid d-flex">
                                        <i class="flaticon-hammer"></i>
                                        <p class="d-flex flex-column bold-text"><?php echo $auction_lang['startingBid']; ?>:
                                            <span><?php echo $rowtime['currency'] . $row_lottery['o_min']; ?></span>
                                        </p>
                                    </div>
                                <?php } elseif ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) { ?>
                                    <div class="current-bid d-flex">
                                        <i class="flaticon-hammer"></i>
                                        <p class="d-flex flex-column bold-text"><?php echo $auction_lang['ticketPrice']; ?>:
                                            <span><?php echo $row_lottery['o_amount']; ?>&nbsp;<i
                                                    class="fi fi-rs-coins"></i></span>
                                        </p>
                                    </div>
                                <?php } elseif ($row_lottery['o_type'] == 7 || $row_lottery['o_type'] == 8) { ?>
                                    <div class="current-bid d-flex">
                                        <i class="flaticon-hammer"></i>
                                        <p class="d-flex flex-column bold-text"><?php echo $auction_lang['currentBid']; ?>:
                                            <span><?php echo $rowtime['currency'] . $row_lottery['o_min']; ?></span>
                                        </p>
                                    </div>
                                <?php } ?>
                            </div>
                            <div class="auction-timer">
                                <p><?php echo $auction_lang['endsIn']; ?>:</p>
                                <div class="countdown" id="timer<?php echo $row_lottery['o_id']; ?>">
                                    <?php
                                    // Calculate time left for each item
                                    $endDateTime = strtotime($row_lottery['o_edate'] . ' ' . $row_lottery['o_etime']);
                                    $currentDateTime = strtotime(date('Y-m-d H:i:s'));
                                    $timeLeft = $endDateTime - $currentDateTime;
                                    ?>
                                    <script>
                                        // Countdown timer script
                                        // Calculate remaining time for this item
                                        var remainingTime<?php echo $row_lottery['o_id']; ?> = <?php echo $timeLeft; ?>;
                                        
                                        function countdown<?php echo $row_lottery['o_id']; ?>() {
                                            var timer = document.getElementById('timer<?php echo $row_lottery['o_id']; ?>');
                                            
                                            var days = Math.floor(remainingTime<?php echo $row_lottery['o_id']; ?> / (60 * 60 * 24));
                                            var hours = Math.floor((remainingTime<?php echo $row_lottery['o_id']; ?> % (60 * 60 * 24)) / (60 * 60));
                                            var minutes = Math.floor((remainingTime<?php echo $row_lottery['o_id']; ?> % (60 * 60)) / 60);
                                            var seconds = Math.floor(remainingTime<?php echo $row_lottery['o_id']; ?> % 60);
                                        
                                            // Display the remaining time based on the condition
                                            if (days > 0) {
                                                timer.innerHTML = '<h4><span id="days<?php echo $row_lottery['o_id']; ?>"></span> Days : <span id="hours<?php echo $row_lottery['o_id']; ?>"></span> Hours</h4>';
                                                document.getElementById('days<?php echo $row_lottery['o_id']; ?>').textContent = days;
                                                document.getElementById('hours<?php echo $row_lottery['o_id']; ?>').textContent = hours;
                                            } else if (hours > 0) {
                                                timer.innerHTML = '<h4><span id="hours<?php echo $row_lottery['o_id']; ?>"></span> Hours : <span id="minutes<?php echo $row_lottery['o_id']; ?>"></span> Minutes</h4>';
                                                document.getElementById('hours<?php echo $row_lottery['o_id']; ?>').textContent = hours;
                                                document.getElementById('minutes<?php echo $row_lottery['o_id']; ?>').textContent = minutes;
                                            } else if (minutes > 0) {
                                                timer.innerHTML = '<h4><span id="minutes<?php echo $row_lottery['o_id']; ?>"></span> Minutes : <span id="seconds<?php echo $row_lottery['o_id']; ?>"></span> Seconds</h4>';
                                                document.getElementById('minutes<?php echo $row_lottery['o_id']; ?>').textContent = minutes;
                                                document.getElementById('seconds<?php echo $row_lottery['o_id']; ?>').textContent = seconds;
                                            } else if (seconds > 0) {
                                                timer.innerHTML = '<h4><span id="seconds<?php echo $row_lottery['o_id']; ?>"></span> Seconds</h4>';
                                                document.getElementById('seconds<?php echo $row_lottery['o_id']; ?>').textContent = seconds;
                                            } else {
                                                timer.innerHTML = '<h4>00:00:00</h4>';
                                                // Refresh the page when the countdown ends
                                                location.reload();
                                            }
                                        
                                            // Update the countdown every second
                                            if (remainingTime<?php echo $row_lottery['o_id']; ?> > 0) {
                                                remainingTime<?php echo $row_lottery['o_id']; ?>--;
                                                setTimeout(countdown<?php echo $row_lottery['o_id']; ?>, 1000);
                                            }
                                        }
                                        
                                        countdown<?php echo $row_lottery['o_id']; ?>(); // Start countdown for this item
                                    </script>
                                </div>
                            </div>

                            <div class="button text-center">
                                <a href="<?php
                                if ($row_lottery['o_type'] == 1 || $row_lottery['o_type'] == 2 || $row_lottery['o_type'] == 7 || $row_lottery['o_type'] == 8) {
                                    echo 'auction';
                                    $lowercaseString = strtolower($row_lottery["o_name"]);
                                    $finalString = str_replace(' ', '-', $lowercaseString);
                                    ?>/<?php echo $finalString; ?>/<?php echo $row_lottery['o_id']; ?>">
                                <?php
                                }
                                else { if ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) {
                                    echo 'raffle.php';
                                } else {
                                    echo 'live.php';
                                }
                                ?>?id=<?php echo $row_lottery['o_id']; ?>">
                                <?php } ?>
                                    <?php
                                    $types_with_bid_now = [1, 2, 7, 8];
                                    echo in_array($row_lottery['o_type'], $types_with_bid_now) ? '<i class="fas fa-gavel"></i> ' . $auction_lang['bidNow'] : 'Play';
                                    ?></a>
                            </div>
                        </div>
                    </div>
                <?php } ?>
            </div>
        </div>
    </div>

    <br>
    <div class="parent-wrap-container">
        <div class="new-designed-auction-header">
            <h3>
                <center><strong><?php echo $client_lang['liveLottery']; ?></strong></center>
            </h3>
              <!--removed .php-->
            <a href="lotteries" class="btn--base btn-md mt-4"><?php echo $client_lang['liveLotteryAll']; ?></a>
        </div>

        <div class="live-lotteries">
            <div class="category-filter"></div>

            <?php
            // Query all lotteries that meet the criteria
            $qry_lottery_all = "SELECT *
                        FROM tbl_offers
                        LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                        LEFT JOIN tbl_cat ON tbl_cat.c_id = tbl_offers.c_id
                        WHERE (tbl_offers.o_date < '" . $date1 . "' OR (tbl_offers.o_date = '" . $date1 . "' AND tbl_offers.o_stime <= '" . $time . "'))
                        AND (tbl_offers.o_edate > '" . $date1 . "' OR (tbl_offers.o_edate = '" . $date1 . "' AND tbl_offers.o_etime >= '" . $time . "'))
                        AND tbl_offers.o_status = 1 
                        AND tbl_offers.o_type IN (5) 
                        ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC LIMIT 0,3";
            $result_lottery_all = mysqli_query($mysqli, $qry_lottery_all);
            ?>

            <div class="row justify-content-center auction-fix">
                <?php while ($row_lottery = mysqli_fetch_assoc($result_lottery_all)) { ?>
                    <div>
                        <div class="auction-item lottery-item">
                            <div class="image flex-lottery-fix">
                            <div class="auction-timer">
                                    <p><?php echo $auction_lang['endsIn']; ?>:</p>
                                    <div class="countdown" id="timer<?php echo $row_lottery['o_id']; ?>">
                                        <?php
                                        // Calculate time left for each item
                                        $endDateTime = strtotime($row_lottery['o_edate'] . ' ' . $row_lottery['o_etime']);
                                        $currentDateTime = strtotime(date('Y-m-d H:i:s'));
                                        $timeLeft = $endDateTime - $currentDateTime;
                                        ?>
                                        <script>
                                        // Countdown timer script
                                        // Calculate remaining time for this item
                                        var remainingTime<?php echo $row_lottery['o_id']; ?> = <?php echo $timeLeft; ?>;
                                        
                                        function countdown<?php echo $row_lottery['o_id']; ?>() {
                                            var timer = document.getElementById('timer<?php echo $row_lottery['o_id']; ?>');
                                            
                                            var days = Math.floor(remainingTime<?php echo $row_lottery['o_id']; ?> / (60 * 60 * 24));
                                            var hours = Math.floor((remainingTime<?php echo $row_lottery['o_id']; ?> % (60 * 60 * 24)) / (60 * 60));
                                            var minutes = Math.floor((remainingTime<?php echo $row_lottery['o_id']; ?> % (60 * 60)) / 60);
                                            var seconds = Math.floor(remainingTime<?php echo $row_lottery['o_id']; ?> % 60);
                                        
                                            // Display the remaining time based on the condition
                                            if (days > 0) {
                                                timer.innerHTML = '<h4><span id="days<?php echo $row_lottery['o_id']; ?>"></span> Days : <span id="hours<?php echo $row_lottery['o_id']; ?>"></span> Hours</h4>';
                                                document.getElementById('days<?php echo $row_lottery['o_id']; ?>').textContent = days;
                                                document.getElementById('hours<?php echo $row_lottery['o_id']; ?>').textContent = hours;
                                            } else if (hours > 0) {
                                                timer.innerHTML = '<h4><span id="hours<?php echo $row_lottery['o_id']; ?>"></span> Hours : <span id="minutes<?php echo $row_lottery['o_id']; ?>"></span> Minutes</h4>';
                                                document.getElementById('hours<?php echo $row_lottery['o_id']; ?>').textContent = hours;
                                                document.getElementById('minutes<?php echo $row_lottery['o_id']; ?>').textContent = minutes;
                                            } else if (minutes > 0) {
                                                timer.innerHTML = '<h4><span id="minutes<?php echo $row_lottery['o_id']; ?>"></span> Minutes : <span id="seconds<?php echo $row_lottery['o_id']; ?>"></span> Seconds</h4>';
                                                document.getElementById('minutes<?php echo $row_lottery['o_id']; ?>').textContent = minutes;
                                                document.getElementById('seconds<?php echo $row_lottery['o_id']; ?>').textContent = seconds;
                                            } else if (seconds > 0) {
                                                timer.innerHTML = '<h4><span id="seconds<?php echo $row_lottery['o_id']; ?>"></span> Seconds</h4>';
                                                document.getElementById('seconds<?php echo $row_lottery['o_id']; ?>').textContent = seconds;
                                            } else {
                                                timer.innerHTML = '<h4>00:00:00</h4>';
                                                // Refresh the page when the countdown ends
                                                location.reload();
                                            }
                                        
                                            // Update the countdown every second
                                            if (remainingTime<?php echo $row_lottery['o_id']; ?> > 0) {
                                                remainingTime<?php echo $row_lottery['o_id']; ?>--;
                                                setTimeout(countdown<?php echo $row_lottery['o_id']; ?>, 1000);
                                            }
                                        }
                                        
                                        countdown<?php echo $row_lottery['o_id']; ?>(); // Start countdown for this item
                                    </script>
                                    </div>
                                </div>
                                <a class="lottery-fix-a" href="<?php
                                    echo 'lottery';
                                    $lowercaseString = strtolower($row_lottery["o_name"]);
                                    $finalString = str_replace(' ', '-', $lowercaseString);
                                    ?>/<?php echo $finalString; ?>/<?php echo $row_lottery['o_id']; ?>">
                                    <img src="placeholder.jpg"
                                        data-src="<?php echo '/seller/images/' . $row_lottery['o_image']; ?>"
                                        class="lazyload img-fluid img-thumbnail" alt="<?php echo $row_lottery['o_name']; ?>"
                                        style="vertical-align: middle;">
                                </a>
                                <div class="auction-content">
                                    <h5 class="lottery-fix-h5"><?php echo $row_lottery['o_name']; ?></h5>
                                    <h6 class="description lottery-description-fix"><?php echo $row_lottery['o_desc']; ?>
                                    </h6>
                                    <?php if ($row_lottery['o_type'] == 1 || $row_lottery['o_type'] == 2) { ?>
                                        <div class="current-bid d-flex">
                                            <i class="flaticon-hammer"></i>
                                            <p class="d-flex flex-column bold-text"><?php echo $auction_lang['startingBid']; ?>:
                                                <span><?php echo $rowtime['currency'] . $row_lottery['o_min']; ?></span>
                                            </p>
                                        </div>
                                    <?php } elseif ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) { ?>
                                        <div class="current-bid d-flex currency-lottery-fix">
                                            <i class="flaticon-hammer"></i>
                                            <p class="d-flex flex-column bold-text"><?php echo $auction_lang['ticketPrice']; ?>:
                                                <span><?php echo $row_lottery['o_amount']; ?>&nbsp;<i
                                                        class="fi fi-rs-coins"></i></span>
                                            </p>
                                        </div>
                                    <?php } elseif ($row_lottery['o_type'] == 7 || $row_lottery['o_type'] == 8) { ?>
                                        <div class="current-bid d-flex">
                                            <i class="flaticon-hammer"></i>
                                            <p class="d-flex flex-column bold-text"><?php echo $auction_lang['currentBid']; ?>:
                                                <span><?php echo $rowtime['currency'] . $row_lottery['o_min']; ?></span>
                                            </p>
                                        </div>
                                    <?php } ?>
                                </div>
                                

                            </div>

                            <?php

                            $query = "
                        SELECT 
                            tbl_offers.o_id, 
                            tbl_offers.o_qty AS total_tickets,
                            COUNT(tbl_ticket.o_id) AS sold_tickets
                        FROM tbl_offers
                        LEFT JOIN tbl_ticket ON tbl_offers.o_id = tbl_ticket.o_id
                        WHERE tbl_offers.o_id = ?
                        GROUP BY tbl_offers.o_id
                    ";

                            $stmt = $mysqli->prepare($query);
                            $stmt->bind_param("i", $row_lottery['o_id']);
                            $stmt->execute();
                            $result = $stmt->get_result();

                            if ($result->num_rows > 0) {
                                while ($row = $result->fetch_assoc()) {
                                    $o_id = $row['o_id'];
                                    $totalTickets = $row['total_tickets'];
                                    $soldTickets = $row['sold_tickets'];

                                    if ($totalTickets > 0) {
                                        $percentageSold = ($soldTickets / $totalTickets) * 100;
                                    } else {
                                        $percentageSold = 0;
                                    }
                                    echo "<div class='super-parent-progress'>";
                                    echo "<div class='progress-parent-container'>";
                                    echo "<div style='height: 100%; width: " . $percentageSold . "%; border-radius: 9999px; background-color: #37f5f9;'></div>";
                                    echo "</div>";
                                    echo "<div style='text-align: center; color: white;'>" . number_format($percentageSold, 2) . "%</div>";
                                    echo "</div>";
                                }
                            } else {
                                echo "No data found for the specified o_id.";
                            }

                            $stmt->close();
                            ?>

                            <div class="button text-center">
                                <a href="<?php
                                    echo 'lottery';
                                    $lowercaseString = strtolower($row_lottery["o_name"]);
                                    $finalString = str_replace(' ', '-', $lowercaseString);
                                    ?>/<?php echo $finalString; ?>/<?php echo $row_lottery['o_id']; ?>">
                                    <?php
                                        echo '<i class="fas fa-ticket-alt"></i> ' . $auction_lang['buyTicket'];
                                    ?></a>
                            </div>
                        </div>
                    </div>
                <?php } ?>
            </div>
        </div>
    </div>
    <br>

    <div class="parent-wrap-container footer-fix-boxer">
        <div class="new-designed-auction-header">
            <h3>
                <center><strong><?php echo $client_lang['liveInvest']; ?></strong></center>
            </h3>
              <!--removed .php-->
            <a href="plans" class="btn--base btn-md mt-4"><?php echo $client_lang['liveInvestAll']; ?></a>
        </div>
        <div class="row gy-4 justify-content-center package-parent-container">
            <?php while ($hyip_row = mysqli_fetch_array($hyip_result)) {
                $plan_color = '#' . $hyip_row['plan_color']; // Retrieve plan_color from database
                ?>
                <div class="package-big-container">
                    <div class="package-card text-center bg_img" style="background-color: <?php echo $plan_color; ?>;">
                        <!-- Set background color dynamically -->
                        <h2 class="gradient-text package-header"><?php echo $hyip_row['plan_name']; ?></h2>
                        <div class="package-range">
                            <?php
                            if ($hyip_row['plan_minimum'] == $hyip_row['plan_maximum']) {
                                echo $hyip_row['plan_minimum'] . ' ' . $invest_lang['coins'];
                            } else {
                                echo $hyip_row['plan_minimum'] . ' - ' . $hyip_row['plan_maximum'] . ' ' . $invest_lang['coins'];
                            }
                            ?>
                        </div>
                        <h6 class="package-card__subtitle gradient-text"><?php echo $hyip_row['plan_description']; ?></h6>

                        <div class="package-return">
                            <?php
                            if ($hyip_row['plan_interest_type'] == 1) {
                                echo $hyip_row['plan_interest'] . '%';
                            } else {
                                echo $hyip_row['plan_interest'] . ' Coins';
                            }
                            ?>     <?php echo $hyip_row['plan_repeat_text']; ?>
                        </div>

                        <ul class="package-card__features mt-4">
                            <li>

                            </li>
                            <li></li>
                            <li><?php echo $invest_lang['for'] . ' '; ?>     <?php echo $hyip_row['plan_duration']; ?></li>
                            <li><?php echo $invest_lang['capitalBack']; ?>:
                                <?php
                                if ($hyip_row['plan_capital_back'] == 1) {
                                    echo '<i class="fi fi-ss-badge-check"></i>';
                                } else {
                                    echo '<i class="fi fi-br-octagon-xmark"></i>';
                                }
                                ?>
                            </li>
                            <li><?php echo $invest_lang['compund']; ?>:
                                <?php
                                if ($hyip_row['plan_compound_interest'] == 1) {
                                    echo '<i class="fi fi-ss-badge-check"></i>';
                                } else {
                                    echo '<i class="fi fi-br-octagon-xmark"></i>';
                                }
                                ?>
                            </li>
                            <li><?php echo $invest_lang['lifetime']; ?>:
                                <?php
                                if ($hyip_row['plan_lifetime'] == 1) {
                                    echo '<i class="fi fi-ss-badge-check"></i>';
                                } else {
                                    echo '<i class="fi fi-br-octagon-xmark"></i>';
                                }
                                ?>
                            </li>
                        </ul>
                        <!--removed .php-->
                        <a href="invest?plan_id=<?php echo $hyip_row['plan_id']; ?>"
                            class="btn--base btn-md mt-4 package-button"><?php echo $invest_lang['investNow']; ?></a>
                    </div><!-- package-card end -->
                </div>
                <?php
            }
            ?>
        </div>
    </div>


</body>

<script>
  function AddWishlist(item)
  {
    const id = item.getAttribute("data");

    $.ajax({
        url: 'insert_wishlist.php',
        type: 'POST',
        data: {
            id: id
        },
        success: function(response) {
          if (response.includes("Inserted"))
          {
            item.src = "images/static/heart-filled.svg";
          }
          else if (response.includes("Removed"))
          {
            item.src = "images/static/heart.svg";
          }
          else
          {
            window.location.href = "/login.php";
          }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("Please login to add into wishlist.");
        }
    });
  }
</script>

</html>

<?php include ("includes/footer.php"); ?>