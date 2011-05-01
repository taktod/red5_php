<?php
require_once dirname(__FILE__) . "/library/argumentUtil.class.php";
require_once dirname(__FILE__) . "/library/systemUtil.class.php";

// start event.
SystemUtil::println("start");
$arg_manager = ArgumentUtil::getInstance();

// get the arguments for event.
$adapter = $arg_manager->getAdapter();
$scope   = $arg_manager->getJavaObject(1);

// do something.

// reply true for connect, false for disconnect.
$arg_manager->setRetval(true);
