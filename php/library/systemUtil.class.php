<?php
require_once dirname(__FILE__) . "/import.php";

class SystemUtil {
	public static function println($msg) {
		echo $msg . System::getProperty("line.separator");
	}
}