<?php

error_reporting(0);

function isImage($file) {
    $validTypes = [
        IMAGETYPE_GIF,
        IMAGETYPE_JPEG,
        IMAGETYPE_PNG,
        IMAGETYPE_BMP,
        IMAGETYPE_WEBP,
        IMAGETYPE_ICO,
    ];

    $imageType = exif_imagetype($file);

    return in_array($imageType, $validTypes);
}

if (isset($_POST) && $_SERVER['REQUEST_METHOD'] == "POST") {
    $path = "uploads/"; // Set your folder path

    if (isset($_FILES['video_local']) && $_FILES['video_local']['error'] === UPLOAD_ERR_OK) {
        $tmp = $_FILES['video_local']['tmp_name'];

        if (isImage($tmp)) { // Check if the uploaded file is an image
            $video_local = rand(0, 99999) . "_" . str_replace(" ", "-", $_FILES['video_local']['name']);

            if (move_uploaded_file($tmp, $path . $video_local)) {
                echo $video_local;
            } else {
                echo "failed";
            }
        } else {
            echo "Invalid file type. Only images are allowed.";
        }
    } else {
        echo "Upload failed.";
    }

    exit;
}
?>
