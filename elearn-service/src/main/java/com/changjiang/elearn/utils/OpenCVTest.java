package com.changjiang.elearn.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class OpenCVTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // 加载 OpenCV 库
    }

    public static void main(String[] args) {
        // 创建一个 3x3 的矩阵，并填充为全 1
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        mat.setTo(new Scalar(1));

        // 打印矩阵内容
        System.out.println("OpenCV 测试矩阵：");
        System.out.println(mat.dump());

        // 打印 OpenCV 版本
        System.out.println("OpenCV 版本：" + Core.VERSION);
    }
}
