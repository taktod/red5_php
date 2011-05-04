<?php
require_once dirname(__FILE__) . "/import.php";

class ConnectionUtil {
	public function invoke($conn, $func, $array) {	
		$wrapper = new ConnectionWrapper($conn);
		$params = array();
		foreach($array as $param) {
			if(is_string($param)) {
				$params[] = bin2hex($param);
			}
			else {
				$params[] = $param;
			}
		}
		$wrapper->invoke($func, $params);
	}
}