#!/usr/bin/env python 
# -*- coding: utf-8 -*-
# @Time    : 2022/4/19 11:41
# @Author  : jingwen
# @File    : student_utils.py
# @Desc:   :
import time

"""
student_course_score_dict为学生上课得分字典
recognition_face_list为当前正在学习的名单
"""
# 根据识别的学生人脸更新其上课得分
def update_student_course_score(student_course_score_dict, recognition_face_list):
    for student_name in recognition_face_list:
        if student_name not in student_course_score_dict:
            # 识别到的学生人脸不在上课得分字典中，将该人脸加入到字典中，设置初始得分=1
            student_course_score_dict.setdefault(student_name, 1)
        elif student_name in recognition_face_list:
            # 识别到的学生人脸在得分字典中，得分+1
            student_course_score_dict[student_name] += 1
    print(f"捕捉到学生:{recognition_face_list}; 学生课堂得分更新完成: {student_course_score_dict}")





