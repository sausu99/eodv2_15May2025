<?php

// Include Composer's autoloader
require __DIR__ . '/vendor/autoload.php';

use Endroid\QrCode\QrCode;
use Endroid\QrCode\Writer\PngWriter;

// Turn off error display to avoid issues with headers
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
error_reporting(E_ALL);

// Get the 'text' parameter from the URL
$text = isset($_GET['text']) ? filter_var($_GET['text'], FILTER_SANITIZE_SPECIAL_CHARS) : '';

if (strlen($text) > 255) {
    die('Input too long.');
}

// Create a new instance of QrCode with the provided text
$qrCode = new QrCode($text);

// Use PngWriter to handle the output
$writer = new PngWriter();

// Set the content type to PNG
header('Content-Type: image/png');

// Output the QR code as a PNG image
echo $writer->write($qrCode)->getString();

?>