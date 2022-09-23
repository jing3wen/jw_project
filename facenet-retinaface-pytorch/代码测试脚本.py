#!/usr/bin/env python 
# -*- coding: utf-8 -*-
# @Time    : 2022/4/19 9:06
# @Author  : jingwen
# @File    : 代码测试脚本.py
# @Desc:   :
import json
import os
import time

import cv2
import numpy as np
from PIL import Image

from retinaface import Retinaface
from apiResponse.ApiResponse import ApiResponse
from SocketService import SocketService


def print_npy_file(file_path):
    npy = np.load(file_path)
    print(npy)
    print("len(npy[0])=", len(npy[0]))
    print("load " + file_path + " done")


"""
print_npy_file("model_data/mobilenet_face_encoding.npy")
print_npy_file("model_data/mobilenet_names.npy")
"""
"""
recognition_face_list = ['jingwen', 'zhangsan']
student_study_time = {'jingwen': 1650339286.388632, 'zhangsan': 1650339286.388632}
print("学生学习起始时间", student_study_time)
"""


# 根据检测到的人脸来更新用户的起始学习时间
def update_student_study_time(student_study_time, recognition_face_list):
    print(f'当前在线学生{recognition_face_list}')
    for student_name in recognition_face_list:
        if student_name not in student_study_time:
            # print(student_name + ' not in student_study_time')
            student_study_time.setdefault(student_name, time.time())
        elif student_name in recognition_face_list:
            # print(student_name + 'in student_study_time')
            student_study_time[student_name] = time.time()
    print("学生起始学习时间更新完成: ", student_study_time)


# judge_time判断未听课时间,单位为s, time.time()单位为s
def check_student_time(student_study_time, judge_time):
    for student_name, study_strat_time in student_study_time.items():
        student_time = time.time() - student_study_time[student_name]
        print(f'学生 {student_name} 距离上一次学习时间为 {student_time:.1f}s')
        if (student_time > judge_time):
            print(f'该学生 {student_name} 已经{judge_time}s内未抬头，判定为未听课')


# 60内为更新学习时间认定为未听课
# update_student_study_time(student_study_time,recognition_face_list)
# check_student_time(student_study_time, judge_time=60)

def test_return():
    a = [[0, 0, 0, 0], [1, 2, 2, 1]]
    list = []
    return a, list


"""
for i in range(5):
    a,b = test_return()
    print(a,b)
"""

"""
first= zip(1, 2)
print(first)
"""
"""b=[]
print(len(b))"""

# 获取图片名称
# print(os.path.basename("img/jingwen.jpg"))
"""
socketService = SocketService()
a = socketService.predictImage("img/jingwen.jpg", save_iamge=1)

""""""
# json封装测试
print(json.dumps(a.apiResopnseToDict(), ensure_ascii=False))
"""
"""
video=socketService.predictVideo('img/test_vedio2.mp4')
print(json.dumps(video.apiResopnseToDict(),ensure_ascii=False))
"""

# json转str
"""
re格式
{
    "code":200,
    "message": "call_SocketService",
    "data":{
        "service_name":"predictImage()",
        "service_params":{
            "image_path":"img/jingwen.jpg", 
            "save_iamge":0
        }
    }
}
"""
"""
msg = '{"code":200,' \
      '"message": "call_SocketService",' \
      '"data":{"service_name":"predictImage()",' \
      '"service_params":{"image_path":"img/jingwen.jpg", "save_iamge":"False"}}}'
re = json.loads(msg)
print("re.code=", re["code"])
print("re.data=", re["data"])
print("re.data.service_params.image_path=", re["data"]["service_params"]["image_path"])
"""
# sendmsg = json.dumps(a.apiResopnseToDict(), ensure_ascii=False)
# print(("%s" % sendmsg))
"""
fps=25
current_student_num=2
student_total=5
print("fps= %.2f,抬头率= %.2f" % (fps,current_student_num / student_total))
"""
"""
name="123_undetected.jpg"
if (name.find("_undetected")!=-1):
    print(""+name.replace("_undetected", "_detected", 1))
else:
    print("not find substring")
"""
"""
img = Image.open("face_dataset/黄晓明.png")
print(img)
img = img.convert("RGB")
print(img)

img2 = Image.open("face_dataset/obama_1.jpg")
print(img2)
print(img2.convert("RGB"))
"""

import subprocess



def video2mp3(file_name):
    """
    将视频转为音频
    :param file_name: 传入视频文件的路径
    :return:
    """
    outfile_name = file_name.split('.')[0] + '.mp3'
    subprocess.call('ffmpeg -i ' + file_name
                    + ' -f mp3 ' + outfile_name, shell=True)


def video_add_mp3(file_name, mp3_file):
    """
     视频添加音频
    :param file_name: 传入视频文件的路径
    :param mp3_file: 传入音频文件的路径
    :return:
    """
    outfile_name = file_name.split('.')[0] + '_voice.mp4'
    subprocess.call('ffmpeg -i ' + file_name
                    + ' -i ' + mp3_file + ' -strict -2 -f mp4 '
                    + outfile_name, shell=True)
"""
video_add_mp3("D:/SpringBoot_v2-master_upload/my_upload/detection_result/huge_detected.mp4",
          "D:/SpringBoot_v2-master_upload/my_upload/file_detection/huge_undetected.mp4",)   # huge_undetected.mp4
"""
