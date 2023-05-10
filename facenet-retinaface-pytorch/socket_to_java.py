#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2022/4/20 8:59
# @Author  : jingwen
# @File    : socket_to_java.py
# @Desc:   :

import socket
import threading
import json
from service.SocketService import SocketService
from apiResponse.ApiResponse import ApiResponse

socketService=SocketService()
def main():
    # 创建服务器套接字
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 获取本地主机名称
    host = "localhost" # socket.gethostname()
    # 设置一个端口
    port = 12345
    # 将套接字与本地主机和端口绑定
    serversocket.bind((host, port))
    # 设置监听最大连接数
    serversocket.listen(5)
    # 获取本地服务器的连接信息
    myaddr = serversocket.getsockname()
    print("服务器地址:%s" % str(myaddr))
    # 循环等待接受客户端信息
    while True:
        # 获取一个客户端连接
        clientsocket, addr = serversocket.accept()
        print("连接地址:%s" % str(addr))
        try:
            t = ServerThreading(clientsocket)  # 为每一个请求开启一个处理线程
            t.start()
            pass
        except Exception as identifier:
            print(identifier)
            pass
        pass
    serversocket.close()
    pass


class ServerThreading(threading.Thread):
    # words = text2vec.load_lexicon()
    def __init__(self, clientsocket, recvsize=1024 * 1024, encoding="utf-8"):
        threading.Thread.__init__(self)
        self._socket = clientsocket
        self._recvsize = recvsize
        self._encoding = encoding
        pass

    def run(self):
        print("开启线程.....")
        try:
            # 接受数据
            msg = ''
            while True:
                # 读取recvsize个字节
                rec = self._socket.recv(self._recvsize)
                # 解码
                msg += rec.decode(self._encoding)
                # 文本接受是否完毕，因为python socket不能自己判断接收数据是否完毕，
                # 所以需要自定义协议标志数据接受完毕
                if msg.strip().endswith('over'):
                    msg = msg[:-4]
                    break
            # 解析json格式的数据
            re = json.loads(msg)
            #print(re)
            # java申请调用socket_service中的方法
            """
                re格式: {
                            "code":200,
                            "message": "call_SocketService",
                            "data":{
                                "service_name":"predictImage()",
                                "service_params":{
                                    "image_path":"D:/SpringBoot_v2-master_upload/my_upload/file_detection/jingwen.jpg", 
                                    "save_iamge":1   # True
                                }
                            }
                        }           
            """
            if re["code"] == 200 and re["message"] == 'call_SocketService':
                service_name=re["data"]["service_name"]
                if service_name == "predictImage()":
                    service_params = re["data"]["service_params"]
                    res = socketService.predictImage(service_params["image_path"],
                                                     service_params["save_image"])
                elif service_name == "predictVideo()":
                    service_params = re["data"]["service_params"]
                    res = socketService.predictVideo(service_params["video_path"],
                                                     service_params["save_video"])
                elif service_name == "update_face_dataset()":
                    res = socketService.update_face_dataset()
                else:
                    res=ApiResponse("500", "socket请求信息参数有误")



            """
            res格式: {
                        "code":200
                        "message":"识别成功"
                        "data":{
                            "recognition_face_list": ["yuwenxing"],
                             "save_file_path": "D:/SpringBoot_v2-master_upload/my_upload/detection_result/jingwen.jpg"
                        }
                    }
            """
            sendmsg = json.dumps(res.apiResopnseToDict(), ensure_ascii=False)
            # 发送数据
            self._socket.send(("%s" % sendmsg).encode(self._encoding))
            pass
        except Exception as identifier:
            self._socket.send("500".encode(self._encoding))
            print(identifier)
            pass
        finally:
            self._socket.close()
        print("任务结束.....")

        pass

    def __del__(self):

        pass


if __name__ == "__main__":
    main()
