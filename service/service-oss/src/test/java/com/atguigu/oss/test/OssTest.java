package com.atguigu.oss.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class OssTest {

    public static void main(String[] args) {
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "";
        String accessKeySecret = "";
        String bucketName = "yygh-test";

        // create OSS instance
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.createBucket(bucketName);

        ossClient.shutdown();
    }
}
