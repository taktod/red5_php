<?php
require_once dirname(__FILE__) . "/library/argumentUtil.class.php";
require_once dirname(__FILE__) . "/library/systemUtil.class.php";

// client connect event.
SystemUtil::println("actionEvent");
$arg_manager = ArgumentUtil::getInstance();

// get the arguments for event.
$adapter = $arg_manager->getAdapter();
$event   = $arg_manager->getJavaObject(1);

// do something.

