#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2022/4/20 9:06
# @Author  : jingwen
# @File    : SocketService.py
# @Desc:   : 将预测代码拆开封装成接口供java客户端调用
import time

import cv2
import numpy as np

from model.retinaface import Retinaface
from apiResponse.ApiResponse import ApiResponse
from encoding import encoding

from socket_config import *


# socket中可能要多次调用下列方法,建立一个service类
class SocketService(object):
    def __init__(self):
        super(SocketService, self).__init__()
        self.retinaface = Retinaface()

    # 内部函数
    def get_file_save_path(self, image_path):
        file_name = os.path.basename(image_path)  # jingwen_undetected.jpg
        save_directory = detection_result
        if not os.path.exists(save_directory):
            os.makedirs(save_directory)
        new_file_name = file_name.replace("_undetected", "_detected", 1)
        save_image_path = save_directory + new_file_name
        return save_image_path

    # 检测图片
    def predictImage(self, image_path="", save_image=False):
        print("检测图片，image_path=", image_path, )
        image = cv2.imread(image_path)
        if image is None:
            # print("图片路径有误")
            return ApiResponse(code=500, message='图片路径有误')
        else:
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            r_image, recognition_face_list = self.retinaface.detect_image(image)
            print(f'FaceNet识别出{len(recognition_face_list)}张人脸:{recognition_face_list}')

            # r_image = cv2.cvtColor(r_image, cv2.COLOR_RGB2BGR)
            # cv2.imshow("after", r_image)
            # cv2.waitKey(0)

            data = {}
            data.setdefault('recognition_face_list', recognition_face_list)

            # 是否保存检测图片
            if save_image:
                r_image = cv2.cvtColor(r_image, cv2.COLOR_RGB2BGR)
                save_file_path = self.get_file_save_path(image_path)
                cv2.imwrite(save_file_path, r_image)
                data.setdefault('save_file_path', save_file_path)
                print('保存检测结果: ' + save_file_path)
            return ApiResponse(code=200, message="识别成功", data=data)

    # 检测视频,完成学生正脸检测，并返回最终得分
    def predictVideo(self, video_path="", save_video=False, video_fps=30):
        print("检测视频，video_path=", video_path)

        data = {}
        # 防止传入数据为摄像头
        if video_path == 0:
            return ApiResponse(code=500, message="该方法为检测视频，请更换摄像头检测方法")
        all_recognition_face_list = []  # 为检测到的所有人脸

        capture = cv2.VideoCapture(video_path)
        # 视频保存路径
        video_save_path = ''
        if save_video:
            video_save_path = self.get_file_save_path(video_path)
            # 视频的编码
            fourcc = cv2.VideoWriter_fourcc(*'mp4v')
            # 视频的分辨率
            size = (int(capture.get(cv2.CAP_PROP_FRAME_WIDTH)), int(capture.get(cv2.CAP_PROP_FRAME_HEIGHT)))
            # 定义视频输出
            out = cv2.VideoWriter(video_save_path, fourcc, video_fps, size)

        ref, frame = capture.read()
        if not ref:  # null    0
            raise ApiResponse(code=500, message="未能正确读取视频，请检查视频路径是否正确")

        input_video_fps = capture.get(5)  # 获取输入视频帧数
        frameRate = int(input_video_fps) * videoTimeRate  # 每隔videoTimeRate秒取一帧
        current_ref = 1  # 当前帧
        fps = 0.0
        while (True):

            ref, frame = capture.read()

            if not ref:
                break
            """
            # 镜头水平反转代码
            """
            # frame = cv2.flip(frame, 180)
            recognition_face_list = []
            if (current_ref % frameRate == 0):
                t1 = time.time()
                # 读取某一帧
                # 格式转变，BGRtoRGB
                frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                # 进行检测
                frame, recognition_face_list = self.retinaface.detect_image(frame)

                frame = np.array(frame)
                # RGBtoBGR满足opencv显示格式
                frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)

                fps = (fps + (1. / (time.time() - t1))) / 2
                print(f'fps= {fps:.2f};  '
                      f'facenet识别出{len(recognition_face_list)}张人脸:{recognition_face_list}  ')

                # 根据当前帧识别的人脸 更新总识别人脸数
                if len(recognition_face_list) != 0:
                    for face_name in recognition_face_list:
                        if face_name not in all_recognition_face_list:
                            all_recognition_face_list.append(face_name)
                frame = cv2.putText(frame, "fps= %.2f, recognition_num= %d" % (fps, len(recognition_face_list)),
                                    (0, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
            current_ref += 1

            # cv2.imshow("video", frame)
            c = cv2.waitKey(1) & 0xff
            if save_video:
                out.write(frame)

            if c == 27:
                capture.release()
                break
        print("Video Detection Done!")
        capture.release()
        if save_video == True:
            out.release()
            """
            # 为视频添加音频信息
            video_add_voice_config=True 表示添加
            video_add_voice_config=False 表示不添加      
            """
            if video_add_voice_config:
                video_save_path = video_add_voice(video_save_path, video_path)
            print("Save processed video to the path :" + video_save_path)
        cv2.destroyAllWindows()

        data.setdefault('save_file_path', video_save_path)
        data.setdefault('recognition_face_list', all_recognition_face_list)
        return ApiResponse(code=200, message="视频检测完成", data=data)

    # 给数据库人脸编码
    def update_face_dataset(self):
        encoding()
        # 更新初始化的retinaface的人脸库信息
        self.retinaface.known_face_encodings = np.load(
            "model/model_data/{backbone}_face_encoding.npy".format(backbone=self.retinaface.facenet_backbone))
        self.retinaface.known_face_names = np.load(
            "model/model_data/{backbone}_names.npy".format(backbone=self.retinaface.facenet_backbone))
        return ApiResponse(code=200, message='人脸库更新成功')
