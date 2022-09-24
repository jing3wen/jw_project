import os

from model.retinaface import Retinaface
from socket_config import face_library
'''
在更换facenet网络后一定要重新进行人脸编码，运行encoding.py。
'''
def encoding():
    print("为人脸库编码...")
    retinaface = Retinaface(1)

    list_dir = os.listdir(face_library)
    image_paths = []
    names = []
    for name in list_dir:
        image_paths.append(face_library + name)
        names.append(name.split("_")[0])

    retinaface.encode_face_dataset(image_paths, names)

    return "编码完成"


if __name__ =="__main__":
    print(encoding())

