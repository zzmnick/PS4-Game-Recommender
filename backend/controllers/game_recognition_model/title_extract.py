import cv2
import numpy as np
import glob
import random

def get_title(path):
    net = cv2.dnn.readNet("yolov3_training_last.weights", "yolov3_testing.cfg")
    print(path)
    img = cv2.imread("./"+path)
    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
    img = cv2.resize(img, None, fx=1, fy=1)
    height, width, channels = img.shape
    blob = cv2.dnn.blobFromImage(img, 0.00392, (416, 416), (0, 0, 0), True, crop=False)
    net.setInput(blob)
    outs = net.forward(output_layers)
    box = 0
    m = 0
    for out in outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]
            if confidence > 0.3:
                # Object detected
                #print(class_id)
                center_x = int(detection[0] * width)
                center_y = int(detection[1] * height)
                w = int(detection[2] * width)
                h = int(detection[3] * height)

                # Rectangle coordinates
                x = int(center_x - w / 2)
                y = int(center_y - h / 2)

                if (width*height)>m:
                    m = width*height
                    box = [x,y,w,h]
    if box == 0:
        return [None],None,None            
    x,y,w,h = box
    portion = img[y:y+h, x:x+w]
    #portion = cv2.cvtColor(portion, cv2.COLOR_BGR2GRAY)
    #th3 = cv2.adaptiveThreshold(portion,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
    #        cv2.THRESH_BINARY,11,2)
    #cv2.imshow("..",portion)
    #cv2.waitkey(0)
    height, width, channels = portion.shape
    #dim = max([height,width])
    #factor = 224/dim
    portion = cv2.resize(portion,None,fx = 224/width, fy = 224/height)
    return portion,height,width




