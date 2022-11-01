## jw_project  jw后台管理系统

觉得好用就点个star吧 :pray: ，不喜欢的可以骂，没必要划走 :punch: 

 **目前已完成功能:** 

 **1. 一个简单的springboot+vue权限管理后台** 

 **2. 基于FaceNet+ RetinaFace的人脸检测功能** 

**TODO：安装教程太简洁了，后续打算写个word文档** 

**_目前系统存在的bug：（逻辑性bug目前应该没有，下面是一些优化想法）_** 

 **_①登录流程存在大量重复查询和多余属性，登录流程太慢_** 

 **_②我写的列表查询一次性几乎查询所有属性数据，点击编辑按钮的时候直接用前端的数据，或者可以尝试只查询显示数据，点击编辑按钮再先后端发送一次查询请求_** 



#### 介绍

本项目[ **_码云仓库_** ](https://gitee.com/jing3wen/jw_project)已与[ **_GitHub仓库_** ](https://github.com/jing3wen/jw_project)同步更新，更新以码云仓库为主，有不懂的直接邮箱1584591284@qq.com提问

**注意：** 我不能保证每次提交代码都更新数据库sql文件，所以当前的数据库sql文件和static中的文件可能对应不上，我大概每周更新一次数据库sql文件，若急需，给我发邮件获取



其他内容后续补上

#### 开发环境
 
    1.java 17   （java8也行）
    2.vue2 
    3.python3.8
    4.pytorch10+cuda11

#### 安装教程  

深度学习环境啥的建议自己配置

1. 直接把整个项目下载下来，按如下目录

  **facenet-retinaface-pytorch** 是人脸检测项目，

  **jw_server** 是java后端， 

  **jw_vue_admin** 是vue前端

  **static/upload** 是上传的文件路径，注意springboot的配置文件application.yml里面要修改你的数据库信息和你本地的上传文件路径

![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E7%9B%AE%E5%BD%95.png)

2. webStorm或者别的开发软件打开vue项目，安装依赖，运行

   IDEA打开jw_server，安装maven依赖，直接运行项目

   PyCharm打开facenet-retinaface-pytorch，运行 socket_to_java.py，开启python的socket服务器

3. 浏览器输入输入 [http://localhost:10090/login](http://)，开始运行项目吧🤣🤣🤣

#### 使用说明

 **1.  登陆界面(gitee挂我图片是吧，晚上别睡太死 :sweat_smile: )** 

![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E5%B0%81%E9%9D%A2.png)


 **2.  用户管理** 
![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png)

 **3.  菜单管理** 
![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86.png)
 
**4.  操作日志** 
![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%97.png)

 **5.  人脸库** 
![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E4%BA%BA%E8%84%B8%E5%BA%93.png)

 **6.  检测文件** 
![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E4%B8%8A%E4%BC%A0%E6%A3%80%E6%B5%8B%E6%96%87%E4%BB%B6.png)
![输入图片说明](%E7%B3%BB%E7%BB%9F%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88/%E6%A3%80%E6%B5%8B%E6%95%88%E6%9E%9C.png)


#### 参与贡献
1.  贡献人：沈靖文
2.  邮箱 1584591284@qq.com




