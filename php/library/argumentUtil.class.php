<?php
require_once dirname(__FILE__) . "/import.php";

class ArgumentUtil {
	private static $instance = null;
	
	private $args;
	private function __construct() {
		$arg_manager = ArgumentManager::getInstance();
		global $_JAVAARG;
		$this->args = $arg_manager->getArgument($_JAVAARG);
	}
	public static function getInstance() {
		if(self::$instance == null) {
			self::$instance = new ArgumentUtil();
		}
		return self::$instance;
	}
	public function setRetval($retval) {
		$arg_manager = ArgumentManager::getInstance();
		global $_JAVAARG;
		$arg_manager->setRetval($_JAVAARG, $retval);
	}
	public function getAdapter() {
		return $this->getJavaObject(0);
	}
	public function getJavaObject($index) {
		if($index >= count($this->args)) {
			return null;
		}
		return $this->args[$index];
	}
	public function getJavaString($java_string) {
		$arg_manager = ArgumentManager::getInstance();
		$string = $arg_manager->toByteString($java_string);
		return pach("H*", $string);
	}
/*	public function getIoBufferAsString($index) {
		if($index >= count($this->args)) {
			return null;
		}
		return trim(new String($this->args[$index]->array()));
	}// */
}
