## 注册Sonatype账号

使用过Jira的用户就很熟悉了，事务与项目跟踪软件。注册好之后也可以用这个账号登陆maven公服仓库https://oss.sonatype.org/

**注意：Username 一定不要是中文，一定要是英文！！！**

## 创建一个Jira
Project: Open Source Project Repository Hosting (OSSRH)

Issue Type: New Project

下面是我项目的配置
[![WX20180708-221640@2x.png](https://i.loli.net/2018/07/08/5b421cd991b36.png)](https://i.loli.net/2018/07/08/5b421cd991b36.png)
注意Group Id 要和项目中pom配置的一样，一定要是域名的反写，这里推荐使用github的域名(如果自己没有长期维护的域名)，自己的域名可能会过期github可是不能随随便便的过期吧~

Project URL 就是你项目再Github上的地址；

SCM url 就是项目clone地址

ok，创建好之后就等待老外回复吧。因为有时差，所以一般他们晚上十点钟以后才能去审查，所以第一次配置的的时候一定要准确，不然改一次要等一天哦~~~

正确的审核反馈如下：

[![WX20180708-222208@2x.png](https://i.loli.net/2018/07/08/5b421e1a8dbad.png)](https://i.loli.net/2018/07/08/5b421e1a8dbad.png)

## 修改项目Pom

这个也是比较重要的
一定要有以下结构

```
<description>A Simple Java Crawler Framework</description>
<licenses>
    <license>
        <name>The Apache Software License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
</licenses>
<developers>
    <developer>
        <name>wangxc</name>
        <email>vector4wang@qq.com</email>
    </developer>
</developers>
<scm>
    <connection>
        scm:git:https://github.com/vector4wang/vw-crawler.git
    </connection>
    <developerConnection>
        scm:git:https://github.com/vector4wang/vw-crawler.git
    </developerConnection>
    <url>https://github.com/vector4wang/vw-crawler</url>
</scm>
```

这是Nexus Rules规定的，不然会出错！

然后就是构建插件与配置

```
<distributionManagement>
    <snapshotRepository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </snapshotRepository>
    <repository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
</distributionManagement>
<build>
    <plugins>
        <plugin>
            <groupId>org.sonatype.plugins</groupId>
            <artifactId>nexus-staging-maven-plugin</artifactId>
            <version>1.6.7</version>
            <extensions>true</extensions>
            <configuration>
                <serverId>ossrh</serverId>
                <nexusUrl>https://oss.sonatype.org/</nexusUrl>
                <autoReleaseAfterClose>true</autoReleaseAfterClose>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
            <version>2.2.1</version>
            <executions>
                <execution>
                    <id>attach-sources</id>
                    <goals>
                        <goal>jar-no-fork</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <!-- java doc -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-javadoc-plugin</artifactId>
            <version>2.9.1</version>
            <executions>
                <execution>
                    <id>attach-javadocs</id>
                    <goals>
                        <goal>jar</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <!-- GPG -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-gpg-plugin</artifactId>
            <version>1.5</version>
            <executions>
                <execution>
                    <id>sign-artifacts</id>
                    <phase>verify</phase>
                    <goals>
                        <goal>sign</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.5.1</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

distributionManagement中对应MavenHome中的setting的文件配置，如下

```
<servers>
    <server>
      <id>ossrh</id>
      <username>UserName</username>
      <password>Password</password>
    </server>
</servers>
```

用户名和密码就是你注册sonatype时的用户名和密码，id要对应pom里的id

nexus-staging-maven-plugin 这个插件是在成功发布到公服上的时候不需要手动去改变status(close,有的文章介绍说要手动关闭)

maven-javadoc-plugin 这个也比较重要，生成javadoc，要求代码里如果要使用注释，就要按照规范去注释，这个大家可以查找相关内容了解下，你也可以在deploy的时候按照提示去修改代码

maven-gpg-plugin 这个是用来生成私钥，下面会用到

ps：我的电脑是mac，在打包的时候报错，说找不到java home，在properti中加上下面配置就行了

```
<javadocExecutable>${java.home}/../bin/javadoc</javadocExecutable>
```

## 安装gpg

windows用户在[gpg4win](https://www.gpg4win.org/)这里下载，mac可以下载GPG_suite
因为都是图形界面，所以直接创建新的秘钥
需要输入用户名、邮箱和密码，一定要记住这个密码

之后需要把此秘钥发布到公钥服务器上(因为是图形工具，很简单，如果是命令行，还请在网上找一下)

## 发布
一切配置好之后，可以使用`mvn clean deploy`看一下结果，如果想发布release版本的需要把version中的snapshot去掉即可~

发布的时候提示你输入密码，这个密码就是上一节中你输入的密码！

之后就可以在仓库中找到自己发布的jar包了，发布release之后，要回到jira上接着评论告知已经发布，可以关闭掉这个jira了！！！


## 后记

最大的问题就是时差问题，因为你遇到的问题可能需要老外那边协助，比如重置一些权限或者其他稀奇古怪的问题，这样一等就是一天。所以要准备十点以后，一旦jira有回复，立马去修改去尝试，然后再告知老外，那是老外可能会立即做出回应，就不需要等一天了~~~