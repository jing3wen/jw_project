#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2022/4/23 11:13
# @Author  : jingwen
# @File    : socket_config.py
# @Desc:   : 定义socket服务的一些配置文件，主要是人脸库的路径
"""

我在测试过程中发现一个大问题：
通过opencv2读取"D:/SpringBoot_v2-master_upload/my_upload/file_detection/file_undetected.mp4"
保存的文件为"D:/SpringBoot_v2-master_upload/my_upload/detection_result/file_detected.mp4"
保存的文件不含有音频信息，右键视频属性->详细信息->音频部分为空白，我看别的静音mp4视频中虽然没有声音但是音频信息依然含有，
保存的文件无法通过http传输到前端，我认为是视频音频信息空白的问题，所以尝试通过ffmpeg指令为保存的文件添加音频信息，
同时在配置文件中设置将该配置可打开或关闭，开发时可关闭，上线时打开

请用户自行配置好ffmpeg环境
"""
import subprocess
import os

videoTimeRate = 0.2  # 每0.2秒检测一帧，可以修改
local_path = "D:/IDEA/project/jw_project/static/upload/"

face_library = local_path + "deep_learning/face_detect/face_database/defined_face/"
file_detection = local_path + "deep_learning/face_detect/face_detect_file/to_detect_file/"
detection_result = local_path + "deep_learning/face_detect/face_detect_file/detect_file_result/"

classroom_file_detection = "D:/SpringBoot_v2-master_upload/my_upload/classroom_file_detection/"
classroom_file_detection_result = "D:/SpringBoot_v2-master_upload/my_upload/classroom_file_detection_result/"
video_add_voice_config = True


def video_add_voice(video_file, voice_file):
    """
     视频添加音频
    :param video_file: 传入视频文件的路径
    :param voice_file: 传入音频文件的路径
    :return:
    """
    outfile_name = video_file.split('.')[0] + '_voice.mp4'
    subprocess.call('ffmpeg -i ' + video_file
                    + ' -i ' + voice_file + ' -strict -2 -f mp4 '
                    + outfile_name, shell=True)
    # 删除原视频文件，若不想删除可以注释掉下行代码
    os.remove(video_file)

    return outfile_name
