<?php
require_once dirname(__FILE__) . "/library/argumentUtil.class.php";
require_once dirname(__FILE__) . "/library/systemUtil.class.php";

// client join event for Application root.
SystemUtil::println("appJoin");
$arg_manager = ArgumentUtil::getInstance();

// get the arguments for event
$adapter = $arg_manager->getAdapter();
$client  = $arg_manager->getJavaObject(1);
$scope   = $arg_manager->getJavaObject(2);

// do something.

// reply true for connect, false for disconnect.
$arg_manager->setRetval(true);
