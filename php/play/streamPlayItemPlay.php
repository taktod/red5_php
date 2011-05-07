<?php
require_once dirname(__FILE__) . "/../library/argumentUtil.class.php";
require_once dirname(__FILE__) . "/../library/systemUtil.class.php";

// stream close event.
SystemUtil::println("streamPlayItemPlay");
$arg_manager = ArgumentUtil::getInstance();

// get the arguments for event.
$adapter = $arg_manager->getAdapter();
$stream  = $arg_manager->getJavaObject(1);
$item    = $arg_manager->getJavaObject(2);
$isLive  = $arg_manager->getJavaObject(3);

// do something.
