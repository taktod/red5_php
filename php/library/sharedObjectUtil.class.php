<?php
require_once dirname(__FILE__) . "/import.php";

class SharedObjectUtil {
	public function setAttribute($shared_object, $name, $data) {
		$wrapper = new SharedObjectWrapper($shared_object);
		if(is_string($data)) {
			// 文字列オブジェクトはhexに変更しておく。
			$data = bin2hex($data);
		}
		$wrapper->setAttribute($name, $data);
	}
}