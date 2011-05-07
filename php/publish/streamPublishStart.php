<?php
require_once dirname(__FILE__) . "/../library/argumentUtil.class.php";
require_once dirname(__FILE__) . "/../library/systemUtil.class.php";

// stream publish event.
SystemUtil::println("streamPublishStart");
$arg_manager = ArgumentUtil::getInstance();

// get the arguments for event.
$adapter = $arg_manager->getAdapter();
$stream  = $arg_manager->getJavaObject(1);

// do something.
