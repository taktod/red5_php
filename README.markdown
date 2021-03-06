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
4. -Dfile.encoding=UTF8をred5.shに追加する。
5. red5を起動する。

<br />

1. put phpExt.jar on [red5_hone]/plugins/
2. put the quercus jar files on plugin directories.(javamail-141.jar, inject-16.jar, resin.jar)
3. put the php programs on webapps/{application}/WEB-INF/php
4. -Dfile.encoding=UTF8, add this on the java start script(red5.sh)
5. execute red5.sh

## required Jar libraries.
inject-16.jar
<br />javamail-141.jar
<br />resin.jar
<br />(すべてQuercusのダウンロードページのwarファイルより入手 / please download from quercus project. war archive does have them.)

## required Jar libraries for compile.
javaee-api-5.1.2.jar from red5
<br />log4j-over-slf4j.1.6.1.jar from red5
<br />logback-classic-0.9.26.jar from red5
<br />logback-core-0.9.26.jar from red5
<br />mina-core-2.0.3.jar from red5
<br />inject-16.jar from Quercus
<br />javamail-141.jar from Quercus
<br />resin.jar from Quercus
<br />red5.jar from red5
<br />com.springsource.slf4j.api-1.6.1.jar from red5
<br />org.springframework.context-3.0.5.RELEASE.jar from red5
<br />org.springframework.beans-3.0.5.RELEASE.jar from red5
<br />org.springframework.core-3.0.5.RELEASE.jar from red5
<br />bcprov.jdk16-145.jar from red5
<br />ehcache-2.2.0.jar from red5
<br />commons-beanutils-1.8.2.jar from red5
<br />com.springsource.org.apache.commons.collections-3.2.1.jar from red5

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

## ディレクトリ指定をApplicationAdapterPhpのBeanに追加しました。 / add the directory property for ApplicationAdapterPhp
red5-web.xmlのweb.handlerのbean指定にdirectoryのPropertyを指定してください。(存在しない場合はWEB-INF/php以下になります。)
<pre>
        <bean id="web.handler" class="com.ttProject.red5.server.adapter.ApplicationAdapterPhp">
                <property name="directory" value="/home/red5/" />
        </bean>
</pre>
