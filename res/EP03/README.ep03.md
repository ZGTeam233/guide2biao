# EP03
-   [点击蓝字回到首页](https://gitee.com/zgteam233/guide2biao)
-   本期为EP03 路由设置指南（网络篇）
-   该内容仍有**硬核**成分，涉及*Linux网络原理*，耐心阅读
-   [**点此进入正文！！！**](https://gitee.com/zgteam233/guide2biao/blob/main/res/EP03/part2.ep03.md)
-   [**点此进入正文！！！**](https://gitee.com/zgteam233/guide2biao/blob/main/res/EP03/part2.ep03.md)
-   [**点此进入正文！！！**](https://gitee.com/zgteam233/guide2biao/blob/main/res/EP03/part2.ep03.md)
-   下为专有名词解释，可以先**跳过**
-   后面这些词都会**标蓝**
-   点击即可**跳转入解释**

#### TCP和UDP
-   这是两种**传输协议**
-   *分别表示按顺序收发* 和 *保证速率收发*
-   **常使用TCP**，大多是游戏常使用UDP
-   少数如游戏如*Minecraft Java*用TCP
-   ~~JAVA25都出来了，还没意识到自己最大的受众是MC~~
-   ![pic](/images/017.png)

#### IPv4/v6
-   **是IPv4和IPv6的合称**
-   IPv4形如*192.168.31.1*，共四段，每段可取**10进制***0*~*255*
-   IPv6形如*fe80:0000:0001:0000:0440:44ff:1233:5678*，共八段，每段可取**16进制***0*~*ffff*

#### MAC地址
-   **网卡物理地址**
-   形如*f3:27:7f:2a:5e:6c*，共六段，每段可取**16进制***0*~*ff*
-   是任何网络设备的唯一标识，但是当今设备多可虚拟MAC

#### VPN
-   即**虚拟个人网络**
-   常用于*翻墙*

#### br-LAN
-   指**局域网网桥**
-   *br*前缀指*Linux Network Bridge*（*Linux网桥*），用于串联*网口*
-   在AP中常将把标为LAN的*网口*用网桥串联在一个*接口*上
-   *网口*在此指**AP上的实体网口**
-   *接口*指**AP系统中软件调用的软接口**
-   **后文均以此称呼**

#### WLAN

