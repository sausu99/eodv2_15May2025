<?php

include("language/language.php");

ini_set('display_errors', '1');
ini_set('display_startup_errors', '1');
error_reporting(E_ALL);

$languages = array(
    array('code' => 'ar', 'name' => 'Arabic (عربى)', 'flag' => 'flag_ar.svg'),
    array('code' => 'bn', 'name' => 'Bangla (বাংলা)', 'flag' => 'flag_bn.svg'),
    array('code' => 'zh-CN', 'name' => 'Chinese (简体中文)', 'flag' => 'flag_zh-CN.svg'),
    array('code' => 'en', 'name' => 'English (US)', 'flag' => 'flag_en.svg'),
    array('code' => 'fr', 'name' => 'French (Français)', 'flag' => 'flag_fr.svg'),
    array('code' => 'de', 'name' => 'German (Deutsch)', 'flag' => 'flag_de.svg'),
    array('code' => 'gr', 'name' => 'Greek (Ελληνικά)', 'flag' => 'flag_gr.svg'),
    array('code' => 'id', 'name' => 'Indonesian (Bahasa Indonesia)', 'flag' => 'flag_id.svg'),
    // array('code' => 'it', 'name' => 'Italian (Italiano)', 'flag' => 'flag_it.svg'),
    // array('code' => 'ja', 'name' => 'Japanese (日本語)', 'flag' => 'flag_ja.svg'),
    // array('code' => 'ko', 'name' => 'Korean (한국어)', 'flag' => 'flag_ko.svg'),
    // array('code' => 'pl', 'name' => 'Polish (Polski)', 'flag' => 'flag_pl.svg'),
    array('code' => 'pt-BR', 'name' => 'Português (Brazil)', 'flag' => 'flag_pt-BR.svg'),
    // array('code' => 'pt-PT', 'name' => 'Portuguese (Portugal)', 'flag' => 'flag_pt.svg'),
    array('code' => 'ru', 'name' => 'Russian (Русский)', 'flag' => 'flag_ru.svg'),
    array('code' => 'es', 'name' => 'Spanish (Español)', 'flag' => 'flag_es.svg'),
    array('code' => 'tr', 'name' => 'Turkish (Türkçe)', 'flag' => 'flag_tr.svg')
);
?>

<!DOCTYPE html>
<html lang="en">
<head>
<base href="/">
    <meta charset="UTF-8">
    <title><?php echo $header_lang['language']; ?></title>
    <link rel="stylesheet" href="assets/css/language_popup.css">
    <style>
        .language-list {
            width: fit-content;
        }

        .language-list a {
            display: block;
            text-align: left;
            text-decoration: none;
            color: #333;
            margin: 15px;
           padding: 5px;
        }

        .language-list a:hover {
            background-color: #f0f0f0;
        }

        .language-list img {
            width: 20px;
            height: auto;
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="language-popup-container">
        <div class="language-list">
            <?php foreach ($languages as $lang): ?>
                <a href="#" onclick="switchLanguage('<?php echo $lang['code']; ?>'); return false;">
                <img src="/images/static/flags/<?php echo $lang['flag']; ?>" alt="<?php echo $lang['name']; ?>">
                    <?php echo $lang['name']; ?>
                </a>
            <?php endforeach; ?>
        </div>
    </div>
    <script>
        function switchLanguage(language) {
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'update_language.php', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    parent.location.reload();
                }
            };
            xhr.send('language=' + language);
        }
    </script>
</body>
</html>
