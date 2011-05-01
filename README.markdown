# Red5_php.

## 概要 / what is this.
Red5のアプリケーションをPHPで書く。
<br />PHPで書いた部分はプログラムを更新すると、即コンパイルされます。
<br />JITコンパイルが効くので、高速に動作します。

This is how to write red5 application with PHP(querccus).
<br />the program on php is compiled immediately.
<br />Jit will provide high speed working.

## 使い方 / how to use
1. phpExt.jarをred5のpluginディレクトリに設置する。
2. quercusのjarファイルをpluginディレクトリに設置する。(javamail-141.jar, inject-16.jar, resin.jar)
3. webapps/{application}/WEB-INF/php以下にphpディレクトリのプログラムを設置する。
4. red5を起動する。

<br />

1. put phpExt.jar on [red5_hone]/plugins/
2. put the quercus jar files on plugin directories.(javamail-141.jar, inject-16.jar, resin.jar)
3. put the php programs on webapps/{application}/WEB-INF/php
4. execute red5.sh

## required Jar libraries.
inject-16.jar
<br />javamail-141.jar
<br />resin.jar
<br />(すべてQuercusのダウンロードページのwarファイルより入手 / please download from quercus project. war archive does have them.)

## 注意 / note
このプログラムで使われているBroadcastStream.javaのデータは最新のRed5のプログラムに対応させてあります。
<br />古いサーバーの場合はBroadcastStream.javaの以下の部分を修正して、アーカイブしなおしてください。
<br />220行目周り
<br />RTMPMessage msg = RTMPMessage.build(rtmpEvent);
<br />// RTMPMessage msg = new RTMPMessage();
<br />// msg.setBody(rtmpEvent);

// RTMPMessage msg = RTMPMessage.build(rtmpEvent);
<br />RTMPMessage msg = new RTMPMessage();
<br />msg.setBody(rtmpEvent);

This program is for newest red5 program.
<br />to use older version please fix BroadcastStream.java
<br />around line #220
<br />RTMPMessage msg = RTMPMessage.build(rtmpEvent);
<br />// RTMPMessage msg = new RTMPMessage();
<br />// msg.setBody(rtmpEvent);

// RTMPMessage msg = RTMPMessage.build(rtmpEvent);
<br />RTMPMessage msg = new RTMPMessage();
<br />msg.setBody(rtmpEvent);
