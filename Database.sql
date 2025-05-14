-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 23, 2024 at 04:21 PM
-- Server version: 5.7.23-23
-- PHP Version: 8.1.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `omkriuhp_prizex`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_achievements`
--

CREATE TABLE `tbl_achievements` (
  `achievement_id` int(11) NOT NULL COMMENT 'Unique identifier for each achievement',
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Name of the achievement',
  `description` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Detailed description of the achievement',
  `image` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `category` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Category to which the achievement belongs (e.g., Auction, Lottery, Shop, etc.)',
  `color` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '#2a2a72',
  `goal` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Specific goal or requirement to earn the achievement',
  `points` int(11) NOT NULL COMMENT 'Points awarded for earning the achievement',
  `target_value` int(11) NOT NULL COMMENT 'The target value to achieve the goal',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of when the achievement was created',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Timestamp of the last update to the achievement'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_address`
--

CREATE TABLE `tbl_address` (
  `address_id` int(11) NOT NULL,
  `u_id` int(11) DEFAULT NULL,
  `address_line1` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `address_line2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `state` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `postal_code` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `country` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `address_type` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '1',
  `nickname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_admin`
--

CREATE TABLE `tbl_admin` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(200) NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_admin`
--

INSERT INTO `tbl_admin` (`id`, `username`, `password`, `email`, `image`) VALUES
(1, 'admin@wowcodes.in', 'Admin@WowCodes', 'admin@yourwebsite.com', 'profile.png');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_admin_logs`
--

CREATE TABLE `tbl_admin_logs` (
  `log_id` int(11) NOT NULL,
  `log_ip` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin_id` int(11) NOT NULL,
  `admin_username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `log_date` datetime NOT NULL,
  `remember_me_token` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_bid`
--

CREATE TABLE `tbl_bid` (
  `bd_id` int(11) NOT NULL,
  `u_id` int(11) NOT NULL,
  `o_id` int(11) NOT NULL,
  `bd_value` varchar(20) NOT NULL,
  `bid_status` int(11) DEFAULT NULL,
  `bd_amount` varchar(255) NOT NULL,
  `bd_date` varchar(255) NOT NULL,
  `bd_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_bot`
--

CREATE TABLE `tbl_bot` (
  `bot_id` int(11) NOT NULL,
  `bot_image` varchar(255) CHARACTER SET utf8 NOT NULL,
  `bot_name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `bot_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_cat`
--

CREATE TABLE `tbl_cat` (
  `c_id` int(11) NOT NULL,
  `c_image` varchar(255) CHARACTER SET utf8 NOT NULL,
  `c_name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `c_desc` varchar(255) CHARACTER SET utf8 NOT NULL,
  `c_color` varchar(255) CHARACTER SET utf8 NOT NULL,
  `c_view` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `c_status` varchar(255) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_cat`
--

INSERT INTO `tbl_cat` (`c_id`, `c_image`, `c_name`, `c_desc`, `c_color`, `c_view`, `c_status`) VALUES
(1, '52234_7.png', 'Home banner', 'banner', '5F0A87', '4', '1'),
(2, '54908_flash.png', 'Flash⚡', '', '000000', '1', '1'),
(3, '312_hourly.png', 'Hourly', '', '000000', '1', '1'),
(4, '43334_daily.png', 'Daily', '', '000000', '1', '1'),
(5, '84546_weekly.png', 'Weekly', '', '000000', '1', '1'),
(6, '18306_monthly.png', 'Monthly', '', '000000', '1', '1'),
(7, '96330_premium.png', 'Premium', '', '000000', '2', '1'),
(8, '14946_hourly.png', 'Hourly', '', '000000', '2', '1'),
(9, '32666_daily.png', 'Daily', '', '000000', '2', '1'),
(10, '79958_weekly.png', 'Weekly', '', '000000', '2', '1'),
(11, '33973_monthly.png', 'Monthly', '', '000000', '2', '1'),
(12, '87558_1.png', 'Electronics', '', '000000', '3', '1'),
(13, '87549_2.png', 'Books', '', '000000', '3', '1'),
(14, '23290_3.png', 'Beauty', '', '000000', '3', '1'),
(15, '8743_4.png', 'Fashion', '', '000000', '3', '1'),
(16, '67645_6.png', 'Appliances', '', '000000', '3', '1'),
(17, '15238_8.png', 'Sports', '', '000000', '3', '1');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_city`
--

CREATE TABLE `tbl_city` (
  `city_id` int(11) NOT NULL,
  `city_name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `city_image` varchar(255) CHARACTER SET utf8 NOT NULL,
  `city_bw_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city_status` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_coin_list`
--

CREATE TABLE `tbl_coin_list` (
  `c_id` int(11) NOT NULL,
  `c_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `c_image` varchar(255) CHARACTER SET utf8 NOT NULL,
  `c_coin` int(11) NOT NULL,
  `c_amount` int(11) NOT NULL,
  `c_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_coin_list`
--

INSERT INTO `tbl_coin_list` (`c_id`, `c_name`, `c_image`, `c_coin`, `c_amount`, `c_status`) VALUES
(1, 'Manual Purchase', '23685_coin1.png', 0, 0, 1),
(2, 'Ultimate Pack', '24740_coin6.png', 9000, 450, 1),
(3, 'Mega Pack', '10430_coin5.png', 5000, 275, 1),
(4, 'Elite Pack', '50839_coin5.png', 2000, 120, 1),
(5, 'Diamond Pack', '96249_coin4.png', 1000, 65, 1),
(6, 'Platinum Pack', '27840_coin4.png', 500, 35, 1),
(7, 'Gold Pack', '42045_coin3.png', 200, 15, 1),
(8, 'Silver Pack', '64621_coin2.png', 100, 9, 1),
(9, 'Bronze Pack', '927_coin2.png', 50, 4, 1),
(10, 'Basic Pack', '8298_coin1.png', 25, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_email`
--

CREATE TABLE `tbl_email` (
  `id` int(11) NOT NULL,
  `host` text COLLATE utf8_unicode_ci NOT NULL,
  `username` text COLLATE utf8_unicode_ci NOT NULL,
  `password` text COLLATE utf8_unicode_ci NOT NULL,
  `port` text COLLATE utf8_unicode_ci NOT NULL,
  `email` text COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_email`
--

INSERT INTO `tbl_email` (`id`, `host`, `username`, `password`, `port`, `email`, `name`, `status`) VALUES
(1, 'mail.wowcodes.in', 'no-reply@wowcodes.in', 'Krishna@WowCodes', '587', 'no-reply@wowcodes.in', 'WowCodes', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_fcm_token`
--

CREATE TABLE `tbl_fcm_token` (
  `id` int(11) NOT NULL,
  `u_id` int(11) NOT NULL DEFAULT '0',
  `fcm_token` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_games`
--

CREATE TABLE `tbl_games` (
  `id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `rps_image` varchar(255) CHARACTER SET utf8 NOT NULL,
  `rps_status` int(11) NOT NULL,
  `rps_win` varchar(255) CHARACTER SET utf8 NOT NULL,
  `rps_min` int(11) NOT NULL,
  `rps_max` varchar(255) CHARACTER SET utf8 NOT NULL,
  `rps_chance` varchar(255) CHARACTER SET utf8 NOT NULL,
  `gn_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gn_win` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gn_min` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gn_max` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gn_chance` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gn_status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spin_status` int(11) NOT NULL,
  `spin_image` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `spin_min` int(11) NOT NULL,
  `spin_max` int(11) NOT NULL,
  `spin_win_min` int(11) NOT NULL,
  `spin_win_max` int(11) NOT NULL,
  `ct_status` int(11) NOT NULL,
  `ct_image` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ct_min` int(11) NOT NULL,
  `ct_max` int(11) NOT NULL,
  `ct_win` int(11) NOT NULL,
  `ct_chance` int(11) NOT NULL,
  `cric_status` int(11) NOT NULL,
  `cric_image` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `cric_min` int(11) NOT NULL,
  `cric_max` int(11) NOT NULL,
  `cric_win` int(11) NOT NULL,
  `cric_chance` int(11) NOT NULL,
  `ouc_status` int(11) NOT NULL,
  `ouc_image` varchar(255) CHARACTER SET utf8 NOT NULL,
  `ouc_amount` int(11) NOT NULL,
  `ouc_bonus1` int(11) NOT NULL,
  `ouc_bonus2` int(11) NOT NULL,
  `ouc_bonus3` int(11) NOT NULL,
  `ouc_min` int(11) NOT NULL,
  `ouc_max` int(11) NOT NULL,
  `ouc_win_min` int(11) NOT NULL,
  `ouc_win_max` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_games`
--

INSERT INTO `tbl_games` (`id`, `rps_image`, `rps_status`, `rps_win`, `rps_min`, `rps_max`, `rps_chance`, `gn_image`, `gn_win`, `gn_min`, `gn_max`, `gn_chance`, `gn_status`, `spin_status`, `spin_image`, `spin_min`, `spin_max`, `spin_win_min`, `spin_win_max`, `ct_status`, `ct_image`, `ct_min`, `ct_max`, `ct_win`, `ct_chance`, `cric_status`, `cric_image`, `cric_min`, `cric_max`, `cric_win`, `cric_chance`, `ouc_status`, `ouc_image`, `ouc_amount`, `ouc_bonus1`, `ouc_bonus2`, `ouc_bonus3`, `ouc_min`, `ouc_max`, `ouc_win_min`, `ouc_win_max`) VALUES
('1', 'img_rps.png', 1, '50', 1, '50', '00', 'img_gn.png', '10', '1', '100', '50', '1', 1, 'img_spin.png', 10, 50, 10, 100, 1, 'img_coin.png', 10, 70, 150, 40, 1, 'img_cric.png', 1, 10, 10, 100, 1, 'img_ouc.png', 200, 5, 10, 20, 100, 300, 100, 100);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_hyip`
--

CREATE TABLE `tbl_hyip` (
  `plan_id` int(11) NOT NULL COMMENT 'id',
  `plan_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'name of the plan',
  `plan_short_description` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'short description',
  `plan_repeat_text` varchar(255) COLLATE utf8_unicode_ci DEFAULT ' ',
  `plan_minimum` decimal(11,0) NOT NULL COMMENT 'minimum investment',
  `plan_maximum` decimal(11,0) NOT NULL COMMENT 'maximum investment',
  `plan_fixed_amount` decimal(11,0) NOT NULL COMMENT 'fixed earning over regular interest',
  `plan_interest` decimal(11,0) NOT NULL COMMENT 'interest value',
  `plan_interest_type` int(11) NOT NULL COMMENT 'A value of 1 indicates percentage (%), while 0 indicates currency.',
  `plan_interest_frequency` int(11) NOT NULL DEFAULT '1' COMMENT 'If \r\n1 then per minutes\r\n2 per hours\r\n3 per days\r\n4 per weeks\r\n5 per month\r\n6 per year',
  `plan_color` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '2a2a72',
  `plan_repeat_time` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'repeat interest distribution in',
  `plan_repeat_day` int(11) NOT NULL DEFAULT '0',
  `plan_duration` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'duration of the plan',
  `plan_description` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'description of the plan',
  `plan_compound_interest` int(11) NOT NULL COMMENT '1 means yes, 0 means no',
  `plan_capital_back` int(11) NOT NULL DEFAULT '0',
  `plan_lifetime` int(11) NOT NULL DEFAULT '0',
  `plan_penalty` decimal(10,0) NOT NULL,
  `plan_penalty_type` decimal(10,0) NOT NULL,
  `plan_cancelable` int(11) NOT NULL,
  `plan_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Triggers `tbl_hyip`
--
DELIMITER $$
CREATE TRIGGER `trg_check_plan_repeat_time_format` BEFORE INSERT ON `tbl_hyip` FOR EACH ROW BEGIN
    IF NEW.plan_repeat_time NOT LIKE '__:__:__' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid format for plan_repeat_time. Format must be HH:MM:SS';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_hyip_order`
--

CREATE TABLE `tbl_hyip_order` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `plan_id` int(11) NOT NULL,
  `investment_amount` int(11) NOT NULL,
  `current_value` int(11) NOT NULL,
  `interest` varchar(255) NOT NULL DEFAULT '0',
  `last_interest_update` timestamp NULL DEFAULT NULL,
  `next_interest_update` timestamp NULL DEFAULT NULL,
  `order_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `plan_cancel_charge` int(11) NOT NULL DEFAULT '0',
  `status` varchar(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_items`
--

CREATE TABLE `tbl_items` (
  `item_id` int(11) NOT NULL,
  `o_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `o_desc` text COLLATE utf8_unicode_ci NOT NULL,
  `o_image` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `o_image1` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `o_image2` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `o_image3` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `o_image4` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `item_cat` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `price` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `item_status` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_kyc`
--

CREATE TABLE `tbl_kyc` (
  `kyc_id` int(11) NOT NULL,
  `u_id` int(11) DEFAULT NULL,
  `id_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_front` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_back` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_country` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dob` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `id_firstname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_lastname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `kyc_status` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_logcat`
--

CREATE TABLE `tbl_logcat` (
  `log_id` int(11) NOT NULL,
  `u_id` int(11) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_lottery_balls`
--

CREATE TABLE `tbl_lottery_balls` (
  `lottery_balls_id` int(11) NOT NULL,
  `lottery_balls_name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `normal_ball_start` int(11) NOT NULL,
  `normal_ball_end` int(11) NOT NULL,
  `normal_ball_limit` int(11) NOT NULL,
  `premium_ball_start` int(11) NOT NULL,
  `premium_ball_end` int(11) NOT NULL,
  `premium_ball_limit` int(11) NOT NULL,
  `created_on` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_lottery_balls`
--

INSERT INTO `tbl_lottery_balls` (`lottery_balls_id`, `lottery_balls_name`, `normal_ball_start`, `normal_ball_end`, `normal_ball_limit`, `premium_ball_start`, `premium_ball_end`, `premium_ball_limit`, `created_on`) VALUES
(1, 'Lotto 6/49', 1, 49, 6, 0, 0, 0, '2024-07-16 20:40:21'),
(2, 'Oz Lotto', 1, 45, 7, 0, 0, 0, '2024-07-16 20:41:03'),
(3, 'Lotto Max', 1, 100, 4, 0, 0, 0, '2024-07-16 20:41:37'),
(4, 'German Lotto', 1, 49, 6, 1, 10, 1, '2024-07-16 20:42:03'),
(5, 'Irish Lotto', 1, 47, 6, 0, 0, 0, '2024-07-16 20:42:46'),
(6, 'French Loto', 1, 49, 5, 1, 10, 1, '2024-07-16 20:42:46'),
(7, 'UK Thunderball', 1, 39, 5, 1, 14, 1, '2024-07-16 20:43:47'),
(8, 'Brazilian Mega-Sena', 1, 60, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(9, 'New York Lotto', 1, 59, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(10, 'Powerball (US)', 1, 69, 5, 1, 26, 1, '2024-07-16 20:44:27'),
(11, 'Mega Millions (US)', 1, 70, 5, 1, 25, 1, '2024-07-16 20:44:27'),
(12, 'EuroMillions (Europe)', 1, 50, 5, 1, 12, 2, '2024-07-16 20:44:27'),
(13, 'UK National Lottery (Lotto)', 1, 59, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(14, 'EuroJackpot', 1, 50, 5, 1, 10, 2, '2024-07-16 20:44:27'),
(15, 'California SuperLotto Plus', 1, 47, 5, 1, 27, 1, '2024-07-16 20:44:27'),
(16, 'Florida Lotto', 1, 53, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(17, 'Texas Lotto', 1, 54, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(18, 'New Jersey Pick-6', 1, 49, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(19, 'Lotto 6aus49 (Germany)', 1, 49, 6, 0, 9, 1, '2024-07-16 20:44:27'),
(20, 'La Primitiva (Spain)', 1, 49, 6, 0, 9, 1, '2024-07-16 20:44:27'),
(21, 'Vikinglotto', 1, 48, 6, 1, 5, 1, '2024-07-16 20:44:27'),
(22, 'Österreichische Lotterien (Austria Lotto)', 1, 45, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(23, 'Japan Loto 7', 1, 37, 7, 0, 0, 0, '2024-07-16 20:44:27'),
(24, 'Japan Loto 6', 1, 43, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(25, 'China Welfare Lottery', 1, 35, 5, 0, 0, 0, '2024-07-16 20:44:27'),
(26, 'Hong Kong Mark Six', 1, 49, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(27, 'Australia Powerball', 1, 35, 7, 1, 20, 1, '2024-07-16 20:44:27'),
(28, 'Saturday Lotto (Australia)', 1, 45, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(29, 'New Zealand Powerball', 1, 40, 6, 1, 10, 1, '2024-07-16 20:44:27'),
(30, 'Brazil Quina', 1, 80, 5, 0, 0, 0, '2024-07-16 20:44:27'),
(31, 'Argentina Loto', 1, 45, 6, 0, 0, 0, '2024-07-16 20:44:27'),
(32, 'South Africa Powerball', 1, 50, 5, 1, 20, 1, '2024-07-16 20:44:27'),
(33, 'South Africa Lotto', 1, 500, 1, 0, 0, 0, '2024-07-16 20:44:27'),
(34, 'EuroJackpot', 1, 49, 5, 1, 10, 2, '2024-07-16 20:44:27'),
(35, 'Vikinglotto', 1, 4, 7, 1, 5, 1, '2024-07-16 20:44:27'),
(36, '3D', 0, 999, 3, 0, 999, 0, '2024-11-17 15:50:44'),
(37, 'siamlotto', 0, 9, 6, 0, 99, 2, '2024-11-18 00:22:42');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_manual_payments`
--

CREATE TABLE `tbl_manual_payments` (
  `manual_payment_id` int(11) NOT NULL,
  `pg_id` int(11) NOT NULL,
  `transaction_id` text COLLATE utf8_unicode_ci NOT NULL,
  `amount` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `payment_date` date NOT NULL,
  `payment_screenshot` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `item_type` int(11) NOT NULL,
  `transaction_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_network`
--

CREATE TABLE `tbl_network` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `money` varchar(255) NOT NULL,
  `refferal_user_id` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_notifications`
--

CREATE TABLE `tbl_notifications` (
  `id` int(11) NOT NULL,
  `u_id` int(11) NOT NULL,
  `tittle` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `body` text COLLATE utf8_unicode_ci,
  `time` datetime DEFAULT NULL,
  `image` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'bell.png',
  `action` int(11) DEFAULT '0',
  `link` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_notification_settings`
--

CREATE TABLE `tbl_notification_settings` (
  `id` int(11) NOT NULL,
  `admin_user` varchar(255) CHARACTER SET utf8 NOT NULL,
  `admin_order` int(11) NOT NULL,
  `admin_coin` int(11) NOT NULL,
  `admin_winner` int(11) NOT NULL,
  `admin_bid` int(11) NOT NULL,
  `seller_order` int(11) NOT NULL,
  `user_order` int(11) NOT NULL,
  `user_scratch` int(11) NOT NULL,
  `user_referral` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_notification_settings`
--

INSERT INTO `tbl_notification_settings` (`id`, `admin_user`, `admin_order`, `admin_coin`, `admin_winner`, `admin_bid`, `seller_order`, `user_order`, `user_scratch`, `user_referral`) VALUES
(1, '1', 1, 0, 0, 1, 0, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_offers`
--

CREATE TABLE `tbl_offers` (
  `id` int(11) NOT NULL,
  `o_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `c_id` int(11) NOT NULL,
  `city_id` int(11) NOT NULL,
  `lottery_balls_id` int(11) NOT NULL DEFAULT '0' COMMENT 'Only for lottery',
  `o_type` varchar(255) NOT NULL,
  `o_amount` int(11) NOT NULL,
  `o_link` varchar(255) NOT NULL,
  `o_date` date NOT NULL,
  `o_edate` date NOT NULL,
  `o_stime` time NOT NULL,
  `o_etime` time NOT NULL,
  `o_color` varchar(255) DEFAULT NULL,
  `o_winners` int(11) NOT NULL DEFAULT '1',
  `winner_type` int(11) DEFAULT '1',
  `winner_id` int(11) NOT NULL DEFAULT '0',
  `winner_name` varchar(255) DEFAULT ' ',
  `winning_value` varchar(255) NOT NULL DEFAULT '0',
  `o_min` varchar(255) NOT NULL DEFAULT '1',
  `o_max` varchar(255) NOT NULL DEFAULT '50',
  `bid_increment` varchar(255) DEFAULT '0',
  `time_increment` int(11) DEFAULT '0',
  `o_price` varchar(255) NOT NULL DEFAULT '0',
  `o_buy` varchar(255) NOT NULL DEFAULT '0',
  `o_qty` varchar(255) DEFAULT '0',
  `o_status` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_order`
--

CREATE TABLE `tbl_order` (
  `o_id` int(11) NOT NULL,
  `u_id` int(11) NOT NULL,
  `seller_id` int(11) NOT NULL,
  `offer_id` int(11) NOT NULL COMMENT 'tbl_offers.item_id',
  `offer_o_id` int(11) NOT NULL COMMENT 'tbl_offers.o_id',
  `total_amount` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `dis_amount` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `pay_amount` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `redeem_item` int(11) NOT NULL DEFAULT '0' COMMENT 'is this withdrawl order?',
  `o_address` varchar(2554) COLLATE utf8_unicode_ci NOT NULL,
  `order_status` int(11) NOT NULL DEFAULT '1',
  `order_date` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `o_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_order_logs`
--

CREATE TABLE `tbl_order_logs` (
  `order_logs_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `order_status` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL DEFAULT '1',
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_payment_gateway`
--

CREATE TABLE `tbl_payment_gateway` (
  `pg_id` int(11) NOT NULL,
  `pg_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `pg_details` text COLLATE utf8_unicode_ci NOT NULL,
  `pg_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pg_type` int(11) NOT NULL COMMENT '1 - AutoApp, 2- AutoWeb, 3 - AutoBoth, 4- Manual',
  `pg_link` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pg_status` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_payment_gateway`
--

INSERT INTO `tbl_payment_gateway` (`pg_id`, `pg_name`, `pg_details`, `pg_image`, `pg_type`, `pg_link`, `pg_status`) VALUES
(1, 'Bank transfer', '<p><strong>Bank Details : </strong></p>\r\n\r\n<p>Bank Name: Dummy,</p>\r\n\r\n<p>Bank Account Number: 919339932830</p>\r\n', '58187_bank.png', 4, '', 1),
(2, 'PayPal', '', '43283_pg_paypal.png', 3, 'paypal', 1),
(3, 'RazorPay', '', '97258_pg_razorpay.png', 3, 'razorpay', 1),
(4, 'OpenPix', '', '60509_pg_openpix.png', 1, 'openpix', 1),
(5, 'Stripe', '', '28714_pg_stripe.png', 1, 'stripe', 1),
(6, 'Flutterwave', '', '68676_pg_flutterwave.png', 1, 'flutterwave', 1),
(7, 'MPesa', '', '18691_pg_mpesa.png', 3, 'mpesa', 1),
(8, 'Paystack', '', '10971_pg_paystack.png', 1, 'paystack', 1),
(9, 'Midtrans', '', '87150_pg_midtrans.png', 1, 'midtrans', 1),
(10, 'Instamojo', '', '87110_pg_instamojo.png', 2, '', 1),
(11, 'Cashfree', '', '36504_pg_cashfree.png', 2, '', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_prizes`
--

CREATE TABLE `tbl_prizes` (
  `prize_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `o_id` int(11) DEFAULT NULL,
  `rank_start` int(11) NOT NULL,
  `rank_end` int(11) NOT NULL,
  `chance` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_referral_bonus`
--

CREATE TABLE `tbl_referral_bonus` (
  `id` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `referral_bonus` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '0',
  `coin_purchase_bonus` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_reviews`
--

CREATE TABLE `tbl_reviews` (
  `id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `comment` text COLLATE utf8_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_scratch`
--

CREATE TABLE `tbl_scratch` (
  `s_id` int(11) NOT NULL COMMENT 'id',
  `u_id` int(11) DEFAULT NULL COMMENT 'user id',
  `s_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'name of scratch card',
  `s_desc` text COLLATE utf8_unicode_ci,
  `s_colour` int(11) DEFAULT NULL COMMENT 'color code',
  `s_type` int(11) DEFAULT NULL COMMENT 'type of card',
  `s_code` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'redeem code',
  `s_link` text CHARACTER SET utf8,
  `s_sdate` date DEFAULT NULL COMMENT 'start date',
  `s_stime` time DEFAULT NULL COMMENT 'start time',
  `s_edate` date DEFAULT NULL COMMENT 'expiry date',
  `s_etime` time DEFAULT NULL COMMENT 'expiry time',
  `s_status` int(11) DEFAULT NULL COMMENT 'status'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_settings`
--

CREATE TABLE `tbl_settings` (
  `id` int(11) NOT NULL,
  `coinvalue` varchar(255) NOT NULL,
  `showad` int(11) NOT NULL,
  `ads_reward` int(11) NOT NULL DEFAULT '0',
  `app_name` varchar(255) NOT NULL,
  `admin_email` varchar(255) NOT NULL,
  `timezone` varchar(255) NOT NULL,
  `signup_bonus` int(11) NOT NULL,
  `referral_bonus` int(11) NOT NULL COMMENT 'Coins earn for referring ',
  `refercode_bonus` int(11) NOT NULL COMMENT 'coins earn for using referral code',
  `commission` varchar(255) NOT NULL,
  `otp_system` int(11) NOT NULL,
  `twilio_sid` varchar(255) NOT NULL,
  `twilio_token` varchar(255) NOT NULL,
  `fcm_key` text NOT NULL,
  `demo_access` int(11) NOT NULL DEFAULT '0',
  `cashfree_appid` varchar(255) NOT NULL,
  `cashfree_secret` varchar(255) NOT NULL,
  `vungle_app` varchar(255) NOT NULL,
  `vungle_placement_rewarded` varchar(255) NOT NULL,
  `adcolony_app` varchar(255) NOT NULL,
  `adcolony_rewarded` varchar(255) NOT NULL,
  `unity_game` varchar(255) NOT NULL,
  `unity_rewarded` varchar(255) NOT NULL,
  `admob_rewarded` varchar(255) NOT NULL,
  `admob_interstitial` varchar(255) NOT NULL,
  `admob_banner` varchar(255) NOT NULL,
  `fb_rewarded` varchar(255) NOT NULL,
  `fb_interstitial` varchar(255) NOT NULL,
  `fb_banner` varchar(255) NOT NULL,
  `applovin_rewarded` varchar(255) NOT NULL,
  `startio_rewarded` varchar(255) NOT NULL,
  `ironsource_rewarded` varchar(255) NOT NULL,
  `mpesa_key` varchar(255) NOT NULL,
  `mpesa_code` varchar(255) NOT NULL,
  `paypal_id` varchar(255) NOT NULL,
  `paypal_secret` varchar(255) NOT NULL,
  `flutterwave_public` varchar(255) NOT NULL,
  `flutterwave_encryption` varchar(255) NOT NULL,
  `razorpay_key` varchar(255) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `paypal_currency` varchar(255) NOT NULL,
  `flutterwavecurrency` varchar(255) NOT NULL,
  `stripe_key` varchar(255) NOT NULL,
  `app_logo` varchar(255) NOT NULL,
  `how_to_play` text NOT NULL,
  `about_us` text NOT NULL,
  `app_privacy_policy` text NOT NULL,
  `app_link` varchar(255) NOT NULL,
  `app_logo_192` varchar(255) NOT NULL,
  `activation_key` text,
  `valid_till` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_settings`
--

INSERT INTO `tbl_settings` (`id`, `coinvalue`, `showad`, `ads_reward`, `app_name`, `admin_email`, `timezone`, `signup_bonus`, `referral_bonus`, `refercode_bonus`, `commission`, `otp_system`, `twilio_sid`, `twilio_token`, `fcm_key`, `demo_access`, `cashfree_appid`, `cashfree_secret`, `vungle_app`, `vungle_placement_rewarded`, `adcolony_app`, `adcolony_rewarded`, `unity_game`, `unity_rewarded`, `admob_rewarded`, `admob_interstitial`, `admob_banner`, `fb_rewarded`, `fb_interstitial`, `fb_banner`, `applovin_rewarded`, `startio_rewarded`, `ironsource_rewarded`, `mpesa_key`, `mpesa_code`, `paypal_id`, `paypal_secret`, `flutterwave_public`, `flutterwave_encryption`, `razorpay_key`, `currency`, `paypal_currency`, `flutterwavecurrency`, `stripe_key`, `app_logo`, `how_to_play`, `about_us`, `app_privacy_policy`, `app_link`, `app_logo_192`, `activation_key`, `valid_till`) VALUES
(1, '1', 0, 0, 'PrizeX', 'hello@wowcodes.in', 'Asia/Kolkata', 25, 20, 10, '10', 0, 'AC48934c7576f31dc664136fc80d23ca60', 'dc9fd697aac95dbf03412a98160172c2', 'AAAAnmBHzZc:APA91bHMpBxDxsh2FDtD8i7lbu7RURF3FraA2TnztO4pkOOh2VJq90kqKMNIDS8BqTAoEPSjbAlDj9eMOvwQp9om6kLJ4Z6A5MUZ7nHq642RF84gpQHopXC55D7m7Q-fHOkiVLVgMmcw', 0, '13293969f18a5fbbd167a34e7e939231', 'cfsk_ma_test_029a2eeef74f79e451da00c1f33ae921_896c84b6', '63a41dcb36f4ef317f7f48c6', 'REWARDED-7170310', 'appcc4f986128fe42b9b6', 'vz8b7f3adf411642f888', '5076899', 'Rewarded_Android', 'ca-app-pub-3940256099942544/5224354917', '', 'ca-app-pub-7444381611301835/3494727534', '888495932175050_888496582174985', '', 'IMG_16_9_APP_INSTALL#888495932175050_941843656840277', 'ca4a0c697d0cd5b3', '211239016', '17e1d1a85', 'dacec101824c46f67ea28f80ab68b9a7e4a3443c0fe9b915da8aecf5ccdbd92d', '4114595', 'ASxb9X35dPZHS8-CTihZiIW3dZWNyKlAZGEN5Ykk5a-rOxDjGCbcei5vIIGiZ7gujWum2jEVHL-UnFcX', 'EDiW7KAeXzjSE0a-cqWByI8x-4L0x2hBHmGKgLHWnQ4Xb5EK47HVsFUUbcNF5ahRgLvBeaa-wTfBazvj', 'FLWPUBK-81797a4166591b6b5f3e5650a77dbe36-X', '952970628f508400688317af', 'rzp_test_DIoVyzJUUK6w5t', '£', 'USD', 'NGN', 'pk_test_51NA6XPDO6wEedkgXaEu9egDfJJTqg1xIHOkYBJ5qBJjWhwBRqMgu6W4tBUlnVSHxgEkUNhxxsvnWaEpTNBwXnXss00o3ZgzJPf', '32_home_a.png', '', '', '<p><strong>At Our Website</strong>, accessible from our website and app, one of our main priorities is the privacy of our visitors and users. This Privacy Policy document contains types of information that is collected and recorded by us and how we use it. If you have additional questions or require more information about our Privacy Policy, do not hesitate to contact us. This Privacy Policy applies only to our online activities and is valid for visitors and users to our website and app with regards to the information that they shared and/or collect in our portal. This policy is not applicable to any information collected offline or via channels other than this website and app. Consent By using our website and app, you hereby consent to our Privacy Policy and agree to its terms. Information we collect The personal information that you are asked to provide, and the reasons why you are asked to provide it, will be made clear to you at the point we ask you to provide your personal information. If you contact us directly, we may receive additional information about you such as your name, email address, phone number, the contents of the message and/or attachments you may send us, and any other information you may choose to provide. When you register in our app, we may ask for your contact information, including items such as but not limited to name, address, email address, profile picture and telephone number. Your profile picture and name might be visible to other users in the app via leaderboards, winners page etc. We might also share your address to third party agencies for item deliveries How we use your information We use the information we collect in various ways, including to: Enhance your in app experience. Using your address to deliver the items purchased by you. Provide, operate, and maintain our website Improve, personalize, and expand our website Understand and analyze how you use our website Develop new products, services, features, and functionality Communicate with you, either directly or through one of our partners, including for customer service, to provide you with updates and other information relating to the website, and for marketing and promotional purposes Send you emails Find and prevent fraud Log Files We follow a standard procedure of using log files. These files log visitors when they visit websites. All hosting companies do this and a part of hosting services&#39; analytics. The information collected by log files include internet protocol (IP) addresses, browser type, Internet Service Provider (ISP), date and time stamp, referring/exit pages, and possibly the number of clicks. These are not linked to any information that is personally identifiable. The purpose of the information is for analyzing trends, administering the site, tracking users&#39; movement on the website, and gathering demographic information. Cookies and Web Beacons Like any other website, We use &#39;cookies&#39;. These cookies are used to store information including visitors&#39; preferences, and the pages on the website that the visitor accessed or visited. The information is used to optimize the users&#39; experience by customizing our web page content based on visitors&#39; browser type and/or other information. Advertising Partners Privacy Policies You may consult this list to find the Privacy Policy for each of the advertising partners of Us. Third-party ad servers or ad networks uses technologies like cookies, JavaScript, or Web Beacons that are used in their respective advertisements and links that appear on our portal, which are sent directly to users&#39; browser. They automatically receive your IP address when this occurs. These technologies are used to measure the effectiveness of their advertising campaigns and/or to personalize the advertising content that you see on websites that you visit. Note that we have no access to or control over these cookies that are used by third-party advertisers. Third Party Privacy Policies Our&#39;s Privacy Policy does not apply to other advertisers or websites. Thus, we are advising you to consult the respective Privacy Policies of these third-party ad servers for more detailed information. It may include their practices and instructions about how to opt-out of certain options. You can choose to disable cookies through your individual browser options. To know more detailed information about cookie management with specific web browsers, it can be found at the browsers&#39; respective websites. CCPA Privacy Rights (Do Not Sell My Personal Information) Under the CCPA, among other rights, California consumers have the right to: Request that a business that collects a consumer&#39;s personal data disclose the categories and specific pieces of personal data that a business has collected about consumers. Request that a business delete any personal data about the consumer that a business has collected. Request that a business that sells a consumer&#39;s personal data, not sell the consumer&#39;s personal data. If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us. GDPR Data Protection Rights We would like to make sure you are fully aware of all of your data protection rights. Every user is entitled to the following: The right to access &ndash; You have the right to request copies of your personal data. We may charge you a small fee for this service. The right to rectification &ndash; You have the right to request that we correct any information you believe is inaccurate. You also have the right to request that we complete the information you believe is incomplete. The right to erasure &ndash; You have the right to request that we erase your personal data, under certain conditions. The right to restrict processing &ndash; You have the right to request that we restrict the processing of your personal data, under certain conditions. The right to object to processing &ndash; You have the right to object to our processing of your personal data, under certain conditions. The right to data portability &ndash; You have the right to request that we transfer the data that we have collected to another organization, or directly to you, under certain conditions. If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us. Children&#39;s Information Another part of our priority is adding protection for children while using the internet. We encourage parents and guardians to observe, participate in, and/or monitor and guide their online activity. Our Portal does not knowingly collect any Personal Identifiable Information from children under the age of 13. If you think that your child provided this kind of information on our website, we strongly encourage you to contact us immediately via email and we will do our best efforts to promptly remove such information from our records. Changes to This Privacy Policy We may update our Privacy Policy from time to time. Thus, we advise you to review this page periodically for any changes. We will notify you of any changes by posting the new Privacy Policy on this page. These changes are effective immediately, after they are posted on this page. Contact Us If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us. Our Privacy Policy was created with the help of the TermsFeed Privacy Policy Generator.</p>', 'https://wowcodes.in/products/prizex/prizex-wowcodes.in.apk', 'logo_192.png', 'a9ee53caae598c35d1b5b409e7174ea2ead74c4ff6bbb74958cb5cc6c84b108d', '');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_ticket`
--

CREATE TABLE `tbl_ticket` (
  `ticket_id` int(11) NOT NULL,
  `ball_1` int(11) DEFAULT NULL,
  `ball_2` int(11) DEFAULT NULL,
  `ball_3` int(11) DEFAULT NULL,
  `ball_4` int(11) DEFAULT NULL,
  `ball_5` int(11) DEFAULT NULL,
  `ball_6` int(11) DEFAULT NULL,
  `ball_7` int(11) DEFAULT NULL,
  `ball_8` int(11) DEFAULT NULL,
  `u_id` int(11) NOT NULL,
  `o_id` int(11) NOT NULL,
  `ticket_price` int(11) NOT NULL,
  `purchase_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `unique_ticket_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ticket_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_transaction`
--

CREATE TABLE `tbl_transaction` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `type_no` int(11) NOT NULL,
  `date` varchar(255) NOT NULL,
  `money` varchar(255) NOT NULL,
  `comments` text
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_users`
--

CREATE TABLE `tbl_users` (
  `id` int(11) NOT NULL,
  `login_type` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT '../placeholder_user.jpg',
  `password` varchar(255) NOT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `remember_me_token` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  `language` varchar(255) NOT NULL DEFAULT 'en',
  `date` varchar(255) DEFAULT NULL,
  `wallet` varchar(11) NOT NULL DEFAULT '0',
  `code` int(11) NOT NULL DEFAULT '0',
  `refferal_code` int(11) DEFAULT NULL,
  `confirm_code` varchar(255) DEFAULT NULL,
  `network` int(11) NOT NULL DEFAULT '0',
  `ban` int(11) NOT NULL DEFAULT '0',
  `status` varchar(255) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user_achievements`
--

CREATE TABLE `tbl_user_achievements` (
  `user_achievement_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT 'Foreign key referencing the user who has this achievement',
  `achievement_id` int(11) NOT NULL COMMENT 'Foreign key referencing the achievement',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT 'Current status of the achievement for the user',
  `progress` int(11) NOT NULL DEFAULT '0' COMMENT 'Current progress towards achieving the goal',
  `earned_at` datetime DEFAULT NULL COMMENT 'Timestamp when the achievement was earned, NULL if not yet earned',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the user-achievement entry was created',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Timestamp of the last update to the user-achievement entry'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_vendor`
--

CREATE TABLE `tbl_vendor` (
  `id` int(11) NOT NULL,
  `email` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `username` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `about` text CHARACTER SET utf8,
  `password` varchar(100) CHARACTER SET utf8 NOT NULL,
  `link` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `ratting` varchar(11) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `permission_auction` int(11) NOT NULL DEFAULT '0',
  `permission_lottery` int(11) NOT NULL DEFAULT '0',
  `permission_shop` int(11) NOT NULL DEFAULT '0',
  `permission_coin` int(11) NOT NULL DEFAULT '0',
  `balance` int(11) DEFAULT '0',
  `joining_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_wallet_passbook`
--

CREATE TABLE `tbl_wallet_passbook` (
  `wp_id` int(11) NOT NULL,
  `wp_user` int(11) NOT NULL,
  `wp_package_id` int(11) NOT NULL,
  `wp_order_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `wp_date` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `wp_status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_winners`
--

CREATE TABLE `tbl_winners` (
  `id` int(11) NOT NULL,
  `o_id` int(11) DEFAULT NULL,
  `u_id` int(11) NOT NULL,
  `participation_id` int(11) NOT NULL,
  `winner_rank` int(11) DEFAULT NULL,
  `winner_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `winning_value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_wishlist`
--

CREATE TABLE `tbl_wishlist` (
  `wishlist_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_achievements`
--
ALTER TABLE `tbl_achievements`
  ADD PRIMARY KEY (`achievement_id`);

--
-- Indexes for table `tbl_address`
--
ALTER TABLE `tbl_address`
  ADD PRIMARY KEY (`address_id`);

--
-- Indexes for table `tbl_admin`
--
ALTER TABLE `tbl_admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_admin_logs`
--
ALTER TABLE `tbl_admin_logs`
  ADD PRIMARY KEY (`log_id`);

--
-- Indexes for table `tbl_bid`
--
ALTER TABLE `tbl_bid`
  ADD PRIMARY KEY (`bd_id`);

--
-- Indexes for table `tbl_bot`
--
ALTER TABLE `tbl_bot`
  ADD PRIMARY KEY (`bot_id`);

--
-- Indexes for table `tbl_cat`
--
ALTER TABLE `tbl_cat`
  ADD PRIMARY KEY (`c_id`);

--
-- Indexes for table `tbl_city`
--
ALTER TABLE `tbl_city`
  ADD PRIMARY KEY (`city_id`);

--
-- Indexes for table `tbl_coin_list`
--
ALTER TABLE `tbl_coin_list`
  ADD PRIMARY KEY (`c_id`);

--
-- Indexes for table `tbl_email`
--
ALTER TABLE `tbl_email`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_fcm_token`
--
ALTER TABLE `tbl_fcm_token`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_games`
--
ALTER TABLE `tbl_games`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_hyip`
--
ALTER TABLE `tbl_hyip`
  ADD PRIMARY KEY (`plan_id`);

--
-- Indexes for table `tbl_hyip_order`
--
ALTER TABLE `tbl_hyip_order`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `plan_id` (`plan_id`);

--
-- Indexes for table `tbl_items`
--
ALTER TABLE `tbl_items`
  ADD PRIMARY KEY (`item_id`);

--
-- Indexes for table `tbl_kyc`
--
ALTER TABLE `tbl_kyc`
  ADD PRIMARY KEY (`kyc_id`);

--
-- Indexes for table `tbl_logcat`
--
ALTER TABLE `tbl_logcat`
  ADD PRIMARY KEY (`log_id`);

--
-- Indexes for table `tbl_lottery_balls`
--
ALTER TABLE `tbl_lottery_balls`
  ADD PRIMARY KEY (`lottery_balls_id`);

--
-- Indexes for table `tbl_manual_payments`
--
ALTER TABLE `tbl_manual_payments`
  ADD PRIMARY KEY (`manual_payment_id`),
  ADD UNIQUE KEY `transaction_id` (`transaction_id`(255));

--
-- Indexes for table `tbl_network`
--
ALTER TABLE `tbl_network`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_notifications`
--
ALTER TABLE `tbl_notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_notification_settings`
--
ALTER TABLE `tbl_notification_settings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_offers`
--
ALTER TABLE `tbl_offers`
  ADD PRIMARY KEY (`o_id`);

--
-- Indexes for table `tbl_order`
--
ALTER TABLE `tbl_order`
  ADD PRIMARY KEY (`o_id`);

--
-- Indexes for table `tbl_order_logs`
--
ALTER TABLE `tbl_order_logs`
  ADD PRIMARY KEY (`order_logs_id`);

--
-- Indexes for table `tbl_payment_gateway`
--
ALTER TABLE `tbl_payment_gateway`
  ADD PRIMARY KEY (`pg_id`);

--
-- Indexes for table `tbl_prizes`
--
ALTER TABLE `tbl_prizes`
  ADD PRIMARY KEY (`prize_id`);

--
-- Indexes for table `tbl_referral_bonus`
--
ALTER TABLE `tbl_referral_bonus`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `level` (`level`);

--
-- Indexes for table `tbl_reviews`
--
ALTER TABLE `tbl_reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `item_id` (`item_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `tbl_scratch`
--
ALTER TABLE `tbl_scratch`
  ADD PRIMARY KEY (`s_id`);

--
-- Indexes for table `tbl_settings`
--
ALTER TABLE `tbl_settings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_ticket`
--
ALTER TABLE `tbl_ticket`
  ADD PRIMARY KEY (`ticket_id`);

--
-- Indexes for table `tbl_transaction`
--
ALTER TABLE `tbl_transaction`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_users`
--
ALTER TABLE `tbl_users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_user_achievements`
--
ALTER TABLE `tbl_user_achievements`
  ADD PRIMARY KEY (`user_achievement_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `achievement_id` (`achievement_id`);

--
-- Indexes for table `tbl_vendor`
--
ALTER TABLE `tbl_vendor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_wallet_passbook`
--
ALTER TABLE `tbl_wallet_passbook`
  ADD PRIMARY KEY (`wp_id`);

--
-- Indexes for table `tbl_winners`
--
ALTER TABLE `tbl_winners`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_wishlist`
--
ALTER TABLE `tbl_wishlist`
  ADD PRIMARY KEY (`wishlist_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_achievements`
--
ALTER TABLE `tbl_achievements`
  MODIFY `achievement_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each achievement';

--
-- AUTO_INCREMENT for table `tbl_address`
--
ALTER TABLE `tbl_address`
  MODIFY `address_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_admin`
--
ALTER TABLE `tbl_admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbl_admin_logs`
--
ALTER TABLE `tbl_admin_logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_bid`
--
ALTER TABLE `tbl_bid`
  MODIFY `bd_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_bot`
--
ALTER TABLE `tbl_bot`
  MODIFY `bot_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_cat`
--
ALTER TABLE `tbl_cat`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `tbl_city`
--
ALTER TABLE `tbl_city`
  MODIFY `city_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_coin_list`
--
ALTER TABLE `tbl_coin_list`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `tbl_email`
--
ALTER TABLE `tbl_email`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbl_fcm_token`
--
ALTER TABLE `tbl_fcm_token`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_hyip`
--
ALTER TABLE `tbl_hyip`
  MODIFY `plan_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id';

--
-- AUTO_INCREMENT for table `tbl_hyip_order`
--
ALTER TABLE `tbl_hyip_order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_items`
--
ALTER TABLE `tbl_items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_kyc`
--
ALTER TABLE `tbl_kyc`
  MODIFY `kyc_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_logcat`
--
ALTER TABLE `tbl_logcat`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_lottery_balls`
--
ALTER TABLE `tbl_lottery_balls`
  MODIFY `lottery_balls_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `tbl_manual_payments`
--
ALTER TABLE `tbl_manual_payments`
  MODIFY `manual_payment_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_network`
--
ALTER TABLE `tbl_network`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_notifications`
--
ALTER TABLE `tbl_notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_notification_settings`
--
ALTER TABLE `tbl_notification_settings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbl_offers`
--
ALTER TABLE `tbl_offers`
  MODIFY `o_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_order`
--
ALTER TABLE `tbl_order`
  MODIFY `o_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_order_logs`
--
ALTER TABLE `tbl_order_logs`
  MODIFY `order_logs_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_payment_gateway`
--
ALTER TABLE `tbl_payment_gateway`
  MODIFY `pg_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tbl_prizes`
--
ALTER TABLE `tbl_prizes`
  MODIFY `prize_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_referral_bonus`
--
ALTER TABLE `tbl_referral_bonus`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_reviews`
--
ALTER TABLE `tbl_reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_scratch`
--
ALTER TABLE `tbl_scratch`
  MODIFY `s_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id';

--
-- AUTO_INCREMENT for table `tbl_settings`
--
ALTER TABLE `tbl_settings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbl_ticket`
--
ALTER TABLE `tbl_ticket`
  MODIFY `ticket_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_transaction`
--
ALTER TABLE `tbl_transaction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_users`
--
ALTER TABLE `tbl_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_user_achievements`
--
ALTER TABLE `tbl_user_achievements`
  MODIFY `user_achievement_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_vendor`
--
ALTER TABLE `tbl_vendor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_wallet_passbook`
--
ALTER TABLE `tbl_wallet_passbook`
  MODIFY `wp_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_winners`
--
ALTER TABLE `tbl_winners`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_wishlist`
--
ALTER TABLE `tbl_wishlist`
  MODIFY `wishlist_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_hyip_order`
--
ALTER TABLE `tbl_hyip_order`
  ADD CONSTRAINT `tbl_hyip_order_ibfk_2` FOREIGN KEY (`plan_id`) REFERENCES `tbl_hyip` (`plan_id`);

--
-- Constraints for table `tbl_reviews`
--
ALTER TABLE `tbl_reviews`
  ADD CONSTRAINT `tbl_reviews_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `tbl_items` (`item_id`),
  ADD CONSTRAINT `tbl_reviews_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`id`);

--
-- Constraints for table `tbl_user_achievements`
--
ALTER TABLE `tbl_user_achievements`
  ADD CONSTRAINT `tbl_user_achievements_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`id`),
  ADD CONSTRAINT `tbl_user_achievements_ibfk_2` FOREIGN KEY (`achievement_id`) REFERENCES `tbl_achievements` (`achievement_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
