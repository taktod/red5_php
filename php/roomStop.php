<?php
require_once dirname(__FILE__) . "/library/argumentUtil.class.php";
require_once dirname(__FILE__) . "/library/systemUtil.class.php";

// room stop event.
SystemUtil::println("roomStop");
$arg_manager = ArgumentUtil::getInstance();

// get the arguments for event.
$adapter = $arg_manager->getAdapter();
$scope   = $arg_manager->getJavaObject(1);

// do something.
