package javaweb.remember.utils;

import java.util.Random;

/**
 * Remarks  : 生成随机字符串
 * File     : RandomNumber.java
 * Project  : I-Remember
 * Software : IntelliJ IDEA
 */
public class RandomNumber {
    public static String createRandomCode(int length){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++) {
            // 三种类型，小写字母，大写字母，数字
            int number = random.nextInt(3);
            long result;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    str.append((char) result);
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    str.append((char) result);
                    break;
                case 2:
                    str.append(random.nextInt(10));
                    break;
            }
        }
        return str.toString();
    }
}
