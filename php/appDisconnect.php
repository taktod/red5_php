<?php
require_once dirname(__FILE__) . "/library/argumentUtil.class.php";
require_once dirname(__FILE__) . "/library/systemUtil.class.php";

// client disconnect event for Application root.
SystemUtil::println("appDisconnect");
$arg_manager = ArgumentUtil::getInstance();

// get the arguments for event.
$adapter = $arg_manager->getAdapter();
$conn    = $arg_manager->getJavaObject(1);

// do something.
